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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "fnc_account", uniqueConstraints = @UniqueConstraint(columnNames = {"userId", "currencyType"}))
@Getter
@Setter
@QueryModel
@Type(label = "资金账户")
@ViewObject(groups = {"AccountAdminVo"}, views = {
		@View(name = "money", type = BigDecimal.class, groups = "AccountAdminVo"),
		@View(name = "coin", type = BigDecimal.class, groups = "AccountAdminVo"),
		@View(name = "point", type = BigDecimal.class, groups = "AccountAdminVo"),
})
public class Account implements Serializable {

	@Id
	@Field(label = "id")
	private Long id;

	@NotNull
	@Column(unique = true)
	@Query({Predicate.EQ, Predicate.IN})
	@Field(label = "用户id")
	@View(groups = "AccountAdminVo")
	@AssociationView(name = "user", groups = "AccountAdminVo", associationGroup = "UserAdminSimpleVo")
	private Long userId;

	@NotNull
	@Field(label = "金额")
	private BigDecimal amount;

	@NotNull
	@Query(Predicate.EQ)
	@Field(label = "货币类型")
	private CurrencyType currencyType;

	@Version
	@NotNull
	@Field(label = "乐观锁")
	private Integer version;

}
