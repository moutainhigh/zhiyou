package com.zy.entity.cms;

import io.gd.generator.annotation.Field;
import io.gd.generator.annotation.Type;
import io.gd.generator.annotation.query.Query;
import io.gd.generator.annotation.query.QueryModel;
import io.gd.generator.api.query.Predicate;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "cms_article_category")
@Getter
@Setter
@QueryModel
@Type(label = "文章分类")
public class ArticleCategory implements Serializable {

	@Id
	@Field(label = "id")
	private Long id;

	@Query(Predicate.EQ)
	@Field(label = "父节点id")
	private Long parentId;

	@NotNull
	@Query(Predicate.EQ)
	@Field(label = "是否顶层节点")
	private Boolean isTopParent;

	@NotBlank
	@Field(label = "名称")
	private String name;

	@NotNull
	@Field(label = "排序")
	private Integer indexNumber;

	@NotNull
	@Field(label = "创建时间")
	private Date createdTime;

	@NotNull
	@Field(label = "是否可见")
	@Query(Predicate.EQ)
	private Boolean isVisiable;

}