package com.zy.entity.mal;

import com.zy.common.extend.StringBinder;
import com.zy.entity.fnc.Payment;
import com.zy.entity.fnc.Profit;
import com.zy.entity.fnc.Transfer;
import com.zy.entity.usr.User;
import com.zy.entity.usr.User.UserRank;
import com.zy.model.ImageVo;
import io.gd.generator.annotation.Field;
import io.gd.generator.annotation.Type;
import io.gd.generator.annotation.query.Query;
import io.gd.generator.annotation.query.QueryModel;
import io.gd.generator.annotation.view.AssociationView;
import io.gd.generator.annotation.view.CollectionView;
import io.gd.generator.annotation.view.View;
import io.gd.generator.annotation.view.ViewObject;
import io.gd.generator.api.query.Predicate;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;

import static com.zy.entity.mal.Order.*;

@Entity
@Table(name = "mal_order")
@Getter
@Setter
@QueryModel
@ViewObject(groups = {VO_LIST, VO_DETAIL, VO_ADMIN, VO_ADMIN_FULL},
		collectionViews = {
				@CollectionView(name = "payments", type = ArrayList.class, groups = {VO_ADMIN_FULL}, elementGroup = Payment.VO_ADMIN),
				@CollectionView(name = "profits", type = ArrayList.class, groups = {VO_ADMIN_FULL}, elementGroup = Profit.VO_ADMIN),
				@CollectionView(name = "transfers", type = ArrayList.class, groups = {VO_ADMIN_FULL}, elementGroup = Transfer.VO_ADMIN),
				@CollectionView(name = "orderItems", type = ArrayList.class, groups = {VO_LIST, VO_DETAIL}, elementGroup = OrderItem.VO)
		},
		views = {
				@View(name = "isPlatformDeliver", type = Boolean.class, groups = {VO_ADMIN, VO_ADMIN_FULL, VO_DETAIL, VO_LIST})
		}

)
@Type(label = "订单")
public class Order implements Serializable {
	
	public static final String VO_ADMIN = "OrderAdminVo";
	public static final String VO_ADMIN_FULL = "OrderAdminFullVo";
	public static final String VO_LIST = "OrderListVo";
	public static final String VO_DETAIL = "OrderDetailVo";

	@Type(label = "订单状态")
	public enum OrderStatus {
		待支付, 待确认, 已支付, 已发货, 已完成, 已退款, 已取消
	}

	@Type(label = "订单类型")
	public enum OrderType {
		普通订单, 补单
	}

	@Id
	@Field(label = "id")
	@View
	private Long id;

	@NotBlank
	@Column(length = 60, unique = true)
	@Query(Predicate.LK)
	@Field(label = "订单编号")
	@View
	private String sn;

	@NotNull
	@Query({Predicate.EQ, Predicate.IN})
	@Field(label = "用户id")
	@View
	@AssociationView(name = "user", groups = {VO_ADMIN, VO_ADMIN_FULL, VO_DETAIL}, associationGroup = User.VO_ADMIN_SIMPLE)
	private Long userId;

	@NotNull
	@Field(label = "买家用户等级")
	@View(groups = {VO_ADMIN, VO_ADMIN_FULL, VO_DETAIL})
	@View(name = "buyerUserRankLabel", type = String.class, groups = {VO_ADMIN, VO_ADMIN_FULL, VO_DETAIL})
	private UserRank buyerUserRank;

	@NotNull
	@Query({Predicate.EQ, Predicate.IN})
	@Field(label = "卖家id")
	@View
	@AssociationView(name = "seller", groups = {VO_ADMIN, VO_ADMIN_FULL, VO_DETAIL}, associationGroup = User.VO_ADMIN_SIMPLE)
	private Long sellerId;

	@Field(label = "卖家用户等级", description = "如果是平台用户可以为空")
	@View(groups = {VO_ADMIN, VO_ADMIN_FULL, VO_DETAIL})
	@View(name = "sellerUserRankLabel", type = String.class, groups = {VO_ADMIN, VO_ADMIN_FULL, VO_DETAIL})
	private UserRank sellerUserRank;

	@Query({Predicate.EQ})
	@Field(label = "子系统id")
	private Long rootId;

	@Query({Predicate.EQ})
	@Field(label = "直属特级id")
	private Long v4UserId;

	@View(groups = {VO_ADMIN, VO_ADMIN_FULL, VO_DETAIL, VO_LIST})
	@NotNull
	@Field(label = "是否支付给平台")
	@Query(Predicate.EQ)
	private Boolean isPayToPlatform;

