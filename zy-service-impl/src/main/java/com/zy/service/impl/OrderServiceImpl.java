
package com.zy.service.impl;

import com.zy.Config;
import com.zy.ServiceUtils;
import com.zy.common.exception.BizException;
import com.zy.common.exception.ConcurrentException;
import com.zy.common.model.query.Page;
import com.zy.common.model.tree.TreeHelper;
import com.zy.common.model.tree.TreeNode;
import com.zy.common.model.tree.TreeNodeResolver;
import com.zy.common.util.DateUtil;
import com.zy.component.FncComponent;
import com.zy.component.MalComponent;
import com.zy.entity.fnc.*;
import com.zy.entity.fnc.Payment.PaymentStatus;
import com.zy.entity.fnc.Payment.PaymentType;
import com.zy.entity.mal.Order;
import com.zy.entity.mal.Order.OrderStatus;
import com.zy.entity.mal.OrderItem;
import com.zy.entity.mal.OrderMonthlySettlement;
import com.zy.entity.mal.Product;
import com.zy.entity.usr.Address;
import com.zy.entity.usr.User;
import com.zy.entity.usr.User.UserRank;
import com.zy.extend.Producer;
import com.zy.mapper.*;
import com.zy.model.BizCode;
import com.zy.model.Constants;
import com.zy.model.TeamModel;
import com.zy.model.dto.OrderCreateDto;
import com.zy.model.dto.OrderDeliverDto;
import com.zy.model.dto.OrderSumDto;
import com.zy.model.query.OrderFillUserQueryModel;
import com.zy.model.query.OrderQueryModel;
import com.zy.model.query.UserQueryModel;
import com.zy.service.OrderService;
import com.zy.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.hibernate.validator.constraints.NotBlank;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.zy.common.util.ValidateUtils.*;
import static com.zy.model.Constants.*;

@Service
@Validated
public class OrderServiceImpl implements OrderService {

	@Autowired
	private OrderMapper orderMapper;

	@Autowired
	private OrderFillUserMapper orderFillUserMapper;

	@Autowired
	private PaymentMapper paymentMapper;

	@Autowired
	private OrderItemMapper orderItemMapper;

	@Autowired
	private UserMapper userMapper;
	
	@Autowired
	private Config config;

	@Autowired
	private ProductMapper productMapper;

	@Autowired
	private AddressMapper addressMapper;

	@Autowired
	private OrderMonthlySettlementMapper orderMonthlySettlementMapper;

	@Autowired
	private MalComponent malComponent;

	@Autowired
	private FncComponent fncComponent;

	@Autowired
	private Producer producer;

	@Autowired
	private UserService userService;

	public static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

	@Override
	public Order create(@NotNull OrderCreateDto orderCreateDto) {
		validate(orderCreateDto);

		Order.OrderType orderType = orderCreateDto.getOrderType();
		if(orderType == Order.OrderType.补单) {
			if(!config.isOpenOrderFill()) {
				throw new BizException(BizCode.ERROR, "补单已关闭");
			}

			long count = orderFillUserMapper.count(OrderFillUserQueryModel.builder().userIdEQ(orderCreateDto.getUserId()).build());
			if(count == 0) {
				throw new BizException(BizCode.ERROR, "当前权限不能补单");
			}
		}

		/* check user */
		Long userId = orderCreateDto.getUserId();
		User user = userMapper.findOne(userId);
		validate(user, NOT_NULL, "user id " + userId + " is not found");
		validate(user, v -> v.getUserType() == User.UserType.代理, "user type is wrong");

		/* check address */
		Long addressId = orderCreateDto.getAddressId();
		Address address = addressMapper.findOne(addressId);
		validate(address, NOT_NULL, "address id " + addressId + " is not found");
		validate(address, v -> v.getUserId().equals(userId), "address " + addressId + " is not own");

		/* check product */
		Long productId = orderCreateDto.getProductId();
		Product product = productMapper.findOne(productId);
		validate(product, NOT_NULL, "product id " + productId + " is not found");
		if (!product.getIsOn()) {
			throw new BizException(BizCode.ERROR, "必须上架的商品才能购买");
		}

		/* calculate parent id */
		Long parentId;
		UserRank userRank = user.getUserRank();
		if (userRank == UserRank.V0 && user.getParentId() == null) {
			parentId = orderCreateDto.getParentId();
			if (parentId == null) {
				throw new BizException(BizCode.ERROR, "首次下单必须填写邀请人");
			}
			User parent = userMapper.findOne(parentId);
			if (parent == null) {
				throw new BizException(BizCode.ERROR, "邀请人手机号没找到");
			} else if (parent.getId().equals(userId)) {
				throw new BizException(BizCode.ERROR, "邀请人不能为自己");
			} else if (parent.getUserRank() == UserRank.V0) {
				throw new BizException(BizCode.ERROR, "邀请人资格不足");
			} else if (parent.getUserType() != User.UserType.代理) {
				throw new BizException(BizCode.ERROR, "非法邀请人");
			}
			user.setParentId(parentId);
			userMapper.update(user);
		} else {
			parentId = user.getParentId();
		}


		String title = orderCreateDto.getTitle();
		long quantity = orderCreateDto.getQuantity();
		UserRank buyerUserRank = user.getUserRank();

		User seller = malComponent.calculateSeller(userRank, productId, quantity, parentId);
		Long sellerId = seller.getId();
		UserRank sellerUserRank = sellerId.equals(config.getSysUserId()) ? null : seller.getUserRank();

		Long v4UserId = calculateV4UserId(user);
		Long rootId = calculateRootId(user);

		BigDecimal price = malComponent.getPrice(productId, user.getUserRank(), quantity);
		BigDecimal amount = price.multiply(new BigDecimal(quantity));

		boolean isPayToPlatform = orderCreateDto.getIsPayToPlatform();
		UserRank upgradeUserRank = malComponent.getUpgradeUserRank(userRank, productId, quantity);
		if(upgradeUserRank == UserRank.V4) {  //升特级只允许支付给平台
			isPayToPlatform = true;
		}

		Date createdTime = null;
		if(orderCreateDto.getOrderType() == Order.OrderType.普通订单) {
			createdTime = new Date();
		} else {
			createdTime = config.getOrderFillTime();
		}

		LocalDate lastDate = LocalDate.now().with(TemporalAdjusters.lastDayOfMonth());
		LocalDateTime localDateTime = LocalDateTime.of(lastDate, LocalTime.parse("23:59:59"));
		Instant instant = localDateTime.atZone(ZoneId.systemDefault()).toInstant();
		Date expiredTime = Date.from(instant);

		Order order = new Order();
		order.setUserId(userId);
		order.setAmount(amount);
		order.setCreatedTime(createdTime);
		order.setIsSettledUp(false);
		order.setIsProfitSettledUp(false);
		order.setIsCopied(false);
		order.setBuyerUserRank(buyerUserRank);
		order.setSellerUserRank(sellerUserRank);
		order.setReceiverAreaId(address.getAreaId());
		order.setReceiverProvince(address.getProvince());
		order.setReceiverCity(address.getCity());
		order.setReceiverDistrict(address.getDistrict());
		order.setReceiverAddress(address.getAddress());
		order.setReceiverRealname(address.getRealname());
		order.setReceiverPhone(address.getPhone());
		order.setBuyerMemo(orderCreateDto.getBuyerMemo());
		order.setOrderStatus(OrderStatus.待支付);
		order.setOrderType(orderType);
		order.setVersion(0);
		order.setSellerId(sellerId);
		order.setSn(ServiceUtils.generateOrderSn());
		order.setIsSettledUp(false);
		order.setDiscountFee(new BigDecimal("0.00"));
		//order.setExpiredTime(DateUtils.addMinutes(new Date(), Constants.SETTING_ORDER_EXPIRE_IN_MINUTES));
		order.setExpiredTime(expiredTime);
		order.setIsPayToPlatform(orderCreateDto.getIsPayToPlatform());
		order.setIsDeleted(false);

		/* 追加字段 */
		order.setIsMultiple(false);
		order.setProductId(productId);
		order.setMarketPrice(product.getMarketPrice());
		order.setQuantity(quantity);
		order.setPrice(price);
		order.setImage(product.getImage1());
		order.setV4UserId(v4UserId);
		order.setRootId(rootId);

		if (StringUtils.isNotBlank(title)) {
			order.setTitle(title);
		} else {
			order.setTitle(product.getTitle());
		}

		validate(order);
		orderMapper.insert(order);

		OrderItem orderItem = new OrderItem();
		orderItem.setPrice(price);
		orderItem.setOrderId(order.getId());
		orderItem.setProductId(productId);
		orderItem.setMarketPrice(product.getMarketPrice());
		orderItem.setQuantity(quantity);
		orderItem.setAmount(amount);
		orderItem.setTitle(product.getTitle());
		orderItem.setImage(product.getImage1());
		validate(orderItem);
		orderItemMapper.insert(orderItem);
		return order;
	}

