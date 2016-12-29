package com.zy.entity.act;

import com.zy.common.extend.StringBinder;
import com.zy.entity.mal.Product;
import com.zy.entity.sys.ConfirmStatus;
import com.zy.entity.usr.User;
import com.zy.entity.usr.UserInfo.Gender;
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
	@Query({Predicate.EQ, Predicate.IN})
	private Long id;

	@NotNull
	@Field(label = "用户id")
	@Query({Predicate.EQ, Predicate.IN})
	@View(groups = {VO_LIST, VO_DETAIL, VO_ADMIN})
	@View(groups = VO_EXPORT, type = String.class,  name = "userNickname", field = @Field(label = "昵称", order = 15))
	@View(groups = VO_EXPORT, type = String.class,  name = "userPhone", field = @Field(label = "手机", order = 15))
	@AssociationView(name = "user", groups = VO_ADMIN, associationGroup = User.VO_ADMIN_SIMPLE)
	private Long userId;

	@NotBlank
	@Field(label = "姓名")
	@Query({Predicate.LK, Predicate.EQ})
	@View(groups = {VO_LIST, VO_DETAIL, VO_ADMIN})
	@View(groups = VO_EXPORT, type = String.class,  field = @Field(label = "客户姓名", order = 20))
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
	@View(groups = VO_EXPORT, type = String.class,  field = @Field(label = "手机号", order = 50))
	private String phone;

	//@NotNull
	@Field(label = "职业")
	@View(name = "jobName", type = String.class, groups = {VO_LIST, VO_DETAIL, VO_ADMIN})
	@View(name = "jobName", type = String.class, groups = VO_EXPORT, field = @Field(label = "职业", order = 55))
	@View(groups = {VO_DETAIL, VO_ADMIN})
	private Long jobId;

	//@NotNull
	@Field(label = "所在地")
	@View(name = "province", type = String.class, groups = {VO_LIST, VO_DETAIL, VO_ADMIN})
	@View(name = "city", type = String.class, groups = {VO_LIST, VO_DETAIL, VO_ADMIN})
	@View(name = "district", type = String.class, groups = {VO_LIST, VO_DETAIL, VO_ADMIN})
	@View(name = "province", type = String.class, groups = VO_EXPORT, field = @Field(label = "所在省", order = 56))
	@View(name = "city", type = String.class, groups = VO_EXPORT, field = @Field(label = "所在市", order = 57))
	@View(name = "district", type = String.class, groups = VO_EXPORT, field = @Field(label = "所在区", order = 58))
	@View(groups = {VO_DETAIL, VO_ADMIN})
	private Long areaId;

	//@NotBlank
	@Field(label = "标签")
	@CollectionView(name = "tagNames", groups = {VO_DETAIL, VO_ADMIN}, elementType = String.class)
	@View(groups = {VO_DETAIL, VO_ADMIN})
	private String tagIds;
	
	@Field(label = "产品")
	@View(groups = {VO_LIST, VO_DETAIL, VO_ADMIN})
	@View(name = "productTitle", type = String.class, groups = {VO_LIST })
	@AssociationView(name = "product", groups = {VO_DETAIL, VO_ADMIN}, associationGroup = Product.VO_LIST)
	private Long productId;

	@NotNull
	@Query(Predicate.EQ)
	@Field(label = "检测结果")
	@View(groups = {VO_LIST, VO_DETAIL, VO_ADMIN})
	@View(groups = VO_EXPORT, field = @Field(label = "检测结果", order = 60))
	private ReportResult reportResult;

	@Query(Predicate.EQ)
	@Field(label = "客服检测结果")
	@View(groups = {VO_ADMIN})
	private ReportResult checkReportResult;

	@Query(Predicate.EQ)
	@Field(label = "回访客服")
	@AssociationView(name = "visitUser", groups = VO_ADMIN, associationGroup = User.VO_ADMIN_SIMPLE)
	private Long visitUserId;

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
	@CollectionView(name= "images", groups = {VO_DETAIL, VO_ADMIN}, type = ArrayList.class, elementType = String.class)
	@CollectionView(name= "imageThumbnails", groups = {VO_DETAIL, VO_ADMIN}, type = ArrayList.class, elementType = String.class)
	@CollectionView(name= "imageBigs", groups = {VO_DETAIL, VO_ADMIN}, type = ArrayList.class, elementType = String.class)
	@View(groups = VO_EXPORT, field = @Field(label = "图片链接", order = 71))
	private String image;

	@Field(label = "检测次数")
	@NotNull
	@Min(1)
	@Query(Predicate.EQ)
	@View(groups = {VO_LIST, VO_DETAIL, VO_ADMIN})
	@View(groups = VO_EXPORT, field = @Field(label = "检测次数", order = 59))
	private Integer times;

	@NotNull
	@Query({Predicate.GTE, Predicate.LT})
	@Field(label = "检测日期")
	@View(groups = {VO_ADMIN})
	@View(name = "reportedDateLabel", type = String.class, groups = {VO_ADMIN, VO_DETAIL, VO_LIST})
	@View(name = "reportedDateLabel", type = String.class,  groups = VO_EXPORT, field = @Field(label = "检测日期", order = 74))
	private Date reportedDate;
	
	@NotNull
	@Field(label = "申请时间")
	@View(groups = {VO_DETAIL, VO_ADMIN})
	@View(name = "appliedTimeLabel", type = String.class, groups = {VO_ADMIN, VO_DETAIL, VO_LIST})
	@View(name = "appliedTimeLabel", type = String.class, groups = VO_EXPORT, field = @Field(label = "申请时间", order = 75))
	private Date appliedTime;

	@NotNull
	@Query({Predicate.GTE, Predicate.LT})
	@Field(label = "创建时间")
	@View(groups = {VO_ADMIN})
	@View(name = "createdTimeLabel", type = String.class, groups = {VO_ADMIN, VO_DETAIL, VO_LIST})
	private Date createdTime;

	@NotNull
	@Query(Predicate.EQ)
	@Field(label = "初审状态")
	@View(groups = {VO_DETAIL, VO_ADMIN})
	@View(groups = VO_EXPORT, field = @Field(label = "初审状态", order = 77))
	private ConfirmStatus preConfirmStatus;

	@Field(label = "初审通过时间")
	@View(groups = {VO_ADMIN})
	@Query({Predicate.GTE, Predicate.LT})
	@View(name = "preConfirmedTimeLabel", type = String.class, groups = {VO_ADMIN})
	@View(name = "preConfirmedTimeLabel", type = String.class, groups = VO_EXPORT, field = @Field(label = "初审通过时间", order = 78))
	private Date preConfirmedTime;

	@NotNull
	@Query(Predicate.EQ)
	@Field(label = "审核状态")
	@View(groups = {VO_LIST, VO_DETAIL, VO_ADMIN})
	@View(groups = VO_EXPORT, field = @Field(label = "审核状态", order = 79))
	private ConfirmStatus confirmStatus;

	@Field(label = "审核备注")
	@View(groups = {VO_DETAIL, VO_ADMIN})
	@View(groups = VO_EXPORT, field = @Field(label = "审核备注", order = 80))
	private String confirmRemark;

	@Field(label = "审核通过时间")
	@View(groups = {VO_DETAIL, VO_ADMIN, VO_EXPORT})
	@View(name = "confirmedTimeLabel", type = String.class, groups = {VO_ADMIN, VO_DETAIL})
	@View(name = "confirmedTimeLabel", type = String.class, groups = VO_EXPORT, field = @Field(label = "初审通过时间", order = 90))
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
