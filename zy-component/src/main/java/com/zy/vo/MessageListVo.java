package com.zy.vo;

import io.gd.generator.annotation.Field;
import com.zy.entity.sys.Message.MessageType;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class MessageListVo implements Serializable {
	/* 原生 */
	@Field(label = "id")
	private Long id;
	@Field(label = "标题")
	private String title;
	@Field(label = "是否已读")
	private Boolean isRead;
	@Field(label = "消息类型")
	private MessageType messageType;

	/* 扩展 */
	@Field(label = "创建时间",order = 999)
	private String createdTimeLabel;

}