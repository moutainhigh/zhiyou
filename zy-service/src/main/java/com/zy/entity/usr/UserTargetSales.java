package com.zy.entity.usr;

import io.gd.generator.annotation.Field;
import io.gd.generator.annotation.Type;
import io.gd.generator.annotation.query.Query;
import io.gd.generator.annotation.query.QueryModel;
import io.gd.generator.annotation.view.AssociationView;
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
import java.util.Date;

import static com.zy.entity.usr.UserTargetSales.VO;
import static io.gd.generator.api.query.Predicate.EQ;

@Entity
@Table(name = "usr_user_target_sales")
@Getter
@Setter
@QueryModel
@ViewObject(groups = {VO})
@Type(label = "用户目标销量")
public class UserTargetSales implements Serializable {

	public static final String VO = "UserTargetSalesVo";

	@Id
	@Field(label = "id")
	@View
	private Long id;

	@NotNull
	@Query(EQ)
	@Field(label = "用户id")
	@View
	@AssociationView(name = "user", groups = VO, associationGroup = User.VO_ADMIN)
	private Long userId;


	@Field(label = "目标销量")
	@View
	private Integer targetCount;

	@Field(label = "年份")
	@View
	@Query(Predicate.EQ)
	private Integer year;

	@Field(label = "月份")
	@View
	@Query(Predicate.EQ)
	private Integer month;

	@Field(label = "创建时间")
	@View
	private Date createTime;


}
