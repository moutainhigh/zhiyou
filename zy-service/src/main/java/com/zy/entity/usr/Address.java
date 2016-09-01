package com.gc.entity.usr;

import io.gd.generator.annotation.Field;
import io.gd.generator.annotation.Type;
import io.gd.generator.annotation.query.Query;
import io.gd.generator.annotation.query.QueryModel;
import io.gd.generator.api.query.Predicate;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

import org.hibernate.validator.constraints.NotBlank;

import com.zy.common.extend.StringBinder;

@Entity
@Table(name = "usr_address")
@Getter
@Setter
@Type(label = "地址")
@QueryModel
public class Address implements Serializable {

	@Id
	@Field(label = "id")
	private Long id;

	@NotNull
	@Query(Predicate.EQ)
	@Field(label = "用户id")
	private Long userId;

	@NotNull
	@Field(label = "是否默认地址")
	private Boolean isDefault;

	@NotNull
	@Field(label = "区域id")
	private Long areaId;

	@NotBlank
	@StringBinder
	@Query(Predicate.LK)
	@Field(label = "姓名")
	private String realname;

	@NotBlank
	@StringBinder
	@Field(label = "电话")
	private String phone;

	@NotBlank
	@Field(label = "省份")
	private String province;

	@NotBlank
	@Field(label = "城市")
	private String city;

	@NotBlank
	@Field(label = "地区")
	private String district;

	@NotBlank
	@StringBinder
	@Field(label = "详细地址")
	private String address;

	@NotNull
	@Query(Predicate.EQ)
	@Field(label = "是否删除")
	private Boolean isDeleted;

}
