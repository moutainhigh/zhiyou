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

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import static com.zy.entity.fnc.AccountLog.VO_ADMIN;
import static com.zy.entity.fnc.AccountLog.VO_SIMPLE;
import static io.gd.generator.api.query.Predicate.*;

@Entity
@Table(name = "fnc_account_log")
@Getter
@Setter
@QueryModel
@ViewObject(groups = {VO_ADMIN, VO_SIMPLE})
@Type(label = "资金流水")
public class AccountLog implements Serializable {

	public static final String VO_ADMIN = "AccountLogAdminVo";
	public static final String VO_SIMPLE = "AccountLogSimpleVo";

	public enum InOut {
		收入, 支出
	}

	public enum AccountLogType {
		充值单, 支付单, 提现单, 收益单, 转账单
	}

	@Id
	@Field(label = "id主键")
	@View
	private Long id;

	@NotNull
	@Query(EQ)
	@Field(label = "资金走向")
	@View
	private InOut inOut;

	@NotNull
	@Query(LK)
	@Field(label = "对应单据标题")
	@View
	private String title;

	@NotNull
	@Query({EQ, IN})
	@Field(label = "所属用户")
	@View
	@AssociationView(name = "user", groups = VO_ADMIN, associationGroup = User.VO_ADMIN_SIMPLE)
	private Long userId;

	@NotNull
	@Query({EQ, IN})
	@Field(label = "对应用户id")
	@View
	@AssociationView(name = "refUser", groups = VO_ADMIN, associationGroup = User.VO_ADMIN_SIMPLE)
	private Long refUserId;

	@NotNull
	@Query({EQ, IN})
	@Field(label = "资金账户日志类型")
	@View(groups = {VO_ADMIN})
	private AccountLogType accountLogType;

	@NotNull
	@Field(label = "对应单据id")
	@View(groups = {VO_ADMIN})
	private Long refId;

	@NotNull
	@Query(EQ)
	@Field(label = "对应单据sn")
	@View(groups = {VO_ADMIN})
	private String refSn;

	@NotNull
	@Query(EQ)
	@Field(label = "币种")
	@View(groups = {VO_ADMIN})
	private CurrencyType currencyType;

	@NotNull
	@Query({GTE, LT})
	@Field(label = " 交易产生时间")
	@View(groups = {VO_ADMIN})
	@View(name = "transTimeLabel", type = String.class, groups = {VO_SIMPLE})
	private Date transTime;

	//@NotNull
	@Field(label = "交易前余额")
	@View(groups = {VO_ADMIN})
	@View(name = "beforeAmountLabel", type = String.class, groups = VO_ADMIN)
	private BigDecimal beforeAmount;

	@NotNull
	@DecimalMin("0.01")
	@Field(label = "交易金额")
	@View
	@View(name = "transAmountLabel", type = String.class, groups = VO_ADMIN)
	private BigDecimal transAmount;

	//@NotNull
	@Field(label = "交易完成后金额")
	@View
	@View(name = "afterAmountLabel", type = String.class, groups = VO_ADMIN)
	private BigDecimal afterAmount;

	@NotNull
	@Field(label = "是否已确认")
	@Query(Predicate.EQ)
	private Boolean isAcknowledged;

}
