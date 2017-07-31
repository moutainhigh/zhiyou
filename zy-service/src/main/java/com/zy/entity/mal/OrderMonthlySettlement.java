package com.zy.entity.mal;

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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "mal_order_monthly_settlement")
@Getter
@Setter
@QueryModel
@ViewObject(groups = {OrderMonthlySettlement.VO_ADMIN})
@Type(label = "订单结算记录")
public class OrderMonthlySettlement implements Serializable {
	
	public static final String VO_ADMIN = "OrderMonthlySettlementAdminVo";

	@Id
	@Field(label = "id")
	@View
	private Long id;

	@NotBlank
	@Column(length = 60, unique = true)
	@Query(Predicate.LK)
	@Field(label = "年月", description = "格式必须为yyyy-MM")
	@View
	private String yearAndMonth;

	@NotNull
	@View(name = "settledUpTimeLabel", type = String.class)
	@Field(label = "结算时间")
	@Query({Predicate.LT, Predicate.GTE})
	private Date settledUpTime;

	@NotNull
	@Field(label = "月结类型")
	@Query({Predicate.EQ})
	private String settlementType;


}
