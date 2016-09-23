package com.zy.entity.fnc;

import com.zy.common.extend.StringBinder;
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

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

import static com.zy.entity.fnc.Bank.VO;
import static com.zy.entity.fnc.Bank.VO_ADMIN;

@Entity
@Table(name = "fnc_bank")
@Getter
@Setter
@QueryModel
@Type(label = "银行信息")
@ViewObject(groups = {VO, VO_ADMIN})
public class Bank implements Serializable {

	public static final String VO_ADMIN = "BankAdminVo";
	public static final String VO = "BankVo";

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
	@View(groups = VO_ADMIN)
	private Boolean isDeleted;
	
	@Field(label = "排序")
	@View(groups = VO_ADMIN)
	@NotNull
	private Integer orderNumber;
}
