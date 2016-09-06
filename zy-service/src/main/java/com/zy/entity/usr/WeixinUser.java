package com.zy.entity.usr;

import io.gd.generator.annotation.Field;
import io.gd.generator.annotation.Type;
import io.gd.generator.annotation.query.Query;
import io.gd.generator.annotation.query.QueryModel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

import static io.gd.generator.api.query.Predicate.EQ;
import static io.gd.generator.api.query.Predicate.IN;

@Entity
@Table(name = "usr_weixin_user")
@Getter
@Setter
@QueryModel
@Type(label = "微信用户")
public class WeixinUser implements Serializable {

	@Id
	@Query(IN)
	@Field(label = "id")
	private Long id;
	@NotNull
	@Query(EQ)
	@Field(label = "用户id")
	private Long userId;

	@NotBlank
	@Column(length = 60, unique = true)
	@Query(EQ)
	@Field(label = "openID")
	private String openId;

	@Column(length = 60)
	@Field(label = "unionId")
	private String unionId;

	@Field(label = "头像")
	private String avatar;
	@Field(label = "昵称")
	private String nickname;


}