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
@Table(name = "adm_admin_role")
@Getter
@Setter
@Type(label = "管理员角色")
public class AdminRole implements Serializable {

	@Id
	@Field(label = "id")
	private Long id;

	@Field(label = "管理员id")
	private Long adminId;

	@Field(label = "角色id")
	private Long roleId;

}
