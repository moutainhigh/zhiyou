package com.gc.entity.fnc;

import static io.gd.generator.api.query.Predicate.EQ;
import static io.gd.generator.api.query.Predicate.GTE;
import static io.gd.generator.api.query.Predicate.IN;
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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

import org.hibernate.validator.constraints.NotBlank;

@Entity
@Table(name = "fnc_profit")
@Getter
@Setter
@QueryModel
@Type(label = "收益单")
@ViewObject(groups = "ProfitAdminVo")
public class Profit implements Serializable {

	@Id
	@Field(label = "id")
	@View
	private Long id;

	@NotNull
	@Query({EQ,IN})
	@Field(label = "用户id")
	@View
	@AssociationView(name = "user", groups = "ProfitAdminVo", associationGroup = "UserAdminSimpleVo")
	private Long userId;

	@Column(length = 60, unique = true)
	@NotBlank
	@Field(label = "收益单号")
	@View
	private String sn;

	@NotBlank
	@Field(label = "收益标题")
	@View
	private String title;

	@NotNull
	@Field(label = "币种")
	@View
	private CurrencyType currencyType;

	@NotNull
	@DecimalMin("0.01")
	@Field(label = "金额")
	@View
	private BigDecimal amount;

	@NotNull
	@Query({GTE,LT})
	@Field(label = "创建时间")
	@View
	private Date createdTime;

	@NotBlank
	@Query(EQ)
	@Field(label = "业务名")
	@View
	private String bizName;

	@Field(label = "备注")
	@View
	private String remark;

}
