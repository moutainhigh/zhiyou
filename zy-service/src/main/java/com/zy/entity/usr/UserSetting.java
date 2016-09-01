package com.gc.entity.usr;

import io.gd.generator.annotation.Field;
import io.gd.generator.annotation.Type;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "usr_user_setting")
@Getter
@Setter
@Type(label = "用户设置")
public class UserSetting implements Serializable {

	@Id
	@Field(label = "id")
	private Long id;

	@Column(unique = true)
	@NotNull
	@Field(label = "用户id")
	private Long userId;

	@Field(label = "是否接受任务短信提醒")
	private Boolean isReceiveTaskSms;

}
