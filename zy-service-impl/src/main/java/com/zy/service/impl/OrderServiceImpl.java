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

import com.zy.ServiceUtils;
import com.zy.common.exception.BizException;
import com.zy.common.exception.ConcurrentException;
import com.zy.common.model.query.Page;
import com.zy.component.MalComponent;
import com.zy.entity.fnc.CurrencyType;
import com.zy.entity.fnc.PayType;
import com.zy.entity.fnc.Payment;
import com.zy.entity.fnc.Payment.PaymentStatus;
import com.zy.entity.fnc.Payment.PaymentType;
import com.zy.entity.mal.Order;
import com.zy.entity.mal.Order.LogisticsFeePayType;
import com.zy.entity.mal.Order.OrderStatus;
import com.zy.entity.mal.OrderItem;
import com.zy.entity.mal.Product;
import com.zy.entity.sys.ConfirmStatus;
import com.zy.entity.usr.Address;
import com.zy.entity.usr.User;
import com.zy.entity.usr.User.UserRank;
import com.zy.mapper.AddressMapper;
import com.zy.mapper.OrderItemMapper;
import com.zy.mapper.OrderMapper;
import com.zy.mapper.PaymentMapper;
import com.zy.mapper.ProductMapper;
import com.zy.mapper.UserMapper;
import com.zy.model.BizCode;
import com.zy.model.Constants;
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
	private PaymentMapper paymentMapper;

	@Autowired
	private UserMapper userMapper;

	@Autowired
	private ProductMapper productMapper;

	@Autowired
	private AddressMapper addressMapper;

	@Autowired
	private MalComponent malComponent;

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

		validate(orderDeliverDto, "logisticsFeePayType", "id");
		boolean useLogistics = orderDeliverDto.isUseLogistics();

		Long id = orderDeliverDto.getId();
		Order order = orderMapper.findOne(id);
		validate(order, NOT_NULL, "order id" + id + " is not found");
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
			LogisticsFeePayType logisticsFeePayType = orderDeliverDto.getLogisticsFeePayType();

			validate(logisticsName, NOT_BLANK, "logistics name is blank");
			validate(logisticsSn, NOT_BLANK, "logistics sn is blank");
			validate(logisticsFee, NOT_NULL, "logistics fee is null");
			validate(logisticsFeePayType, NOT_NULL, "logistics fee pay type is null");
			validate(orderDeliverDto, "logisticsName", "logisticsSn", "logisticsFee");

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

		/* 平级奖 */
		// TODO

	}

	@Override
	public List<Order> findAll(@NotNull OrderQueryModel queryModel) {
		return this.orderMapper.findAll(queryModel);
	}

	@Override
	public long count(OrderQueryModel build) {
		return orderMapper.count(build);
	}

	@Override
	public Order pay(String sn, PayType payType, String offlineImage, String offlineMemo) {
		validate(payType, NOT_NULL, "order payType" + payType + " is null");
		if(payType == PayType.银行汇款){
			validate(offlineImage, NOT_BLANK, "order offlineImage" + offlineImage + " is blank");
			validate(offlineMemo, NOT_BLANK, "order offlineMemo" + offlineMemo + " is blank");
		}
		Order order =  orderMapper.findBySn(sn);
		validate(order, NOT_NULL, "order sn" + sn + " not found");
		
		List<Payment> payments = paymentMapper.findByRefId(order.getId());
		Payment payment = payments.stream().filter(v -> v.getPayType() == payType)
				.filter(v -> v.getExpiredTime() == null || v.getExpiredTime().after(new Date()))
				.filter(v -> v.getPaymentStatus() == PaymentStatus.待支付)
				.findFirst().orElse(null);
		if(payment == null){
			payment = new Payment();
			Date date = new Date();
			payment.setPaymentStatus(PaymentStatus.待支付);
			payment.setPaymentType(PaymentType.订单支付);
			payment.setPayType(payType);
			payment.setRefId(order.getId());
			payment.setCreatedTime(date);
			payment.setExpiredTime(DateUtils.addMinutes(date, Constants.OFFLINE_PAY_EXPIRE_IN_MINUTES));
			payment.setCurrencyType1(CurrencyType.现金);
			payment.setAmount1(order.getAmount());
			payment.setSn(ServiceUtils.generatePaymentSn());
			payment.setTitle(order.getTitle());
			payment.setUserId(order.getUserId());
			payment.setVersion(0);
			if(payType == PayType.银行汇款){
				payment.setOfflineImage(offlineImage);
				payment.setOfflineMemo(offlineMemo);
				payment.setConfirmStatus(ConfirmStatus.待审核);
			}
		}
		
		return orderMapper.findOne(order.getId());
	}
}
