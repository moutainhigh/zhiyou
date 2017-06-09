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
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "act_activity_team_apply")
@Getter
@Setter
@Type(label = "活动团队报名")
@QueryModel
@ViewObject(groups = {ActivityTeamApply.VO_ADMIN, ActivityTeamApply.VO_LIST})
public class ActivityTeamApply implements Serializable {

	public static final String VO_ADMIN = "ActivityTeamApplyAdminVo";
	public static final String VO_LIST = "ActivityTeamApplyListVo";

	@Type(label = "活动团队报名单支付状态")
	public enum PaidStatus {
		未支付, 已支付
	}

	@Id
	@Field(label = "id")
	@View
	private Long id;

	@NotNull
	@Query(Predicate.EQ)
	@Field(label = "活动团队报名单支付状态")
	@View(groups = {VO_ADMIN, VO_LIST})
	private PaidStatus paidStatus;

	@NotNull
	@Field(label = "活动id")
	@Query({Predicate.IN, Predicate.EQ})
	@View
	@AssociationView(name = "activity", associationGroup =Activity.VO_LIST, groups = {VO_ADMIN, VO_LIST})
	private Long activityId;

	@Field(label = "购买人id")
	@Query({Predicate.IN, Predicate.EQ})
	@View(groups = VO_ADMIN)
	@AssociationView(name = "buyerUser", associationGroup = User.VO_ADMIN_SIMPLE, groups = VO_ADMIN)
	private Long buyerId;


	@NotNull
	@Min(0)
	@Field(label = "数量")
	@View
	private Long count;

	@Field(label = "总金额")
	@NotNull
	@View(name = "amountLabel", type = String.class)
	private BigDecimal amount;



	@NotNull
	@Field(label = "创建时间")
	@Query({Predicate.GTE, Predicate.LT})
	@View(groups = VO_ADMIN)
	@View(name = "createTimeLabel", type = String.class, groups = {VO_ADMIN, VO_LIST})
	private Date createTime;

	@NotNull
	@Field(label = "支付时间")
	@Query({Predicate.GTE, Predicate.LT})
	@View(groups = VO_ADMIN)
	@View(name = "paidTimeLabel", type = String.class, groups = {VO_ADMIN, VO_LIST})
	private Date paidTime;


}
