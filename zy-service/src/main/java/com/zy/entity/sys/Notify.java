package com.zy.entity.sys;

import io.gd.generator.annotation.Field;
import io.gd.generator.annotation.Type;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

import org.hibernate.validator.constraints.NotBlank;

@Entity
@Table(name = "sys_notify")
@Getter
@Setter
@Type(label = "消息队列")
public class Notify implements Serializable {

	@Id
	@Field(label = "id")
	private Long id;

	@NotBlank
	@Field(label = "主题")
	private String topic;

	@NotBlank
	@Lob
	@Field(label = "数据体")
	private String payload;

	@NotNull
	@Field(label = "是否发送")
	private Boolean isSent;

	@NotNull
	@Field(label = "创建时间")
	private Date createdTime;

	@Field(label = "发送时间")
	private Date sentTime;


}
