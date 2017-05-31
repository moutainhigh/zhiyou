package com.zy.vo;

import io.gd.generator.annotation.Field;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class ActivityTicketListVo implements Serializable {
	/* 原生 */
	@Field(label = "id")
	private Long id;
	@Field(label = "团队报名订单id")
	private Long teamApplyId;
	@Field(label = "二维码存储路径")
	private String codeImageUrl;
	@Field(label = "是否使用过")
	private Integer isUsed;

	/* 扩展 */
	@Field(label = "使用者姓名")
	private String usedUserName;
}