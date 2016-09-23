package com.zy.entity.act;

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
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

import static com.zy.entity.act.ActivitySignIn.VO_ADMIN;

@Entity
@Table(name = "act_activity_sign_in", uniqueConstraints = @UniqueConstraint(columnNames = {"activityId", "userId"}))
@Getter
@Setter
@Type(label = "活动签到")
@QueryModel
@ViewObject(groups = VO_ADMIN)
public class ActivitySignIn implements Serializable {

	public static final String VO_ADMIN = "ActivitySignInAdminVo";

	@Id
	@Field(label = "id")
	@View(groups = VO_ADMIN)
	private Long id;

	@NotNull
	@Field(label = "用户id")
	@Query({Predicate.IN, Predicate.EQ})
	@View(groups = VO_ADMIN)
	@AssociationView(name = "user", associationGroup = User.VO_ADMIN_SIMPLE, groups = VO_ADMIN)
	private Long userId;

	@NotNull
	@Field(label = "活动id")
	@Query({Predicate.IN, Predicate.EQ})
	@View(groups = VO_ADMIN)
	private Long activityId;

	@NotNull
	@Field(label = "签到时间")
	@Query({Predicate.GTE, Predicate.LT})
	@View(groups = VO_ADMIN)
	@View(name = "signedInTimeLabel", type = String.class, groups = VO_ADMIN)
	private Date signedInTime;

}
