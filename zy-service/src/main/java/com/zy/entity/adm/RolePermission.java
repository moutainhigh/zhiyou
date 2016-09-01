package com.gc.entity.adm;

import io.gd.generator.annotation.Field;
import io.gd.generator.annotation.Type;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "adm_role_permission")
@Getter
@Setter
@Type(label = "角色权限")
public class RolePermission implements Serializable {

	@Id
	@Field(label = "id")
	private Long id;

	@Field(label = "权限标示")
	private String permission;

	@Field(label = "权限名")
	private String permissionName;

	@Field(label = "角色id")
	private Long roleId;

}
