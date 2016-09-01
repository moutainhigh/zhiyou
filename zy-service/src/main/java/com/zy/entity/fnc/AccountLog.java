package com.gc.entity.fnc;

import static io.gd.generator.api.query.Predicate.EQ;
import static io.gd.generator.api.query.Predicate.GTE;
import static io.gd.generator.api.query.Predicate.IN;
import static io.gd.generator.api.query.Predicate.LK;
import static io.gd.generator.api.query.Predicate.LT;
import io.gd.generator.annotation.Field;
import io.gd.generator.annotation.Type;
import io.gd.generator.annotation.query.Query;
import io.gd.generator.annotation.query.QueryModel;
import io.gd.generator.annotation.view.AssociationView;
import io.gd.generator.annotation.view.View;
import io.gd.generator.annotation.view.ViewObject;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "fnc_account_log")
@Getter
@Setter
@QueryModel
@ViewObject(groups = {"AccountLogSimpleVo", "AccountLogAdminVo"})
@Type(label = "资金流水")
public class AccountLog implements Serializable {

	public enum InOut {
		收入, 支出
	}

	public enum AccountLogType {
		充值单, 支付单, 提现单, 收益单
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
	@AssociationView(name = "user", groups = "AccountLogAdminVo", associationGroup = "UserAdminSimpleVo")
	private Long userId;

	@NotNull
	@Query(EQ)
	@Field(label = "资金账户日志类型")
	@View(groups = {"AccountLogAdminVo"})
	private AccountLogType accountLogType;

	@NotNull
	@Field(label = "对应单据id")
	@View(groups = {"AccountLogAdminVo"})
	private Long refId;

	@NotNull
	@Query(EQ)
	@Field(label = "对应单据sn")
	@View(groups = {"AccountLogAdminVo"})
	private String refSn;

	@NotNull
	@Query(EQ)
	@Field(label = "币种")
	@View(groups = {"AccountLogAdminVo"})
	private CurrencyType currencyType;

	@NotNull
	@Query({GTE, LT})
	@Field(label = " 交易产生时间")
	@View(groups = {"AccountLogAdminVo"})
	@View(name = "transTimeLabel", type = String.class, groups = {"AccountLogSimpleVo"})
	private Date transTime;

	@NotNull
	@Field(label = "交易前余额")
	@View(groups = {"AccountLogAdminVo"})
	private BigDecimal beforeAmount;

	@NotNull
	@DecimalMin("0.01")
	@Field(label = "交易金额")
	@View
	private BigDecimal transAmount;

	@NotNull
	@Field(label = "交易完成后金额")
	@View
	private BigDecimal afterAmount;

}
