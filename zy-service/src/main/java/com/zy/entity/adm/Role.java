package com.gc.entity.adm;

import static io.gd.generator.api.query.Predicate.LK;
import io.gd.generator.annotation.Field;
import io.gd.generator.annotation.Type;
import io.gd.generator.annotation.query.Query;
import io.gd.generator.annotation.query.QueryModel;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

import org.hibernate.validator.constraints.NotBlank;

@Entity
@Table(name = "adm_role")
@Getter
@Setter
@QueryModel
@Type(label = "角色")
public class Role implements Serializable {

	@Id
	@Field(label = "id")
	private Long id;

	@NotBlank
	@Field(label = "角色名称")
	@Query(LK)
	private String name;

}