	@Override
	public void cancel(@NotNull Long id) {
		Order order = orderMapper.findOne(id);
		validate(order, NOT_NULL, "order id " + id + "is not found");
		OrderStatus orderStatus = order.getOrderStatus();
		if (orderStatus == OrderStatus.已取消) {
			return; // 幂等处理
		} else if (orderStatus != OrderStatus.待支付) {
			throw new BizException(BizCode.ERROR, "只有待支付订单才能取消");
		}
		order.setOrderStatus(OrderStatus.已取消);
		if (orderMapper.update(order) == 0) {
			throw new ConcurrentException();
		}
	}

	@Override
	public Page<Order> findPage(@NotNull OrderQueryModel orderQueryModel) {
		if (orderQueryModel.getPageNumber() == null)
			orderQueryModel.setPageNumber(0);
		if (orderQueryModel.getPageSize() == null)
			orderQueryModel.setPageSize(20);
		if (orderQueryModel.getOrderBy() == null) {
			orderQueryModel.setOrderBy("createdTime");
		}

		long total = orderMapper.count(orderQueryModel);
		List<Order> data = orderMapper.findAll(orderQueryModel);
		Page<Order> page = new Page<>();
		page.setPageNumber(orderQueryModel.getPageNumber());
		page.setPageSize(orderQueryModel.getPageSize());
		page.setData(data);
		page.setTotal(total);
		return page;
	}

	@Override
	public Order findOne(@NotNull Long id) {
		return orderMapper.findOne(id);
	}

	@Override
	public Order findBySn(@NotBlank String sn) {
		return orderMapper.findBySn(sn);
	}

	@Override
	public void confirmPay(@NotNull Long id) {
		Order order = orderMapper.findOne(id);
		validate(order, NOT_NULL, "order id" + id + " is not found");
		if (order.getIsPayToPlatform()){
			throw new BizException(BizCode.ERROR, "平台收款订单不能确认");
		}
		malComponent.successOrder(id);
	}
	
	@Override
	public void rejectPay(@NotNull Long id, String remark) {
		Order order = orderMapper.findOne(id);
		validate(order, NOT_NULL, "order id" + id + " is not found");
		if (order.getIsPayToPlatform()){
			throw new BizException(BizCode.ERROR, "平台收款订单不能驳回");
		}
		malComponent.failureOrder(id, remark);
	}
	
	@Override
	public void deliver(@NotNull OrderDeliverDto orderDeliverDto) {

		validate(orderDeliverDto, "id", "isUseLogistics");
		boolean isUseLogistics = orderDeliverDto.getIsUseLogistics();

		Long id = orderDeliverDto.getId();
		Order order = orderMapper.findOne(id);
		validate(order, NOT_NULL, "order id" + id + " is not found");

		if (order.getSellerId().equals(config.getSysUserId())) {
			throw new BizException(BizCode.ERROR, "平台发货订单不能自己发货");
		}

		OrderStatus orderStatus = order.getOrderStatus();
		if (orderStatus == OrderStatus.已发货) {
			return; // 幂等处理
		} else if (orderStatus != OrderStatus.已支付) {
			throw new BizException(BizCode.ERROR, "只有已支付的订单才能发货");
		}

		if (order.getIsCopied()) {
			throw new BizException(BizCode.ERROR, "已转订单不能发货");
		}

		if (isUseLogistics) {

			String logisticsName = orderDeliverDto.getLogisticsName();
			String logisticsSn = orderDeliverDto.getLogisticsSn();
			BigDecimal logisticsFee = orderDeliverDto.getLogisticsFee();

			validate(logisticsName, NOT_BLANK, "logistics name is blank");
			validate(logisticsSn, NOT_BLANK, "logistics sn is blank");
			validate(logisticsFee, NOT_NULL, "logistics fee is null");
			validate(orderDeliverDto, "logisticsName", "logisticsSn", "logisticsFee");

			order.setLogisticsName(logisticsName);
			order.setLogisticsSn(logisticsSn);
			order.setLogisticsFee(logisticsFee);
			order.setIsBuyerPayLogisticsFee(false);
		} else {
			order.setLogisticsFee(null);
			order.setLogisticsName(null);
			order.setLogisticsSn(null);
		}

		order.setIsUseLogistics(isUseLogistics);

		order.setDeliveredTime(new Date());
		order.setOrderStatus(OrderStatus.已发货);

		if (orderMapper.update(order) == 0) {
			throw new ConcurrentException();
		}

		producer.send(Constants.TOPIC_ORDER_DELIVERED, order.getId());
	}

	@Override
	public void platformDeliver(@NotNull OrderDeliverDto orderDeliverDto) {
		validate(orderDeliverDto, "id", "isUseLogistics");
		boolean isUseLogistics = orderDeliverDto.getIsUseLogistics();

		Long id = orderDeliverDto.getId();
		Order order = orderMapper.findOne(id);
		validate(order, NOT_NULL, "order id" + id + " is not found");

		if (!order.getSellerId().equals(config.getSysUserId())) {
			throw new BizException(BizCode.ERROR, "非平台发货订单不能平台发货");
		}

		OrderStatus orderStatus = order.getOrderStatus();
		if (orderStatus == OrderStatus.已发货) {
			return; // 幂等处理
		} else if (orderStatus != OrderStatus.已支付) {
			throw new BizException(BizCode.ERROR, "只有已支付的订单才能发货");
		}

		String logisticsName = orderDeliverDto.getLogisticsName();
		String logisticsSn = orderDeliverDto.getLogisticsSn();
		BigDecimal logisticsFee = orderDeliverDto.getLogisticsFee();
		Date date = new Date();

		if (isUseLogistics) {
			validate(logisticsName, NOT_BLANK, "logistics name is blank");
			validate(logisticsSn, NOT_BLANK, "logistics sn is blank");
			validate(logisticsFee, NOT_NULL, "logistics fee is null");
			validate(orderDeliverDto, "logisticsName", "logisticsSn", "logisticsFee");

			order.setLogisticsName(logisticsName);
			order.setLogisticsSn(logisticsSn);
			order.setIsBuyerPayLogisticsFee(true);
			order.setLogisticsFee(logisticsFee);
		}

		order.setIsUseLogistics(isUseLogistics);
		order.setDeliveredTime(date);
		order.setOrderStatus(OrderStatus.已发货);

		if (orderMapper.update(order) == 0) {
			throw new ConcurrentException();
		}
		
		producer.send(Constants.TOPIC_ORDER_DELIVERED, order.getId());

		/* 同时发货拷贝订单 */
		if (order.getIsCopied()) {
			Order refOrder = orderMapper.findOne(order.getRefId());
			if (isUseLogistics) {
				refOrder.setLogisticsName(logisticsName);
				refOrder.setLogisticsSn(logisticsSn);
				refOrder.setIsBuyerPayLogisticsFee(true);
				refOrder.setLogisticsFee(logisticsFee);
			}
			refOrder.setIsUseLogistics(isUseLogistics);
			refOrder.setDeliveredTime(date);
			refOrder.setOrderStatus(OrderStatus.已发货);

			if (orderMapper.update(refOrder) == 0) {
				throw new ConcurrentException();
			}

			producer.send(Constants.TOPIC_ORDER_DELIVERED, refOrder.getId());
		}

	}

