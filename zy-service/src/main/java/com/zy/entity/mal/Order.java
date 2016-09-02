package com.zy.entity.mal;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import com.zy.entity.fnc.CurrencyType;

import io.gd.generator.annotation.Field;
import io.gd.generator.annotation.Type;
import io.gd.generator.annotation.query.Query;
import io.gd.generator.annotation.query.QueryModel;
import io.gd.generator.annotation.view.CollectionView;
import io.gd.generator.annotation.view.View;
import io.gd.generator.annotation.view.ViewObject;
import io.gd.generator.api.query.Predicate;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "mal_order")
@Getter
@Setter
@QueryModel
@ViewObject(groups = { "OrderListVo", "OrderDetailVo", "OrderAdminVo" },
	collectionViews = {
			@CollectionView(name = "orderItems", type = ArrayList.class, groups = {"OrderAdminVo"}, elementGroup = "OrderItemAdminVo"),
			@CollectionView(name = "orderItems", type = ArrayList.class, groups = {"OrderListVo", "OrderDetailVo"}, elementGroup = "OrderItemVo")
	}
)
@Type(label = "订单")
public class Order implements Serializable {

	@Type(label = "订单状态")
	public enum OrderStatus {
		待支付, 已支付, 已发货, 已完成, 已退款, 已取消
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
	private Long userId;
	
	@NotNull
	@Query({Predicate.EQ, Predicate.IN})
	@Field(label = "用户id")
	@View
	private Long sellerId;

	@NotBlank
	@Field(label = "标题")
	@View
	private String title;

	@NotNull
	@View(name = "createdTimeLabel", type = String.class)
	@Field(label = "下单时间")
	private Date createdTime;

	@Field(label = "过期时间")
	@View(name = "expiredTimeLabel", type = String.class)
	private Date expiredTime;

	@View(name = "paidTimeLabel", type = String.class, groups = { "OrderDetailVo", "OrderAdminVo" })
	@Field(label = "支付时间")
	private Date paidTime;

	@View(name = "refundedTimeLabel", type = String.class, groups = { "OrderDetailVo", "OrderAdminVo" })
	@Field(label = "退款时间")
	private Date refundedTime;

	@NotNull
	@Field(label = "货币类型")
	@View(groups = "OrderAdminVo")
	private CurrencyType currencyType;

	@NotNull
	@Query(Predicate.EQ)
	@Field(label = "订单状态")
	@View
	private OrderStatus orderStatus;

	@NotNull
	@DecimalMin("0.00")
	@Field(label = "邮费")
	@View(groups = {"OrderDetailVo", "OrderAdminVo"})
	private BigDecimal postFee;
	
	@NotNull
	@DecimalMin("0.00")
	@Field(label = "优惠金额")
	@View(groups = "OrderAdminVo")
	private BigDecimal discountFee;

	@NotNull
	@DecimalMin("0.00")
	@View(name = "amount", type = String.class, groups = { "OrderDetailVo", "OrderListVo"})
	@View(groups = { "OrderAdminVo" })
	@Field(label = "应付总金额", description = "应付总金额 = Σ订单子项金额 - 优惠金额")
	private BigDecimal amount;

	@DecimalMin("0.00")
	@View(type = String.class, groups = { "OrderDetailVo"})
	@View(groups = { "OrderAdminVo" })
	@Field(label = "退款金额")
	private BigDecimal refund;

	@View(type = String.class, groups = { "OrderDetailVo"})
	@View(groups = { "OrderAdminVo" })
	@Field(label = "退款备注")
	private String refundRemark;

	@View(groups = { "OrderDetailVo", "OrderAdminVo" })
	@Field(label = "备注")
	private String remark;

	@NotNull
	@Version
	@Field(label = "乐观锁")
	private Integer version;
	
	@Field(label = "是否已结算")
	@View(groups = {"OrderAdminVo"})
	@NotNull
	private Boolean isSettledUp;

}
