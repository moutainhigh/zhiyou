package com.gc.entity.fnc;

import io.gd.generator.annotation.Field;
import io.gd.generator.annotation.Type;
import io.gd.generator.annotation.query.Query;
import io.gd.generator.annotation.query.QueryModel;
import io.gd.generator.annotation.view.AssociationView;
import io.gd.generator.annotation.view.View;
import io.gd.generator.annotation.view.ViewObject;
import io.gd.generator.api.query.Predicate;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

import org.hibernate.validator.constraints.NotBlank;

@Entity
@Table(name = "fnc_payment", uniqueConstraints = @UniqueConstraint(columnNames = {"bizName", "bizSn"}))
@Getter
@Setter
@QueryModel
@Type(label = "支付订单")
@ViewObject(groups = "PaymentAdminVo")
public class Payment implements Serializable {

	public enum PaymentStatus {
		待支付, 已支付, 已退款, 已取消
	}

	@Id
	@Field(label = "id")
	@View
	private Long id;

	@NotBlank
	@Column(length = 60, unique = true)
	@Field(label = "支付订单号")
	@View
	private String sn;

	@NotNull
	@Query(Predicate.EQ)
	@Field(label = "用户id")
	@View
	@AssociationView(name = "user", groups = "PaymentAdminVo", associationGroup = "UserAdminSimpleVo")
	private Long userId;

	@NotBlank
	@Field(label = "标题")
	@View
	private String title;

	@NotNull
	@Field(label = "支付方式", description = "目前固定为余额")
	@View
	private PayType payType;

	@NotBlank
	@Column(length = 60)
	@Query(Predicate.EQ)
	@Field(label = "业务类型")
	@View
	private String bizName;

	@NotBlank
	@Column(length = 60)
	@Field(label = "业务sn")
	@View
	private String bizSn;

	@NotNull
	@Field(label = "下单时间")
	@View
	private Date createdTime;

	@Field(label = "过期时间")
	@View
	private Date expiredTime;

	@Field(label = "支付时间")
	@View
	private Date paidTime;

	@NotNull
	@Field(label = "支付状态")
	@View
	private PaymentStatus paymentStatus;

	@NotNull
	@Field(label = " 货币1")
	@View
	private CurrencyType currencyType1;

	@NotNull
	@DecimalMin("0.01")
	@Field(label = "货币1支付金额")
	@View
	private BigDecimal amount1;

	@Field(label = "货币2")
	@View
	private CurrencyType currencyType2;

	@DecimalMin("0.01")
	@Field(label = " 货币2支付金额")
	@View
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
	private Date refundedTime;

	@Field(label = "退款备注")
	@View
	private String refundRemark;

	@Field(label = "取消备注")
	@View
	private String cancelRemark;

	@Field(label = "备注")
	@View
	private String remark;

	@NotNull
	@Version
	@Field(label = "乐观锁")
	private Integer version;

}
