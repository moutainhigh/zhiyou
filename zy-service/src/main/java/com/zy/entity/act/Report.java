package com.zy.entity.act;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;

import com.zy.common.extend.StringBinder;
import com.zy.entity.sys.ConfirmStatus;
import com.zy.entity.usr.Portrait.Gender;

import io.gd.generator.annotation.Field;
import io.gd.generator.annotation.Type;
import io.gd.generator.annotation.query.Query;
import io.gd.generator.annotation.query.QueryModel;
import io.gd.generator.annotation.view.AssociationView;
import io.gd.generator.annotation.view.CollectionView;
import io.gd.generator.annotation.view.View;
import io.gd.generator.annotation.view.ViewObject;
import io.gd.generator.api.query.Predicate;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "act_report")
@Getter
@Setter
@Type(label = "检测报告")
@QueryModel
@ViewObject(groups = {"ReportListVo", "ReportDetailVo", "ReportAdminVo"})
public class Report implements Serializable {

	@Type(label = "检测结果")
	public enum ReportResult {
		阴性, 弱阳性, 阳性, 干扰色
	}
	
	@Id
	@Field(label = "id")
	@View
	private Long id;

	@NotNull
	@Field(label = "用户id")
	@Query({Predicate.EQ, Predicate.IN})
	@View
	@AssociationView(name = "user", groups = "ReportAdminVo", associationGroup = "UserAdminSimpleVo")
	private Long userId;

	@NotNull
	@Field(label = "姓名")
	@Query({Predicate.LK})
	@View
	private String realname;

	@NotNull
	@Field(label = "年龄")
	@View
	private Integer age;
	
	@NotNull
	@Field(label = "性别")
	@View
	private Gender gender;
	
	@Column(length = 60)
	//@NotBlank
	@Pattern(regexp = "^1[\\d]{10}$")
	@StringBinder
	@Query(Predicate.EQ)
	@Field(label = "手机号")
	@View
	private String phone;

	//@NotNull
	@Field(label = "职业")
	@View(name = "jobName", type = String.class)
	@View(groups = {"ReportDetailVo", "ReportAdminVo"})
	private Long jobId;

	//@NotNull
	@Field(label = "所在地")
	@View(name = "province", type = String.class)
	@View(name = "city", type = String.class)
	@View(name = "district", type = String.class)
	@View(groups = {"ReportDetailVo", "ReportAdminVo"})
	private Long areaId;

	//@NotBlank
	@Field(label = "标签")
	@CollectionView(name = "tagNames", groups = {"ReportDetailVo", "ReportAdminVo"}, elementType = String.class)
	@View(groups = {"ReportDetailVo", "ReportAdminVo"})
	private String tagIds;
	
	@NotNull
	@Field(label = "检测结果")
	@View
	private ReportResult reportResult;

	@NotBlank
	@Column(length = 2000)
	@StringBinder
	@Field(label = "文字")
	@View(groups = {"ReportDetailVo", "ReportAdminVo"})
	private String text;

	@NotBlank
	@URL
	@StringBinder
	@Field(label = "图片1")
	@View(groups = {"ReportDetailVo", "ReportAdminVo"})
	@View(name = "image1Big", groups = {"ReportDetailVo", "ReportAdminVo"})
	@View(name = "image1Thumbnail", groups = {"ReportDetailVo", "ReportAdminVo"})
	private String image1;

	@URL
	@StringBinder
	@Field(label = "图片2")
	@View(groups = {"ReportDetailVo", "ReportAdminVo"})
	@View(name = "image2Big", groups = {"ReportDetailVo", "ReportAdminVo"})
	@View(name = "image2Thumbnail", groups = {"ReportDetailVo", "ReportAdminVo"})
	private String image2;

	@URL
	@StringBinder
	@Field(label = "图片3")
	@View(groups = {"ReportDetailVo", "ReportAdminVo"})
	@View(name = "image3Big", groups = {"ReportDetailVo", "ReportAdminVo"})
	@View(name = "image3Thumbnail", groups = {"ReportDetailVo", "ReportAdminVo"})
	private String image3;

	@StringBinder
	@URL
	@Field(label = "图片4")
	@View(groups = {"ReportDetailVo", "ReportAdminVo"})
	@View(name = "image4Big", groups = {"ReportDetailVo", "ReportAdminVo"})
	@View(name = "image4Thumbnail", groups = {"ReportDetailVo", "ReportAdminVo"})
	private String image4;

	@StringBinder
	@Field(label = "图片5")
	@URL
	@View(groups = {"ReportDetailVo", "ReportAdminVo"})
	@View(name = "image5Big", groups = {"ReportDetailVo", "ReportAdminVo"})
	@View(name = "image5Thumbnail", groups = {"ReportDetailVo", "ReportAdminVo"})
	private String image5;

	@StringBinder
	@URL
	@View(groups = {"ReportDetailVo", "ReportAdminVo"})
	@View(name = "image6Big", groups = {"ReportDetailVo", "ReportAdminVo"})
	@View(name = "image6Thumbnail", groups = {"ReportDetailVo", "ReportAdminVo"})
	@Field(label = "图片6")
	private String image6;

	@NotNull
	@Field(label = "申请时间")
	@View(groups = {"ReportDetailVo", "ReportAdminVo"})
	@View(name = "appliedTimeLabel", type = String.class, groups = {"ReportAdminVo", "ReportDetailVo", "ReportListVo"})
	private Date appliedTime;

	@NotNull
	@Field(label = "创建时间")
	@View(groups = {"ReportAdminVo"})
	@View(name = "createdTimeLabel", type = String.class, groups = {"ReportAdminVo", "ReportDetailVo", "ReportListVo"})
	private Date createdTime;

	@NotNull
	@Query(Predicate.EQ)
	@Field(label = "初审状态")
	@View(groups = {"ReportDetailVo", "ReportAdminVo"})
	private ConfirmStatus preConfirmStatus;

	@NotNull
	@Query(Predicate.EQ)
	@Field(label = "审核状态")
	@View(groups = {"ReportListVo", "ReportDetailVo", "ReportAdminVo"})
	private ConfirmStatus confirmStatus;

	@Field(label = "审核备注")
	@View(groups = {"ReportDetailVo", "ReportAdminVo"})
	private String confirmRemark;

	@Field(label = "审核通过时间")
	@View(groups = {"ReportDetailVo", "ReportAdminVo"})
	private Date confirmedTime;
	
	@Field(label = "是否已结算")
	@View(groups = {"ReportAdminVo"})
	@NotNull
	@Query(Predicate.EQ)
	private Boolean isSettledUp;

	@NotNull
	@Version
	@Field(label = "乐观锁")
	private Integer version;
	
}
