package com.zy.entity.act;

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
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "act_activity_apply", uniqueConstraints = @UniqueConstraint(columnNames = {"activityId", "userId"}))
@Getter
@Setter
@Type(label = "活动报名")
@QueryModel
@ViewObject(groups = "ActivityApplyAdminVo")
public class ActivityApply implements Serializable {

	@Id
	@Field(label = "id")
	@View(groups = "ActivityApplyAdminVo")
	private Long id;

	@NotNull
	@Field(label = "用户id")
	@Query({Predicate.IN, Predicate.EQ})
	@View(groups = "ActivityApplyAdminVo")
	@AssociationView(name = "user", associationGroup = "UserAdminSimpleVo", groups = "ActivityApplyAdminVo")
	private Long userId;

	@NotNull
	@Field(label = "活动id")
	@Query({Predicate.IN, Predicate.EQ})
	@View(groups = "ActivityApplyAdminVo")
	private Long activityId;

	@NotNull
	@Field(label = "报名时间")
	@Query({Predicate.GTE, Predicate.LT})
	@View(groups = "ActivityApplyAdminVo")
	@View(name = "appliedTimeLabel", type = String.class, groups = "ActivityApplyAdminVo")
	private Date appliedTime;

	@NotNull
	@Field(label = "是否取消")
	@Query(Predicate.EQ)
	@View(groups = "ActivityApplyAdminVo")
	private Boolean isCancelled;

	@NotNull
	@Field(label = "邀请人id")
	@Query({Predicate.IN, Predicate.EQ})
	@View(groups = "ActivityApplyAdminVo")
	@AssociationView(name = "inviter", associationGroup = "UserAdminSimpleVo", groups = "ActivityApplyAdminVo")
	private Long inviterId;

}
