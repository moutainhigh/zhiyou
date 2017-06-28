package com.zy.entity.cms;

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

import static com.zy.entity.cms.Tour.*;

@Entity
@Table(name = "cms_Tour")
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

	@NotBlank
	@Field(label = "作者")
	@View(groups = { VO_DETAIL, VO_LIST, VO_ADMIN })
	private String author;

	@NotNull
	@Field(label = "发布时间")
	@View(name = "releasedTimeLabel", type = String.class, groups = { VO_DETAIL, VO_LIST, VO_ADMIN })
	@Query({Predicate.LT, Predicate.GTE})
	private Date releasedTime;

	@NotNull
	@Query(Predicate.EQ)
	@Field(label = "是否发布")
	@View(groups = { VO_ADMIN  })
	private Boolean isReleased;

	@View(name = "imageBig", groups = { VO_DETAIL, VO_ADMIN })
	@View(name = "imageThumbnail" )
	@View(groups = { VO_ADMIN })
	@Field(label = "主图")
	private String image;

	@Lob
	@NotBlank
	@View(groups = { VO_DETAIL, VO_ADMIN })
	@Field(label = "内容")
	private String content;

	@NotNull
	@View(name = "createdTimeLabel", type = String.class, groups = { VO_ADMIN })
	@Field(label = "创建时间")
	private Date createdTime;

	@NotNull
	@View(groups = { VO_DETAIL, VO_LIST, VO_ADMIN })
	@Field(label = "访问数")
	private Long visitCount;

	@NotNull
	@Field(label = "排序")
	@View(groups = { VO_ADMIN  })
	private Integer orderNumber;

	@Field(label = "是否热门")
	@View(groups = { VO_LIST, VO_ADMIN })
	@NotNull
	@Query(Predicate.EQ)
	private Boolean isHot;

}