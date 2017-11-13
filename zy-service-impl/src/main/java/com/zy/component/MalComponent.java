 package com.zy.component;

 import com.zy.Config;
 import com.zy.common.exception.BizException;
 import com.zy.common.exception.ConcurrentException;
 import com.zy.entity.mal.Order;
 import com.zy.entity.mal.OrderItem;
 import com.zy.entity.mal.Product;
 import com.zy.entity.mergeusr.MergeUser;
 import com.zy.entity.usr.User;
 import com.zy.entity.usr.User.UserRank;
 import com.zy.extend.Producer;
 import com.zy.mapper.OrderItemMapper;
 import com.zy.mapper.OrderMapper;
 import com.zy.mapper.ProductMapper;
 import com.zy.mapper.UserMapper;
 import com.zy.model.BizCode;
 import com.zy.model.Constants;
 import com.zy.service.MergeUserService;
 import com.zy.service.UserCheckService;
 import groovy.lang.Binding;
 import groovy.lang.GroovyShell;
 import groovy.lang.Script;
 import org.codehaus.groovy.control.CompilerConfiguration;
 import org.codehaus.groovy.control.customizers.ImportCustomizer;
 import org.slf4j.Logger;
 import org.slf4j.LoggerFactory;
 import org.springframework.beans.factory.annotation.Autowired;
 import org.springframework.stereotype.Component;
 import org.springframework.validation.annotation.Validated;

 import javax.validation.constraints.NotNull;
 import java.math.BigDecimal;
 import java.util.Calendar;
 import java.util.Date;

 import static com.zy.common.util.ValidateUtils.NOT_NULL;
 import static com.zy.common.util.ValidateUtils.validate;
 import static com.zy.entity.mal.Order.OrderStatus.*;
 import static com.zy.entity.usr.User.UserRank.V4;

@Component
@Validated
public class MalComponent {

	private Logger logger = LoggerFactory.getLogger(MalComponent.class);

	@Autowired
	private Config config;

	@Autowired
	private UserMapper userMapper;

	@Autowired
	private OrderMapper orderMapper;

	@Autowired
	private OrderItemMapper orderItemMapper;

	@Autowired
	private ProductMapper productMapper;

	@Autowired
	private UsrComponent usrComponent;

	@Autowired
	private MergeUserService mergeUserService;

	@Autowired
	private UserCheckService userCheckService;

	@Autowired
	private Producer producer;

	public BigDecimal getPrice(@NotNull Long productId, @NotNull User.UserRank userRank, long quantity) {

		Product product = productMapper.findOne(productId);
		validate(product, NOT_NULL, "product id " + productId + " is not found");
		Product.ProductPriceType productPriceType = product.getProductPriceType();
		if (productPriceType == Product.ProductPriceType.一般价格) {
			return product.getPrice();
		} else if (productPriceType == Product.ProductPriceType.脚本价格) {
			GroovyShell withdrawFeeRateShell;
			ImportCustomizer importCustomizer = new ImportCustomizer();
			CompilerConfiguration compilerConfiguration = new CompilerConfiguration();
			compilerConfiguration.addCompilationCustomizers(importCustomizer);
			withdrawFeeRateShell = new GroovyShell(compilerConfiguration);
			String script = product.getPriceScript();
			Binding binding = new Binding();
			Script parse = withdrawFeeRateShell.parse(script);
			binding.setVariable("userRank", userRank);
			binding.setVariable("quantity", quantity);
			parse.setBinding(binding);
			BigDecimal result = (BigDecimal) parse.run();
			return result;
		} else {
			throw new BizException(BizCode.ERROR, "暂不支持矩阵价格");
		}
	}

	public User calculateSeller(User.UserRank userRank, Long productId, long quantity, Long parentId) {
		Product product = productMapper.findOne(productId);

		if (userRank == V4) {
			return userMapper.findOne(config.getSysUserId());
		}
		UserRank upgradeUserRank = null;
		if (product.getProductType() == 1){
			upgradeUserRank = getUpgradeUserRank(userRank, productId, quantity);
		}else if (product.getProductType() == 2){
			upgradeUserRank = userRank;
		}

		if (upgradeUserRank == V4) {
			return userMapper.findOne(config.getSysUserId());
		}

		int whileTimes = 0;
		while (parentId != null) {
			if (whileTimes > 1000) {
				break; // 防御性循环引用校验
			}
			User parent = userMapper.findOne(parentId);
			if (parent.getUserType() != User.UserType.代理) {
				logger.error("代理父级数据错误,parentId=" + parentId);
				throw new BizException(BizCode.ERROR, "代理父级数据错误"); // 防御性校验
			}
			if (parent.getUserRank().getLevel() > upgradeUserRank.getLevel()) {
				return parent;
			}
			parentId = parent.getParentId();
			whileTimes ++;
		}

		logger.error("代理父级数据错误,parentId=" + parentId);
		throw new BizException(BizCode.ERROR, "代理父级数据错误");

	}


