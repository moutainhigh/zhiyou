package com.zy.service.impl;

import static com.zy.common.util.ValidateUtils.NOT_BLANK;
import static com.zy.common.util.ValidateUtils.NOT_NULL;
import static com.zy.common.util.ValidateUtils.validate;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.zy.Config;
import com.zy.ServiceUtils;
import com.zy.common.exception.BizException;
import com.zy.common.exception.ConcurrentException;
import com.zy.common.model.query.Page;
import com.zy.component.FncComponent;
import com.zy.component.MalComponent;
import com.zy.entity.fnc.CurrencyType;
import com.zy.entity.fnc.Profit;
import com.zy.entity.fnc.Transfer;
import com.zy.entity.mal.Order;
import com.zy.entity.mal.Order.LogisticsFeePayType;
import com.zy.entity.mal.Order.OrderStatus;
import com.zy.entity.mal.OrderItem;
import com.zy.entity.mal.Product;
import com.zy.entity.usr.Address;
import com.zy.entity.usr.User;
import com.zy.entity.usr.User.UserRank;
import com.zy.mapper.AddressMapper;
import com.zy.mapper.OrderItemMapper;
import com.zy.mapper.OrderMapper;
import com.zy.mapper.ProductMapper;
import com.zy.mapper.UserMapper;
import com.zy.model.BizCode;
import com.zy.model.dto.OrderCreateDto;
import com.zy.model.dto.OrderDeliverDto;
import com.zy.model.query.OrderQueryModel;
import com.zy.service.OrderService;

@Service
@Validated
public class OrderServiceImpl implements OrderService {

	@Autowired
	private OrderMapper orderMapper;

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

		Long sellerId = malComponent.calculateSellerId(userRank, quantity, parentId);
		User seller = userMapper.findOne(sellerId);
		Boolean isPlatformDeliver;
		if (seller.getUserType() == User.UserType.平台) {
			isPlatformDeliver = true;
		} else {
			isPlatformDeliver = false;
		}

		BigDecimal price = malComponent.getPrice(productId, user.getUserRank(), quantity);
		BigDecimal amount = price.multiply(new BigDecimal(quantity));

