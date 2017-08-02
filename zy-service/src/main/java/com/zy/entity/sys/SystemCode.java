package com.zy.entity.sys;

import com.zy.entity.usr.User;
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
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

import static com.zy.entity.cms.Article.VO_ADMIN;
import static com.zy.entity.cms.Article.VO_DETAIL;

@Entity
@Table(name = "ts_system_code")
@Getter
@Setter
@ViewObject(groups = {VO_ADMIN})
@QueryModel
@Type(label = "系统数字")
public class SystemCode implements Serializable {

	public static final String VO_ADMIN = "BlackOrWhiteAdminVo";

	@Id
	@Field(label = "id")
	@View
	private Long id;

	@NotBlank
	@Field(label = "系统类型")
	@Query(Predicate.LK)
	@Length(max = 60)
	@View(groups = VO_ADMIN)
	private String systemType;

	@NotBlank
	@Field(label = "名称")
	@Query(Predicate.LK)
	@Length(max = 60)
	@View(groups = VO_ADMIN)
	private String systemName;

	@NotBlank
	@Field(label = "默认值")
	@Length(max = 60)
	@View(groups = VO_ADMIN)
	private String systemValue;

	@NotBlank
	@Field(label = "描述")
	@Length(max = 60)
	@View(groups = VO_ADMIN)
	private String systemDesc;

	@NotNull
	@Field(label = "是否删除,0不删除,1删除")
	@View(groups = VO_ADMIN)
	private Integer systemFlag;

	@NotNull
	@Field(label = "创建时间")
	@View(groups = VO_ADMIN)
	@View(name = "createTimeLabel", type = String.class, groups = {VO_ADMIN})
	private Date createDate;

	@NotNull
	@Field(label = "创建人id")
	@Query({Predicate.IN, Predicate.EQ})
	@View(groups = VO_ADMIN)
	@AssociationView(name = "createUser", associationGroup = User.VO_ADMIN_SIMPLE, groups = VO_ADMIN)
	private Long createBy;

	@Field(label = "修改时间")
	@View(groups = VO_ADMIN)
	@View(name = "updateTimeLabel", type = String.class, groups = {VO_ADMIN})
	private Date updateDate;

	@Field(label = "修改人id")
	@Query({Predicate.IN, Predicate.EQ})
	@View(groups = VO_ADMIN)
	@AssociationView(name = "updateUser", associationGroup = User.VO_ADMIN_SIMPLE, groups = VO_ADMIN)
	private Long updateBy;

}