	@Override
	public void receive(@NotNull Long id) {
		Order order = orderMapper.findOne(id);
		validate(order, NOT_NULL, "order id" + id + " is not found");
		OrderStatus orderStatus = order.getOrderStatus();
		if (orderStatus == OrderStatus.已完成) {
			return; // 幂等处理
		} else if (orderStatus != OrderStatus.已发货) {
			throw new BizException(BizCode.ERROR, "只有已发货的订单才能确认收货");
		}

		if (order.getSellerId().equals(config.getSysUserId()) && order.getIsCopied()) {
			throw new BizException(BizCode.ERROR, "已转订单不能确认收货");
		}

		order.setOrderStatus(OrderStatus.已完成);
		if (orderMapper.update(order) == 0) {
			throw new ConcurrentException();
		}
		producer.send(Constants.TOPIC_ORDER_RECEIVED, order.getId());

		if (order.getIsCopied()) {
			Order refOrder = orderMapper.findOne(order.getRefId());
			refOrder.setOrderStatus(OrderStatus.已完成);
			if (orderMapper.update(refOrder) == 0) {
				throw new ConcurrentException();
			}
			producer.send(Constants.TOPIC_ORDER_RECEIVED, refOrder.getId());
		}
	}

	@Override
	public Long copy(@NotNull Long orderId) {
		Order persistentOrder = orderMapper.findOne(orderId);
		validate(persistentOrder, NOT_NULL, "order id" + orderId + " is not found");

		OrderItem persistentOrderItem = orderItemMapper.findByOrderId(orderId).get(0);
		Long productId = persistentOrderItem.getProductId();
		int minQuantity = config.isOld(productId) ? SETTING_OLD_MIN_QUANTITY : SETTING_NEW_MIN_QUANTITY;


		Long quantity = persistentOrderItem.getQuantity();
		if (!persistentOrder.getSellerId().equals(config.getSysUserId()) && persistentOrder.getSellerUserRank() == UserRank.V4 && quantity >= minQuantity && quantity % minQuantity == 0) {
			if (persistentOrder.getIsCopied()) {
				return persistentOrder.getRefId(); // 幂等操作
			}
			Long sysUserId = config.getSysUserId();
			BigDecimal v4Price = malComponent.getPrice(productId, UserRank.V4, quantity);
			BigDecimal v4Amount = v4Price.multiply(BigDecimal.valueOf(quantity)).setScale(2, BigDecimal.ROUND_HALF_UP);
			Product product = productMapper.findOne(productId);
			Long userId = persistentOrder.getSellerId();
			User user = userMapper.findOne(userId);

			Long v4UserId = calculateV4UserId(user);
			Long rootId = calculateRootId(user);

			LocalDate lastDate = LocalDate.now().with(TemporalAdjusters.lastDayOfMonth());
			LocalDateTime localDateTime = LocalDateTime.of(lastDate, LocalTime.parse("23:59:59"));
			Instant instant = localDateTime.atZone(ZoneId.systemDefault()).toInstant();
			Date expiredTime = Date.from(instant);

			Date date = new Date();
			Order order = new Order();
			order.setUserId(userId);
			order.setAmount(v4Amount);
			order.setCreatedTime(new Date());
			order.setIsSettledUp(false);
			order.setIsProfitSettledUp(false);
			order.setBuyerUserRank(UserRank.V4);
			order.setSellerUserRank(null);
			order.setReceiverAreaId(persistentOrder.getReceiverAreaId());
			order.setReceiverProvince(persistentOrder.getReceiverProvince());
			order.setReceiverCity(persistentOrder.getReceiverCity());
			order.setReceiverDistrict(persistentOrder.getReceiverDistrict());
			order.setReceiverAddress(persistentOrder.getReceiverAddress());
			order.setReceiverRealname(persistentOrder.getReceiverRealname());
			order.setReceiverPhone(persistentOrder.getReceiverPhone());
			order.setBuyerMemo("转订单为平台发货");
			order.setOrderStatus(OrderStatus.待支付);
			order.setVersion(0);
			order.setSellerId(sysUserId);
			order.setSn(ServiceUtils.generateOrderSn());
			order.setIsSettledUp(false);
			order.setDiscountFee(new BigDecimal("0.00"));
			order.setExpiredTime(expiredTime);
			order.setOrderType(persistentOrder.getOrderType());

			order.setIsCopied(true);
			order.setCopiedTime(date);
			order.setRefId(orderId);
			
			order.setIsDeleted(false);
			order.setTitle(persistentOrder.getTitle());

			/* 追加字段 */
			order.setIsMultiple(false);
			order.setProductId(productId);
			order.setMarketPrice(product.getMarketPrice());
			order.setQuantity(quantity);
			order.setPrice(v4Price);
			order.setImage(product.getImage1());
			order.setV4UserId(v4UserId);
			order.setRootId(rootId);

			if (persistentOrder.getIsPayToPlatform()) {
				order.setIsPayToPlatform(false);
			} else {
				order.setIsPayToPlatform(true);
			}

			validate(order);
			orderMapper.insert(order);

			OrderItem orderItem = new OrderItem();
			orderItem.setPrice(v4Price);
			orderItem.setOrderId(order.getId());
			orderItem.setProductId(productId);
			orderItem.setMarketPrice(product.getMarketPrice());
			orderItem.setQuantity(quantity);
			orderItem.setAmount(v4Amount);
			orderItem.setTitle(product.getTitle());
			orderItem.setImage(product.getImage1());
			validate(orderItem);
			orderItemMapper.insert(orderItem);

			if (persistentOrder.getIsPayToPlatform()) {
				malComponent.successOrder(order.getId());
			}
			
			persistentOrder.setIsCopied(true);
			persistentOrder.setCopiedTime(date);
			persistentOrder.setRefId(order.getId());

			if (orderMapper.update(persistentOrder) == 0) {
				throw new ConcurrentException();
			}
			
			return order.getId();
		} else {
			throw new BizException(BizCode.ERROR, "不符合转订单条件");
		}

	}

