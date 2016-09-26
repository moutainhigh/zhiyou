package com.zy.entity.act;

import com.zy.common.extend.StringBinder;
import com.zy.entity.sys.ConfirmStatus;
import com.zy.entity.usr.Portrait.Gender;
import com.zy.entity.usr.User;
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
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

@Entity
@Table(name = "act_report")
@Getter
@Setter
@Type(label = "检测报告")
@QueryModel
@ViewObject(groups = {Report.VO_LIST, Report.VO_DETAIL, Report.VO_ADMIN, Report.VO_EXPORT})
public class Report implements Serializable {

	public static final String VO_ADMIN = "ReportAdminVo";
	public static final String VO_LIST = "ReportListVo";
	public static final String VO_DETAIL = "ReportDetailVo";
	public static final String VO_EXPORT = "ReportExportVo";

	@Type(label = "检测结果")
	public enum ReportResult {
		阴性, 弱阳性, 阳性, 干扰色
	}
	
	@Id
	@Field(label = "id")
	@View(groups = {VO_LIST, VO_DETAIL, VO_ADMIN})
	@View(groups = VO_EXPORT, field = @Field(label = "报告编号", order = 10))
	private Long id;

	@NotNull
	@Field(label = "用户id")
	@Query({Predicate.EQ, Predicate.IN})
	@View(groups = {VO_LIST, VO_DETAIL, VO_ADMIN})
	@View(groups = VO_EXPORT, name = "nickname", field = @Field(label = "昵称", order = 15))
	@AssociationView(name = "user", groups = VO_ADMIN, associationGroup = User.VO_ADMIN_SIMPLE)
	private Long userId;

	@NotNull
	@Field(label = "姓名")
	@Query({Predicate.EQ, Predicate.IN})
	@View(groups = {VO_LIST, VO_DETAIL, VO_ADMIN})
	@View(groups = VO_EXPORT, field = @Field(label = "客户姓名", order = 20))
	private String realname;

	@NotNull
	@Field(label = "年龄")
	@View(groups = {VO_LIST, VO_DETAIL, VO_ADMIN})
	@View(groups = VO_EXPORT, field = @Field(label = "年龄", order = 30))
	private Integer age;
	
	@NotNull
	@Field(label = "性别")
	@View(groups = {VO_LIST, VO_DETAIL, VO_ADMIN})
	@View(groups = VO_EXPORT, field = @Field(label = "性别", order = 40))
	private Gender gender;
	
	@Column(length = 60)
	//@NotBlank
	@Pattern(regexp = "^1[\\d]{10}$")
	@StringBinder
	@Query(Predicate.EQ)
	@Field(label = "手机号")
	@View(groups = {VO_LIST, VO_DETAIL, VO_ADMIN})
	@View(groups = VO_EXPORT, field = @Field(label = "手机号", order = 50))
	private String phone;

	//@NotNull
	@Field(label = "职业")
	@View(name = "jobName", type = String.class, groups = {VO_LIST, VO_DETAIL, VO_ADMIN})
	@View(groups = {VO_DETAIL, VO_ADMIN})
	private Long jobId;

	//@NotNull
	@Field(label = "所在地")
	@View(name = "province", type = String.class, groups = {VO_LIST, VO_DETAIL, VO_ADMIN, VO_EXPORT})
	@View(name = "city", type = String.class, groups = {VO_LIST, VO_DETAIL, VO_ADMIN, VO_EXPORT})
	@View(name = "district", type = String.class, groups = {VO_LIST, VO_DETAIL, VO_ADMIN, VO_EXPORT})
	@View(groups = {VO_DETAIL, VO_ADMIN})
	private Long areaId;

	//@NotBlank
	@Field(label = "标签")
	@CollectionView(name = "tagNames", groups = {VO_DETAIL, VO_ADMIN}, elementType = String.class)
	@View(groups = {VO_DETAIL, VO_ADMIN})
	private String tagIds;
	
	@NotNull
	@Query(Predicate.EQ)
	@Field(label = "检测结果")
	@View(groups = {VO_LIST, VO_DETAIL, VO_ADMIN})
	@View(groups = VO_EXPORT, field = @Field(label = "检测结果", order = 60))
	private ReportResult reportResult;

	@NotBlank
	@Column(length = 2000)
	@StringBinder
	@Field(label = "文字")
	@View(groups = {VO_DETAIL, VO_ADMIN})
	@View(groups = VO_EXPORT, field = @Field(label = "检测心得", order = 70))
	private String text;

	@NotBlank
	@StringBinder
	@Field(label = "图片")
	@View(groups = {VO_DETAIL, VO_ADMIN})
	@Column(length = 2000)
	@CollectionView(name= "images", type = ArrayList.class, elementType = String.class)
	@CollectionView(name= "imageThumbnails", type = ArrayList.class, elementType = String.class)
	@CollectionView(name= "imageBigs", type = ArrayList.class, elementType = String.class)
	private String image;

	@Field(label = "检测次数")
	@NotNull
	@Min(1)
	@Query(Predicate.EQ)
	@View(groups = {VO_DETAIL, VO_ADMIN, VO_EXPORT})
	private Integer times;

	@NotNull
	@Field(label = "申请时间")
	@View(groups = {VO_DETAIL, VO_ADMIN})
	@View(name = "appliedTimeLabel", type = String.class, groups = {VO_ADMIN, VO_DETAIL, VO_LIST, VO_EXPORT})
	private Date appliedTime;

	@NotNull
	@Query({Predicate.GTE, Predicate.LT})
	@Field(label = "创建时间")
	@View(groups = {VO_ADMIN})
	@View(name = "createdTimeLabel", type = String.class, groups = {VO_ADMIN, VO_DETAIL, VO_LIST, VO_EXPORT})
	private Date createdTime;

	@NotNull
	@Query(Predicate.EQ)
	@Field(label = "初审状态")
	@View(groups = {VO_DETAIL, VO_ADMIN, VO_EXPORT})
	private ConfirmStatus preConfirmStatus;

	@Field(label = "初审通过时间")
	@View(groups = {VO_ADMIN, VO_EXPORT})
	@View(name = "preConfirmedTimeLabel", type = String.class, groups = {VO_ADMIN})
	private Date preConfirmedTime;

	@NotNull
	@Query(Predicate.EQ)
	@Field(label = "审核状态")
	@View(groups = {VO_LIST, VO_DETAIL, VO_ADMIN, VO_EXPORT})
	private ConfirmStatus confirmStatus;

	@Field(label = "审核备注")
	@View(groups = {VO_DETAIL, VO_ADMIN, VO_EXPORT})
	private String confirmRemark;

	@Field(label = "审核通过时间")
	@View(groups = {VO_DETAIL, VO_ADMIN, VO_EXPORT})
	@View(name = "confirmedTimeLabel", type = String.class, groups = {VO_ADMIN, VO_DETAIL})
	private Date confirmedTime;
	
	@Field(label = "是否已结算")
	@View(groups = {VO_DETAIL, VO_ADMIN})
	@NotNull
	@Query(Predicate.EQ)
	private Boolean isSettledUp;

	@Field(label = "是否热门")
	@View(groups = {VO_ADMIN})
	@NotNull
	@Query(Predicate.EQ)
	private Boolean isHot;

	@NotNull
	@Version
	@Field(label = "乐观锁")
	private Integer version;
	
}
