package com.gc.entity.cms;

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
@Table(name = "cms_help")
@Getter
@Setter
@Type(label = "帮助")
public class Help implements Serializable {

	@Id
	@Field(label = "id")
	private Long id;

	@NotBlank
	@Field(label = "标题")
	private String title;

	@NotBlank
	@Lob
	@Field(label = "内容")
	private String content;

	@NotNull
	@Field(label = "创建时间")
	private Date createdTime;

	@NotNull
	@Field(label = "帮助分类id")
	private Long helpCategoryId;

	@NotNull
	@Field(label = "排序")
	private Integer indexNumber;


}