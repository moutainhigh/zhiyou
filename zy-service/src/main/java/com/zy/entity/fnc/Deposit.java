package com.zy.entity.fnc;

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

import com.zy.entity.usr.User;
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

@Entity
@Table(name = "fnc_deposit")
@Getter
@Setter
@QueryModel
@Type(label = "充值单")
@ViewObject(groups = {Deposit.VO_ADMIN, Deposit.VO_LIST, Deposit.VO_EXPORT})
public class Deposit implements Serializable {

	public static final String VO_ADMIN = "DepositAdminVo";
	public static final String VO_LIST = "DepositListVo";
	public static final String VO_EXPORT = "DepositExportVo";

	public enum DepositStatus {
		待充值, 待确认, 充值成功, 已取消
	}

	@Id
	@Field(label = "id")
	@View(groups = {VO_LIST, VO_ADMIN})
	private Long id;

	@NotNull
	@Query({Predicate.EQ, Predicate.IN})
	@Field(label = "用户id")
	@View(groups = {VO_LIST, VO_ADMIN})
	@AssociationView(name = "user", groups = VO_ADMIN, associationGroup = User.VO_ADMIN_SIMPLE)
	@View(groups = VO_EXPORT, type = String.class,  name = "userNickname", field = @Field(label = "昵称", order = 5))
	@View(groups = VO_EXPORT, type = String.class,  name = "userPhone", field = @Field(label = "手机", order = 10))
	private Long userId;

	@NotBlank
	@Field(label = "标题")
	@View(groups = {VO_LIST, VO_ADMIN})
	@View(groups = VO_EXPORT, field = @Field(label = "标题", order = 15))
	private String title;

	@Column(length = 60, unique = true)
	@NotBlank
	@Field(label = "充值单号")
	@View(groups = {VO_LIST, VO_ADMIN})
	@View(groups = VO_EXPORT, field = @Field(label = "充值单号", order = 20))
	@Query(Predicate.EQ)
	private String sn;

	@NotNull
	@Field(label = "充值货币1")
	@View(groups = {VO_LIST, VO_ADMIN})
	@View(groups = VO_EXPORT, field = @Field(label = "充值货币1", order = 25))
	private CurrencyType currencyType1;

	@NotNull
	@DecimalMin("0.01")
	@Field(label = "充值货币1金额")
	@View(groups = {VO_LIST, VO_ADMIN})
	@View(name = "amount1Label", type = String.class, groups = {VO_LIST, VO_ADMIN})
	@View(name = "amount1Label", type = String.class, groups = VO_EXPORT, field = @Field(label = "充值货币1金额", order = 30))
	private BigDecimal amount1;

	@Field(label = "充值货币2")
	@View(groups = {VO_LIST, VO_ADMIN})
	private CurrencyType currencyType2;

	@DecimalMin("0.01")
	@Field(label = "充值货币2金额")
	@View(groups = {VO_LIST, VO_ADMIN})
	@View(name = "amount2Label", type = String.class, groups = {VO_LIST, VO_ADMIN})
	private BigDecimal amount2;

	@NotNull
	@DecimalMin("0.01")
	@Field(label = "充值总金额")
	@View(groups = {VO_LIST, VO_ADMIN})
	@View(name = "totalAmountLabel", type = String.class, groups = {VO_LIST, VO_ADMIN})
	private BigDecimal totalAmount;

	@NotNull
	@Field(label = "外部单据是否已经创建")
	@View(groups = {VO_LIST, VO_ADMIN})
	private Boolean isOuterCreated;

	@Field(label = "外部sn")
	@View(groups = {VO_LIST, VO_ADMIN})
	private String outerSn;

	@Field(label = "二维码")
	@View(groups = {VO_LIST, VO_ADMIN})
	private String qrCodeUrl;

	@Field(label = "微信openid", description = "微信支付专用")
	@View(groups = {VO_LIST, VO_ADMIN})
	private String weixinOpenId;

	@NotNull
	@Query(Predicate.EQ)
	@Field(label = "支付方式", description = "不能为余额支付")
	@View(groups = {VO_LIST, VO_ADMIN})
	@View(groups = VO_EXPORT, field = @Field(label = "支付方式", order = 40))
	private PayType payType;

	@Query({Predicate.GTE, Predicate.LT})
	@Field(label = "支付成功时间")
	@View(groups = {VO_LIST, VO_ADMIN})
	@View(name = "paidTimeLabel", type = String.class, groups = {VO_LIST, VO_ADMIN})
	@View(name = "paidTimeLabel", type = String.class, groups = VO_EXPORT, field = @Field(label = "支付成功时间", order = 45))
	private Date paidTime;

	@NotNull
	@Field(label = "创建时间")
	@View(groups = {VO_LIST, VO_ADMIN})
	@View(name = "createdTimeLabel", type = String.class, groups = {VO_LIST, VO_ADMIN})
	@View(name = "createdTimeLabel", type = String.class, groups = VO_EXPORT, field = @Field(label = "创建时间", order = 50))
	@Query({Predicate.LT, Predicate.GTE})
	private Date createdTime;

	@Field(label = "过期时间")
	@View(groups = {VO_LIST, VO_ADMIN})
	@Query(Predicate.LT)
	@NotNull
	@View(name = "expiredTimeLabel", type = String.class, groups = VO_EXPORT, field = @Field(label = "过期时间", order = 55))
	private Date expiredTime;

	@NotNull
	@Query({Predicate.EQ, Predicate.IN})
	@Field(label = "充值单状态")
	@View(groups = {VO_LIST, VO_ADMIN})
	@View(name = "depositStatusStyle", type = String.class, groups = {VO_ADMIN})
	@View(groups = VO_EXPORT, field = @Field(label = "充值单状态", order = 60))
	private DepositStatus depositStatus;

	@View(groups = VO_ADMIN)
	@Field(label = "备注")
	@Column(length = 1000)
	@View(groups = VO_EXPORT, field = @Field(label = "备注", order = 65))
	private String remark;

	@Field(label = "银行汇款截图")
	@View(groups = {VO_LIST, VO_ADMIN})
	@Column(length = 1000)
	@CollectionView(name= "offlineImages", type = ArrayList.class, elementType = ImageVo.class)
	@View(groups = VO_EXPORT, field = @Field(label = "银行汇款截图", order = 70))
	private String offlineImage;
	
	@Field(label = "银行汇款备注")
	@View(groups = {VO_LIST, VO_ADMIN})
	@View(groups = VO_EXPORT, field = @Field(label = "银行汇款备注", order = 75))
	private String offlineMemo;
	
	@NotNull
	@Version
	@Field(label = "乐观锁")
	private Integer version;

	@Field(label = "操作者id")
	private Long operatorId;

}
