package com.zy.entity.act;

import com.zy.common.extend.StringBinder;
import com.zy.entity.usr.User;
import com.zy.entity.usr.UserInfo.Gender;
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
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.Date;

import static io.gd.generator.api.query.Predicate.GTE;
import static io.gd.generator.api.query.Predicate.LT;

@Entity
@Table(name = "act_policy")
@Getter
@Setter
@Type(label = "保险单")
@QueryModel
@ViewObject(groups = {Policy.VO_LIST, Policy.VO_DETAIL, Policy.VO_ADMIN, Policy.VO_EXPORT})
public class Policy implements Serializable {

	public static final String VO_ADMIN = "PolicyAdminVo";
	public static final String VO_LIST = "PolicyListVo";
	public static final String VO_DETAIL = "PolicyDetailVo";
	public static final String VO_EXPORT = "PolicyExportVo";

	@Type(label = "保险进度状态")
	public enum PolicyStatus {
		审核中, 已生效, 未通过, 已到期
	}

	@Id
	@Field(label = "id")
	@View(groups = {VO_LIST, VO_DETAIL, VO_ADMIN})
	@Query({Predicate.EQ})
	@View(groups = VO_EXPORT, field = @Field(label = "序号", order = 10))
	private Long id;

	@NotNull
	@Query({Predicate.EQ, Predicate.IN})
	@Field(label = "保险进度状态")
	@View
	@View(name = "policyStatusStyle", type = String.class, groups = {VO_ADMIN})
	private Policy.PolicyStatus policyStatus;

	@NotNull
	@Query(Predicate.EQ)
	@Field(label = "检测报告id")
	@View(groups = {VO_LIST, VO_DETAIL, VO_ADMIN})
	private Long reportId;
	
	@NotNull
	@Field(label = "用户id")
	@Query({Predicate.EQ, Predicate.IN})
	@View(groups = {VO_LIST, VO_DETAIL, VO_ADMIN})
	@AssociationView(name = "user", groups = VO_ADMIN, associationGroup = User.VO_ADMIN_SIMPLE)
	private Long userId;

	@NotNull
	@Field(label = "姓名")
	@Query({Predicate.LK, Predicate.EQ})
	@View(groups = {VO_LIST, VO_DETAIL, VO_ADMIN})
	@View(groups = VO_EXPORT, field = @Field(label = "姓名", order = 20))
	private String realname;

	//@NotNull
	@Field(label = "出生年月日")
	@View(groups = {VO_LIST, VO_DETAIL, VO_ADMIN})
	@Temporal(TemporalType.DATE)
	@View(name = "birthdayLabel", type = String.class, groups = VO_EXPORT, field = @Field(label = "出生年月", order = 50))
	private Date birthday;
	
	//@NotNull
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
	@View(groups = VO_EXPORT, field = @Field(label = "手机号码", order = 60))
	private String phone;

	@NotNull
	@Field(label = "编号")
	@Query({Predicate.LK, Predicate.EQ})
	@View(groups = {VO_LIST, VO_DETAIL, VO_ADMIN})
	@Column(length = 60, unique = true)
	private String code;

	@NotBlank
	@Field(label = "身份证号")
	@View(groups = {VO_LIST, VO_DETAIL, VO_ADMIN})
	@Query(Predicate.LK)
	@View(groups = VO_EXPORT, field = @Field(label = "证件号码", order = 30))
	private String idCardNumber;

	@URL
	@StringBinder
	@Field(label = "图片1", description = "身份证正面照")
	@View(name = "image1Thumbnail", groups = {VO_LIST, VO_DETAIL, VO_ADMIN})
	@View
	private String image1;

	@URL
	@StringBinder
	@Field(label = "图片2")
	private String image2;

	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	@Query({GTE,LT})
	@Field(label = "创建时间")
	@View(name = "createdTimeLabel", type = String.class, groups = {VO_ADMIN})
	private Date createdTime;

	@Temporal(TemporalType.TIMESTAMP)
	@Query({GTE,LT})
	@Field(label = "有效时间起")
	@View(name = "validTimeBeginLabel", type = String.class, groups = {VO_ADMIN})
	private Date validTimeBegin;

	@Temporal(TemporalType.TIMESTAMP)
	@Query({GTE,LT})
	@Field(label = "有效时间止")
	@View(name = "validTimeEndLabel", type = String.class, groups = {VO_ADMIN})
	private Date validTimeEnd;

	@NotNull
	@Version
	@Field(label = "乐观锁")
	private Integer version;

	@NotNull
	@Query({Predicate.GTE, Predicate.LT})
	private Date updateTime;

	@NotNull
	@Query({Predicate.GTE, Predicate.LT})
	private Long updateId;
	
}