	private Long calculateV4UserId(User user) {
		Long parentId = user.getParentId();
		int whileTimes = 0;
		while (parentId != null) {
			if (whileTimes > 1000) {
				throw new BizException(BizCode.ERROR, "循环引用错误, user id is " + user.getId());
			}
			User parent = userMapper.findOne(parentId);
			if (parent.getUserRank() == UserRank.V4) {
				return parentId;
			}
			parentId = parent.getParentId();
			whileTimes ++;
		}
		return null;
	}

	private Long calculateRootId(User user) {
		if (user.getIsRoot() != null && user.getIsRoot()) {
			return user.getId();
		} else {
			Long parentId = user.getParentId();
			int whileTimes = 0;
			while (parentId != null) {
				if (whileTimes > 1000) {
					throw new BizException(BizCode.ERROR, "循环引用错误, user id is " + user.getId());
				}
				User parent = userMapper.findOne(parentId);
				if (parent.getIsRoot() != null && parent.getIsRoot()) {
					return parentId;
				}
				parentId = parent.getParentId();
				whileTimes++;
			}
			return null;
		}
	}

	@Override
	public void settleUpProfit(@NotNull Long orderId) {
		Order order = orderMapper.findOne(orderId);
		validate(order, NOT_NULL, "order id" + orderId + " is not found");
		if (order.getIsProfitSettledUp()) {
			return; // 幂等操作
		}
		if (order.getOrderStatus() != OrderStatus.已支付 && order.getOrderStatus() != OrderStatus.已发货 && order.getOrderStatus() != OrderStatus.已完成) {
			throw new BizException(BizCode.ERROR, "只有已支付订单才能结算奖励");
		}

		OrderItem orderItem = orderItemMapper.findByOrderId(orderId).get(0);

		Long quantity = orderItem.getQuantity();
		@SuppressWarnings("unused")
		Long productId = orderItem.getProductId();
		Long buyerId = order.getUserId();
		Long sellerId = order.getSellerId();
		User buyer = userMapper.findOne(buyerId);
		@SuppressWarnings("unused")
		User seller = userMapper.findOne(sellerId);
		UserRank buyerUserRank = order.getBuyerUserRank();
		@SuppressWarnings("unused")
		UserRank sellerUserRank = order.getSellerUserRank();
		@SuppressWarnings("unused")
		Long sysUserId = config.getSysUserId();

		Date paidTime = order.getPaidTime();

		if (config.isOld(productId)) {

			/* 销量奖 */
			if (buyerUserRank == UserRank.V4) {
				final BigDecimal saleBonus = new BigDecimal("8.00").multiply(BigDecimal.valueOf(quantity));
				fncComponent.createProfit(buyerId, Profit.ProfitType.销量奖, orderId, "销量奖", CurrencyType.积分, saleBonus, paidTime, null);
			}

			/* 一级平级奖 */
			if (buyerUserRank == UserRank.V3) {
				Long buyerParentId = buyer.getParentId();
				if (buyerParentId != null) {
					User buyerParent = userMapper.findOne(buyerParentId);
					if (buyerParent.getUserRank() == UserRank.V3) {
						final BigDecimal v3FlatBonus = new BigDecimal("7.00").multiply(BigDecimal.valueOf(quantity));
						fncComponent.createTransfer(sellerId, buyerParentId, Transfer.TransferType.一级平级奖, orderId, "一级平级奖", CurrencyType.积分, v3FlatBonus, paidTime);
					}
				}
			}

			/* 特级平级奖 + 一级越级奖 */
			if (buyerUserRank == UserRank.V4) {

				int index = 0;
				final BigDecimal v4FlatBonus1 = new BigDecimal("6.00").multiply(BigDecimal.valueOf(quantity));
				final BigDecimal v4FlatBonus2 = new BigDecimal("4.00").multiply(BigDecimal.valueOf(quantity));
				final BigDecimal v4FlatBonus3 = new BigDecimal("4.00").multiply(BigDecimal.valueOf(quantity));

				final BigDecimal v3SkipBonus = new BigDecimal("3.00").multiply(BigDecimal.valueOf(quantity));

				Long parentId = buyer.getParentId();


				Long skipBonusUserId = null;
				boolean firstParent = true;

				int whileTimes = 0;
				while (parentId != null) {
					if (whileTimes > 1000) {
						throw new BizException(BizCode.ERROR, "循环引用"); // 防御性校验
					}
					User parent = userMapper.findOne(parentId);
					if (firstParent) {
						if (parent.getUserRank() == UserRank.V3) {
							Date lastUpgradedTime = parent.getLastUpgradedTime();
							if (lastUpgradedTime != null && DateUtils.addMonths(lastUpgradedTime, 3).after(new Date())) {
								skipBonusUserId = parentId;
							}
						}
						firstParent = false;
					}

					if (parent.getUserRank() == UserRank.V4) {
						index++;
						if (index == 0) {
							continue;
						} else if (index == 1) {
							fncComponent.createProfit(parentId, Profit.ProfitType.特级平级奖, orderId, "特级平级奖", CurrencyType.积分, v4FlatBonus1, paidTime, null);
							if (skipBonusUserId != null) {
								/* 一级越级奖 */
								fncComponent.createTransfer(parentId, skipBonusUserId, Transfer.TransferType.一级越级奖, orderId, "一级越级奖", CurrencyType.积分, v3SkipBonus, paidTime);
							}

						} else if (index == 2) {
							fncComponent.createProfit(parentId, Profit.ProfitType.特级平级奖, orderId, "特级平级奖", CurrencyType.积分, v4FlatBonus2, paidTime, null);
						} else if (index == 3) {
							fncComponent.createProfit(parentId, Profit.ProfitType.特级平级奖, orderId, "特级平级奖", CurrencyType.积分, v4FlatBonus3, paidTime, null);
						} else {
							break;
						}

					}
					parentId = parent.getParentId();
					whileTimes++;
				}
			}

			order.setIsProfitSettledUp(true);
			if (orderMapper.update(order) == 0) {
				throw new ConcurrentException();
			}
		} else {
			/* 平级推荐奖 */
			if(buyerUserRank == UserRank.V3) {
				Long parentId = buyer.getParentId();
				if(parentId != null) {
					User buyerParent = userMapper.findOne(parentId);
					if(buyerParent.getUserRank() != UserRank.V4) {

						int whileTimes = 0;
						while (parentId != null) {
							if (whileTimes > 1000) {
								throw new BizException(BizCode.ERROR, "循环引用"); // 防御性校验
							}

							buyerParent = userMapper.findOne(parentId);
							if(buyerParent.getUserRank() == UserRank.V4) {
								final BigDecimal saleBonus = new BigDecimal("10.00").multiply(BigDecimal.valueOf(quantity));
								BigDecimal fee = saleBonus.multiply(FEE_RATE);
								BigDecimal saleBonusAfter = saleBonus.subtract(fee);
								fncComponent.createProfit(parentId, Profit.ProfitType.平级推荐奖, orderId, "平级推荐奖", CurrencyType.积分, saleBonusAfter, paidTime, "已扣除手续费:" + fee + ";费率: " + FEE_RATE);

								final BigDecimal flatBonus = new BigDecimal("9.00").multiply(BigDecimal.valueOf(quantity));
								BigDecimal flatBonusAfter = flatBonus.subtract(flatBonus.multiply(FEE_RATE));
								fncComponent.createTransfer(parentId, buyer.getParentId(), Transfer.TransferType.平级推荐奖, orderId, "平级推荐奖", CurrencyType.积分, flatBonusAfter, paidTime);
								break;
							}

							parentId = buyerParent.getParentId();
							whileTimes++;
						}

					}
				}

			}

			/* 特级推荐奖 */
			if(buyerUserRank == UserRank.V4) {

				Long parentId = buyer.getParentId();
				User buyerParent = null;
				boolean foundV4Parent = false;
				int whileTimes = 0;
				while (parentId != null && !foundV4Parent) {
					if (whileTimes > 1000) {
						throw new BizException(BizCode.ERROR, "循环引用"); // 防御性校验
					}

					buyerParent = userMapper.findOne(parentId);
					if(buyerParent.getUserRank() == UserRank.V4) {
						foundV4Parent = true;
					}

					if(foundV4Parent) {
						final BigDecimal saleBonus = new BigDecimal("6.00").multiply(BigDecimal.valueOf(quantity));
						BigDecimal fee = saleBonus.multiply(FEE_RATE);
						BigDecimal saleBonusAfter = saleBonus.subtract(fee);
						fncComponent.createProfit(parentId, Profit.ProfitType.特级推荐奖, orderId, "特级推荐奖", CurrencyType.积分, saleBonusAfter, paidTime, "已扣除手续费:" + fee + ";费率: " + FEE_RATE);
						break;
					}

					parentId = buyerParent.getParentId();
					whileTimes++;
				}

				if(foundV4Parent) {
					if(!parentId.equals(buyer.getParentId())) {
						Date lastUpgradedTime = seller.getLastUpgradedTime();
						if (lastUpgradedTime != null && DateUtils.addMonths(lastUpgradedTime, 3).after(new Date())) {
							final BigDecimal saleBonus = new BigDecimal("3.00").multiply(BigDecimal.valueOf(quantity));
							fncComponent.createTransfer(parentId, buyer.getParentId(), Transfer.TransferType.特级推荐奖, orderId, "特级推荐奖", CurrencyType.积分, saleBonus, paidTime);
						}
					}
				}
			}
			order.setIsProfitSettledUp(true);
			if (orderMapper.update(order) == 0) {
				throw new ConcurrentException();
			}
		}

	}

