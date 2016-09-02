package com.zy.entity.fnc;

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

import com.zy.common.extend.StringBinder;
import com.zy.entity.sys.ConfirmStatus;

@Entity
@Table(name = "fnc_bank")
@Getter
@Setter
@QueryModel
@Type(label = "银行信息")
@ViewObject(groups = {"BankVo", "BankAdminVo"})
public class Bank implements Serializable {

	@Id
	@Field(label = "id")
	@View
	private Long id;

	@NotBlank
	@StringBinder
	@Query(Predicate.LK)
	@Field(label = "银行名称")
	@View
	private String name;

	@NotBlank
	@StringBinder
	@Field(label = "代码")
	@View
	private String code;

	@NotNull
	@Query(Predicate.EQ)
	@Field(label = "是否删除")
	private Boolean isDeleted;
	
}
