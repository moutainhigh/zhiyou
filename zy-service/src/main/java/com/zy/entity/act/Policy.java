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

@Entity
@Table(name = "act_policy")
@Getter
@Setter
@Type(label = "保险单")
@QueryModel
@ViewObject(groups = {Policy.VO_LIST, Policy.VO_DETAIL, Policy.VO_ADMIN})
public class Policy implements Serializable {

	public static final String VO_ADMIN = "PolicyAdminVo";
	public static final String VO_LIST = "PolicyListVo";
	public static final String VO_DETAIL = "PolicyDetailVo";

	@Id
	@Field(label = "id")
	@View(groups = {VO_LIST, VO_DETAIL, VO_ADMIN})
	@Query({Predicate.EQ})
	private Long id;

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
	private String realname;

	//@NotNull
	@Field(label = "出生年月日")
	@View(groups = {VO_LIST, VO_DETAIL, VO_ADMIN})
	@Temporal(TemporalType.DATE)
	private Date birthday;
	
	//@NotNull
	@Field(label = "性别")
	@View(groups = {VO_LIST, VO_DETAIL, VO_ADMIN})
	private Gender gender;
	
	@Column(length = 60)
	//@NotBlank
	@Pattern(regexp = "^1[\\d]{10}$")
	@StringBinder
	@Query(Predicate.EQ)
	@Field(label = "手机号")
	@View(groups = {VO_LIST, VO_DETAIL, VO_ADMIN})
	private String phone;

	@NotNull
	@Field(label = "编号")
	@Query({Predicate.LK, Predicate.EQ})
	@View(groups = {VO_ADMIN})
	@Column(length = 60, unique = true)
	private String code;

	@NotBlank
	@Field(label = "身份证号")
	@View
	@Query(Predicate.LK)
	private String idCardNumber;

	@URL
	@StringBinder
	@Field(label = "图片1")
	@View
	@View(name = "image1Thumbnail")
	private String image1;

	@URL
	@StringBinder
	@Field(label = "图片2")
	@View
	@View(name = "image2Thumbnail")
	private String image2;

	@NotNull
	@Version
	@Field(label = "乐观锁")
	private Integer version;
	
}