	private BigDecimal getMonthlyRate(BigDecimal totalQuantity) {
		BigDecimal lvl1 = new BigDecimal("1200.00");
		BigDecimal lvl2 = new BigDecimal("2400.00");
		BigDecimal lvl3 = new BigDecimal("4800.00");
		BigDecimal lvl4 = new BigDecimal("9600.00");
		BigDecimal lvl5 = new BigDecimal("19200.00");
		BigDecimal lvl6 = new BigDecimal("38400.00");
		BigDecimal lvl7 = new BigDecimal("57600.00");
		BigDecimal lvl8 = new BigDecimal("86400.00");
		BigDecimal lvl9 = new BigDecimal("129600.00");
		BigDecimal lvl10= new BigDecimal("200000.00");
		BigDecimal lvl11 = new BigDecimal("300000.00");

		BigDecimal rate0 = new BigDecimal("0.03");
		BigDecimal rate1 = new BigDecimal("0.05");
		BigDecimal rate2 = new BigDecimal("0.07");
		BigDecimal rate3 = new BigDecimal("0.09");
		BigDecimal rate4 = new BigDecimal("0.11");
		BigDecimal rate5 = new BigDecimal("0.12");
		BigDecimal rate6 = new BigDecimal("0.13");
		BigDecimal rate7 = new BigDecimal("0.14");
		BigDecimal rate8 = new BigDecimal("0.15");
		BigDecimal rate9 = new BigDecimal("0.16");
		BigDecimal rate10 = new BigDecimal("0.17");
		BigDecimal rate11 = new BigDecimal("0.18");

		BigDecimal rate;

		if (totalQuantity.compareTo(lvl1) < 0) {
			rate = rate0;
		} else if (totalQuantity.compareTo(lvl1) >= 0 && totalQuantity.compareTo(lvl2) <= 0){
			rate = rate1;
		} else if (totalQuantity.compareTo(lvl2) > 0 && totalQuantity.compareTo(lvl3) <= 0){
			rate = rate2;
		} else if (totalQuantity.compareTo(lvl3) > 0 && totalQuantity.compareTo(lvl4) <= 0){
			rate = rate3;
		} else if (totalQuantity.compareTo(lvl4) > 0 && totalQuantity.compareTo(lvl5) <= 0){
			rate = rate4;
		} else if (totalQuantity.compareTo(lvl5) > 0 && totalQuantity.compareTo(lvl6) <= 0){
			rate = rate5;
		} else if (totalQuantity.compareTo(lvl6) > 0 && totalQuantity.compareTo(lvl7) <= 0){
			rate = rate6;
		} else if (totalQuantity.compareTo(lvl7) > 0 && totalQuantity.compareTo(lvl8) <= 0){
			rate = rate7;
		} else if (totalQuantity.compareTo(lvl8) > 0 && totalQuantity.compareTo(lvl9) <= 0){
			rate = rate8;
		} else if (totalQuantity.compareTo(lvl9) > 0 && totalQuantity.compareTo(lvl10) <= 0){
			rate = rate9;
		} else if (totalQuantity.compareTo(lvl10) > 0 && totalQuantity.compareTo(lvl11) <= 0){
			rate = rate10;
		} else {
			rate = rate11;
		}
		return rate;
	}

