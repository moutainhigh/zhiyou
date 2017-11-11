
package com.zy.service.impl;


import com.zy.Config;
import com.zy.ServiceUtils;
import com.zy.common.exception.BizException;
import com.zy.common.exception.ConcurrentException;
import com.zy.common.model.query.Page;
import com.zy.common.model.tree.TreeHelper;
import com.zy.common.model.tree.TreeNode;
import com.zy.common.model.tree.TreeNodeResolver;
import com.zy.common.util.BeanUtils;
import com.zy.common.util.DateUtil;
import com.zy.component.FncComponent;
import com.zy.component.MalComponent;
import com.zy.entity.fnc.*;
import com.zy.entity.fnc.Payment.PaymentStatus;
import com.zy.entity.fnc.Payment.PaymentType;
import com.zy.entity.mal.*;
import com.zy.entity.mal.Order.OrderStatus;
import com.zy.entity.mergeusr.MergeUser;
import com.zy.entity.usr.Address;
import com.zy.entity.usr.User;
import com.zy.entity.usr.User.UserRank;
import com.zy.entity.usr.UserUpgrade;
import com.zy.extend.Producer;
import com.zy.mapper.*;
import com.zy.model.BizCode;
import com.zy.model.Constants;
import com.zy.model.TeamModel;
import com.zy.model.dto.OrderCreateDto;
import com.zy.model.dto.OrderDeliverDto;
import com.zy.model.dto.OrderSumDto;
import com.zy.model.query.*;
import com.zy.service.MergeUserService;
import com.zy.service.OrderService;
import io.gd.generator.api.query.Direction;
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
	private UserUpgradeMapper userUpgradeMapper;

	@Autowired
	private MergeUserService mergeUserService;

	@Autowired
	private MergeUserMapper mergeUserMapper;

	@Autowired
	private OrderStoreMapper orderStoreMapper;

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

		/* check product */
        Long productId = orderCreateDto.getProductId();
        Product product = productMapper.findOne(productId);
        validate(product, NOT_NULL, "product id " + productId + " is not found");
        if (!product.getIsOn()) {
            throw new BizException(BizCode.ERROR, "必须上架的商品才能购买");
        }

        Long userId = orderCreateDto.getUserId();

		/* check address */
        Long addressId = orderCreateDto.getAddressId();
        Address address = addressMapper.findOne(addressId);
        validate(address, NOT_NULL, "address id " + addressId + " is not found");
        validate(address, v -> v.getUserId().equals(userId), "address " + addressId + " is not own");

        String title = orderCreateDto.getTitle();
        long quantity = orderCreateDto.getQuantity();


		/* check user */
        Long parentId = null;
        User user = null;
        UserRank userRank = null;
        UserRank buyerUserRank = null;
        Long sellerId = null;
        UserRank sellerUserRank = null;
        Long v4UserId = null;

        if (product.getProductType() == 2){
            MergeUser mergeUser = mergeUserService.findByUserIdAndProductType(userId,product.getProductType());
            if (mergeUser == null){
                if (product.getSkuCode().equals("zy-slj")){
                    mergeUser = new MergeUser();
                    mergeUser.setUserId(userId);
                    mergeUser.setParentId(orderCreateDto.getParentId());
                    mergeUser.setInviterId(orderCreateDto.getParentId());
                    mergeUser.setProductType(product.getProductType());
                    mergeUser.setUserRank(UserRank.V0);
                    mergeUser.setRegisterTime(new Date());
                    mergeUserMapper.insert(mergeUser);

                    userRank = UserRank.V0;
                    buyerUserRank = userRank;
                    parentId = orderCreateDto.getParentId();
                    v4UserId = calculateV4MergeUserId(mergeUser);
                }else if (product.getSkuCode().equals("zy-slj-pyqzy")){
                    mergeUserService.createByUserId(userId,2);
                    MergeUser mergeU = mergeUserService.findByUserIdAndProductType(userId,product.getProductType());

                    userRank = mergeU.getUserRank();
                    buyerUserRank = userRank;

                    parentId = mergeU.getParentId();

                    v4UserId = calculateV4MergeUserId(mergeU);
                }
                MergeUser mUser = malComponent.catchSellerId(userRank, productId, quantity, parentId);
                sellerId = mUser.getUserId();
                sellerUserRank = mUser.getUserRank();
            }else {
                userRank = mergeUser.getUserRank();
                buyerUserRank = mergeUser.getUserRank();
                parentId = mergeUser.getParentId();
                MergeUser mUser = malComponent.catchSellerId(userRank, productId, quantity, parentId);
                sellerId = mUser.getUserId();
                sellerUserRank = mUser.getUserRank();
                v4UserId = calculateV4MergeUserId(mergeUser);
            }
        }else {
            user = userMapper.findOne(userId);
            validate(user, NOT_NULL, "user id " + userId + " is not found");
            validate(user, v -> v.getUserType() == User.UserType.代理, "user type is wrong");

			/* calculate parent id */
            userRank = user.getUserRank();
            buyerUserRank = user.getUserRank();
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

            User seller = malComponent.calculateSeller(userRank, productId, quantity, parentId);
            sellerId = seller.getId();
            sellerUserRank = sellerId.equals(config.getSysUserId()) ? null : seller.getUserRank();
            v4UserId = calculateV4UserId(user);
        }
        if (userRank != UserRank.V4){
            if (product.getProductType() == 2){
                Boolean  flag = checkOrderStore(sellerId,2,quantity);
                if (flag == false){
                    throw new BizException(BizCode.ERROR, "卖家库存不足，请提醒卖家进货");
                }
            }
        }

        BigDecimal price = malComponent.getPrice(productId, userRank, quantity);
        BigDecimal amount = price.multiply(new BigDecimal(quantity));

        boolean isPayToPlatform = orderCreateDto.getIsPayToPlatform();

//		Long rootId = calculateRootId(user);

        if (product.getProductType() == 1){
            UserRank upgradeUserRank = malComponent.getUpgradeUserRank(userRank, productId, quantity);
            if(upgradeUserRank == UserRank.V4) {  //升特级只允许支付给平台
                isPayToPlatform = true;
            }
        }

        Date createdTime = null;
        if(orderCreateDto.getOrderType() == Order.OrderType.普通订单) {
            createdTime = new Date();
        } else {
            createdTime = config.getOrderFillTime();
        }

        Date expiredTime = null;
        if (product.getProductType() == 1 || (product.getProductType() == 2 && product.getSkuCode().equals("zy-slj"))){
            LocalDate lastDate = LocalDate.now().with(TemporalAdjusters.lastDayOfMonth());
            LocalDateTime localDateTime = LocalDateTime.of(lastDate, LocalTime.parse("23:59:59"));
            Instant instant = localDateTime.atZone(ZoneId.systemDefault()).toInstant();
            expiredTime = Date.from(instant);
        }else if (product.getProductType() == 2 && product.getSkuCode().equals("zy-slj-pyqzy")){
            if (userRank == UserRank.V4){
                Calendar calendar = Calendar.getInstance();
                calendar.set(2017, 10, 11, 23, 59, 59);
                expiredTime = calendar.getTime();
            }else if (userRank == UserRank.V3) {
                int whileTimes = 0;
                while ( parentId != null) {
                    if (whileTimes > 1000) {
                        throw new BizException(BizCode.ERROR, "循环引用错误, user id is " + user.getId());
                    }
                    User parent = userMapper.findOne(parentId);
                    if (parent.getUserRank() == User.UserRank.V4) {
                        parentId = parent.getId();
                        break;
                    }else {
                        parentId = parent.getParentId();
                    }
                    whileTimes ++;
                }

                //根据id查询团队省级人数
                UserQueryModel userQueryModel  = new UserQueryModel();
                userQueryModel.setIsDeletedEQ(false);
                userQueryModel.setIsFrozenEQ(false);
                List<User> users = userMapper.findAll(userQueryModel);
                List<User> children = TreeHelper.sortBreadth2(users, parentId.toString(), v -> {
                    TreeNode treeNode = new TreeNode();
                    treeNode.setId(v.getId().toString());
                    treeNode.setParentId(v.getParentId() == null ? null : v.getParentId().toString());
                    return treeNode;
                });

                List<User> list = children.stream().filter(v -> v.getUserRank() == User.UserRank.V3).collect(Collectors.toList());
                //找出第一个特级是parentId
                List<User> uses = new ArrayList<>();
                for (User use: list) {
                    Long pId = calculateV4UserId(use);
                    if (pId != null && pId.toString().equals(parentId.toString())){
                        uses.add(use);
                    }
                }
                if (uses.size() >= 8 ){
                    if (isPayToPlatform == true){
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(2017, 10, 11, 23, 59, 59);
                        expiredTime = calendar.getTime();
                    }else {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(2017, 10, 12, 23, 59, 59);
                        expiredTime = calendar.getTime();
                    }
                }else if (uses.size() < 8  && uses.size() > 0){
                    //判断时间小于11 31 23 59 59
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(2017, 11, 31, 23, 59, 59);
                    expiredTime = calendar.getTime();
                }
            }
        }

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
        order.setProductType(product.getProductType());
        order.setSn(ServiceUtils.generateOrderSn());
        order.setIsSettledUp(false);
        order.setDiscountFee(new BigDecimal("0.00"));
        //order.setExpiredTime(DateUtils.addMinutes(new Date(), Constants.SETTING_ORDER_EXPIRE_IN_MINUTES));
        order.setExpiredTime(expiredTime);
        order.setIsPayToPlatform(orderCreateDto.getIsPayToPlatform());
        order.setIsDeleted(false);
        order.setExaltFlage(0);
        order.setSendQuantity(0);
		/* 追加字段 */
        order.setIsMultiple(false);
        order.setProductId(productId);
        order.setMarketPrice(product.getMarketPrice());
        order.setQuantity(quantity);
        order.setPrice(price);
        order.setImage(product.getImage1());
        order.setV4UserId(v4UserId);
