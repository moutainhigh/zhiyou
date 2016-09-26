package com.zy.entity.mal;

import com.zy.common.extend.StringBinder;
import com.zy.entity.fnc.CurrencyType;
import com.zy.entity.fnc.Payment;
import com.zy.entity.fnc.Profit;
import com.zy.entity.fnc.Transfer;
import com.zy.entity.usr.User;
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
				@View(name = "imageThumbnail", type = String.class, groups = {VO_ADMIN, VO_ADMIN_FULL}),
				@View(name = "price", type = BigDecimal.class, groups = {VO_ADMIN, VO_ADMIN_FULL}),
				@View(name = "priceLabel", type = String.class, groups = {VO_ADMIN, VO_ADMIN_FULL}),
				@View(name = "quantity", type = Long.class, groups = {VO_ADMIN, VO_ADMIN_FULL})
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
		待支付, 已支付, 已发货, 已完成, 已退款, 已取消, 待确认
	}

	@Type(label = "物流费支付方式")
	public enum LogisticsFeePayType {
		无, 买家付, 卖家付
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
	@AssociationView(name = "user", groups = {VO_ADMIN, VO_ADMIN_FULL}, associationGroup = User.VO_ADMIN_SIMPLE)
	private Long userId;

	@NotNull
	@Query({Predicate.EQ, Predicate.IN})
	@Field(label = "卖家id")
	@View
	@AssociationView(name = "seller", groups = {VO_ADMIN, VO_ADMIN_FULL}, associationGroup = User.VO_ADMIN_SIMPLE)
	private Long sellerId;

	@NotBlank
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
	@Field(label = "货币类型")
	@View(groups = VO_ADMIN)
	private CurrencyType currencyType;

	@NotNull
	@Query(Predicate.EQ)
	@Field(label = "订单状态")
	@View
	@View(name = "orderStatusStyle", type = String.class, groups = {VO_ADMIN, VO_ADMIN_FULL})
	private OrderStatus orderStatus;

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
	private String buyerMemo;

	@View(groups = {VO_DETAIL, VO_ADMIN, VO_ADMIN_FULL})
	@Field(label = "卖家留言")
	private String sellerMemo;

	@View(groups = {VO_DETAIL, VO_ADMIN, VO_ADMIN_FULL})
	@Field(label = "备注")
	private String remark;

	@NotNull
	@Version
	@Field(label = "乐观锁")
	private Integer version;

	@Field(label = "是否已结算")
	@View(groups = {VO_DETAIL, VO_ADMIN, VO_ADMIN_FULL})
	@NotNull
	@Query(Predicate.EQ)
	private Boolean isSettledUp;

	@Field(label = "是否物流发货")
	@View(name = "useLogisticsLabel", type = String.class, groups = {VO_ADMIN, VO_ADMIN_FULL})
	@View(groups = {VO_DETAIL, VO_ADMIN})
	private Boolean useLogistics;

	@Field(label = "物流费支付类型")
	@View(groups = {VO_ADMIN, VO_ADMIN_FULL})
	private LogisticsFeePayType logisticsFeePayType;

	@View(groups = {VO_DETAIL, VO_ADMIN, VO_ADMIN_FULL})
	@NotNull
	@Field(label = "是否平台发货")
	@Query(Predicate.EQ)
	private Boolean isPlatformDeliver;

	@View(name = "deliveredTimeLabel", type = String.class)
	@Field(label = "发货时间")
	@View(groups = {VO_DETAIL, VO_ADMIN, VO_ADMIN_FULL})
	private Date deliveredTime;

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

	@NotNull
	@Field(label = "收件人区域")
	@View(groups = {VO_DETAIL, VO_ADMIN, VO_ADMIN_FULL})
	private Long receiverAreaId;

	@NotBlank
	@StringBinder
	@Field(label = "收件人姓名")
	@View(groups = {VO_DETAIL, VO_ADMIN, VO_ADMIN_FULL})
	private String receiverRealname;

	@NotBlank
	@StringBinder
	@Field(label = "收件人电话")
	@View(groups = {VO_DETAIL, VO_ADMIN, VO_ADMIN_FULL})
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
	@CollectionView(name= "offlineImages", type = ArrayList.class, elementType = String.class, groups = {VO_DETAIL, VO_ADMIN, VO_ADMIN_FULL})
	@CollectionView(name= "offlineImageThumbnails", type = ArrayList.class, elementType = String.class, groups = {VO_DETAIL, VO_ADMIN, VO_ADMIN_FULL})
	private String offlineImage;

	@Field(label = "银行汇款备注")
	@View(groups = {VO_DETAIL, VO_ADMIN, VO_ADMIN_FULL})
	@StringBinder
	private String offlineMemo;

}
