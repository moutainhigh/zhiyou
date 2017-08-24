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

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import static com.zy.entity.report.LargeArea.*;


@Entity
@Table(name = "sys_large_area")
@Getter
@Setter
@Type(label = "大区类型")
@QueryModel
@ViewObject(groups = {LargeArea.VO_LIST}
)
public class LargeArea implements Serializable {

	public static final String VO_LIST = "LargeAreaListVo";

	@Id
	@Field(label = "id")
	@Query(Predicate.EQ)
	@View
	private Long id;

	@Field(label = "用户id")
	@NotNull
	@Query(Predicate.EQ)
	@View
	private Long userId;


	@Field(label = "大区类型：1 东 2 南 3 西 4 北 5 中")
	@NotNull
	@Query(Predicate.EQ)
	@View
	private Integer type;

}