	@NotBlank
	@Query(Predicate.LK)
	@Field(label = "标题")
	@View
	private String title;

	@NotNull
	@View(name = "createdTimeLabel", type = String.class)
	@Field(label = "下单时间")
	@Query({Predicate.LT, Predicate.GTE})
	private Date createdTime;

	@Field(label = "过期时间")
	@View(name = "expiredTimeLabel", type = String.class)
	private Date expiredTime;

	@View(name = "paidTimeLabel", type = String.class, groups = {VO_DETAIL, VO_ADMIN, VO_ADMIN_FULL})
	@Field(label = "支付时间")
	@Query({Predicate.LT, Predicate.GTE})
	private Date paidTime;

	@View(name = "refundedTimeLabel", type = String.class, groups = {VO_DETAIL, VO_ADMIN, VO_ADMIN_FULL})
	@Field(label = "退款时间")
	private Date refundedTime;

	@NotNull
	@Query({Predicate.EQ, Predicate.IN})
	@Field(label = "订单状态")
	@View
	@View(name = "orderStatusStyle", type = String.class, groups = {VO_ADMIN, VO_ADMIN_FULL})
	private OrderStatus orderStatus;

	@NotNull
	@Query({Predicate.EQ})
	@Field(label = "订单类型")
	@View
	private OrderType orderType;

	@NotNull
	@DecimalMin("0.00")
	@Field(label = "优惠金额")
	@View(groups = VO_ADMIN)
	private BigDecimal discountFee;

	@NotNull
	@DecimalMin("0.00")
	@View(name = "amountLabel", type = String.class)
	@View
	@Field(label = "应付总金额", description = "应付总金额 = Σ订单子项金额 - 优惠金额")
	private BigDecimal amount;

	@DecimalMin("0.00")
	@View(groups = {VO_DETAIL, VO_ADMIN, VO_ADMIN_FULL})
	@Field(label = "退款金额")
	private BigDecimal refund;

	@View(type = String.class, groups = {VO_DETAIL})
	@View(groups = {VO_ADMIN})
	@Field(label = "退款备注")
	private String refundRemark;

	@View(groups = {VO_DETAIL, VO_ADMIN, VO_ADMIN_FULL})
	@Field(label = "买家留言")
	@Column(length = 1000)
	private String buyerMemo;

	@View(groups = {VO_DETAIL, VO_ADMIN, VO_ADMIN_FULL})
	@Field(label = "卖家留言")
	@Column(length = 1000)
	private String sellerMemo;

	@View(groups = {VO_DETAIL, VO_ADMIN, VO_ADMIN_FULL})
	@Field(label = "备注")
	private String remark;

	@NotNull
	@Version
	@Field(label = "乐观锁")
	private Integer version;
	
	@Field(label = "奖励是否结算")
	@View(groups = {VO_DETAIL, VO_ADMIN, VO_ADMIN_FULL})
	@NotNull
	@Query(Predicate.EQ)
	private Boolean isProfitSettledUp;

	@Field(label = "是否已结算")
	@View(groups = {VO_DETAIL, VO_ADMIN, VO_ADMIN_FULL})
	@NotNull
	@Query(Predicate.EQ)
	private Boolean isSettledUp;

	@View(groups = {VO_DETAIL, VO_ADMIN, VO_ADMIN_FULL})
	@NotNull
	@Field(label = "是否已拷贝")
	@Query(Predicate.EQ)
	private Boolean isCopied;

	@View(name = "copiedTimeLabel", type = String.class, groups = {VO_ADMIN, VO_ADMIN_FULL})
	@Field(label = "拷贝时间")
	@View(groups = {VO_ADMIN, VO_ADMIN_FULL})
	private Date copiedTime;

	@Field(label = "对应订单id")
	@View(groups = {VO_ADMIN, VO_ADMIN_FULL})
	private Long refId;

	@View(name = "deliveredTimeLabel", type = String.class, groups = {VO_DETAIL, VO_ADMIN, VO_ADMIN_FULL})
	@Field(label = "发货时间")
	@View(groups = {VO_DETAIL, VO_ADMIN, VO_ADMIN_FULL})
	private Date deliveredTime;

	@Field(label = "是否物流发货")
	@View(name = "isUseLogisticsLabel", type = String.class, groups = {VO_ADMIN, VO_ADMIN_FULL})
	@View(groups = {VO_DETAIL, VO_ADMIN})
	private Boolean isUseLogistics;

