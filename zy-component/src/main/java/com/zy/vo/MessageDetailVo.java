package com.gc.vo;

import com.gc.entity.sys.Message.MessageType;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class MessageDetailVo implements Serializable {
	/* 原生 */
	private Long id;
	private String title;
	private String content;
	private Boolean isRead;
	private MessageType messageType;

	/* 扩展 */
	private String createdTimeLabel;

}