	@Override
	public void settleUpMonthly(@NotBlank String yearAndMonth) {

		DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM");
		TemporalAccessor temporalAccessor = df.parse(yearAndMonth);
		Predicate<User> predicate = v -> {
			User.UserRank userRank = v.getUserRank();
			if (userRank != null && userRank == UserRank.V4) {
				return true;
			}
			return false;
		};

		int year = temporalAccessor.get(ChronoField.YEAR);
		int month = temporalAccessor.get(ChronoField.MONTH_OF_YEAR);

		YearMonth yearMonth = YearMonth.of(year, month);
		YearMonth nowYearMonth = YearMonth.now();
		if (!nowYearMonth.isAfter(yearMonth)) {
			throw new BizException(BizCode.ERROR, "未达到发放时间");
		}

		OrderMonthlySettlement orderMonthlySettlement = orderMonthlySettlementMapper.findByYearAndMonth(yearAndMonth);
		if (orderMonthlySettlement != null) {
			return; // 幂等操作
		}

		LocalDate beginDate = LocalDate.of(year, month, 1);
		LocalDate endDate = beginDate.with(TemporalAdjusters.firstDayOfNextMonth());

		LocalDateTime beginDateTime = LocalDateTime.of(beginDate, LocalTime.MIN);
		LocalDateTime endDateTime = LocalDateTime.of(endDate, LocalTime.MIN);

		Date begin = Date.from(beginDateTime.atZone(ZoneId.systemDefault()).toInstant());
		Date end = Date.from(endDateTime.atZone(ZoneId.systemDefault()).toInstant());

		List<Order> orders = orderMapper.findAll(OrderQueryModel.builder()
				.orderStatusIN(new OrderStatus[] {OrderStatus.已支付, OrderStatus.已发货, OrderStatus.已完成})
				.productIdEQ(2L)  //月结算只针对2.0的产品
				.paidTimeGTE(begin).paidTimeLT(end).build());
		orders = orders.stream().filter(v -> {
			OrderStatus orderStatus = v.getOrderStatus();
			if(orderStatus == OrderStatus.已发货 || orderStatus == OrderStatus.已完成) {
				return Constants.SETTING_SUPER_ADMIN_ID.equals(v.getSellerId());
			}
			return true;
		}).collect(Collectors.toList());
		List<User> users = userMapper.findAll(UserQueryModel.builder().userTypeEQ(User.UserType.代理).build());
		List<User> v4Users = users.stream().filter(predicate).collect(Collectors.toList());
		Map<Long, User> userMap = users.stream().collect(Collectors.toMap(User::getId, Function.identity()));

		Map<Long, TeamModel> teamMap = v4Users.stream()
				.map(v -> {
					TeamModel teamModel = new TeamModel();
					teamModel.setUser(v);
					teamModel.setV4Children(TreeHelper.sortBreadth2(v4Users, String.valueOf(v.getId()), u -> {
						TreeNode treeNode = new TreeNode();
						treeNode.setId(String.valueOf(u.getId()));
						Long directV4ParentId = getDirectV4ParentId(predicate, userMap, u);
						treeNode.setParentId(u.getParentId() == null ? null : String.valueOf(directV4ParentId));
						return treeNode;

					}));
					teamModel.setDirectV4Children(users.stream().filter(u -> {
						Long userId = u.getId();
						if (userId.equals(v.getId())) {
							return false;
						}
						Long directV4ParentId = null;
						int times = 0;
						Long parentId = u.getParentId();
						while(parentId != null) {
							if (times > 1000) {
								throw new BizException(BizCode.ERROR, "循环引用");
							}
							User parent = userMap.get(parentId);
							if (predicate.test(parent)) {
								directV4ParentId = parentId;
								break;
							}
							parentId = parent.getParentId();
							times ++;
						}
						if (directV4ParentId != null && directV4ParentId.equals(v.getId())) {
							return true;
						}
						return false;
					}).collect(Collectors.toList()));
					return teamModel;
				}).collect(Collectors.toMap(v -> v.getUser().getId(), Function.identity()));

		Map<Long, Long> userQuantityMap = orders.stream().collect(Collectors.toMap(Order::getUserId, Order::getQuantity, (x , y) -> x + y));
		userQuantityMap.entrySet().stream().forEach(v -> {
			if (v.getValue() > 0) {
				System.out.println(userMap.get(v.getKey()).getNickname() + "个人销量" + v.getValue() + "元");
			}
		});

		Map<Long, Long> teamQuantityMap = v4Users.stream().collect(Collectors.toMap(User::getId, v -> {
			Long userId = v.getId();
			Long myQuantity = userQuantityMap.get(userId) == null ? 0L : userQuantityMap.get(userId);
			Long teamQuantity = teamMap.get(userId).getV4Children().stream()
					.map(u -> userQuantityMap.get(u.getId()) == null ? 0L : userQuantityMap.get(u.getId()))
					.reduce(0L, (x, y) -> x + y);
			return teamQuantity + myQuantity;
		}));

		teamQuantityMap.entrySet().stream().forEach(v -> {
			if (v.getValue() > 0) {
				logger.error(userMap.get(v.getKey()).getNickname() + "团队业绩" + v.getValue() + "次");
			}
		});

		BigDecimal rate = new BigDecimal("128");
		Map<Long, BigDecimal> profitMap = v4Users.stream().collect(Collectors.toMap(User::getId, v -> {
			Long userId = v.getId();
			BigDecimal quantity = new BigDecimal(teamQuantityMap.get(userId));
			BigDecimal rate1 = getMonthlyRate(quantity);
			BigDecimal profit = quantity.multiply(rate1).multiply(rate);
			for (User user : teamMap.get(userId).getDirectV4Children()) {
				if (predicate.test(user)) {
					Long childUserId = user.getId();
					BigDecimal childQuantity = new BigDecimal(teamQuantityMap.get(childUserId));
					BigDecimal childRate = getMonthlyRate(childQuantity);
					BigDecimal childProfit = childQuantity.multiply(childRate).multiply(rate);
					profit = profit.subtract(childProfit);
				}
			}

			return profit;
		}));

		BigDecimal zero = new BigDecimal("0.00");
		Date now = new Date();
		for (Map.Entry<Long, BigDecimal> entry : profitMap.entrySet()) {
			BigDecimal amount = entry.getValue();
			Long userId = entry.getKey();
			if (amount.compareTo(zero) > 0) {
				try {TimeUnit.MILLISECONDS.sleep(100);} catch (InterruptedException e1) {}
				BigDecimal fee = amount.multiply(FEE_RATE);
				BigDecimal amountAfter = amount.subtract(fee);
				fncComponent.createProfit(userId, Profit.ProfitType.返利奖, null, year + "年" + month + "返利奖", CurrencyType.积分, amountAfter, now, "已扣除手续费:" + fee + ";费率: " + FEE_RATE);
				logger.error(userMap.get(userId).getNickname() + "返利奖" + amount + "积分");
			}
		}

		/* 期权奖励 */
		LongSummaryStatistics summaryStatistics = orders.stream().mapToLong((x) -> x.getQuantity()).summaryStatistics();
		long companySales = summaryStatistics.getSum();
		logger.error("公司总销量:" + companySales);
		Map<Long, BigDecimal> profitShareMap = v4Users.stream().collect(Collectors.toMap(User::getId, v -> {
			Long userId = v.getId();
			Long userQuantity = userQuantityMap.get(userId);
			if(userQuantity != null) {
				BigDecimal quantity = new BigDecimal(userQuantity);
				BigDecimal profit = quantity.multiply(new BigDecimal("0.4"));  //特级服务商：每人新增服务量*0.4股计算；
				if(v.getIsDirector()) {  //联席董事: 每人新增服务量*0.4股+公司月总服务量*0.6股；
					BigDecimal company = new BigDecimal(companySales).multiply(new BigDecimal("0.6"));
					profit = profit.add(company);
				}
				return profit;
			} else {
				return new BigDecimal("0.00");
			}

		}));
		logger.error("profitShareMap end........");
		for (Map.Entry<Long, BigDecimal> entry : profitShareMap.entrySet()) {
			BigDecimal amount = entry.getValue();
			Long userId = entry.getKey();
			if (amount.compareTo(zero) > 0) {
				try {TimeUnit.MILLISECONDS.sleep(100);} catch (InterruptedException e1) {}
				BigDecimal fee = amount.multiply(FEE_RATE);
				BigDecimal amountAfter = amount.subtract(fee);
				fncComponent.createProfit(userId, Profit.ProfitType.期权奖励, null, year + "年" + month + "期权奖励", CurrencyType.货币期权, amountAfter, now, "已扣除手续费:" + fee + ";费率: " + FEE_RATE);
				logger.error(userMap.get(userId).getNickname() + "期权奖励" + amount + "货币期权");
			}
		}

		/* 董事贡献奖 */
		List<User> v4Directors = v4Users.stream().filter(v -> v.getIsDirector()).collect(Collectors.toList());
		Map<Long, TeamModel> teamV4Map = v4Directors.stream()
				.map(v -> {
					TeamModel teamModel = new TeamModel();
					teamModel.setUser(v);
					teamModel.setV4Children(sortBreadth2(v4Users, String.valueOf(v.getId()), u -> {
						TreeNode treeNode = new TreeNode();
						treeNode.setId(String.valueOf(u.getId()));
						Long directV4ParentId = getDirectV4ParentId(predicate, userMap, u);
						treeNode.setParentId(u.getParentId() == null ? null : String.valueOf(directV4ParentId));
						return treeNode;

					}));
					return teamModel;
				}).collect(Collectors.toMap(v -> v.getUser().getId(), Function.identity()));

		Map<Long, BigDecimal> profitDirectorMap = v4Directors.stream().collect(Collectors.toMap(User::getId, v -> {
			Long userId = v.getId();
			Long myQuantity = userQuantityMap.get(userId) == null ? 0L : userQuantityMap.get(userId);
			BigDecimal profit = new BigDecimal(myQuantity).multiply(new BigDecimal("2.00"));
			BigDecimal teamProfit = teamV4Map.get(userId).getV4Children().stream()
					.map(u -> {
						Long quantity = userQuantityMap.get(u.getId()) == null ? 0L : userQuantityMap.get(u.getId());
						BigDecimal innerProfit = new BigDecimal("0.00");
						if(u.getIsDirector()) {
							innerProfit = new BigDecimal(quantity);
						} else {
							innerProfit = new BigDecimal(quantity).multiply(new BigDecimal("2.00"));
						}
						return innerProfit;
					})
					.reduce(new BigDecimal("0.00"), BigDecimal::add);
					return profit.add(teamProfit);
		}));

		for (Map.Entry<Long, BigDecimal> entry : profitDirectorMap.entrySet()) {
			BigDecimal amount = entry.getValue();
			Long userId = entry.getKey();
			if (amount.compareTo(zero) > 0) {
				try {TimeUnit.MILLISECONDS.sleep(100);} catch (InterruptedException e1) {}
				BigDecimal fee = amount.multiply(FEE_RATE);
				BigDecimal amountAfter = amount.subtract(fee);
				fncComponent.createProfit(userId, Profit.ProfitType.董事贡献奖, null, year + "年" + month + "董事贡献奖", CurrencyType.积分, amountAfter, now, "已扣除手续费:" + fee + ";费率: " + FEE_RATE);
				logger.error(userMap.get(userId).getNickname() + "董事贡献奖" + amount + "积分");
			}
		}

		orderMonthlySettlement = new OrderMonthlySettlement();
		orderMonthlySettlement.setYearAndMonth(yearAndMonth);
		orderMonthlySettlement.setSettledUpTime(now);
		orderMonthlySettlementMapper.insert(orderMonthlySettlement);
	}

