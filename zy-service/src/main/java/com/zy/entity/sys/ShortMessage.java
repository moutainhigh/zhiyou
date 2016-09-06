package com.zy.entity.sys;

import io.gd.generator.annotation.Field;
import io.gd.generator.annotation.Type;
import io.gd.generator.annotation.query.Query;
import io.gd.generator.annotation.query.QueryModel;
import io.gd.generator.api.query.Predicate;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "sys_short_message")
@Getter
@Setter
@QueryModel
@Type(label = "短信")
public class ShortMessage implements Serializable {

	@Id
	@Field(label = "id")
	private Long id;

	@Field(label = "消息")
	private String message;

	@Query(Predicate.EQ)
	@Field(label = "手机")
	private String phone;

	@Field(label = "错误码")
	private Boolean isSuccess;

	@Field(label = "错误信息")
	private String errorMessage;

	@Temporal(TemporalType.TIMESTAMP)
	@Query({Predicate.GTE, Predicate.LT})
	@Field(label = "创建时间")
	private Date createdTime;

	@Field(label = "ip地址")
	private String ip;

}