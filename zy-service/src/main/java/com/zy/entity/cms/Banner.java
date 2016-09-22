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
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

import static com.zy.entity.cms.Banner.VO_ADMIN;

@Entity
@Table(name = "cms_banner")
@Getter
@Setter
@QueryModel
@Type(label = "banner")
@ViewObject(groups = VO_ADMIN, views = {
		@View(name = "width", type = int.class, groups = VO_ADMIN),
		@View(name = "height", type = int.class, groups = VO_ADMIN)
})
public class Banner implements Serializable {

	public static final String VO_ADMIN = "BannerAdminVo";

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
	@View(groups = VO_ADMIN)
	private Long id;

	@NotNull
	@Query(Predicate.EQ)
	@Field(label = "是否发布")
	@View(groups = VO_ADMIN)
	private Boolean isReleased;

	@Field(label = "banner图片")
	@View(groups = VO_ADMIN)
	@View(name = "imageThumbnail", groups = VO_ADMIN)
	@NotBlank
	@URL
	private String image;

	@Field(label = "url")
	@View(groups = VO_ADMIN)
	@URL
	private String url;

	@Query(Predicate.LK)
	@Field(label = "标题")
	@View(groups = VO_ADMIN)
	@NotBlank
	@Length(max = 60)
	private String title;

	@Field(label = "排序")
	@View(groups = VO_ADMIN)
	@NotNull
	private Integer orderNumber;

	@Query(Predicate.EQ)
	@Field(label = "banner位置")
	@View(groups = VO_ADMIN)
	@NotNull
	private BannerPosition bannerPosition;

	@Field(label = "是否新窗口打开", description = "手机端无效")
	@View(groups = VO_ADMIN)
	@NotNull
	private Boolean isOpenBlank;

}