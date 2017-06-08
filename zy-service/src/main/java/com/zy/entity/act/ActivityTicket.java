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
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Table(name = "act_activity_ticket")
@Getter
@Setter
@Type(label = "活动团队报名")
@QueryModel
@ViewObject(groups = {ActivityTicket.VO_ADMIN, ActivityTicket.VO_LIST})
public class ActivityTicket implements Serializable {

	public static final String VO_ADMIN = "ActivityTicketAdminVo";
	public static final String VO_LIST = "ActivityTicketListVo";


	@Id
	@Field(label = "id")
	@View
	private Long id;


	@NotNull
	@Field(label = "团队订单id")
	@Query({Predicate.IN, Predicate.EQ})
	@View(groups = VO_ADMIN)
	@AssociationView(name = "activityTeamApply", associationGroup =Activity.VO_LIST, groups = {VO_ADMIN})
	private Long teamApplyId;

	@Field(label = "用户id")
	@Query({Predicate.IN, Predicate.EQ})
	@View(groups = VO_ADMIN)
	@AssociationView(name = "usedUser", associationGroup = User.VO_LIST, groups = {VO_ADMIN, VO_LIST})
	private Long userId;


	@Field(label = "二维码存储路径")
	@NotNull
	@Query({Predicate.EQ})
	private String codeImageUrl;


	@Field(label = "是否被使用")
	@NotNull
	@Query({Predicate.EQ})
	private Integer isUsed;

}