//		order.setRootId(rootId);

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


    private Long calculateV4MergeUserId(MergeUser mergeUser) {
		Long parentId = mergeUser.getParentId();
		int whileTimes = 0;
		while (parentId != null) {
			if (whileTimes > 1000) {
				throw new BizException(BizCode.ERROR, "循环引用错误, mergeUser id is " + mergeUser.getUserId());
			}
			MergeUser parent = mergeUserService.findByUserIdAndProductType(parentId,2);
			if (parent.getUserRank() == User.UserRank.V4) {
				return parentId;
			}
			parentId = parent.getParentId();
			whileTimes ++;
		}
		return null;
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
	public void modifyOrder(Order order) {
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
		order.setDeliveredId(orderDeliverDto.getDeliveredId());
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
	public void settleUpProfit(@NotBlank String yearAndMonth) {

		DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM");
		TemporalAccessor temporalAccessor = df.parse(yearAndMonth);

		int year = temporalAccessor.get(ChronoField.YEAR);
		int month = temporalAccessor.get(ChronoField.MONTH_OF_YEAR);

		YearMonth yearMonth = YearMonth.of(year, month);
		YearMonth nowYearMonth = YearMonth.now();
		if (!nowYearMonth.isAfter(yearMonth)) {
			throw new BizException(BizCode.ERROR, "未达到发放时间");
		}

		OrderMonthlySettlement orderMonthlySettlement = orderMonthlySettlementMapper.findByYearAndMonth(yearAndMonth, Constants.SETTLEMENT_TYPE_1);
		if (orderMonthlySettlement != null) {
			return; // 幂等操作
		}
       //查询v4的晋升时间 按晋升时间 转成Map
		Map<Long,List<UserUpgrade>> userUpgradeMap = userUpgradeMapper.findAll(UserUpgradeQueryModel.builder().toUserRankEQ(User.UserRank.V4).orderBy("upgradedTime")
				.direction(Direction.ASC).build()).stream().collect(Collectors.groupingBy(UserUpgrade::getUserId));
		LocalDate beginDate = LocalDate.of(year, month, 1);
		LocalDate endDate = beginDate.with(TemporalAdjusters.firstDayOfNextMonth());

		LocalDateTime beginDateTime = LocalDateTime.of(beginDate, LocalTime.MIN);
		LocalDateTime endDateTime = LocalDateTime.of(endDate, LocalTime.MIN);

		Date begin = Date.from(beginDateTime.atZone(ZoneId.systemDefault()).toInstant());
		Date end = Date.from(endDateTime.atZone(ZoneId.systemDefault()).toInstant());

		List<User> deletedUsers = userMapper.findAll(UserQueryModel.builder().isDeletedEQ(true).build());
		List<Long> deletedUserIds = deletedUsers.stream().map(v -> v.getId()).collect(Collectors.toList());
		List<Order> orders = orderMapper.findAll(OrderQueryModel.builder()
				.orderStatusIN(new OrderStatus[] {OrderStatus.已支付, OrderStatus.已发货, OrderStatus.已完成})
				.productIdEQ(2L)  //月结算只针对2.0的产品
				.paidTimeGTE(begin).paidTimeLT(end)
				.isProfitSettledUpEQ(false)
				.build());

		final Long toV4Quantity = 3600L;

		@SuppressWarnings("unused")
		Long sysUserId = config.getSysUserId();
		for (Order order : orders) {

			Long orderId = order.getId();

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

			Date paidTime = new Date();//order.getPaidTime(); 根据要求改成   当前job跑的时间

			if (config.isOld(productId)) {

				/* 销量奖 */
				if (buyerUserRank == UserRank.V4) {
					final BigDecimal saleBonus = new BigDecimal("8.00").multiply(BigDecimal.valueOf(quantity));
					fncComponent.createProfit(buyerId, Profit.ProfitType.销量奖, orderId, "销量奖", CurrencyType.积分, saleBonus, paidTime, null,saleBonus,new BigDecimal(0.00));
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
								fncComponent.createProfit(parentId, Profit.ProfitType.特级平级奖, orderId, "特级平级奖", CurrencyType.积分, v4FlatBonus1, paidTime, null,v4FlatBonus1,new BigDecimal(0.00));
								if (skipBonusUserId != null) {
									/* 一级越级奖 */
									fncComponent.createTransfer(parentId, skipBonusUserId, Transfer.TransferType.一级越级奖, orderId, "一级越级奖", CurrencyType.积分, v3SkipBonus, paidTime);
								}

							} else if (index == 2) {
								fncComponent.createProfit(parentId, Profit.ProfitType.特级平级奖, orderId, "特级平级奖", CurrencyType.积分, v4FlatBonus2, paidTime, null,v4FlatBonus2,new BigDecimal(0.00));
							} else if (index == 3) {
								fncComponent.createProfit(parentId, Profit.ProfitType.特级平级奖, orderId, "特级平级奖", CurrencyType.积分, v4FlatBonus3, paidTime, null,v4FlatBonus2,new BigDecimal(0.00));
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
				/* 业绩奖 */
				if (buyerUserRank != UserRank.V4 && order.getQuantity() >= toV4Quantity) {  //直升特级
					if (sellerId.equals(sysUserId)) {
						final BigDecimal saleBonus = new BigDecimal("9.00").multiply(BigDecimal.valueOf(quantity));
						BigDecimal fee = saleBonus.multiply(FEE_RATE);
						BigDecimal saleBonusAfter = saleBonus.subtract(fee);
						try {
							TimeUnit.MILLISECONDS.sleep(50);
						} catch (InterruptedException e1) {
						}
						if (deletedUserIds.contains(buyerId)) {
							Long parentId = buyer.getParentId();
							User parent = getV4ParentId(parentId);
							Boolean newV4 =newV4(userUpgradeMap.get(parent.getId()),yearMonth);
							if(newV4){
								 fee = saleBonus.multiply(FEE_RATE1);
								 saleBonusAfter = saleBonus.subtract(fee);
								fncComponent.createProfit(parent.getId(), Profit.ProfitType.业绩奖, orderId, year + "年" + month + "月业绩奖", CurrencyType.积分, saleBonusAfter, paidTime, "已扣除手续费:" + fee + ";费率: " + FEE_RATE1,saleBonus,fee);
								logger.error(buyer.getNickname() + "直升特级, 但被踢出系统，业绩奖给上级" + parent.getNickname() + "业绩奖：" + saleBonusAfter + "费率：" + fee);
							}else{
								fncComponent.createProfit(parent.getId(), Profit.ProfitType.业绩奖, orderId, year + "年" + month + "月业绩奖", CurrencyType.积分, saleBonusAfter, paidTime, "保留发放:" + fee + ";占比: " + FEE_RATE.multiply(new BigDecimal(100))+"%",saleBonus,fee);
								logger.error(buyer.getNickname() + "直升特级, 但被踢出系统，业绩奖给上级" + parent.getNickname() + "业绩奖：" + saleBonusAfter + "保留发放：" + fee);
							}

						} else {
							Boolean newV4 =newV4(userUpgradeMap.get(buyerId),yearMonth);
							if(newV4){
								fee = saleBonus.multiply(FEE_RATE1);
								saleBonusAfter = saleBonus.subtract(fee);
								fncComponent.createProfit(buyerId, Profit.ProfitType.业绩奖, orderId, year + "年" + month + "月业绩奖", CurrencyType.积分, saleBonusAfter, paidTime, "已扣除手续费:" + fee + ";费率: " + FEE_RATE.multiply(new BigDecimal(100))+"%",saleBonus,fee);
								logger.error(buyer.getNickname() + "直升特级, 业绩奖：" + saleBonusAfter + "保留发放：" + fee);
							 }else{
								fncComponent.createProfit(buyerId, Profit.ProfitType.业绩奖, orderId, year + "年" + month + "月业绩奖", CurrencyType.积分, saleBonusAfter, paidTime, "保留发放:" + fee + ";占比: " + FEE_RATE.multiply(new BigDecimal(100))+"%",saleBonus,fee);
								logger.error(buyer.getNickname() + "直升特级, 业绩奖：" + saleBonusAfter + "保留发放：" + fee);
							}

						}

						Long parentId = buyer.getParentId();
						User parent = getV4ParentId(parentId);

						if (parent.getUserRank() == UserRank.V4) {
							final BigDecimal saleBonusToV4Parent = new BigDecimal("1.00").multiply(BigDecimal.valueOf(quantity));
							BigDecimal feeToV4Parent = saleBonusToV4Parent.multiply(FEE_RATE);
							Boolean newV4 =newV4(userUpgradeMap.get(parent.getId()),yearMonth);
							if(newV4){
								feeToV4Parent = saleBonusToV4Parent.multiply(FEE_RATE1);
							}
							BigDecimal saleBonusAfterToV4Parent = saleBonusToV4Parent.subtract(feeToV4Parent);

							try {
								TimeUnit.MILLISECONDS.sleep(50);
							} catch (InterruptedException e1) {
							}
							if(newV4){
								fncComponent.createProfit(parent.getId(), Profit.ProfitType.业绩奖, orderId, year + "年" + month + "月" + buyer.getNickname() + "业绩奖", CurrencyType.积分, saleBonusAfterToV4Parent, paidTime, "已扣除手续费:" + feeToV4Parent + ";费率: " + FEE_RATE1,saleBonusToV4Parent,feeToV4Parent);
								logger.error(buyer.getNickname() + "直升特级, 给上级" + parent.getNickname() + "的业绩奖：" + saleBonusAfterToV4Parent + "已扣除手续费：" + feeToV4Parent);
							}else{
								fncComponent.createProfit(parent.getId(), Profit.ProfitType.业绩奖, orderId, year + "年" + month + "月" + buyer.getNickname() + "业绩奖", CurrencyType.积分, saleBonusAfterToV4Parent, paidTime, "保留发放:" + feeToV4Parent + ";占比: " + FEE_RATE.multiply(new BigDecimal(100))+"%",saleBonusToV4Parent,feeToV4Parent);
								logger.error(buyer.getNickname() + "直升特级, 给上级" + parent.getNickname() + "的业绩奖：" + saleBonusAfterToV4Parent + "保留发放：" + feeToV4Parent);
							}

						}
					}
				}
				if (buyerUserRank == UserRank.V4) {
					final BigDecimal saleBonus = new BigDecimal("10.00").multiply(BigDecimal.valueOf(quantity));
					BigDecimal fee = saleBonus.multiply(FEE_RATE);
					BigDecimal saleBonusAfter = saleBonus.subtract(fee);
					try {
						TimeUnit.MILLISECONDS.sleep(50);
					} catch (InterruptedException e1) {
					}

					if (deletedUserIds.contains(buyerId)) {
						Long parentId = buyer.getParentId();
						User parent = getV4ParentId(parentId);
						Boolean newV4 =newV4(userUpgradeMap.get(parent.getId()),yearMonth);
						if (newV4){
							 fee = saleBonus.multiply(FEE_RATE1);
							 saleBonusAfter = saleBonus.subtract(fee);
							fncComponent.createProfit(parent.getId(), Profit.ProfitType.业绩奖, orderId, year + "年" + month + "月业绩奖", CurrencyType.积分, saleBonusAfter, paidTime, "已扣除手续费:" + fee + ";费率: " + FEE_RATE1,saleBonus,fee);
							logger.error(buyer.getNickname() + "特级业绩奖，但被系统踢出，奖励给上级" + parent.getNickname() + "：" + saleBonusAfter + "已扣除手续费：" + fee);
						}else{
							fncComponent.createProfit(parent.getId(), Profit.ProfitType.业绩奖, orderId, year + "年" + month + "月业绩奖", CurrencyType.积分, saleBonusAfter, paidTime, "保留发放:" + fee + ";占比: " + FEE_RATE.multiply(new BigDecimal(100))+"%",saleBonus,fee);
							logger.error(buyer.getNickname() + "特级业绩奖，但被系统踢出，奖励给上级" + parent.getNickname() + "：" + saleBonusAfter + "保留发放：" + fee);
						}

					} else {
						Boolean newV4 =newV4(userUpgradeMap.get(buyerId),yearMonth);
						if (newV4){
							fee = saleBonus.multiply(FEE_RATE1);
							saleBonusAfter = saleBonus.subtract(fee);
							fncComponent.createProfit(buyerId, Profit.ProfitType.业绩奖, orderId, year + "年" + month + "月业绩奖", CurrencyType.积分, saleBonusAfter, paidTime, "已扣除手续费:" + fee + ";费率: " + FEE_RATE1,saleBonus,fee);
							logger.error(buyer.getNickname() + "特级业绩奖：" + saleBonusAfter + "已扣除手续费：" + fee);
						}else{
							fncComponent.createProfit(buyerId, Profit.ProfitType.业绩奖, orderId, year + "年" + month + "月业绩奖", CurrencyType.积分, saleBonusAfter, paidTime, "保留发放:" + fee + ";占比: " + FEE_RATE.multiply(new BigDecimal(100))+"%",saleBonus,fee);
							logger.error(buyer.getNickname() + "特级业绩奖：" + saleBonusAfter + "保留发放：" + fee);
						}

					}

				}
				/*平级推荐奖*/
//				if(buyerUserRank == UserRank.V3 && order.getQuantity() < toV4Quantity) {
//					Long parentId = buyer.getParentId();
//					if(parentId != null) {
//						User buyerParent = userMapper.findOne(parentId);
//						if(buyerParent.getUserRank() != UserRank.V4) {
//
//							int whileTimes = 0;
//							while (parentId != null) {
//								if (whileTimes > 1000) {
//									throw new BizException(BizCode.ERROR, "循环引用"); // 防御性校验
//								}
//
//								buyerParent = userMapper.findOne(parentId);
//								if(buyerParent.getUserRank() == UserRank.V4) {
//									final BigDecimal saleBonus = new BigDecimal("10.00").multiply(BigDecimal.valueOf(quantity));
//									BigDecimal fee = saleBonus.multiply(FEE_RATE);
//									BigDecimal saleBonusAfter = saleBonus.subtract(fee);
//									fncComponent.createProfit(parentId, Profit.ProfitType.平级推荐奖, orderId, "平级推荐奖", CurrencyType.积分, saleBonusAfter, paidTime, "已扣除手续费:" + fee + ";费率: " + FEE_RATE);
//									logger.error(buyer.getNickname() + "给V4上级" + buyerParent.getNickname() + "的平级推荐奖：" + saleBonusAfter + "已扣除手续费：" + fee);
//
//									final BigDecimal flatBonus = new BigDecimal("9.00").multiply(BigDecimal.valueOf(quantity));
//									BigDecimal flatBonusAfter = flatBonus.subtract(flatBonus.multiply(FEE_RATE));
//									fncComponent.createTransfer(parentId, buyer.getParentId(), Transfer.TransferType.平级推荐奖, orderId, "平级推荐奖", CurrencyType.积分, flatBonusAfter, paidTime);
//									logger.error(buyer.getNickname() + "的V4上级" + buyerParent.getNickname() + "给上级：" + flatBonusAfter);
//									break;
//								}
//
//								parentId = buyerParent.getParentId();
//								whileTimes++;
//							}
//
//						}
//					}
//
//				}

				/* 特级推荐奖 */
				if (buyerUserRank != UserRank.V4 && order.getQuantity() >= toV4Quantity) {
					//直升特级 推荐奖还给上级V4的上级V4
					Long parentId = buyer.getParentId();
					User buyerParent = null;
					boolean foundV4Parent = false;
					int whileTimes = 0;
					while (parentId != null) {
						if (whileTimes > 1000) {
							throw new BizException(BizCode.ERROR, "循环引用"); // 防御性校验
						}
						buyerParent = userMapper.findOne(parentId);
						if (!deletedUserIds.contains(parentId)) {
							if(buyerParent.getUserRank() == UserRank.V4) {
								if (!foundV4Parent) {
									foundV4Parent = true;
								} else {
									final BigDecimal saleBonus = new BigDecimal("6.00").multiply(BigDecimal.valueOf(quantity));
									BigDecimal fee = saleBonus.multiply(FEE_RATE);
									Boolean newV4 =newV4(userUpgradeMap.get(parentId),yearMonth);
									if (newV4){
										fee = saleBonus.multiply(FEE_RATE1);
									}
									BigDecimal saleBonusAfter = saleBonus.subtract(fee);
									try {
										TimeUnit.MILLISECONDS.sleep(50);
									} catch (InterruptedException e1) {
									}
									if (newV4){
										fncComponent.createProfit(parentId, Profit.ProfitType.推荐奖, orderId, year + "年" + month + "月推荐奖", CurrencyType.积分, saleBonusAfter, paidTime, "已扣除手续费:" + fee + ";费率: " + FEE_RATE1,saleBonus,fee);
										logger.error(buyer.getNickname() + "直升特级，订单给上级V4的上级V4：" + buyerParent.getNickname() + "推荐奖：" + saleBonusAfter + "已扣除手续费:" + fee);
									}else{
										fncComponent.createProfit(parentId, Profit.ProfitType.推荐奖, orderId, year + "年" + month + "月推荐奖", CurrencyType.积分, saleBonusAfter, paidTime, "保留发放:" + fee + ";占比: " + FEE_RATE.multiply(new BigDecimal(100))+"%",saleBonus,fee);
										logger.error(buyer.getNickname() + "直升特级，订单给上级V4的上级V4：" + buyerParent.getNickname() + "推荐奖：" + saleBonusAfter + "保留发放:" + fee);
									}

									break;
								}
							}
						}
						parentId = buyerParent.getParentId();
						whileTimes++;
					}
				}
				if(buyerUserRank == UserRank.V4) {

					Long parentId = buyer.getParentId();
					User buyerParent = null;
					boolean foundV4Parent = false;
					int whileTimes = 0;
					while ((parentId != null && !foundV4Parent)) {
						if (whileTimes > 1000) {
							throw new BizException(BizCode.ERROR, "循环引用"); // 防御性校验
						}

						buyerParent = userMapper.findOne(parentId);
						if(buyerParent.getUserRank() == UserRank.V4) {
							foundV4Parent = true;
						}

						if(foundV4Parent && !deletedUserIds.contains(parentId)) {
							final BigDecimal saleBonus = new BigDecimal("6.00").multiply(BigDecimal.valueOf(quantity));
							BigDecimal fee = saleBonus.multiply(FEE_RATE);
							Boolean newV4 =newV4(userUpgradeMap.get(parentId),yearMonth);
							if (newV4){
								fee = saleBonus.multiply(FEE_RATE1);
							}
							BigDecimal saleBonusAfter = saleBonus.subtract(fee);
							try {
								TimeUnit.MILLISECONDS.sleep(50);
							} catch (InterruptedException e1) {
							}
							if (newV4){
								fncComponent.createProfit(parentId, Profit.ProfitType.推荐奖, orderId, year + "年" + month + "月推荐奖", CurrencyType.积分, saleBonusAfter, paidTime, "已扣除手续费:" + fee + ";费率: " + FEE_RATE1,saleBonus,fee);
								logger.error(buyer.getNickname() + "buyerUserRank==V4, quantity: " + quantity + " order id= " + order.getId() + ";" + buyerParent.getNickname() + "推荐奖：" + saleBonusAfter + "已扣除手续费:" + fee);
							}else{
								fncComponent.createProfit(parentId, Profit.ProfitType.推荐奖, orderId, year + "年" + month + "月推荐奖", CurrencyType.积分, saleBonusAfter, paidTime, "保留发放:" + fee + ";占比: " + FEE_RATE.multiply(new BigDecimal(100))+"%",saleBonus,fee);
								logger.error(buyer.getNickname() + "buyerUserRank==V4, quantity: " + quantity + " order id= " + order.getId() + ";" + buyerParent.getNickname() + "推荐奖：" + saleBonusAfter + "保留发放:" + fee);
							}

							break;
						}

						parentId = buyerParent.getParentId();
						whileTimes++;
					}

//					if(foundV4Parent) {
//						if(!parentId.equals(buyer.getParentId())) {
//							User parent = userMapper.findOne(buyer.getParentId());
//							if (parent.getUserRank() == UserRank.V3) {
//								Date lastUpgradedTime = seller.getLastUpgradedTime();
//								if (lastUpgradedTime != null && DateUtils.addMonths(lastUpgradedTime, 3).after(new Date())) {
//									final BigDecimal saleBonus = new BigDecimal("3.00").multiply(BigDecimal.valueOf(quantity));
//									fncComponent.createTransfer(parentId, buyer.getParentId(), Transfer.TransferType.推荐奖, orderId, "推荐奖", CurrencyType.积分, saleBonus, paidTime);
//									logger.error(parent.getNickname() + "给到" + buyerParent.getNickname() + "推荐奖：" + saleBonus);
//								}
//							}
//						}
//					}
				}
				order.setIsProfitSettledUp(true);
				if (orderMapper.update(order) == 0) {
					throw new ConcurrentException();
				}
			}
		}

		orderMonthlySettlement = new OrderMonthlySettlement();
		orderMonthlySettlement.setYearAndMonth(yearAndMonth);
		orderMonthlySettlement.setSettledUpTime(new Date());
		orderMonthlySettlement.setSettlementType(Constants.SETTLEMENT_TYPE_1);
		orderMonthlySettlementMapper.insert(orderMonthlySettlement);

	}

	/*
		期权
	 */
	@Override
	public void settleUpOption(@NotBlank String yearAndMonth) {

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

		LocalDate beginDate = LocalDate.of(year, month, 1);
		LocalDate endDate = beginDate.with(TemporalAdjusters.firstDayOfNextMonth());

		LocalDateTime beginDateTime = LocalDateTime.of(beginDate, LocalTime.MIN);
		LocalDateTime endDateTime = LocalDateTime.of(endDate, LocalTime.MIN);

		Date begin = Date.from(beginDateTime.atZone(ZoneId.systemDefault()).toInstant());
		Date end = Date.from(endDateTime.atZone(ZoneId.systemDefault()).toInstant());

		List<Order> orders = orderMapper.findAll(OrderQueryModel.builder()
				.orderStatusIN(new OrderStatus[] {OrderStatus.已支付, OrderStatus.已发货, OrderStatus.已完成})
				.productIdEQ(2L)  //月结算只针对2.0的产品
				.paidTimeGTE(begin).paidTimeLT(end)
				.sellerIdEQ(Constants.SETTING_SETTING_ID)
				.build());

		List<User> users = userMapper.findAll(UserQueryModel.builder().userTypeEQ(User.UserType.代理).build());
		List<User> v4Users = users.stream().filter(predicate).collect(Collectors.toList());
		Map<Long, User> userMap = users.stream().collect(Collectors.toMap(User::getId, Function.identity()));

		final BigDecimal zero = new BigDecimal("0.00");
		final Date now = new Date();
		final Long toV4Quantity = 3600L;

		Map<Long, Boolean> toV4Map = orders.stream()
				.filter(v -> v.getQuantity() >= toV4Quantity && v.getBuyerUserRank().ordinal() < UserRank.V4.ordinal())
				.collect(Collectors.toMap(v -> v.getUserId(), v -> true, (existingValue, newValue) -> existingValue));
		List<Order> copyOrders = orders.stream().map(v -> {
			Long userId = v.getUserId();

			Order copy = new Order();
			BeanUtils.copyProperties(v, copy);

			Boolean toV4 = toV4Map.get(userId);
			logger.error("进入copy订单，判断用户" + userId + "是否直升特级：" + toV4);
			User user = userMap.get(userId);
			if (toV4 != null) {
				User parent = userMap.get(user.getParentId());
				while(parent.getUserRank() != UserRank.V4) {
					parent = userMap.get(parent.getParentId());
				}
				copy.setUserId(parent.getId());
				logger.error(user.getNickname() + "直升特级，订单给上级(user.parentId)：" + parent.getNickname() + parent.getUserRank());
			}
			return copy;
		}).collect(Collectors.toList());

		Map<Long, Long> userQuantityMap = copyOrders.stream().collect(Collectors.toMap(Order::getUserId, Order::getQuantity, (x, y) -> x + y));
		userQuantityMap.entrySet().stream().forEach(v -> {
			if (v.getValue() > 0) {
				System.out.println(userMap.get(v.getKey()).getNickname() + "个人销量" + v.getValue() + "元");
			}
		});

			/* 期权奖励 */
		LongSummaryStatistics summaryStatistics = copyOrders.stream().mapToLong((x) -> x.getQuantity()).summaryStatistics();
		long directorCount = userMapper.count(UserQueryModel.builder().isDirectorEQ(true).build());
		long companySales = summaryStatistics.getSum();
		logger.error("公司总销量:" + companySales);
		Map<Long, BigDecimal> profitShareMap = v4Users.stream().collect(Collectors.toMap(User::getId, v -> {
			Long userId = v.getId();
			Long userQuantity = userQuantityMap.get(userId);
			if(userQuantity != null) {
				BigDecimal quantity = new BigDecimal(userQuantity);
				BigDecimal profit = quantity.multiply(new BigDecimal("0.4"));  //特级服务商：每人新增服务量*0.4股计算；
				if(v.getIsDirector() != null && v.getIsDirector()) {  //联席董事: 每人新增服务量*0.4股+公司月总服务量*0.6股；
					BigDecimal company = new BigDecimal(companySales).multiply(new BigDecimal("0.6"));
					logger.error("directorCount:" + directorCount);
					if (directorCount > 0) {
						company = company.divide(new BigDecimal(directorCount), 0, BigDecimal.ROUND_DOWN);
					}
					profit = profit.add(company);
				}
				return profit;
			} else {
				if(v.getIsDirector() != null && v.getIsDirector()) {  //联席董事: 每人新增服务量*0.4股+公司月总服务量*0.6股；
					BigDecimal company = new BigDecimal(companySales).multiply(new BigDecimal("0.6"));
					logger.error("directorCount:" + directorCount);
					if (directorCount > 0) {
						company = company.divide(new BigDecimal(directorCount), 0, BigDecimal.ROUND_DOWN);
					}
					return company;
				}
				return new BigDecimal("0.00");
			}

		}));
		logger.error("profitShareMap end........");
		for (Map.Entry<Long, BigDecimal> entry : profitShareMap.entrySet()) {
			BigDecimal amount = entry.getValue();
			Long userId = entry.getKey();
			if (amount.compareTo(zero) > 0) {
				try {TimeUnit.MILLISECONDS.sleep(200);} catch (InterruptedException e1) {}
				//				BigDecimal fee = amount.multiply(FEE_RATE);
				//				BigDecimal amountAfter = amount.subtract(fee);
				fncComponent.createProfit(userId, Profit.ProfitType.期权奖励, null, year + "年" + month + "月期权奖励", CurrencyType.货币期权, amount, now, null,amount,new BigDecimal(0.00));
				logger.error(userMap.get(userId).getNickname() + "期权奖励" + amount + "货币期权");
			}
		}
	}

	@Override
	public void settleUpRebate(@NotBlank String yearAndMonth) {
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

		LocalDate beginDate = LocalDate.of(year, month, 1);
		LocalDate endDate = beginDate.with(TemporalAdjusters.firstDayOfNextMonth());

		LocalDateTime beginDateTime = LocalDateTime.of(beginDate, LocalTime.MIN);
		LocalDateTime endDateTime = LocalDateTime.of(endDate, LocalTime.MIN);

		Date begin = Date.from(beginDateTime.atZone(ZoneId.systemDefault()).toInstant());
		Date end = Date.from(endDateTime.atZone(ZoneId.systemDefault()).toInstant());

		List<Order> orders = orderMapper.findAll(OrderQueryModel.builder()
				.orderStatusIN(new OrderStatus[] {OrderStatus.已支付, OrderStatus.已发货, OrderStatus.已完成})
				.productIdEQ(2L)  //月结算只针对2.0的产品
				.paidTimeGTE(begin).paidTimeLT(end)
				.sellerIdEQ(Constants.SETTING_SETTING_ID)
				.build());

		final Long toV4Quantity = 3600L;
		final BigDecimal zero = new BigDecimal("0.00");
		final Date now = new Date();

		Map<Long, Boolean> toV4Map = orders.stream()
				.filter(v -> v.getQuantity() >= toV4Quantity && v.getBuyerUserRank().ordinal() < UserRank.V4.ordinal())
				.collect(Collectors.toMap(v -> v.getUserId(), v -> true, (existingValue, newValue) -> existingValue));

		//查询v4的晋升时间 按晋升时间 转成Map
		Map<Long,List<UserUpgrade>> userUpgradeMap = userUpgradeMapper.findAll(UserUpgradeQueryModel.builder().toUserRankEQ(User.UserRank.V4).orderBy("upgradedTime")
				.direction(Direction.ASC).build()).stream().collect(Collectors.groupingBy(UserUpgrade::getUserId));
		List<User> users = userMapper.findAll(UserQueryModel.builder().userTypeEQ(User.UserType.代理).build());
		List<User> v4Users = users.stream().filter(predicate).collect(Collectors.toList());
		Map<Long, User> userMap = users.stream().collect(Collectors.toMap(User::getId, Function.identity()));

		logger.error("准备返利奖， copy order");
		List<Order> copyOrders = orders.stream().map(v -> {
			Long userId = v.getUserId();

			Order copy = new Order();
			BeanUtils.copyProperties(v, copy);

			Boolean toV4 = toV4Map.get(userId);
			logger.error("进入copy订单，判断用户" + userId + "是否直升特级：" + toV4);
			User user = userMap.get(userId);
			if (toV4 != null) {
				User parent = userMap.get(user.getParentId());
				while(parent.getUserRank() != UserRank.V4) {
					parent = userMap.get(parent.getParentId());
				}
				copy.setUserId(parent.getId());
				logger.error(user.getNickname() + "直升特级，订单给上级(user.parentId)：" + parent.getNickname() + parent.getUserRank());
			}
			return copy;
		}).collect(Collectors.toList());

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

		{
			Map<Long, Long> userQuantityMap = copyOrders.stream().collect(Collectors.toMap(Order::getUserId, Order::getQuantity, (x, y) -> x + y));
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

			for (Map.Entry<Long, BigDecimal> entry : profitMap.entrySet()) {
				BigDecimal amount = entry.getValue();
				Long userId = entry.getKey();
				Boolean newV4 =newV4(userUpgradeMap.get(userId),yearMonth);
				if (amount.compareTo(zero) > 0) {
					try {
						TimeUnit.MILLISECONDS.sleep(200);
					} catch (InterruptedException e1) {
					}
					BigDecimal fee = amount.multiply(FEE_RATE);
					if(newV4){
						fee = amount.multiply(FEE_RATE1);
					}
					BigDecimal amountAfter = amount.subtract(fee);
					if (newV4){
						fncComponent.createProfit(userId, Profit.ProfitType.返利奖, null, year + "年" + month + "月返利奖", CurrencyType.积分, amountAfter, now, "已扣除手续费:" + fee + ";费率: " + FEE_RATE1,amount,fee);
						logger.error(userMap.get(userId).getNickname() + "返利奖" + amountAfter + "积分");
					}else{
						fncComponent.createProfit(userId, Profit.ProfitType.返利奖, null, year + "年" + month + "月返利奖", CurrencyType.积分, amountAfter, now, "保留发放:" + fee + ";占比: " + FEE_RATE.multiply(new BigDecimal(100))+"%",amount,fee);
						logger.error(userMap.get(userId).getNickname() + "返利奖" + amountAfter + "积分");
					}

				}
			}
		}
	}

	@Override
	public void settleUpDirector(@NotBlank String yearAndMonth) {

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

		LocalDate beginDate = LocalDate.of(year, month, 1);
		LocalDate endDate = beginDate.with(TemporalAdjusters.firstDayOfNextMonth());

		LocalDateTime beginDateTime = LocalDateTime.of(beginDate, LocalTime.MIN);
		LocalDateTime endDateTime = LocalDateTime.of(endDate, LocalTime.MIN);

		Date begin = Date.from(beginDateTime.atZone(ZoneId.systemDefault()).toInstant());
		Date end = Date.from(endDateTime.atZone(ZoneId.systemDefault()).toInstant());

		List<Order> orders = orderMapper.findAll(OrderQueryModel.builder()
				.orderStatusIN(new OrderStatus[] {OrderStatus.已支付, OrderStatus.已发货, OrderStatus.已完成})
				.productIdEQ(2L)  //月结算只针对2.0的产品
				.paidTimeGTE(begin).paidTimeLT(end)
				.sellerIdEQ(Constants.SETTING_SETTING_ID)
				.build());

		final Long toV4Quantity = 3600L;
		final BigDecimal zero = new BigDecimal("0.00");
		final Date now = new Date();

		Map<Long, Boolean> toV4Map = orders.stream()
				.filter(v -> v.getQuantity() >= toV4Quantity && v.getBuyerUserRank().ordinal() < UserRank.V4.ordinal())
				.collect(Collectors.toMap(v -> v.getUserId(), v -> true, (existingValue, newValue) -> existingValue));
		//查询v4的晋升时间 按晋升时间 转成Map
		Map<Long,List<UserUpgrade>> userUpgradeMap = userUpgradeMapper.findAll(UserUpgradeQueryModel.builder().toUserRankEQ(User.UserRank.V4).orderBy("upgradedTime")
				.direction(Direction.ASC).build()).stream().collect(Collectors.groupingBy(UserUpgrade::getUserId));
		List<User> users = userMapper.findAll(UserQueryModel.builder().userTypeEQ(User.UserType.代理).build());
		List<User> v4Users = users.stream().filter(predicate).collect(Collectors.toList());
		Map<Long, User> userMap = users.stream().collect(Collectors.toMap(User::getId, Function.identity()));

		Map<Long, Long> userQuantityMap = orders.stream().collect(Collectors.toMap(Order::getUserId, Order::getQuantity, (x, y) -> x + y));
		userQuantityMap.entrySet().stream().forEach(v -> {
			if (v.getValue() > 0) {
				System.out.println("董事贡献奖：" + userMap.get(v.getKey()).getNickname() + "个人销量" + v.getValue() + "元");
			}
		});

		logger.error("准备董事贡献奖励计算....");
		List<User> v4Directors = v4Users.stream().filter(v -> v.getIsDirector() != null && v.getIsDirector()).collect(Collectors.toList());
		Map<Long, TeamModel> teamV4Map = v4Directors.stream()
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
					return teamModel;
				}).collect(Collectors.toMap(v -> v.getUser().getId(), Function.identity()));

		logger.error("董事贡献奖励：profitDirectorMap...");
		Map<Long, BigDecimal> profitDirectorMap = v4Directors.stream().collect(Collectors.toMap(User::getId, v -> {
			Long userId = v.getId();
			Long myQuantity = userQuantityMap.get(userId) == null ? 0L : userQuantityMap.get(userId);
			BigDecimal profit = new BigDecimal(myQuantity).multiply(new BigDecimal("2.00"));
			BigDecimal teamProfit = teamV4Map.get(userId).getV4Children().stream()
					.map(u -> {
						logger.error("董事贡献奖励：计算teamV4Map...");
						Long parentId = u.getParentId();
						boolean isChildrenOfDirector = false;
						while(!isChildrenOfDirector) {
							if (parentId.equals(userId)) {
								break;
							}
							User user = userMap.get(parentId);
							Long directV4ParentId = getDirectV4ParentId(predicate, userMap, user);
							if (user.getIsDirector() != null && user.getIsDirector()) {
								isChildrenOfDirector = true;
								break;
							}

							parentId = directV4ParentId;
						}
						//自己是董事且有量 计算 只能算到上级董事
						if(u.getIsDirector() != null && u.getIsDirector()){
							if (! getflage(userMap,u,userId)){
								return zero;
							}
						}
						Long quantity = userQuantityMap.get(u.getId()) == null ? 0L : userQuantityMap.get(u.getId());
						BigDecimal innerProfit = zero;
						if(u.getIsDirector() != null && u.getIsDirector()) {
							innerProfit = new BigDecimal(quantity);
						} else {
							if (isChildrenOfDirector) {
								innerProfit = new BigDecimal(quantity);
							} else {
								innerProfit = new BigDecimal(quantity).multiply(new BigDecimal("2.00"));
							}
						}
						logger.error("董事贡献奖励, 上级：" + v.getNickname() + "下级：" + u.getNickname() + "最终收益：" + innerProfit);
						return innerProfit;
					})
					.reduce(new BigDecimal("0.00"), BigDecimal::add);
			return profit.add(teamProfit);
		}));

		logger.error("董事贡献奖励：for循环...");
		for (Map.Entry<Long, BigDecimal> entry : profitDirectorMap.entrySet()) {
			BigDecimal amount = entry.getValue();
			Long userId = entry.getKey();
			Boolean newV4 =newV4(userUpgradeMap.get(userId),yearMonth);
			if (amount.compareTo(zero) > 0) {
				try {TimeUnit.MILLISECONDS.sleep(200);} catch (InterruptedException e1) {}
				BigDecimal fee = amount.multiply(FEE_RATE);
				if (newV4){
					fee = amount.multiply(FEE_RATE1);
				}
				BigDecimal amountAfter = amount.subtract(fee);
				if(newV4){
					fncComponent.createProfit(userId, Profit.ProfitType.董事贡献奖, null, year + "年" + month + "月董事贡献奖", CurrencyType.积分, amountAfter, now, "已扣除手续费:" + fee + ";费率: " + FEE_RATE1,amount,fee);
					logger.error(userMap.get(userId).getNickname() + "董事贡献奖" + amount + "积分");
				 }else{
					fncComponent.createProfit(userId, Profit.ProfitType.董事贡献奖, null, year + "年" + month + "月董事贡献奖", CurrencyType.积分, amountAfter, now, "保留发放:" + fee + ";占比: " + FEE_RATE.multiply(new BigDecimal(100))+"%",amount,fee);
					logger.error(userMap.get(userId).getNickname() + "董事贡献奖" + amount + "积分");
				}

			}
		}
		logger.error("董事贡献奖励：end...");

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

	/*
	 *月结
	 */
	@Override
	public void settleUpMonthly(@NotBlank String yearAndMonth) {
		logger.error("entry settleUpMonthly....");

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

		OrderMonthlySettlement orderMonthlySettlement = orderMonthlySettlementMapper.findByYearAndMonth(yearAndMonth, Constants.SETTLEMENT_TYPE_2);
		if (orderMonthlySettlement != null) {
			return; // 幂等操作
		}

		List<User> deletedUsers = userMapper.findAll(UserQueryModel.builder().isDeletedEQ(true).build());
		List<Long> deletedUserIds = deletedUsers.stream().map(v -> v.getId()).collect(Collectors.toList());

		LocalDate beginDate = LocalDate.of(year, month, 1);
		LocalDate endDate = beginDate.with(TemporalAdjusters.firstDayOfNextMonth());

		LocalDateTime beginDateTime = LocalDateTime.of(beginDate, LocalTime.MIN);
		LocalDateTime endDateTime = LocalDateTime.of(endDate, LocalTime.MIN);

		Date begin = Date.from(beginDateTime.atZone(ZoneId.systemDefault()).toInstant());
		Date end = Date.from(endDateTime.atZone(ZoneId.systemDefault()).toInstant());

		List<Order> orders = orderMapper.findAll(OrderQueryModel.builder()
				.orderStatusIN(new OrderStatus[] {OrderStatus.已支付, OrderStatus.已发货, OrderStatus.已完成})
				.productIdEQ(2L)  //月结算只针对2.0的产品
				.paidTimeGTE(begin).paidTimeLT(end)
				.sellerIdEQ(Constants.SETTING_SETTING_ID)
				.build());

		final Long toV4Quantity = 3600L;
		final BigDecimal zero = new BigDecimal("0.00");
		final Date now = new Date();

		Map<Long, Boolean> toV4Map = orders.stream()
				.filter(v -> v.getQuantity() >= toV4Quantity && v.getBuyerUserRank().ordinal() < UserRank.V4.ordinal())
				.collect(Collectors.toMap(v -> v.getUserId(), v -> true, (existingValue, newValue) -> existingValue));

		List<User> users = userMapper.findAll(UserQueryModel.builder().userTypeEQ(User.UserType.代理).build());
		List<User> v4Users = users.stream().filter(predicate).collect(Collectors.toList());
		Map<Long, User> userMap = users.stream().collect(Collectors.toMap(User::getId, Function.identity()));
        //查询v4的晋升时间 按晋升时间 转成Map
		Map<Long,List<UserUpgrade>> userUpgradeMap = userUpgradeMapper.findAll(UserUpgradeQueryModel.builder().toUserRankEQ(User.UserRank.V4).orderBy("upgradedTime")
				.direction(Direction.ASC).build()).stream().collect(Collectors.groupingBy(UserUpgrade::getUserId));
		List<Order> copyOrders = orders.stream().map(v -> {
			Long userId = v.getUserId();

			Order copy = new Order();
			BeanUtils.copyProperties(v, copy);

			Boolean toV4 = toV4Map.get(userId);
			User user = userMap.get(userId);
			if (toV4 != null  && v.getBuyerUserRank() != UserRank.V4) {
				User parent = userMap.get(user.getParentId());
				while(parent.getUserRank() != UserRank.V4) {
					parent = userMap.get(parent.getParentId());
				}
				copy.setUserId(parent.getId());
				logger.error(user.getNickname() + "直升特级，订单给上级(user.parentId)：" + parent.getNickname() + parent.getUserRank());
			}
			return copy;
		}).collect(Collectors.toList());

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

		{
			Map<Long, Long> userQuantityMap = copyOrders.stream().collect(Collectors.toMap(Order::getUserId, Order::getQuantity, (x, y) -> x + y));
			userQuantityMap.entrySet().stream().forEach(v -> {
				if (v.getValue() > 0) {
					System.out.println("返利奖：" + userMap.get(v.getKey()).getNickname() + "个人销量" + v.getValue() + "元");
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
					logger.error("返利奖：" + userMap.get(v.getKey()).getNickname() + "团队业绩" + v.getValue() + "次");
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

			for (Map.Entry<Long, BigDecimal> entry : profitMap.entrySet()) {
				BigDecimal amount = entry.getValue();
				Long userId = entry.getKey();

				if (amount.compareTo(zero) > 0) {
					try {
						TimeUnit.MILLISECONDS.sleep(200);
					} catch (InterruptedException e1) {
					}
					BigDecimal fee = amount.multiply(FEE_RATE);
					BigDecimal amountAfter = amount.subtract(fee);
					if (deletedUserIds.contains(userId)) {
						User v4Parent = getV4ParentId(userId);
						Boolean newV4 =newV4(userUpgradeMap.get(v4Parent.getId()),yearMonth);
						if(newV4){
							fee = amount.multiply(FEE_RATE1);
							amountAfter = amount.subtract(fee);
							fncComponent.createProfit(v4Parent.getId(), Profit.ProfitType.返利奖, null, year + "年" + month + "月返利奖", CurrencyType.积分, amountAfter, now, "已扣除手续费:" + fee + ";费率:" + FEE_RATE1, amount, fee);
							logger.error(userMap.get(userId).getNickname() + "被踢出系统，奖励给上级" + v4Parent.getNickname() + ";返利奖" + amount + "积分");
						 }else {
								fncComponent.createProfit(v4Parent.getId(), Profit.ProfitType.返利奖, null, year + "年" + month + "月返利奖", CurrencyType.积分, amountAfter, now, "保留发放:" + fee + ";占比: " + FEE_RATE.multiply(new BigDecimal(100)) + "%", amount, fee);
								logger.error(userMap.get(userId).getNickname() + "被踢出系统，奖励给上级" + v4Parent.getNickname() + ";返利奖" + amount + "积分");
							}

						} else {
						  Boolean newV4 =newV4(userUpgradeMap.get(userId),yearMonth);
							if (newV4){
								fee = amount.multiply(FEE_RATE1);
								amountAfter = amount.subtract(fee);
								fncComponent.createProfit(userId, Profit.ProfitType.返利奖, null, year + "年" + month + "月返利奖", CurrencyType.积分, amountAfter, now, "已扣除手续费:" + fee + ";费率: " + FEE_RATE1,amount,fee);
								logger.error(userMap.get(userId).getNickname() + "返利奖" + amount + "积分");
							}else{
								fncComponent.createProfit(userId, Profit.ProfitType.返利奖, null, year + "年" + month + "月返利奖", CurrencyType.积分, amountAfter, now, "保留发放:" + fee + ";占比: " + FEE_RATE.multiply(new BigDecimal(100))+"%",amount,fee);
								logger.error(userMap.get(userId).getNickname() + "返利奖" + amount + "积分");
							}
					}
				}
			}
		}
		{

			Map<Long, Long> userQuantityMap = copyOrders.stream().collect(Collectors.toMap(Order::getUserId, Order::getQuantity, (x, y) -> x + y));
			userQuantityMap.entrySet().stream().forEach(v -> {
				if (v.getValue() > 0) {
					System.out.println("期权奖励：" + userMap.get(v.getKey()).getNickname() + "个人销量" + v.getValue() + "元");
				}
			});

			/* 期权奖励 */
			LongSummaryStatistics summaryStatistics = copyOrders.stream().mapToLong((x) -> x.getQuantity()).summaryStatistics();
			long directorCount = userMapper.count(UserQueryModel.builder().isDirectorEQ(true).build());
			long companySales = summaryStatistics.getSum();
			logger.error("公司总销量:" + companySales);
			Map<Long, BigDecimal> profitShareMap = v4Users.stream().collect(Collectors.toMap(User::getId, v -> {
				Long userId = v.getId();
				Long userQuantity = userQuantityMap.get(userId);
				if (userQuantity != null) {
					BigDecimal quantity = new BigDecimal(userQuantity);
					BigDecimal profit = quantity.multiply(new BigDecimal("0.4"));  //特级服务商：每人新增服务量*0.4股计算；
					if (v.getIsDirector() != null && v.getIsDirector()) {  //联席董事: 每人新增服务量*0.4股+公司月总服务量*0.6股/联席董事人数；
						BigDecimal company = new BigDecimal(companySales).multiply(new BigDecimal("0.6"));
						if (directorCount > 0) {
							company = company.divide(new BigDecimal(directorCount), 0, BigDecimal.ROUND_DOWN);
						}
						profit = profit.add(company);
					}
					return profit;
				} else {
					if(v.getIsDirector() != null && v.getIsDirector()) {  //联席董事: 每人新增服务量*0.4股+公司月总服务量*0.6股；
						BigDecimal company = new BigDecimal(companySales).multiply(new BigDecimal("0.6"));
						logger.error("directorCount:" + directorCount);
						if (directorCount > 0) {
							company = company.divide(new BigDecimal(directorCount), 0, BigDecimal.ROUND_DOWN);
						}
						return company;
					}
					return new BigDecimal("0.00");
				}

			}));
			for (Map.Entry<Long, BigDecimal> entry : profitShareMap.entrySet()) {
				BigDecimal amount = entry.getValue();
				Long userId = entry.getKey();
				if (amount.compareTo(zero) > 0) {
					try {
						TimeUnit.MILLISECONDS.sleep(200);
					} catch (InterruptedException e1) {
					}
					//				BigDecimal fee = amount.multiply(FEE_RATE);
					//				BigDecimal amountAfter = amount.subtract(fee);
					if (deletedUserIds.contains(userId)) {
						User v4Parent = getV4ParentId(userId);
						fncComponent.createProfit(v4Parent.getId(), Profit.ProfitType.期权奖励, null, year + "年" + month + "月期权奖励", CurrencyType.货币期权, amount, now, null,amount,new BigDecimal(0.00));
						logger.error(userMap.get(userId).getNickname() + "被踢出系统，奖励给上级" + v4Parent.getNickname() + ";期权奖励" + amount + "货币期权");
					} else {
						fncComponent.createProfit(userId, Profit.ProfitType.期权奖励, null, year + "年" + month + "月期权奖励", CurrencyType.货币期权, amount, now, null,amount,new BigDecimal(0.00));
						logger.error(userMap.get(userId).getNickname() + "期权奖励" + amount + "货币期权");
					}
				}
			}
		}
		{
			Map<Long, Long> userQuantityMap = orders.stream().collect(Collectors.toMap(Order::getUserId, Order::getQuantity, (x, y) -> x + y));
			userQuantityMap.entrySet().stream().forEach(v -> {
				if (v.getValue() > 0) {
					System.out.println("董事贡献奖：" + userMap.get(v.getKey()).getNickname() + "个人销量" + v.getValue() + "元");
				}
			});

			logger.error("准备董事贡献奖励计算....");
			List<User> v4Directors = v4Users.stream().filter(v -> v.getIsDirector() != null && v.getIsDirector()).collect(Collectors.toList());
			Map<Long, TeamModel> teamV4Map = v4Directors.stream()
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
						return teamModel;
					}).collect(Collectors.toMap(v -> v.getUser().getId(), Function.identity()));

			logger.error("董事贡献奖励：profitDirectorMap...");
			Map<Long, BigDecimal> profitDirectorMap = v4Directors.stream().collect(Collectors.toMap(User::getId, v -> {
				Long userId = v.getId();
				Long myQuantity = userQuantityMap.get(userId) == null ? 0L : userQuantityMap.get(userId);
				BigDecimal profit = new BigDecimal(myQuantity).multiply(new BigDecimal("2.00"));
				BigDecimal teamProfit = teamV4Map.get(userId).getV4Children().stream()
						.map(u -> {
							logger.error("董事贡献奖励：计算teamV4Map...");
							Long parentId = u.getParentId();
							Long tempParentId = null;
							boolean isChildrenOfDirector = false;
							boolean isMoreLevelDirector = false;
							while(!isChildrenOfDirector) {
								if (parentId.equals(userId)) {
									break;
								}
								User user = userMap.get(parentId);
								Long directV4ParentId = getDirectV4ParentId(predicate, userMap, user);
								if (user.getIsDirector() != null && user.getIsDirector()) {
									isChildrenOfDirector = true;
									break;
								}

								parentId = directV4ParentId;
							}

							if (isChildrenOfDirector) {
								tempParentId = userMap.get(parentId).getParentId();
								while(!isMoreLevelDirector) {
									if (tempParentId.equals(userId)) {
										break;
									}
									User user = userMap.get(tempParentId);
									Long directV4ParentId = getDirectV4ParentId(predicate, userMap, user);
									if (user.getIsDirector() != null && user.getIsDirector()) {
										isMoreLevelDirector = true;
										break;
									}

									tempParentId = directV4ParentId;
								}
							}

							Long quantity = userQuantityMap.get(u.getId()) == null ? 0L : userQuantityMap.get(u.getId());
							BigDecimal innerProfit = zero;
							if (isMoreLevelDirector) {
								return innerProfit;
							}
							//自己是董事且有量 计算 只能算到上级董事
							if(u.getIsDirector() != null && u.getIsDirector()){
								if (! getflage(userMap,u,userId)){
									return zero;
								}
							}

							if(u.getIsDirector() != null && u.getIsDirector()) {
								innerProfit = new BigDecimal(quantity);
							} else {
								if (isChildrenOfDirector) {
									innerProfit = new BigDecimal(quantity);
								} else {
									innerProfit = new BigDecimal(quantity).multiply(new BigDecimal("2.00"));
								}
							}
							logger.error("董事贡献奖励, 上级：" + v.getNickname() + "下级：" + u.getNickname() + "最终收益：" + innerProfit);
							return innerProfit;
						})
						.reduce(new BigDecimal("0.00"), BigDecimal::add);
				return profit.add(teamProfit);
			}));

			logger.error("董事贡献奖励：for循环...");
			for (Map.Entry<Long, BigDecimal> entry : profitDirectorMap.entrySet()) {
				BigDecimal amount = entry.getValue();
				Long userId = entry.getKey();
				if (amount.compareTo(zero) > 0) {
					try {TimeUnit.MILLISECONDS.sleep(200);} catch (InterruptedException e1) {}
					BigDecimal fee = amount.multiply(FEE_RATE);
					BigDecimal amountAfter = amount.subtract(fee);
					if (deletedUserIds.contains(userId)) {
						User v4Parent = getV4ParentId(userId);
						Boolean newV4 =newV4(userUpgradeMap.get(v4Parent.getId()),yearMonth);
						if (newV4){
							fee = amount.multiply(FEE_RATE1);
							amountAfter = amount.subtract(fee);
							fncComponent.createProfit(v4Parent.getId(), Profit.ProfitType.董事贡献奖, null, year + "年" + month + "月董事贡献奖", CurrencyType.积分, amountAfter, now, "已扣除手续费:" + fee + ";费率: " + FEE_RATE1,amount,fee);
							logger.error(userMap.get(userId).getNickname() + "被踢出系统，奖励给上级" + v4Parent.getNickname()   + ";董事贡献奖" + amount + "积分");
						}else{
							fncComponent.createProfit(v4Parent.getId(), Profit.ProfitType.董事贡献奖, null, year + "年" + month + "月董事贡献奖", CurrencyType.积分, amountAfter, now, "保留发放:" + fee + ";占比: " + FEE_RATE.multiply(new BigDecimal(100))+"%",amount,fee);
							logger.error(userMap.get(userId).getNickname() + "被踢出系统，奖励给上级" + v4Parent.getNickname()   + ";董事贡献奖" + amount + "积分");
						}

					} else {
						Boolean newV4 =newV4(userUpgradeMap.get(userId),yearMonth);
						if(newV4){
							fee = amount.multiply(FEE_RATE1);
							amountAfter = amount.subtract(fee);
							fncComponent.createProfit(userId, Profit.ProfitType.董事贡献奖, null, year + "年" + month + "月董事贡献奖", CurrencyType.积分, amountAfter, now, "已扣除手续费:" + fee + ";费率: " + FEE_RATE1,amount,fee);
							logger.error(userMap.get(userId).getNickname() + "董事贡献奖" + amount + "积分");
						}else{
							fncComponent.createProfit(userId, Profit.ProfitType.董事贡献奖, null, year + "年" + month + "月董事贡献奖", CurrencyType.积分, amountAfter, now, "保留发放:" + fee + ";占比: " + FEE_RATE.multiply(new BigDecimal(100))+"%",amount,fee);
							logger.error(userMap.get(userId).getNickname() + "董事贡献奖" + amount + "积分");
						}

					}

				}
			}
			logger.error("董事贡献奖励：end...");
		}

		logger.error("月结 结束。。。准备插入月结标志 orderMonthlySettlement");
		orderMonthlySettlement = new OrderMonthlySettlement();
		orderMonthlySettlement.setYearAndMonth(yearAndMonth);
		orderMonthlySettlement.setSettledUpTime(now);
		orderMonthlySettlement.setSettlementType(Constants.SETTLEMENT_TYPE_2);
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
		Long loginId = config.getSysUserId();
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
				fncComponent.createAndGrantProfit(sellerId, loginId, Profit.ProfitType.订单收款, orderId, "订单" + order.getSn() + "收款", CurrencyType.积分, amount, paidTime);
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
			payment.setCurrencyType1(CurrencyType.U币);
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
		Long userId = orderQueryModel.getUserIdEQ();
		Map<String ,Object> returnMap = new HashMap<>();
		int moth = DateUtil.getMoth(new Date());
		long salesVolumeData [] = new long[moth-1];
        long shipmentData [] = new long[moth-1];
        double svData [] = new double[moth-1];
        double sData [] = new double[moth-1];
        long salesVolumeTeamData [] = new long[moth-1];
        long shipmentTeamData [] = new long[moth-1];
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
			if (i-2 >= 0 && salesVolumeData [i-1] != 0 && salesVolumeData [i-2] != 0){
				sv = new BigDecimal((salesVolumeData [i-2] - salesVolumeData [i-1]) / salesVolumeData [i-1] * 100).setScale(2 , RoundingMode.UP).doubleValue()  ;
			}else if (i-2 >= 0 && salesVolumeData [i-1] == 0 && salesVolumeData [i-2] > 0){
				sv = 100;
			}else if (i-2 >= 0 && salesVolumeData [i-1] > 0 && salesVolumeData [i-2] == 0){
				sv = -100;
			}
			data = sv;
			if (i-2 >= 0){
				svData[i-2] = data;
			}
			//出货量环比
			double s = 0.00d;
			if (i-2 >= 0 && shipmentData [i-1] != 0 && shipmentData [i-2] != 0){
				s = new BigDecimal((shipmentData [i-2] - shipmentData [i-1]) / shipmentData [i-1] * 100 ).setScale(2 , RoundingMode.UP).doubleValue() ;
			}else if (i-2 >= 0 && shipmentData [i-1] == 0 && shipmentData [i-2] > 0){
				s = 100;
			}else if (i-2 >= 0 && shipmentData [i-1] > 0 && shipmentData [i-2] == 0){
				s = -100;
			}
			data = s;
			if (i-2 >= 0) {
				sData[i - 2] = data;
			}
		}
		//查询我的团队进、出货量
		List<Long> userIdList = new ArrayList<>();
		List<User> userList = new ArrayList<>();
		List<User> users = userMapper.findAll(new UserQueryModel());
		List<User> children = TreeHelper.sortBreadth2(users, orderQueryModel.getUserIdEQ().toString(), v -> {
			TreeNode treeNode = new TreeNode();
			treeNode.setId(v.getId().toString());
			treeNode.setParentId(v.getParentId() == null ? null : v.getParentId().toString());
			return treeNode;
		});
		children.add(0,userMapper.findOne(userId));
		userList = children.stream().filter(v -> v.getUserRank() == User.UserRank.V4).collect(Collectors.toList());
        if (userList.size() > 0 && userList != null){
            for (User user: userList) {
                userIdList.add(user.getId());
            }
			orderQueryModel.setUserIdEQ(null);
			orderQueryModel.setSellerIdEQ(null);
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
        returnMap.put("salesVolumeData", DateUtil.longarryToString(salesVolumeData, true));
        returnMap.put("shipmentData", DateUtil.longarryToString(shipmentData, true));
        returnMap.put("svData", DateUtil.arryToString(svData, true));
        returnMap.put("sData", DateUtil.arryToString(sData, true));
        returnMap.put("salesVolumeTeamData", DateUtil.longarryToString(salesVolumeTeamData, true));
        returnMap.put("shipmentTeamData", DateUtil.longarryToString(shipmentTeamData, true));
		return returnMap;
	}

    @Override
    public Map<String, Object> querySalesVolumeDetail(OrderQueryModel orderQueryModel) {
        Map<String ,Object> returnMap = new HashMap<>();
        int moth = DateUtil.getMoth(new Date());
        long salesVolumeData [] = new long[moth-1];
        long shipmentData [] = new long[moth-1];
		double svData [] = new double[moth-1];
		double sData [] = new double[moth-1];
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
			if (i-2 >= 0 && salesVolumeData [i-1] != 0 && salesVolumeData [i-2] != 0){
				sv = new BigDecimal((salesVolumeData [i-2] - salesVolumeData [i-1]) / salesVolumeData [i-1] * 100).setScale(2 , RoundingMode.UP).doubleValue()  ;
			}else if (i-2 >= 0 && salesVolumeData [i-1] == 0 && salesVolumeData [i-2] > 0){
				sv = 100;
			}else if (i-2 >= 0 && salesVolumeData [i-1] > 0 && salesVolumeData [i-2] == 0){
				sv = -100;
			}
			data = sv;
			if (i-2 >= 0) {
				svData[i - 2] = data;
			}
			//出货量环比
			double s = 0.00d;
			if (i-2 >= 0 && shipmentData [i-1] != 0 && shipmentData [i-2] != 0){
				s = new BigDecimal((shipmentData [i-2] - shipmentData [i-1]) / shipmentData [i-1] * 100 ).setScale(2 , RoundingMode.UP).doubleValue() ;
			}else if (i-2 >= 0 && shipmentData [i-1] == 0 && shipmentData [i-2] > 0){
				s = 100;
			}else if (i-2 >= 0 && shipmentData [i-1] > 0 && shipmentData [i-2] == 0){
				s = -100;
			}
			data = s;
			if (i-2 >= 0) {
				sData[i - 2] = data;
			}
		}
        returnMap.put("salesVolumeData", DateUtil.longarryToString(salesVolumeData, true));
        returnMap.put("shipmentData", DateUtil.longarryToString(shipmentData, true));
		returnMap.put("svData", DateUtil.arryToString(svData, true));
		returnMap.put("sData", DateUtil.arryToString(sData, true));
        return returnMap;
    }

	private User getV4ParentId(Long id) {
	    Long parentId = id;
	    User parent = userMapper.findOne(parentId);
	    int whileTimes = 0;
	    while (parent.getUserRank() != UserRank.V4) {
		    if (whileTimes > 1000) {
			    throw new BizException(BizCode.ERROR, "循环引用"); // 防御性校验
		    }
		    parent = userMapper.findOne(parent.getParentId());
	    }
	    if (parent.getIsDeleted() != null && parent.getIsDeleted()) {
		    getV4ParentId(parent.getParentId());
	    }
	    return parent;
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
		plist = plist.stream().filter(u -> u.getIsDirector() != null && !u.getIsDirector()).collect(Collectors.toList());
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


	/**
	 * 判断是不是新生特级（两个月内升上来的）
	 * @param userUpgradesList
	 * @param yearMonth
     * @return
     */
	private boolean newV4(List<UserUpgrade>userUpgradesList,YearMonth yearMonth){
		if (userUpgradesList!=null&&!userUpgradesList.isEmpty()){
			UserUpgrade userUpgrade = userUpgradesList.get(0);
			YearMonth yearMonth1 = YearMonth.of(DateUtil.getYear(DateUtil.getMonthData(userUpgrade.getUpgradedTime(), 2, 0)),
					DateUtil.getMothNum(DateUtil.getMonthData(userUpgrade.getUpgradedTime(), 2, 0)));
			if (yearMonth1.isAfter(yearMonth)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 查询 当前董事是不是 直属董事（即董事下面的董事）
	 * 是 ：返回true
	 * @param userMap
	 * @param u
     * @return
     */
	private boolean getflage(Map<Long,User>userMap,User u,long userId){
	    boolean returnfalge = false;
		boolean flage =true;
		User u1 = u;
		do{
		  User up = userMap.get(u1.getParentId());
		  if(up==null) {
			  flage= false;
			  returnfalge=true;
		  }else if(up.getIsDirector() != null && up.getIsDirector()){
			if (up.getId()==userId) {
				returnfalge=true;
			}
			 flage=false;
		  }
			u1=up;
	   }while (flage);
		return returnfalge;
	}

	/**
	 * 检测 能不能下单
	 * @param parentId 发货人的ID
	 * @return
     */
	@Override
	public Boolean checkOrderStore(Long parentId, Integer productType,Long quantity) {
		OrderStoreQueryModel orderStoreQueryModel = new OrderStoreQueryModel();
		orderStoreQueryModel.setIsEndEQ(1);
		orderStoreQueryModel.setUserIdEQ(parentId);
		orderStoreQueryModel.setProductTypeEQ(productType);
		List<OrderStore> orderList = orderStoreMapper.findAll(orderStoreQueryModel);
		if(orderList!=null&&!orderList.isEmpty()){
			OrderStore orderStoreOld = orderList.get(0);
			if (orderStoreOld.getAfterNumber()>=quantity){
				return true;
			}
		}

		return false;
	}

}