	@Override
	public void settleUp(@NotNull Long orderId) {
		Order order = orderMapper.findOne(orderId);
		validate(order, NOT_NULL, "order id" + orderId + " is not found");
		if (order.getIsSettledUp()) {
			return; // 幂等操作
		}
		if (order.getOrderStatus() != OrderStatus.已完成) {
			throw new BizException(BizCode.ERROR, "只有已完成订单才能结算");
		}

		OrderItem orderItem = orderItemMapper.findByOrderId(orderId).get(0);

		Long quantity = orderItem.getQuantity();
		Long productId = orderItem.getProductId();
		Long buyerId = order.getUserId();
		Long sellerId = order.getSellerId();
		User buyer = userMapper.findOne(buyerId);
		User seller = userMapper.findOne(sellerId);
		@SuppressWarnings("unused")
		UserRank buyerUserRank = buyer.getUserRank();
		@SuppressWarnings("unused")
		UserRank sellerUserRank = seller.getUserRank();
		Long sysUserId = config.getSysUserId();

		Date paidTime = order.getPaidTime();

		/* 订单收款 */
		if (order.getIsPayToPlatform()) {
			BigDecimal amount = order.getAmount();
			if (sellerId.equals(sysUserId)) { // 平台收款
				/*fncComponent.createAndGrantProfit(sysUserId, Profit.ProfitType.平台收款, orderId, "订单" + order.getSn() + "平台收款", currencyType, amount);*/
			} else {
				BigDecimal v4Amount = malComponent.getPrice(productId, UserRank.V4, quantity).multiply(BigDecimal.valueOf(quantity)).setScale(2, BigDecimal.ROUND_HALF_UP);
				if (order.getIsCopied()) {
					amount = amount.subtract(v4Amount);
					/*fncComponent.createAndGrantProfit(sysUserId, Profit.ProfitType.平台收款, orderId, "订单" + order.getSn() + "平台收款", currencyType, v4Amount);*/
				}
				fncComponent.createAndGrantProfit(sellerId, Profit.ProfitType.订单收款, orderId, "订单" + order.getSn() + "收款", CurrencyType.积分, amount, paidTime);
			}
		}

		/* 邮费 */
		if (order.getIsUseLogistics() && order.getIsBuyerPayLogisticsFee()) {
			BigDecimal logisticsFee = order.getLogisticsFee();
			if (logisticsFee.compareTo(new BigDecimal("0.00")) > 0) {
				fncComponent.createTransfer(buyerId, sysUserId, Transfer.TransferType.邮费, orderId, "邮费", CurrencyType.积分, logisticsFee, paidTime);
			}
		}

		order.setIsSettledUp(true);
		if (orderMapper.update(order) == 0) {
			throw new ConcurrentException();
		}

	}

	@Override
	public List<Order> findAll(@NotNull OrderQueryModel orderQueryModel) {
		if (orderQueryModel.getOrderBy() == null) {
			orderQueryModel.setOrderBy("createdTime");
		}
		return this.orderMapper.findAll(orderQueryModel);
	}

	@Override
	public long count(OrderQueryModel build) {
		return orderMapper.count(build);
	}

	@Override
	public OrderSumDto sum(OrderQueryModel orderQueryModel) {
		return orderMapper.orderSum(orderQueryModel);
	}
	
	@Override
	public void offlinePay(Long orderId, String offlineImage, String offlineMemo){

		Order order = orderMapper.findOne(orderId);
		validate(order, NOT_NULL, "order id " + orderId + " is not found");

		OrderStatus orderStatus = order.getOrderStatus();
		if (orderStatus != OrderStatus.待支付)  {
			throw new BizException(BizCode.ERROR, "只有待支付状态的订单才能操作");
		}
		if(order.getIsPayToPlatform()){
			Payment payment = new Payment();
			payment.setAmount1(order.getAmount());
			payment.setCurrencyType1(CurrencyType.现金);
			payment.setPaymentType(PaymentType.订单支付);
			payment.setRefId(orderId);
			payment.setUserId(order.getUserId());
			payment.setTitle(order.getTitle());
			payment.setPayType(PayType.银行汇款);
			payment.setExpiredTime(DateUtils.addMinutes(new Date(), Constants.SETTING_PAYMENT_OFFLINE_EXPIRE_IN_MINUTES));
			payment.setSn(ServiceUtils.generatePaymentSn());
			payment.setCreatedTime(new Date());
			payment.setOfflineMemo(offlineMemo);
			payment.setOfflineImage(offlineImage);
			payment.setPaymentStatus(PaymentStatus.待确认);
			payment.setVersion(0);
			validate(payment);
			paymentMapper.insert(payment);
		}
		order.setOfflineMemo(offlineMemo);
		order.setOfflineImage(offlineImage);
		order.setOrderStatus(OrderStatus.待确认);
		if (orderMapper.update(order) == 0) {
			throw new ConcurrentException();
		}
	
	}

	@Override
	public void delete(Long orderId) {
		Order order = orderMapper.findOne(orderId);
		validate(order, NOT_NULL, "order id " + orderId + " is not found");
		if (order.getIsDeleted()) {
			return; // 幂等操作
		}
		order.setIsDeleted(true);
		if (orderMapper.update(order) == 0) {
			throw new ConcurrentException();
		}
	}

