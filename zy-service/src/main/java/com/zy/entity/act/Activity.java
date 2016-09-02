package com.zy.entity.act;

import io.gd.generator.annotation.Field;
import io.gd.generator.annotation.Type;
import io.gd.generator.annotation.query.Query;
import io.gd.generator.annotation.query.QueryModel;
import io.gd.generator.annotation.view.CollectionView;
import io.gd.generator.annotation.view.View;
import io.gd.generator.annotation.view.ViewObject;
import io.gd.generator.api.query.Predicate;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "act_activity")
@Getter
@Setter
@Type(label = "活动")
@QueryModel
@ViewObject(groups = {"ActivityListVo", "ActivityDetailVo", "ActivityAdminVo", "ActivityAdminFullVo"},
		views = {
				@View(name = "status", type = String.class)
		},
		collectionViews = {
				@CollectionView(name = "activityApplies", elementGroup = "ActivityApplyAdminVo", groups = "ActivityAdminFullVo"),
				@CollectionView(name = "activityCollects", elementGroup = "ActivityCollectAdminVo", groups = "ActivityAdminFullVo"),
				@CollectionView(name = "activitySignIns", elementGroup = "ActivitySignInAdminVo", groups = "ActivityAdminFullVo")
		}

)
public class Activity implements Serializable {

	@Id
	@Field(label = "id")
	@Query(Predicate.IN)
	@View
	private Long id;

	@Field(label = "标题")
	@NotBlank
	@Length(max = 90)
	@Query(Predicate.LK)
	@View
	private String title;

	@NotNull
	@Field(label = "地区")
	@Query({Predicate.EQ, Predicate.IN})
	@View(name = "province", type = String.class)
	@View(name = "city", type = String.class)
	@View(name = "district", type = String.class)
	@View(groups = {"ActivityAdminVo", "ActivityAdminFullVo"})
	private Long areaId;

	@NotBlank
	@Field(label = "详细地址", description = "精确到门牌号")
	@View
	private String address;

	//@NotBlank
	@Field(label = "纬度", description = "地图控件位置")
	@View(groups = {"ActivityDetailVo", "ActivityAdminVo", "ActivityAdminFullVo"})
	private Double latitude;
	
	//@NotBlank
	@Field(label = " 经度", description = "地图控件位置")
	@View(groups = {"ActivityDetailVo", "ActivityAdminVo", "ActivityAdminFullVo"})
	private Double longitude; 

	@NotBlank
	@Field(label = "活动主图")
	@View(name = "imageBig")
	@View(name = "imageThumbnail")
	@View(groups = "ActivityAdminVo")
	private String image;

	@NotBlank
	@Lob
	@Field(label = "活动详情")
	@View(groups = {"ActivityDetailVo", "ActivityAdminVo", "ActivityAdminFullVo"})
	private String detail;

	@NotNull
	@Field(label = "报名截止时间")
	@Query({Predicate.GTE, Predicate.LT})
	@View(name = "applyDeadlineLabel", type = String.class)
	@View(name = "applyDeadlineFormatted", type = String.class, groups = "ActivityAdminVo")
	private Date applyDeadline;

	@NotNull
	@Field(label = "活动开始时间")
	@Query({Predicate.GTE, Predicate.LT})
	@View(name = "startTimeLabel", type = String.class)
	@View(name = "startTimeFormatted", type = String.class, groups = "ActivityAdminVo")
	private Date startTime;

	@NotNull
	@Field(label = "活动结束时间")
	@View(name = "endTimeLabel", type = String.class)
	@View(name = "endTimeFormatted", type = String.class, groups = "ActivityAdminVo")
	private Date endTime;

	@NotNull
	@Min(0)
	@Field(label = "签到数")
	@View(groups = {"ActivityAdminVo", "ActivityAdminFullVo"})
	private Long signedInCount;

	@NotNull
	@Min(0)
	@Field(label = "关注数")
	@View
	private Long collectedCount;

	@NotNull
	@Min(0)
	@Field(label = "报名数")
	@View
	private Long appliedCount;

	@NotNull
	@Min(0)
	@Field(label = "浏览数")
	@View
	private Long viewedCount;

	@NotNull
	@Field(label = "是否发布")
	@Query(Predicate.EQ)
	@View(groups = {"ActivityAdminVo", "ActivityAdminFullVo"})
	private Boolean isReleased;

	@NotNull
	@Version
	@Field(label = "乐观锁")
	private Integer version;

}
