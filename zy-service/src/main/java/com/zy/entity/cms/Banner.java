package com.zy.entity.cms;

import io.gd.generator.annotation.Field;
import io.gd.generator.annotation.Type;
import io.gd.generator.annotation.query.Query;
import io.gd.generator.annotation.query.QueryModel;
import io.gd.generator.annotation.view.View;
import io.gd.generator.annotation.view.ViewObject;
import io.gd.generator.api.query.Predicate;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;

@Entity
@Table(name = "cms_banner")
@Getter
@Setter
@QueryModel
@Type(label = "banner")
@ViewObject(groups = "BannerAdminVo", views = {
		@View(name = "width", type = int.class, groups = "BannerAdminVo"),
		@View(name = "height", type = int.class, groups = "BannerAdminVo")
})
public class Banner implements Serializable {

	@Type(label = "banner位置")
	public enum BannerPosition {
		首页顶部(900, 300);

		BannerPosition(int width, int height) {
			this.width = width;
			this.height = height;
		}

		@Field(label = "宽度")
		private final int width;

		@Field(label = "高度")
		private final int height;

		public int getWidth() {
			return width;
		}

		public int getHeight() {
			return height;
		}

	}

	@Id
	@Field(label = "id")
	@View(groups = "BannerAdminVo")
	private Long id;

	@NotNull
	@Query(Predicate.EQ)
	@Field(label = "是否发布")
	@View(groups = "BannerAdminVo")
	private Boolean isReleased;

	@Field(label = "banner图片")
	@View(groups = "BannerAdminVo")
	@View(name = "imageThumbnail", groups = "BannerAdminVo")
	@NotBlank
	@URL
	private String image;

	@Field(label = "url")
	@View(groups = "BannerAdminVo")
	@URL
	private String url;

	@Query(Predicate.LK)
	@Field(label = "标题")
	@View(groups = "BannerAdminVo")
	@NotBlank
	@Length(max = 60)
	private String title;

	@Field(label = "排序")
	@View(groups = "BannerAdminVo")
	@NotNull
	private Integer orderNumber;

	@Query(Predicate.EQ)
	@Field(label = "banner位置")
	@View(groups = "BannerAdminVo")
	@NotNull
	private BannerPosition bannerPosition;

	@Field(label = "是否新窗口打开", description = "手机端无效")
	@View(groups = "BannerAdminVo")
	@NotNull
	private Boolean isOpenBlank;

}