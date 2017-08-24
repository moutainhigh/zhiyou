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
import static com.zy.entity.report.SalesVolume.*;


@Entity
@Table(name = "rpt_salesvolume")
@Getter
@Setter
@Type(label = "销量报表")
@QueryModel
@ViewObject(groups = {SalesVolume.VO_LIST}
)
public class SalesVolume implements Serializable {

	public static final String VO_LIST = "SalesVolumeListVo";

	@Id
	@Field(label = "id")
	@Query(Predicate.EQ)
	@View
	private Long id;

	@Field(label = "用户等级")
	@NotNull
	@Query(Predicate.EQ)
	@View
	private Integer userRank;

	@NotBlank
	@Field(label = "用户名")
	@Query(Predicate.LK)
	@View
	private String userName;

	@NotBlank
	@Field(label = "用户手机号")
	@Query(Predicate.LK)
	@View
	private String userPhone;

	@Field(label = "达成量")
	@NotNull
	@View
	private Integer amountReached;

	@Field(label = "目标量")
	@NotNull
	@View
	private Integer amountTarget;

	@Field(label = "达成率")
	@NotNull
	@View
	private Double achievement;

	@Field(label = "排名")
	@NotNull
	@View
	private Integer ranking;

	@Field(label = "上升或下降名次")
	@NotNull
	@View
	private Integer number;

	@Field(label = "1 上升 2 持平 3 下降")
	@NotNull
	@View
	private Integer type;

	@Field(label = "大区类型：1 东 2 南 3 西 4 北 5 中")
	@NotNull
	@View
	@Query(Predicate.EQ)
	private Integer areaType;

	@NotNull
	@Field(label = "插入时间")
	@Query({Predicate.GTE, Predicate.LT})
	@View(name = "createTimeLabel", type = String.class,groups = VO_LIST)
	private Date create_time;
}
