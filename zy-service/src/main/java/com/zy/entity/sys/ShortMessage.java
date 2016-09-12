package com.zy.entity.sys;

import io.gd.generator.annotation.Field;
import io.gd.generator.annotation.Type;
import io.gd.generator.annotation.query.Query;
import io.gd.generator.annotation.query.QueryModel;
import io.gd.generator.api.query.Predicate;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
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

	@Query(Predicate.EQ)
	@Field(label = "手机")
	@NotBlank
	private String phone;

	@Field(label = "消息")
	@Column(length = 2000)
	@NotBlank
	private String content;

	@Temporal(TemporalType.TIMESTAMP)
	@Query({Predicate.GTE, Predicate.LT})
	@Field(label = "创建时间")
	@NotNull
	private Date createdTime;

	@Field(label = "ip地址")
	private String ip;

	@Field(label = "token")
	@Column(length = 60, unique = true)
	private String token;

}