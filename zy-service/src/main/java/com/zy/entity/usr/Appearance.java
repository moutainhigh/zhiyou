package com.gc.entity.usr;

import static io.gd.generator.api.query.Predicate.EQ;
import static io.gd.generator.api.query.Predicate.IN;
import io.gd.generator.annotation.Field;
import io.gd.generator.annotation.Type;
import io.gd.generator.annotation.query.Query;
import io.gd.generator.annotation.query.QueryModel;
import io.gd.generator.annotation.view.AssociationView;
import io.gd.generator.annotation.view.View;
import io.gd.generator.annotation.view.ViewObject;
import io.gd.generator.api.query.Predicate;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;

import com.zy.common.extend.StringBinder;
import com.gc.entity.sys.ConfirmStatus;

@Entity
@Table(name = "usr_appearance")
@Getter
@Setter
@QueryModel
@ViewObject(groups = {"AppearanceVo", "AppearanceAdminVo"})
@Type(label = "实名认证")
public class Appearance implements Serializable {

	@Id
	@Field(label = "实名认证id")
	@View
	private Long id;

	@NotNull
	@Query({EQ,IN})
	@Column(unique = true)
	@Field(label = "用户id")
	@View
	@AssociationView(name = "user", groups = "AppearanceAdminVo", associationGroup = "UserAdminSimpleVo")
	private Long userId;

	@NotBlank
	@Field(label = "真实姓名")
	@View
	private String realname;

	@NotBlank
	@Field(label = "身份证号")
	@View
	private String idCardNumber;

	@NotBlank
	@URL
	@StringBinder
	@Field(label = "图片1")
	@View
	@View(name = "image1Thumbnail")
	private String image1;
	
	@NotBlank
	@URL
	@StringBinder
	@Field(label = "图片2")
	@View
	@View(name = "image2Thumbnail")
	private String image2;

	@NotNull
	@Query(Predicate.EQ)
	@Field(label = "审核状态")
	@View
	private ConfirmStatus confirmStatus;

	@Field(label = "审核备注")
	@View
	private String confirmRemark;

	@NotNull
	@Field(label = "申请时间")
	@View(groups = "AppearanceAdminVo")
	private Date appliedTime;
	
	@Field(label = "审核通过时间")
	@View(groups = "AppearanceAdminVo")
	private Date confirmedTime;

}
