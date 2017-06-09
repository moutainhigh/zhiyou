package com.zy.vo;

import com.zy.entity.act.ActivityTeamApply;
import io.gd.generator.annotation.Field;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class ActivityTicketAdminVo implements Serializable {
	/* 原生 */
	@Field(label = "id")
	private Long id;
	@Field(label = "是否使用过")
	private Integer isUsed;

	/* 扩展 */
	@Field(label = "使用者")
	private UserListVo usedUser;
	@Field(label = "团队报名订单")
	private ActivityTeamApplyAdminVo activityTeamApply;

}