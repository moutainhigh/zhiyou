package com.zy.entity.cms;

import io.gd.generator.annotation.Field;
import io.gd.generator.annotation.Type;
import io.gd.generator.annotation.query.Query;
import io.gd.generator.annotation.query.QueryModel;
import io.gd.generator.api.query.Predicate;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import lombok.Getter;
import lombok.Setter;

import org.hibernate.validator.constraints.NotBlank;

import com.zy.entity.usr.User.UserType;

@Entity
@Table(name = "cms_help_category")
@Getter
@Setter
@QueryModel
@Type(label = "帮助分类")
public class HelpCategory implements Serializable {

	@Id
	@Field(label = "id")
	private Long id;

	@NotBlank
	@Pattern(regexp = "[a-zA-Z]*")
	@Query(Predicate.EQ)
	@Field(label = "code")
	private String code;

	@NotBlank
	@Field(label = "内容")
	private String name;

	@NotNull
	@Field(label = "创建时间")
	private Date createdTime;

	@NotNull
	@Query(Predicate.EQ)
	@Field(label = "针对用户类型")
	private UserType userType;

	@NotNull
	@Field(label = "排序")
	private Integer indexNumber;

	@NotNull
	@Query({Predicate.GTE, Predicate.LT})
	private Long createId;

	@NotNull
	@Query({Predicate.GTE, Predicate.LT})
	private Date updateTime;

	@NotNull
	@Query({Predicate.GTE, Predicate.LT})
	private Long updateId;

}