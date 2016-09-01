package com.gc.vo;

import com.gc.entity.sys.Message.MessageType;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
public class MessageAdminVo implements Serializable {
	/* 原生 */
	private Long id;
	private String title;
	private String content;
	private Boolean isRead;
	private Date createdTime;
	private Date readTime;
	private MessageType messageType;
	private String batchNumber;

	/* 扩展 */
	private UserAdminSimpleVo user;

}