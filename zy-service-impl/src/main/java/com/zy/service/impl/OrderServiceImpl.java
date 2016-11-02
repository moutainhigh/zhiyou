package com.zy.service.impl;

import com.zy.Config;
import com.zy.ServiceUtils;
import com.zy.common.exception.BizException;
import com.zy.common.exception.ConcurrentException;
import com.zy.common.model.query.Page;
import com.zy.component.FncComponent;
import com.zy.component.MalComponent;
import com.zy.entity.fnc.*;
import com.zy.entity.fnc.Payment.PaymentStatus;
import com.zy.entity.fnc.Payment.PaymentType;
import com.zy.entity.mal.Order;
import com.zy.entity.mal.Order.OrderStatus;
import com.zy.entity.mal.OrderItem;
import com.zy.entity.mal.Product;
import com.zy.entity.usr.Address;
import com.zy.entity.usr.User;
import com.zy.entity.usr.User.UserRank;
import com.zy.extend.Producer;
import com.zy.mapper.*;
import com.zy.model.BizCode;
import com.zy.model.Constants;
import com.zy.model.dto.OrderCreateDto;
import com.zy.model.dto.OrderDeliverDto;
import com.zy.model.dto.OrderSumDto;
import com.zy.model.query.OrderQueryModel;
import com.zy.service.OrderService;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import static com.zy.common.util.ValidateUtils.*;

@Service
@Validated
public class OrderServiceImpl implements OrderService {

	@Autowired
	private OrderMapper orderMapper;
	
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
	private MalComponent malComponent;

	@Autowired
	private FncComponent fncComponent;
	
	@Autowired
	private Producer producer;

	@Override
	public Order create(@NotNull OrderCreateDto orderCreateDto) {
		validate(orderCreateDto);

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
		if (userRank == UserRank.V0) {
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

		Order order = new Order();
		order.setUserId(userId);
		order.setAmount(amount);
		order.setCreatedTime(new Date());
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
		order.setVersion(0);
		order.setSellerId(sellerId);
		order.setCurrencyType(CurrencyType.现金);
		order.setSn(ServiceUtils.generateOrderSn());
		order.setIsSettledUp(false);
		order.setDiscountFee(new BigDecimal("0.00"));
		order.setExpiredTime(DateUtils.addMinutes(new Date(), Constants.SETTING_ORDER_EXPIRE_IN_MINUTES));
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
		Long quantity = persistentOrderItem.getQuantity();
		if (!persistentOrder.getSellerId().equals(config.getSysUserId()) && persistentOrder.getSellerUserRank() == UserRank.V4 && quantity >= 100 && quantity % 100 == 0) {
			if (persistentOrder.getIsCopied()) {
				return persistentOrder.getRefId(); // 幂等操作
			}
			Long productId = persistentOrderItem.getProductId();
			Long sysUserId = config.getSysUserId();
			BigDecimal v4Price = malComponent.getPrice(productId, UserRank.V4, quantity);
			BigDecimal v4Amount = v4Price.multiply(BigDecimal.valueOf(quantity)).setScale(2, BigDecimal.ROUND_HALF_UP);
			Product product = productMapper.findOne(productId);
			Long userId = persistentOrder.getSellerId();
			User user = userMapper.findOne(userId);

			Long v4UserId = calculateV4UserId(user);
			Long rootId = calculateRootId(user);

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
			order.setCurrencyType(CurrencyType.现金);
			order.setSn(ServiceUtils.generateOrderSn());
			order.setIsSettledUp(false);
			order.setDiscountFee(new BigDecimal("0.00"));
			order.setExpiredTime(DateUtils.addMinutes(new Date(), Constants.SETTING_ORDER_EXPIRE_IN_MINUTES));

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

		if (config.isOld(productId)) {

			/* 销量奖 */
			if (buyerUserRank == UserRank.V4) {
				final BigDecimal saleBonus = new BigDecimal("8.00").multiply(BigDecimal.valueOf(quantity));
				fncComponent.createProfit(buyerId, Profit.ProfitType.销量奖, orderId, "销量奖", CurrencyType.现金, saleBonus);
			}

			/* 一级平级奖 */
			if (buyerUserRank == UserRank.V3) {
				Long buyerParentId = buyer.getParentId();
				if (buyerParentId != null) {
					User buyerParent = userMapper.findOne(buyerParentId);
					if (buyerParent.getUserRank() == UserRank.V3) {
						final BigDecimal v3FlatBonus = new BigDecimal("7.00").multiply(BigDecimal.valueOf(quantity));
						fncComponent.createTransfer(sellerId, buyerParentId, Transfer.TransferType.一级平级奖, orderId, "一级平级奖", CurrencyType.现金, v3FlatBonus);
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
							fncComponent.createProfit(parentId, Profit.ProfitType.特级平级奖, orderId, "特级平级奖", CurrencyType.现金, v4FlatBonus1);
							if (skipBonusUserId != null) {
								/* 一级越级奖 */
								fncComponent.createTransfer(parentId, skipBonusUserId, Transfer.TransferType.一级越级奖, orderId, "一级越级奖", CurrencyType.现金, v3SkipBonus);
							}

						} else if (index == 2) {
							fncComponent.createProfit(parentId, Profit.ProfitType.特级平级奖, orderId, "特级平级奖", CurrencyType.现金, v4FlatBonus2);
						} else if (index == 3) {
							fncComponent.createProfit(parentId, Profit.ProfitType.特级平级奖, orderId, "特级平级奖", CurrencyType.现金, v4FlatBonus3);
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
			// DO NOTHING
		}

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
				fncComponent.createAndGrantProfit(sellerId, Profit.ProfitType.订单收款, orderId, "订单" + order.getSn() + "收款", CurrencyType.现金, amount);
			}
		}

		/* 邮费 */
		if (order.getIsUseLogistics() && order.getIsBuyerPayLogisticsFee()) {
			BigDecimal logisticsFee = order.getLogisticsFee();
			if (logisticsFee.compareTo(new BigDecimal("0.00")) > 0) {
				fncComponent.createTransfer(buyerId, sysUserId, Transfer.TransferType.邮费, orderId, "邮费", CurrencyType.现金, logisticsFee);
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
			payment.setCurrencyType1(order.getCurrencyType());
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
}