	@Query(Predicate.LK)
	@Field(label = "物流公司名")
	@StringBinder
	@View(groups = {VO_DETAIL, VO_ADMIN, VO_ADMIN_FULL})
	private String logisticsName;

	@Query(Predicate.LK)
	@Field(label = "物流单号")
	@StringBinder
	@View(groups = {VO_DETAIL, VO_ADMIN, VO_ADMIN_FULL})
	private String logisticsSn;

	@DecimalMin("0.00")
	@Field(label = "物流费")
	@View(groups = {VO_DETAIL, VO_ADMIN, VO_ADMIN_FULL})
	private BigDecimal logisticsFee;

	@Field(label = "是否买家支付邮费")
	@View(groups = {VO_ADMIN_FULL})
	private Boolean isBuyerPayLogisticsFee;

	@NotNull
	@Field(label = "收件人区域")
	@View(groups = {VO_DETAIL, VO_ADMIN, VO_ADMIN_FULL})
	private Long receiverAreaId;

	@NotBlank
	@StringBinder
	@Field(label = "收件人姓名")
	@View(groups = {VO_DETAIL, VO_ADMIN, VO_ADMIN_FULL})
	@Query(Predicate.EQ)
	private String receiverRealname;

	@NotBlank
	@StringBinder
	@Field(label = "收件人电话")
	@View(groups = {VO_DETAIL, VO_ADMIN, VO_ADMIN_FULL})
	@Query(Predicate.EQ)
	private String receiverPhone;

	@NotBlank
	@Field(label = "收件人省份")
	@View(groups = {VO_DETAIL, VO_ADMIN, VO_ADMIN_FULL})
	private String receiverProvince;

	@NotBlank
	@Field(label = "收件人城市")
	@View(groups = {VO_DETAIL, VO_ADMIN, VO_ADMIN_FULL})
	private String receiverCity;

	@NotBlank
	@Field(label = "收件人地区")
	@View(groups = {VO_DETAIL, VO_ADMIN, VO_ADMIN_FULL})
	private String receiverDistrict;

	@NotBlank
	@StringBinder
	@Field(label = "收件人详细地址")
	@View(groups = {VO_DETAIL, VO_ADMIN, VO_ADMIN_FULL})
	private String receiverAddress;

	@Field(label = "银行汇款截图")
	@View(groups = {VO_DETAIL, VO_ADMIN, VO_ADMIN_FULL})
	@Column(length = 1000)
	@StringBinder
	@CollectionView(name= "offlineImages", type = ArrayList.class, elementType = ImageVo.class, groups = {VO_DETAIL, VO_ADMIN, VO_ADMIN_FULL})
	private String offlineImage;

	@Field(label = "银行汇款备注")
	@View(groups = {VO_DETAIL, VO_ADMIN, VO_ADMIN_FULL})
	@StringBinder
	private String offlineMemo;

	@Field(label = "是否删除")
	@View(groups = {VO_ADMIN_FULL})
	@NotNull
	@Query(Predicate.EQ)
	private Boolean isDeleted;

	/* 追加冗余 */
	@NotNull
	@Field(label = "是否多子订单")
	private Boolean isMultiple;

	//@NotNull
	@Query(Predicate.EQ)
	@Field(label = "商品id")
	@View
	private Long productId;

	//@NotBlank
	@Field(label = "商品图")
	@View(name = "imageThumbnail", type = String.class, groups = {VO_ADMIN, VO_ADMIN_FULL, VO_DETAIL, VO_LIST})
	private String image;

	//@NotNull
	@DecimalMin("0.00")
	@Field(label = "原价/市场价")
	private BigDecimal marketPrice;

	//@NotNull
	@DecimalMin("0.00")
	@Field(label = "单价")
	@View(name = "price", type = BigDecimal.class, groups = {VO_ADMIN, VO_ADMIN_FULL})
	@View(name = "priceLabel", type = String.class, groups = {VO_ADMIN, VO_ADMIN_FULL})
	private BigDecimal price;

	//@NotNull
	@Min(1L)
	@Field(label = "数量")
	@View(name = "quantity", type = Long.class, groups = {VO_ADMIN, VO_ADMIN_FULL, VO_DETAIL})
	private Long quantity;

	@View
	@Field(label = "是否结算")
	@Query(Predicate.EQ)
	private Boolean isSettlement;

}
