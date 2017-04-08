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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

import static com.zy.entity.cms.Article.*;

@Entity
@Table(name = "cms_matter")
@Getter
@Setter
@ViewObject(groups = {VO_LIST, VO_DETAIL, VO_ADMIN})
@QueryModel
@Type(label = "资源")
public class Matter implements Serializable {

	public static final String VO_Matter = "MatterVo";

	@Id
	@Field(label = "id")
	@View
	private Long id;

	@NotBlank
	@Column(length = 100)
	@Field(label = "标题")
	@View(groups = { VO_Matter })
	@Query(Predicate.LK)
	private String title;

	@Field(label = "简述")
	@View(groups = { VO_Matter })
	private String description;

	@NotBlank
	@Field(label = "文件类型")
	@View(groups = { VO_Matter})
	@Query(Predicate.EQ)
	private Integer type;

	@NotBlank
	@Field(label = "文件路径")
	@View(groups = { VO_Matter})
	private String url;

	@NotBlank
	@Field(label = "权限")
	@View(groups = { VO_Matter})
	@Query({Predicate.EQ, Predicate.IN})
	private Integer authority;

	@NotNull
	@Field(label = "发布时间")
	@View(name = "uploadTime", type = String.class, groups = { VO_Matter })
	@Query({Predicate.LT, Predicate.GTE})
	private Date uploadTime;

	@Id
	@Field(label = "上传用户id")
	@View
	private Long uploadUserId;

	@NotNull
	@Query(Predicate.EQ)
	@Field(label = "是否发布")
	@View(groups = { VO_Matter  })
	private Integer status;

	@NotNull
	@View(groups = { VO_Matter })
	@Field(label = "点击量")
	private Integer clickedCount;

	@NotNull
	@View(groups = { VO_Matter })
	@Field(label = "关注量")
	private Integer collectedCount;

	@NotNull
	@View(groups = { VO_Matter })
	@Field(label = "下载量")
	private Integer downloadCount;

	@NotNull
	@View(groups = { VO_Matter })
	private String author;

	@NotNull
	@View(groups = { VO_Matter })
	private Boolean isCollected;

}