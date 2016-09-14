package com.zy.entity.act;

import com.zy.common.extend.StringBinder;
import com.zy.entity.sys.ConfirmStatus;
import com.zy.entity.usr.Portrait.Gender;

import io.gd.generator.annotation.Field;
import io.gd.generator.annotation.Type;
import io.gd.generator.annotation.query.Query;
import io.gd.generator.annotation.query.QueryModel;
import io.gd.generator.annotation.view.AssociationView;
import io.gd.generator.annotation.view.View;
import io.gd.generator.annotation.view.ViewObject;
import io.gd.generator.api.query.Predicate;
import lombok.Getter;
import lombok.Setter;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "act_report")
@Getter
@Setter
@Type(label = "检测报告")
@QueryModel
@ViewObject(groups = {"ReportVo", "ReportAdminVo"})
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
	@Temporal(TemporalType.DATE)
	@Field(label = "检测时间")
	@View
	@View(name = "dateLabel", type = String.class)
	private Date date;
	
	@NotNull
	@Field(label = "年龄")
	@View
	private Integer age;
	
	@NotNull
	@Field(label = "性别")
	@View
	private Gender gender;
	
	@NotNull
	@Field(label = "检测结果")
	@View
	private ReportResult reportResult;

	@NotBlank
	@Column(length = 2000)
	@StringBinder
	@Field(label = "文字")
	@View
	private String text;

	@NotBlank
	@URL
	@StringBinder
	@Field(label = "图片1")
	@View
	@View(name = "image1Big")
	@View(name = "image1Thumbnail")
	private String image1;

	@URL
	@StringBinder
	@Field(label = "图片2")
	@View
	@View(name = "image2Big")
	@View(name = "image2Thumbnail")
	private String image2;

	@URL
	@StringBinder
	@Field(label = "图片3")
	@View
	@View(name = "image3Big")
	@View(name = "image3Thumbnail")
	private String image3;

	@StringBinder
	@URL
	@Field(label = "图片4")
	@View
	@View(name = "image4Big")
	@View(name = "image4Thumbnail")
	private String image4;

	@StringBinder
	@Field(label = "图片5")
	@URL
	@View
	@View(name = "image5Big")
	@View(name = "image5Thumbnail")
	private String image5;

	@StringBinder
	@URL
	@View
	@View(name = "image6Big")
	@View(name = "image6Thumbnail")
	@Field(label = "图片6")
	private String image6;

	@NotNull
	@Field(label = "创建时间")
	@View(groups = {"ReportAdminVo"})
	@View(name = "createdTimeLabel", type = String.class)
	private Date createdTime;

	@NotNull
	@Query(Predicate.EQ)
	@Field(label = "初审状态")
	@View(groups = {"ReportVo", "ReportAdminVo"})
	private ConfirmStatus preConfirmStatus;

	@NotNull
	@Query(Predicate.EQ)
	@Field(label = "审核状态")
	@View(groups = {"ReportVo", "ReportAdminVo"})
	private ConfirmStatus confirmStatus;

	@Field(label = "审核备注")
	@View
	private String confirmRemark;

	@NotNull
	@Field(label = "申请时间")
	@View(groups = {"ReportVo", "ReportAdminVo"})
	private Date appliedTime;
	
	@Field(label = "审核通过时间")
	@View(groups = {"ReportVo", "ReportAdminVo"})
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
