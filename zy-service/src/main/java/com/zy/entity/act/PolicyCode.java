package com.zy.entity.act;

import io.gd.generator.annotation.Field;
import io.gd.generator.annotation.Type;
import io.gd.generator.annotation.query.Query;
import io.gd.generator.annotation.query.QueryModel;
import io.gd.generator.annotation.view.View;
import io.gd.generator.annotation.view.ViewObject;
import io.gd.generator.api.query.Predicate;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "act_policy_code")
@Getter
@Setter
@Type(label = "保险单号")
@QueryModel
@ViewObject(groups = {PolicyCode.VO_ADMIN, PolicyCode.VO_EXPORT})
public class PolicyCode implements Serializable {

	public static final String VO_ADMIN = "PolicyCodeAdminVo";
	public static final String VO_EXPORT = "PolicyCodeExportVo";

	@Id
	@Field(label = "id")
	@View(groups = {VO_ADMIN})
	@Query({Predicate.EQ})
	private Long id;

	@NotBlank
	@Field(label = "编号")
	@Query({Predicate.LK, Predicate.EQ})
	@View(groups = {VO_ADMIN})
	@View(groups = VO_EXPORT, field = @Field(label = "编号", order = 10))
	@Column(length = 60, unique = true)
	private String code;

	@NotNull
	@Query({Predicate.GTE, Predicate.LT})
	@Field(label = "创建时间")
	@View(groups = {VO_ADMIN})
	@View(name = "createdTimeLabel", type = String.class, groups = {VO_ADMIN})
	private Date createdTime;

	@Field(label = "使用时间")
	@View(groups = {VO_ADMIN})
	@Query({Predicate.GTE, Predicate.LT})
	@View(name = "usedTimeLabel", type = String.class, groups = {VO_ADMIN})
	private Date usedTime;

	@Field(label = "是否使用")
	@View(groups = {VO_ADMIN})
	@NotNull
	@Query(Predicate.EQ)
	@View(name = "isUsedLabel", type = String.class, groups = VO_EXPORT, field = @Field(label = "是否使用", order = 15))
	private Boolean isUsed;

	@NotBlank
	@Field(label = "批次号")
	@Query({Predicate.EQ})
	@View(groups = {VO_ADMIN})
	@View(groups = VO_EXPORT, field = @Field(label = "批次号", order = 5))
	private String batchCode;

	@NotNull
	@Version
	@Field(label = "乐观锁")
	private Integer version;


	@Field(label = "旅游是否使用")
	@View(groups = {VO_ADMIN})
	@NotNull
	@Query(Predicate.EQ)
	@View(name = "tourUsedLabel", type = String.class, groups = VO_EXPORT, field = @Field(label = "旅游是否使用", order = 15))
	private Boolean tourUsed;
}
