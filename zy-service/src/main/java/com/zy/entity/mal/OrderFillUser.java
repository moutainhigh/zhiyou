package com.zy.entity.mal;

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
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "mal_order_fill_user")
@Getter
@Setter
@QueryModel
@ViewObject(groups = {OrderFillUser.VO_ADMIN})
@Type(label = "用户补订单")
public class OrderFillUser implements Serializable {

	public static final String VO_ADMIN = "OrderFillUserAdminVo";

	@Id
	@Field(label = "id")
	@View
	private Long id;

	@NotNull
	@Query({Predicate.EQ, Predicate.IN})
	@Field(label = "用户id")
	@View
	@AssociationView(name = "user", groups = {VO_ADMIN}, associationGroup = User.VO_ADMIN_SIMPLE)
	private Long userId;

	@NotBlank
	@View(groups = {VO_ADMIN})
	@Field(label = "备注")
	private String remark;

	@View(groups = {VO_ADMIN})
	private Long createId;

	@View(groups = {VO_ADMIN})
	private Date createTime;

	@View(groups = {VO_ADMIN})
	private Long updateId;

	@View(groups = {VO_ADMIN})
	private Date updateTime;

	@View(groups = {VO_ADMIN})
	private Integer status;
}