		Order order = new Order();
		order.setUserId(userId);
		order.setAmount(amount);
		order.setCreatedTime(new Date());
		order.setIsSettledUp(false);
		order.setIsPlatformDeliver(isPlatformDeliver);
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
		throw new BizException(BizCode.ERROR, "暂不支持主动取消订单");
	}

	@Override
	public Page<Order> findPage(@NotNull OrderQueryModel orderQueryModel) {
		if (orderQueryModel.getPageNumber() == null)
			orderQueryModel.setPageNumber(0);
		if (orderQueryModel.getPageSize() == null)
			orderQueryModel.setPageSize(20);
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
	public void deliver(@NotNull OrderDeliverDto orderDeliverDto) {

		validate(orderDeliverDto, "id", "useLogistics");
		boolean useLogistics = orderDeliverDto.getUseLogistics();

		Long id = orderDeliverDto.getId();
		Order order = orderMapper.findOne(id);
		validate(order, NOT_NULL, "order id" + id + " is not found");

		if (order.getIsPlatformDeliver()) {
			throw new BizException(BizCode.ERROR, "平台发货订单不能自己发货");
		}

		OrderStatus orderStatus = order.getOrderStatus();
		if (orderStatus == OrderStatus.已发货) {
			return; // 幂等处理
		} else if (orderStatus != OrderStatus.已支付) {
			throw new BizException(BizCode.ERROR, "只有已支付的订单才能发货");
		}


		if (useLogistics) {

			String logisticsName = orderDeliverDto.getLogisticsName();
			String logisticsSn = orderDeliverDto.getLogisticsSn();
			BigDecimal logisticsFee = orderDeliverDto.getLogisticsFee();

			validate(logisticsName, NOT_BLANK, "logistics name is blank");
			validate(logisticsSn, NOT_BLANK, "logistics sn is blank");
			validate(logisticsFee, NOT_NULL, "logistics fee is null");
			validate(orderDeliverDto, "logisticsName", "logisticsSn", "logisticsFee");

			order.setLogisticsName(logisticsName);
			order.setLogisticsSn(logisticsSn);
			order.setLogisticsFeePayType(LogisticsFeePayType.无);
			order.setLogisticsFee(logisticsFee);

		} else {
			order.setLogisticsFee(null);
			order.setLogisticsFeePayType(null);
			order.setLogisticsName(null);
			order.setLogisticsSn(null);
		}

		order.setUseLogistics(useLogistics);

		order.setDeliveredTime(new Date());
		order.setOrderStatus(OrderStatus.已发货);

		if (orderMapper.update(order) == 0) {
			throw new ConcurrentException();
		}

	}

	@Override
	public void platformDeliver(@NotNull OrderDeliverDto orderDeliverDto) {
		validate(orderDeliverDto, "id", "useLogistics");
		boolean useLogistics = orderDeliverDto.getUseLogistics();

		Long id = orderDeliverDto.getId();
		Order order = orderMapper.findOne(id);
		validate(order, NOT_NULL, "order id" + id + " is not found");

		if (!order.getIsPlatformDeliver()) {
			throw new BizException(BizCode.ERROR, "非平台发货订单不能平台发货");
		}

		OrderStatus orderStatus = order.getOrderStatus();
		if (orderStatus == OrderStatus.已发货) {
			return; // 幂等处理
		} else if (orderStatus != OrderStatus.已支付) {
			throw new BizException(BizCode.ERROR, "只有已支付的订单才能发货");
		}


		if (useLogistics) {

			String logisticsName = orderDeliverDto.getLogisticsName();
			String logisticsSn = orderDeliverDto.getLogisticsSn();
			BigDecimal logisticsFee = orderDeliverDto.getLogisticsFee();


			validate(logisticsName, NOT_BLANK, "logistics name is blank");
			validate(logisticsSn, NOT_BLANK, "logistics sn is blank");
			validate(logisticsFee, NOT_NULL, "logistics fee is null");
			validate(orderDeliverDto, "logisticsName", "logisticsSn", "logisticsFee");

			LogisticsFeePayType logisticsFeePayType;

			User seller = userMapper.findOne(order.getSellerId());
			User.UserType sellerUserType = seller.getUserType();

			if (sellerUserType == User.UserType.平台) {
				logisticsFeePayType = LogisticsFeePayType.买家付;
			} else {
				logisticsFeePayType = LogisticsFeePayType.卖家付;
			}

			order.setLogisticsName(logisticsName);
			order.setLogisticsSn(logisticsSn);
			order.setLogisticsFeePayType(logisticsFeePayType);
			order.setLogisticsFee(logisticsFee);

		} else {
			order.setLogisticsFee(null);
			order.setLogisticsFeePayType(null);
			order.setLogisticsName(null);
			order.setLogisticsSn(null);
		}

		order.setUseLogistics(useLogistics);

		order.setDeliveredTime(new Date());
		order.setOrderStatus(OrderStatus.已发货);

		if (orderMapper.update(order) == 0) {
			throw new ConcurrentException();
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

		order.setOrderStatus(OrderStatus.已完成);
		if (orderMapper.update(order) == 0) {
			throw new ConcurrentException();
		}
	}

	@Override
	public void modifyIsPlatformDeliver(@NotNull Long orderId, boolean isPlatformDeliver) {
		Order order = orderMapper.findOne(orderId);
		validate(order, NOT_NULL, "order id" + orderId + " is not found");
		if (order.getOrderStatus() != OrderStatus.已支付) {
			throw new BizException(BizCode.ERROR, "只有已支付的订单才能转换为平台发货");
		}
		order.setIsPlatformDeliver(isPlatformDeliver);
		if (orderMapper.update(order) == 0) {
			throw new ConcurrentException();
		}

	}

	@Override
	public void modifySellerId(@NotNull Long orderId, @NotNull Long sellerId) {
		Order order = orderMapper.findOne(orderId);
		validate(order, NOT_NULL, "order id" + orderId + " is not found");
		User seller = userMapper.findOne(sellerId);
		validate(seller, NOT_NULL, "seller id" + seller + " is not found");

		if (order.getOrderStatus() != OrderStatus.已支付) {
			throw new BizException(BizCode.ERROR, "只有已支付的订单才能修改卖家");
		}
		order.setSellerId(sellerId);
		if (orderMapper.update(order) == 0) {
			throw new ConcurrentException();
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
		UserRank buyerUserRank = buyer.getUserRank();
		UserRank sellerUserRank = seller.getUserRank();

		/* 订单收款 */
		if (buyerUserRank != UserRank.V4) {
			BigDecimal amount = order.getAmount();
			if (sellerUserRank == UserRank.V4 && order.getIsPlatformDeliver()) {
				amount = amount.subtract(malComponent.getPrice(productId, UserRank.V4, quantity).multiply(BigDecimal.valueOf(quantity))).setScale(2, BigDecimal.ROUND_HALF_UP);
			}
			fncComponent.createProfit(sellerId, Profit.ProfitType.订单收款, orderId, "订单收款", CurrencyType.现金, amount);
		}

		/* 邮费 */
		if (order.getUseLogistics()) {
			LogisticsFeePayType logisticsFeePayType = order.getLogisticsFeePayType();
			BigDecimal logisticsFee = order.getLogisticsFee();
			Boolean isPlatformDeliver = order.getIsPlatformDeliver();
			if (logisticsFee.compareTo(new BigDecimal("0.00")) > 0) {
				if (isPlatformDeliver) {
					Long sysUserId = config.getSysUserId();
					if (logisticsFeePayType == LogisticsFeePayType.买家付) {
						fncComponent.createTransfer(buyerId, sysUserId, Transfer.TransferType.邮费, orderId, "邮费", CurrencyType.现金, logisticsFee);
					} else if (logisticsFeePayType == LogisticsFeePayType.卖家付) {
						fncComponent.createTransfer(sellerId, sysUserId, Transfer.TransferType.邮费, orderId, "邮费", CurrencyType.现金, logisticsFee);
					}
				} else {
//					Long sysUserId = config.getSysUserId();
					if (logisticsFeePayType == LogisticsFeePayType.买家付) {
						fncComponent.createTransfer(buyerId, sellerId, Transfer.TransferType.邮费, orderId, "邮费", CurrencyType.现金, logisticsFee);
					} else if (logisticsFeePayType == LogisticsFeePayType.卖家付) {
						// ignore
					}
				}
			}
		}
		
		/* 销量奖 */
		if (buyerUserRank == UserRank.V4) {
			final BigDecimal saleBonus = new BigDecimal("8.00").multiply(BigDecimal.valueOf(quantity));
			fncComponent.createProfit(buyerId, Profit.ProfitType.销量奖, orderId, "销量奖", CurrencyType.现金, saleBonus);
		} else if (sellerUserRank == UserRank.V4 && order.getIsPlatformDeliver()) {
			final BigDecimal saleBonus = new BigDecimal("8.00").multiply(BigDecimal.valueOf(quantity));
			fncComponent.createProfit(sellerId, Profit.ProfitType.销量奖, orderId, "销量奖", CurrencyType.现金, saleBonus);
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

			while(parentId != null) {
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
					index ++;
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

			}

		}

		order.setIsSettledUp(true);
		if (orderMapper.update(order) == 0) {
			throw new ConcurrentException();
		}

	}

	@Override
	public List<Order> findAll(@NotNull OrderQueryModel queryModel) {
		return this.orderMapper.findAll(queryModel);
	}

	@Override
	public long count(OrderQueryModel build) {
		return orderMapper.count(build);
	}
}
