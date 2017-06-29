package com.zy.entity.tour;

import io.gd.generator.annotation.Field;
import io.gd.generator.annotation.Type;
import io.gd.generator.annotation.query.Query;
import io.gd.generator.annotation.query.QueryModel;
import io.gd.generator.annotation.view.View;
import io.gd.generator.annotation.view.ViewObject;
import io.gd.generator.api.query.Predicate;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

import static com.zy.entity.cms.Article.*;

@Entity
@Table(name = "ts_tour")
@Getter
@Setter
@ViewObject(groups = {VO_LIST, VO_DETAIL, VO_ADMIN})
@QueryModel
@Type(label = "旅游")
public class Tour implements Serializable {

	public static final String VO_ADMIN = "TourAdminVo";
	public static final String VO_LIST = "TourListVo";
	public static final String VO_DETAIL = "TourDetailVo";

	@Id
	@Field(label = "id")
	@View
	private Long id;

	@NotBlank
	@Column(length = 100)
	@Field(label = "标题")
	@View(groups = { VO_DETAIL, VO_LIST, VO_ADMIN })
	@Query(Predicate.LK)
	private String title;

	@Field(label = "摘要")
	@View(groups = { VO_DETAIL, VO_ADMIN })
	private String brief;

	@Field(label = "天数")
	@View(groups = { VO_DETAIL, VO_ADMIN })
	private Integer days;

	@NotNull
	@Query(Predicate.EQ)
	@Field(label = "是否发布")
	@View(groups = { VO_ADMIN  })
	private Boolean isReleased;

	@View(name = "image", groups = { VO_DETAIL, VO_ADMIN })
	@View(groups = { VO_ADMIN })
	@Field(label = "主图")
	private String image;

	@Lob
	@NotBlank
	@View(groups = { VO_DETAIL, VO_ADMIN })
	@Field(label = "内容")
	private String content;

	@NotNull
	@View(name = "createdTime", type = String.class, groups = { VO_ADMIN })
	@Field(label = "创建时间")
	private Date createdTime;

	@NotBlank
	@Field(label = "创建人")
	@View(groups = { VO_DETAIL, VO_LIST, VO_ADMIN })
	private Long createby;


	@NotNull
	@View(name = "updateTime", type = String.class, groups = { VO_ADMIN })
	@Field(label = "更新时间")
	private Date updateTime;

	@NotBlank
	@Field(label = "更新人")
	@View(groups = { VO_DETAIL, VO_LIST, VO_ADMIN })
	private Long updateby;



}