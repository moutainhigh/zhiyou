package com.zy.vo;

import com.zy.entity.cms.Notice.NoticeType;
import io.gd.generator.annotation.Field;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class NoticeListVo implements Serializable {
	/* 原生 */
	@Field(label = "id")
	private Long id;
	@Field(label = "标题")
	private String title;
	@Field(label = "通知类型")
	private NoticeType noticeType;

	/* 扩展 */
	@Field(label = "创建时间")
	private String createdTimeLabel;
}