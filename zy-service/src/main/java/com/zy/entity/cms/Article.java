package com.zy.entity.cms;

import io.gd.generator.annotation.Field;
import io.gd.generator.annotation.Type;
import io.gd.generator.annotation.query.Query;
import io.gd.generator.annotation.query.QueryModel;
import io.gd.generator.annotation.view.View;
import io.gd.generator.annotation.view.ViewObject;
import io.gd.generator.api.query.Predicate;

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
@Table(name = "cms_article")
@Getter
@Setter
@ViewObject(groups = { "ArticleListVo", "ArticleDetailVo", "ArticleAdminVo" })
@QueryModel
@Type(label = "文章")
public class Article implements Serializable {

	@Id
	@Field(label = "id")
	@View
	private Long id;

	@NotNull
	@Query({Predicate.EQ, Predicate.IN})
	@Field(label = "文章类型id")
	@View(groups = { "ArticleAdminVo"  })
	private Long articleCategoryId;

	@NotBlank
	@Field(label = "标题")
	@View(groups = { "ArticleDetailVo", "ArticleListVo", "ArticleAdminVo"  })
	private String title;

	@Field(label = "摘要")
	@View(groups = { "ArticleDetailVo", "ArticleAdminVo"  })
	private String brief;

	@NotBlank
	@Field(label = "作者")
	@View(groups = { "ArticleDetailVo", "ArticleAdminVo"  })
	private String author;

	@NotNull
	@Field(label = "发布时间")
	@View(name = "releasedTimeLabel", type = String.class, groups = { "ArticleDetailVo", "ArticleListVo", "ArticleAdminVo"  })
	private Date releasedTime;

	@NotNull
	@Query(Predicate.EQ)
	@Field(label = "是否发布")
	@View(groups = { "ArticleAdminVo"  })
	private Boolean isReleased;

	@View(name = "imageBig", groups = { "ArticleDetailVo", "ArticleAdminVo"  })
	@View(name = "imageThumbnail" )
	@View(groups = { "ArticleAdminVo" })
	@Field(label = "主图")
	private String image;

	@Lob
	@NotBlank
	@View(groups = { "ArticleDetailVo", "ArticleAdminVo"  })
	@Field(label = "内容")
	private String content;

	@NotNull
	@View(name = "createdTimeLabel", type = String.class, groups = { "ArticleAdminVo"  })
	@Field(label = "创建时间")
	private Date createdTime;

	@NotNull
	@View(groups = { "ArticleDetailVo", "ArticleAdminVo"  })
	@Field(label = "访问数")
	private Long visitCount;

}