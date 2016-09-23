package com.zy.entity.fnc;

import com.zy.entity.usr.User;
import io.gd.generator.annotation.Field;
import io.gd.generator.annotation.Type;
import io.gd.generator.annotation.query.Query;
import io.gd.generator.annotation.query.QueryModel;
import io.gd.generator.annotation.view.AssociationView;
import io.gd.generator.annotation.view.View;
import io.gd.generator.annotation.view.ViewObject;
import io.gd.generator.api.query.Predicate;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import static com.zy.entity.fnc.Payment.VO_ADMIN;

@Entity
@Table(name = "fnc_payment")
@Getter
@Setter
@QueryModel
@Type(label = "支付订单")
@ViewObject(groups = VO_ADMIN)
public class Payment implements Serializable {

	public static final String VO_ADMIN = "PaymentAdminVo";

	public enum PaymentType {
		订单支付
	}

	public enum PaymentStatus {
		待支付, 已支付, 已退款, 已取消, 待确认
	}

	@Id
	@Field(label = "id")
	@View
	private Long id;

	@NotBlank
	@Column(length = 60, unique = true)
	@Field(label = "支付订单号")
	@View
	@Length(max = 60)
	@Query({Predicate.EQ})
	private String sn;

	@NotNull
	@Query(Predicate.EQ)
	@Field(label = "用户id")
	@View
	@AssociationView(name = "user", groups = VO_ADMIN, associationGroup = User.VO_ADMIN_SIMPLE)
	private Long userId;

	@NotBlank
	@Field(label = "标题")
	@View
	@Length(max = 255)
	private String title;

	@NotNull
	@Field(label = "支付方式", description = "目前固定为余额")
	@View
	@Query({Predicate.EQ, Predicate.IN})
	private PayType payType;

	@Query(Predicate.EQ)
	@Field(label = "支付单类型")
	@View
	@NotNull
	private PaymentType paymentType;

	@Field(label = "关联业务id", description = "可以不填写")
	@View
	@Query(Predicate.EQ)
	private Long refId;

	@NotNull
	@Field(label = "下单时间")
	@View
	@View(name = "createdTimeLabel", type = String.class)
	@Query({Predicate.LT, Predicate.GTE})
	private Date createdTime;

	@Field(label = "过期时间")
	@View
	@Query(Predicate.LT)
	@View(name = "expiredTimeLabel", type = String.class)
	@NotNull
	private Date expiredTime;

	@Field(label = "支付时间")
	@View
	@View(name = "paidTimeLabel", type = String.class)
	@Query({Predicate.LT, Predicate.GTE})
	private Date paidTime;

	@NotNull
	@Field(label = "支付状态")
	@Query(Predicate.EQ)
	@View
	@View(name = "paymentStatusStyle", type = String.class, groups = {VO_ADMIN})
	private PaymentStatus paymentStatus;

	@NotNull
	@Field(label = " 货币1")
	@View
	private CurrencyType currencyType1;

	@NotNull
	@DecimalMin("0.01")
	@Field(label = "货币1支付金额")
	@View
	@View(name = "amount1Label", type = String.class, groups = VO_ADMIN)
	private BigDecimal amount1;

	@Field(label = "货币2")
	@View
	private CurrencyType currencyType2;

	@DecimalMin("0.01")
	@Field(label = " 货币2支付金额")
	@View
	@View(name = "amount2Label", type = String.class, groups = VO_ADMIN)
	private BigDecimal amount2;

	@DecimalMin("0.00")
	@Field(label = "货币1退款金额")
	@View
	private BigDecimal refund1;

	@DecimalMin("0.00")
	@Field(label = " 货币2退款金额")
	@View
	private BigDecimal refund2;

	@Field(label = "退款时间")
	@View
	@View(name = "refundedTimeLabel", type = String.class)
	private Date refundedTime;

	@Field(label = "退款备注")
	@View
	private String refundRemark;

	@Field(label = "取消备注")
	@View
	private String cancelRemark;

	@Field(label = "备注")
	@View
	@Column(length = 1000)
	private String remark;

	@Field(label = "外部sn")
	@View
	private String outerSn;

	@Field(label = "银行汇款截图")
	@View
	@View(name = "offlineImageThumbnail")
	@Column(length = 1000)
	private String offlineImage;
	
	@Field(label = "银行汇款备注")
	@View
	private String offlineMemo;
	
	@NotNull
	@Version
	@Field(label = "乐观锁")
	private Integer version;

	@Field(label = "操作者id")
	private Long operatorId;

}
