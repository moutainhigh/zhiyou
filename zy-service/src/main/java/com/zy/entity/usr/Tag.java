package com.gc.entity.usr;

import io.gd.generator.annotation.Field;
import io.gd.generator.annotation.Type;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

import org.hibernate.validator.constraints.NotBlank;

@Entity
@Table(name = "usr_tag")
@Getter
@Setter
@Type(label = "标签")
public class Tag implements Serializable {

	@Id
	@Field(label = "id")
	private Long id;

	@NotBlank
	@Field(label = "标签类型")
	private String tagType;

	@NotBlank
	@Column(length = 60, unique = true)
	@Field(label = "标签名")
	private String tagName;

}
