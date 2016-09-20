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

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

import static com.zy.entity.fnc.Account.VO_ADMIN;

@Entity
@Table(name = "fnc_account", uniqueConstraints = @UniqueConstraint(columnNames = {"userId", "currencyType"}))
@Getter
@Setter
@QueryModel
@Type(label = "资金账户")
@ViewObject(groups = {VO_ADMIN}, views = {
		@View(name = "money", type = BigDecimal.class, groups = VO_ADMIN),
		@View(name = "coin", type = BigDecimal.class, groups = VO_ADMIN),
		@View(name = "point", type = BigDecimal.class, groups = VO_ADMIN),
		@View(name = "moneyLabel", type = String.class, groups = VO_ADMIN),
		@View(name = "coinLabel", type = String.class, groups = VO_ADMIN),
		@View(name = "pointLabel", type = String.class, groups = VO_ADMIN)
})
public class Account implements Serializable {

	public static final String VO_ADMIN = "AccountAdminVo";

	@Id
	@Field(label = "id")
	private Long id;

	@NotNull
	@Column(unique = true)
	@Query({Predicate.EQ, Predicate.IN})
	@Field(label = "用户id")
	@View(groups = VO_ADMIN)
	@AssociationView(name = "user", groups = VO_ADMIN, associationGroup = User.VO_ADMIN_SIMPLE)
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