	public UserRank getUpgradeUserRank(User.UserRank userRank, Long productId, long quantity) {
		UserRank upgradeUserRank = userRank;
		if (config.isOld(productId)) {
			if (userRank == UserRank.V0) {
				if (quantity >= 300) {
					upgradeUserRank = UserRank.V3;
				} else if (quantity >= 100 && quantity < 300) {
					upgradeUserRank = UserRank.V2;
				} else if (quantity >= 15) {
					upgradeUserRank = UserRank.V1;
				}
			} else if (userRank == UserRank.V1) {
				if (quantity >= 300) {
					upgradeUserRank = UserRank.V3;
				} else if (quantity >= 100 && quantity < 300) {
					upgradeUserRank = UserRank.V2;
				}
			} else if (userRank == UserRank.V2) {
				if (quantity >= 300) {
					upgradeUserRank = UserRank.V3;
				}
			}
		} else {
			if (userRank == UserRank.V0) {
				if (quantity >= 240) {
					upgradeUserRank = UserRank.V3;
				} else if (quantity >= 80 && quantity < 240) {
					upgradeUserRank = UserRank.V2;
				} else if (quantity >= 5) {
					upgradeUserRank = UserRank.V1;
				}
			} else if (userRank == UserRank.V1) {
				if (quantity >= 240) {
					upgradeUserRank = UserRank.V3;
				} else if (quantity >= 80 && quantity < 240) {
					upgradeUserRank = UserRank.V2;
				}
			} else if (userRank == UserRank.V2) {
				if (quantity >= 240) {
					upgradeUserRank = UserRank.V3;
				}
			}

			/*一次性购买3600支 升级成特级服务商*/
			if(quantity >= 3600) {
				upgradeUserRank = UserRank.V4;
			}
		}
		return upgradeUserRank;
	}

	public Boolean isToV4(User.UserRank userRank, Long productId, long quantity) {
		UserRank upgradeUserRank = userRank;
		if(userRank != null && userRank == UserRank.V4){
			return  null;
		}
		if (config.isOld(productId)) {
			return null;
		} else {
			/*一次性购买3600支 升级成特级服务商*/
			if(quantity >= 3600) {
				return true;
			}
		}
		return null;
	}


	public void successOrder(@NotNull Long orderId) {
		final Order order = orderMapper.findOne(orderId);
		Product product = productMapper.findOne(order.getProductId());
		validate(order, NOT_NULL, "order does not found, order id " + orderId);
		Order.OrderStatus orderStatus = order.getOrderStatus();
		if (orderStatus == 已支付) {
			return; // 幂等操作
		} else if (orderStatus != 待支付 || orderStatus != 待确认) {
			logger.warn("订单状态警告 {} 订单id {}", order.getOrderStatus(), order.getId());
		}


		Date paidTime = null;
		if(order.getOrderType() == Order.OrderType.补单) {
			paidTime = order.getCreatedTime();
		} else {
			paidTime = new Date();
		}
		order.setOrderStatus(已支付);
		order.setPaidTime(paidTime);
		if(orderMapper.update(order) == 0) {
			throw new ConcurrentException();
		}
		//判断推荐人是否要升级
		MergeUser inviter = null;
		if (order.getInviterId() != null){
			inviter = mergeUserService.findByUserIdAndProductType(order.getInviterId(), 2);
		}

		//判断商品类型
		if (order.getProductType() == 2){
			userCheckService.editOderStoreIn(order.getId(),order.getUserId(),2);
			if (order.getBuyerUserRank() != UserRank.V4 && order.getQuantity() < 2000){
				userCheckService.editOrderStoreOut(order.getId(),order.getSellerId(),2);
			}
			if (product.getSkuCode().equals("zy-slj")){
				if ( inviter != null && inviter.getUserRank() != UserRank.V0){
					userCheckService.checkUserLevel(order.getInviterId(), 0l,2);
				}
				userCheckService.checkUserLevel(order.getUserId(),order.getQuantity(),2);
			}

		}

		OrderItem orderItem = orderItemMapper.findByOrderId(orderId).get(0);
		long quantity = orderItem.getQuantity();
		Long productId = orderItem.getProductId();

		Long userId = order.getUserId();
		User user = userMapper.findOne(userId);
		UserRank userRank = user.getUserRank();

		UserRank upgradeUserRank = null;

		if (product.getProductType() == 1){
			upgradeUserRank = getUpgradeUserRank(userRank, productId, quantity);
		}else if (product.getProductType() == 2 && product.getSkuCode().equals("zy-slj-pyqzy")){
			upgradeUserRank = userRank;
			Calendar calendar = Calendar.getInstance();
			calendar.set(2017, 10, 12, 23, 59, 59);
			Date date = calendar.getTime();
			Date now = new Date();
			if (now.getTime() > date.getTime() ){
				mergeUserService.createByUserId(userId, 2);
			}
		}

		if (product.getProductType() == 1){
			Boolean isToV4 = isToV4(userRank,productId,quantity);
			if (upgradeUserRank.getLevel() > userRank.getLevel()) {
				usrComponent.upgrade(userId, userRank, upgradeUserRank,isToV4);
			}
		}

		producer.send(Constants.TOPIC_ORDER_PAID, order.getId());
	}



