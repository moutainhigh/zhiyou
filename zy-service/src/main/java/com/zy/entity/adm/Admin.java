package com.zy.entity.adm;

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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "adm_admin")
@Getter
@Setter
@QueryModel
@Type(label = "管理员")
@ViewObject(groups = "AdminAdminVo")
public class Admin implements Serializable {

	@Id
	@Field(label = "id")
	@View
	private Long id;

	@Query(Predicate.IN)
	@Field(label = "用户id")
	@Column(unique = true)
	@View
	@AssociationView(name = "user", associationGroup = "UserAdminSimpleVo", groups = {"AdminAdminVo"})
	private Long userId;

	@Field(label = "角色名", description = "角色名冗余")
	@View
	private String roleNames;

	@Field(label = "站点id", description = "暂不使用")
	@View
	private Long siteId;

}
