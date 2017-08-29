package com.zy.entity.report;

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
import java.util.Date;


@Entity
@Table(name = "rpt_amount_reached")
@Getter
@Setter
@Type(label = "销量目标表")
@QueryModel
@ViewObject(groups = {}
)
public class AmountReached implements Serializable {

//	public static final String VO_LIST = "SalesVolumeListVo";

	@Id
	@Field(label = "id")
	@Query(Predicate.EQ)
	@View
	private Long id;

	@NotBlank
	@Field(label = "用户手机号")
	@Query(Predicate.LK)
	@View
	private String userPhone;

	@Field(label = "目标量")
	@NotNull
	@View
	private Long amountTarget;

	@NotNull
	@Field(label = "插入时间")
	@View(name = "createTimeLabel", type = String.class)
	private Date createTime;
}
