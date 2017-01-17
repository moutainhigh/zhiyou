package com.zy.entity.sys;

import io.gd.generator.annotation.Field;
import io.gd.generator.annotation.Type;
import io.gd.generator.annotation.query.Query;
import io.gd.generator.annotation.query.QueryModel;
import io.gd.generator.api.query.Predicate;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "sys_area")
@Getter
@Setter
@Type(label = "地区")
@QueryModel
public class Area implements Serializable {

	@Type(label = "地区类型")
	public enum AreaType {
		省, 市, 区
	}

	@Id
	@Query(Predicate.IN)
	@Field(label = "id")
	private Long id;

	@Query(Predicate.EQ)
	@Field(label = "地区类型")
	private AreaType areaType;

	@Query({Predicate.EQ, Predicate.IN})
	@Field(label = "父节点id")
	private Long parentId;

	@Field(label = "名称")
	private String name;

	@Field(label = "排序")
	private Integer orderNumber;

	@Field(label = "编码")
	private Integer code;

}