	@Override
	public void undelete(Long orderId) {
		Order order = orderMapper.findOne(orderId);
		validate(order, NOT_NULL, "order id " + orderId + " is not found");
		if (!order.getIsDeleted()) {
			return; // 幂等操作
		}
		order.setIsDeleted(false);
		if (orderMapper.update(order) == 0) {
			throw new ConcurrentException();
		}
	}

	/**
	 * 查询销量
	 * @return
     */
	@Override
	public Map<String, Object> querySalesVolume(OrderQueryModel orderQueryModel) {
		Map<String ,Object> returnMap = new HashMap<>();
		int moth = DateUtil.getMoth(new Date());
		Long salesVolumeData [] = new Long[moth-1];
		Long shipmentData [] = new Long[moth-1];
        double svData [] = new double[moth-1];
        double sData [] = new double[moth-1];
        Long salesVolumeTeamData [] = new Long[moth-1];
        Long shipmentTeamData [] = new Long[moth-1];
		for (int i = moth - 1; i >= 1;i--){
			orderQueryModel.setPaidTimeGTE(DateUtil.getBeforeMonthBegin(new Date(),0-i,0));
			orderQueryModel.setPaidTimeLT(DateUtil.getBeforeMonthEnd(new Date(),0-(i-1),0));
			Long data = 0l;
			//进货量
			Long salesVolume  = orderMapper.queryRetailPurchases(orderQueryModel);
			data = salesVolume;
			salesVolumeData[i-1] = data;
			//出货量
			Long shipment  = orderMapper.queryShipment(orderQueryModel);
			data = shipment;
			shipmentData[i-1] = data;
		}
		//计算环比
		for (int i = moth - 1; i >= 1;i--){
			double data = 0d;
			//进货量环比
			double sv = 0.00d;
			if (i-2 >= 0 && salesVolumeData [i-2] != 0){
				sv = new BigDecimal((salesVolumeData [i-1] - salesVolumeData [i-2]) / salesVolumeData [i-2] * 100).setScale(2 , RoundingMode.UP).doubleValue()  ;
			}else if (i-2 >= 0 && salesVolumeData [i-2] == 0 && salesVolumeData [i-2] > 0){
				sv = 100;
			}
			data = sv;
			svData[i-1] = data;
			//出货量环比
			double s = 0.00d;
			if (i-2 >= 0 && shipmentData [i-2] != 0){
				s = new BigDecimal((shipmentData [i-1] - shipmentData [i-2]) / shipmentData [i-2] * 100 ).setScale(2 , RoundingMode.UP).doubleValue() ;
			}else if (i-2 >= 0 && shipmentData [i-2] == 0 && shipmentData [i-1] > 0){
				s = 100;
			}
			data = s;
			sData[i-1] = data;
		}

		//查询我的团队进、出货量
		List<Long> userIdList = new ArrayList<>();
		List<User> userList = new ArrayList<>();
		List<User> users = userService.findAll(new UserQueryModel());
		List<User> children = TreeHelper.sortBreadth2(users, orderQueryModel.getUserIdEQ().toString(), v -> {
			TreeNode treeNode = new TreeNode();
			treeNode.setId(v.getId().toString());
			treeNode.setParentId(v.getParentId() == null ? null : v.getParentId().toString());
			return treeNode;
		});
		userList = children.stream().filter(v -> v.getUserRank() == User.UserRank.V4).collect(Collectors.toList());
        if (userList.size() > 0 && userList != null){
            for (User user: userList) {
                userIdList.add(user.getId());
            }
            orderQueryModel.setUserIdList(userIdList);
            orderQueryModel.setSellerIdList(userIdList);
            for (int i = moth - 1; i >= 1;i--){
                orderQueryModel.setPaidTimeGTE(DateUtil.getBeforeMonthBegin(new Date(),0-i,0));
                orderQueryModel.setPaidTimeLT(DateUtil.getBeforeMonthEnd(new Date(),0-(i-1),0));
                Long data = 0l;
                //进货量
                Long salesVolumeTeam  = orderMapper.queryRetailPurchases(orderQueryModel);
                data = salesVolumeTeam;
                salesVolumeTeamData[i-1] = data;
                //出货量
                Long shipmentTeam  = orderMapper.queryShipment(orderQueryModel);
                data = shipmentTeam;
                shipmentTeamData[i-1] = data;
            }
        }
		returnMap.put("salesVolumeData", salesVolumeData);
		returnMap.put("shipmentData", shipmentData);
		returnMap.put("svData", svData);
		returnMap.put("sData", sData);
        returnMap.put("salesVolumeTeamData", salesVolumeTeamData);
        returnMap.put("shipmentTeamData", shipmentTeamData);
		return returnMap;
	}

    @Override
    public Map<String, Object> querySalesVolumeDetail(OrderQueryModel orderQueryModel) {
        Map<String ,Object> returnMap = new HashMap<>();
        int moth = DateUtil.getMoth(new Date());
        Long salesVolumeData [] = new Long[moth-1];
        Long shipmentData [] = new Long[moth-1];
        for (int i = moth - 1; i >= 1;i--){
            orderQueryModel.setPaidTimeGTE(DateUtil.getBeforeMonthBegin(new Date(),0-i,0));
            orderQueryModel.setPaidTimeLT(DateUtil.getBeforeMonthEnd(new Date(),0-(i-1),0));
            Long data = 0l;
            //进货量
            Long salesVolume  = orderMapper.queryRetailPurchases(orderQueryModel);
            data = salesVolume;
            salesVolumeData[i-1] = data;
            //出货量
            Long shipment  = orderMapper.queryShipment(orderQueryModel);
            data = shipment;
            shipmentData[i-1] = data;
        }
        returnMap.put("salesVolumeData", salesVolumeData);
        returnMap.put("shipmentData", shipmentData);
        return returnMap;
    }

    private static List<User> sortBreadth2(Collection<User> entities, String parentId, TreeNodeResolver<User> treeNodeResolver) {
		List<User> result = new ArrayList<>();
		Map<String, List<User>> childrenMap = entities.stream().collect(Collectors.groupingBy(v -> treeNodeResolver.apply(v).getParentId() == null ? "DEFAULT_NULL_KEY" : treeNodeResolver.apply(v).getParentId()));
		sortBreadth2(childrenMap, result, parentId, treeNodeResolver);
		return result;
	}

	private static void sortBreadth2(Map<String, List<User>> childrenMap, Collection<User> result, String parentId, TreeNodeResolver<User> treeNodeResolver) {
		parentId = parentId == null ? "DEFAULT_NULL_KEY" : parentId;
		List<User> plist = childrenMap.get(parentId);
		if (plist == null) {
			return;
		}
		plist = plist.stream().filter(u -> !u.getIsDirector()).collect(Collectors.toList());
		result.addAll(plist);
		for (User user : plist) {
			sortBreadth2(childrenMap, result, treeNodeResolver.apply(user).getId(), treeNodeResolver);
		}
	}

	private Long getDirectV4ParentId(Predicate<User> predicate, Map<Long, User> userMap, User u) {
		int times = 0;
		Long directV4ParentId = null;
		Long parentId = u.getParentId();
		while(parentId != null) {
			if (times > 1000) {
				throw new BizException(BizCode.ERROR, "循环引用");
			}
			User parent = userMap.get(parentId);
			if(parent != null) {
				if (predicate.test(parent)) {
					directV4ParentId = parentId;
					break;
				}
			}
			parentId = parent.getParentId();
			times ++;
		}
		return directV4ParentId;
	}
}
