package com.zy.entity.sys;

import io.gd.generator.annotation.Field;
import io.gd.generator.annotation.Type;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

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
	@Column(length = 60, unique = true)
	@Field(label = "token")
	private String token;

	@NotNull
	@Field(label = "创建时间")
	private Date createdTime;

	@Field(label = "业务id")
	@NotNull
	private Long refId;

	@Field(label = "version")
	@NotNull
	private String version;

	@NotNull
	@Field(label = "是否发送")
	private Boolean isSent;

	@Field(label = "发送时间")
	private Date sentTime;


}
