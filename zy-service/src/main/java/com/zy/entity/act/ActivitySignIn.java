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
@Table(name = "act_activity_sign_in", uniqueConstraints = @UniqueConstraint(columnNames = {"activityId", "userId"}))
@Getter
@Setter
@Type(label = "活动签到")
@QueryModel
@ViewObject(groups = "ActivitySignInAdminVo")
public class ActivitySignIn implements Serializable {

	@Id
	@Field(label = "id")
	@View(groups = "ActivitySignInAdminVo")
	private Long id;

	@NotNull
	@Field(label = "用户id")
	@Query({Predicate.IN, Predicate.EQ})
	@View(groups = "ActivitySignInAdminVo")
	@AssociationView(name = "user", associationGroup = "UserAdminSimpleVo", groups = "ActivitySignInAdminVo")
	private Long userId;

	@NotNull
	@Field(label = "活动id")
	@Query({Predicate.IN, Predicate.EQ})
	@View(groups = "ActivitySignInAdminVo")
	private Long activityId;

	@NotNull
	@Field(label = "签到时间")
	@Query({Predicate.GTE, Predicate.LT})
	@View(groups = "ActivitySignInAdminVo")
	@View(name = "signedInTimeLabel", type = String.class, groups = "ActivitySignInAdminVo")
	private Date signedInTime;

}
