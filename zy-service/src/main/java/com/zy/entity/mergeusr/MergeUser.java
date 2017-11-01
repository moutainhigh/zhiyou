package com.zy.entity.mergeusr;

import com.zy.entity.usr.User;
import com.zy.entity.usr.User.UserRank;
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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

import static com.zy.entity.mergeusr.MergeUser.VO_ADMIN;
import static io.gd.generator.api.query.Predicate.EQ;
import static io.gd.generator.api.query.Predicate.IN;

@Entity
@Table(name = "usr_merge_user")
@Getter
@Setter
@QueryModel
@Type(label = "用户")
@ViewObject(groups = { VO_ADMIN})
public class MergeUser implements Serializable {

	public static final String VO_ADMIN = "UserAdminVo";

	@Id
	@Query(Predicate.IN)
	@Field(label = "id")
	@View(groups = { VO_ADMIN})
	private Long id;

	@NotNull
	@Query({EQ,IN})
	@Column(unique = true)
	@Field(label = "用户id")
	@View
	@AssociationView(name = "user", groups = VO_ADMIN, associationGroup = User.VO_ADMIN_FULL)
	private Long userId;

	@NotNull
	@Query({EQ,IN})
	@Column(unique = true)
	@Field(label = "原始上级id")
	@View
	@AssociationView(name = "inviter", groups = VO_ADMIN, associationGroup = User.VO_ADMIN_SIMPLE)
	private Long inviterId;

	@NotNull
	@Query({EQ,IN})
	@Column(unique = true)
	@Field(label = "上级id")
	@View
	@AssociationView(name = "parent", groups = VO_ADMIN, associationGroup = User.VO_ADMIN_SIMPLE)
	private Long parentId;


	@NotNull
	@Query(Predicate.EQ)
	@Field(label = "用户等级")
	@View(groups = {VO_ADMIN})
	@View(name = "userRankLabel", type = String.class, groups = {VO_ADMIN})
	private UserRank userRank;


	@Query(Predicate.EQ)
	@Field(label = "产品类型")
	@View(groups = {VO_ADMIN})
	private Integer productType;


}
