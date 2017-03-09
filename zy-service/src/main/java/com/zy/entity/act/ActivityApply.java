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
import java.math.BigDecimal;
import java.util.Date;

import static com.zy.entity.act.ActivityApply.VO_ADMIN;

@Entity
@Table(name = "act_activity_apply", uniqueConstraints = @UniqueConstraint(columnNames = {"activityId", "userId"}))
@Getter
@Setter
@Type(label = "活动报名")
@QueryModel
@ViewObject(groups = VO_ADMIN)
public class ActivityApply implements Serializable {

	public static final String VO_ADMIN = "ActivityApplyAdminVo";

	@Type(label = "活动报名状态")
	public enum ActivityApplyStatus {
		已报名, 已支付
	}

	@Id
	@Field(label = "id")
	@View(groups = VO_ADMIN)
	private Long id;

	@NotNull
	@Query(Predicate.EQ)
	@Field(label = "活动报名状态")
	@View(groups = VO_ADMIN)
	private ActivityApplyStatus activityApplyStatus;

	@NotNull
	@Field(label = "用户id")
	@Query({Predicate.IN, Predicate.EQ})
	@View(groups = VO_ADMIN)
	@AssociationView(name = "user", associationGroup = User.VO_ADMIN_SIMPLE, groups = VO_ADMIN)
	private Long userId;

	@Field(label = "代付人id")
	@Query({Predicate.IN, Predicate.EQ})
	@View(groups = VO_ADMIN)
	@AssociationView(name = "payerUser", associationGroup = User.VO_ADMIN_SIMPLE, groups = VO_ADMIN)
	private Long payerUserId;

	@Field(label = "活动报名费")
	@NotNull
	@View(name = "amountLabel", type = String.class)
	private BigDecimal amount;

	@NotNull
	@Field(label = "活动id")
	@Query({Predicate.IN, Predicate.EQ})
	@View(groups = VO_ADMIN)
	private Long activityId;

	@NotNull
	@Field(label = "报名时间")
	@Query({Predicate.GTE, Predicate.LT})
	@View(groups = VO_ADMIN)
	@View(name = "appliedTimeLabel", type = String.class, groups = VO_ADMIN)
	private Date appliedTime;

	@NotNull
	@Field(label = "是否取消")
	@Query(Predicate.EQ)
	@View(groups = VO_ADMIN)
	private Boolean isCancelled;

	@Field(label = "邀请人id")
	@Query({Predicate.IN, Predicate.EQ})
	@View(groups = VO_ADMIN)
	@AssociationView(name = "inviter", associationGroup = User.VO_ADMIN_SIMPLE, groups = VO_ADMIN)
	private Long inviterId;


	@Field(label = "短信通知是否已发送")
	@Query({Predicate.EQ})
	@View(groups = VO_ADMIN)
	@NotNull
	private Boolean isSmsSent;

}