	public void failureOrder(@NotNull Long orderId, String remark) {
		final Order order = orderMapper.findOne(orderId);
		validate(order, NOT_NULL, "order does not found, order id " + orderId);
		Order.OrderStatus orderStatus = order.getOrderStatus();
		if (orderStatus == 已取消) {
			return; // 幂等操作
		} else if (orderStatus != 待确认) {
			throw new BizException(BizCode.ERROR, "只有待确认订单才能拒绝");
		}

		order.setOrderStatus(Order.OrderStatus.已取消);
		order.setSellerMemo(remark);
		if(orderMapper.update(order) == 0) {
			throw new ConcurrentException();
		}

		producer.send(Constants.TOPIC_ORDER_OFFLINE_REJECTED, order.getId());

	}

	public MergeUser catchSellerId(UserRank userRank, Long productId, long quantity, Long parentId) {
		Product product = productMapper.findOne(productId);

		if (userRank == V4) {
			return mergeUserService.findByUserIdAndProductType(config.getSysUserId(),product.getProductType());
		}
		UserRank upgradeUserRank = null;
		upgradeUserRank = getUpUserRank(userRank, productId, quantity);

		if (upgradeUserRank == V4) {
			return mergeUserService.findByUserIdAndProductType(config.getSysUserId(),product.getProductType());
		}

		int whileTimes = 0;
		while (parentId != null) {
			if (whileTimes > 1000) {
				break; // 防御性循环引用校验
			}
			MergeUser parent = mergeUserService.findByUserIdAndProductType(parentId,product.getProductType());
			if (parent == null) {
				logger.error("代理父级数据错误,parentId=" + parentId);
				throw new BizException(BizCode.ERROR, "代理父级数据错误"); // 防御性校验
			}
			if (parent.getUserRank().getLevel() > upgradeUserRank.getLevel()) {
				return parent;
			}
			parentId = parent.getParentId();
			whileTimes ++;
		}

		logger.error("代理父级数据错误,parentId=" + parentId);
		throw new BizException(BizCode.ERROR, "代理父级数据错误");
	}

	public UserRank getUpUserRank(User.UserRank userRank, Long productId, long quantity) {
		UserRank upgradeUserRank = userRank;
		if (userRank == UserRank.V0) {
			if (quantity >= 150 && quantity <2000) {
				upgradeUserRank = UserRank.V3;
			} else if (quantity >= 20 && quantity < 150) {
				upgradeUserRank = UserRank.V2;
			} else if (quantity >= 4 && quantity < 20) {
				upgradeUserRank = UserRank.V1;
			}
		} else if (userRank == UserRank.V1) {
			if (quantity >= 150 && quantity <2000) {
				upgradeUserRank = UserRank.V3;
			} else if (quantity >= 20 && quantity < 150) {
				upgradeUserRank = UserRank.V2;
			}
		} else if (userRank == UserRank.V2) {
			if (quantity >= 150 && quantity <2000) {
				upgradeUserRank = UserRank.V3;
			}
		}

		/*一次性购买3600支 升级成特级服务商*/
		if(quantity >= 2000) {
			upgradeUserRank = UserRank.V4;
		}
		return upgradeUserRank;
	}
}
