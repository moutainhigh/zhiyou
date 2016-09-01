package com.zy.entity.act;

import io.gd.generator.annotation.Field;
import io.gd.generator.annotation.Type;
import io.gd.generator.annotation.query.Query;
import io.gd.generator.annotation.query.QueryModel;
import io.gd.generator.api.query.Predicate;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "act_activity_collect", uniqueConstraints = @UniqueConstraint(columnNames = {"activityId", "userId"}))
@Getter
@Setter
@Type(label = "活动关注")
@QueryModel
public class ActivityCollect implements Serializable {

	@Id
	@Field(label = "id")
	private Long id;

	@NotNull
	@Field(label = "用户id")
	@Query({Predicate.IN, Predicate.EQ})
	private Long userId;

	@NotNull
	@Field(label = "活动id")
	@Query({Predicate.IN, Predicate.EQ})
	private Long activityId;

	@NotBlank
	@Field(label = "关注时间")
	@Query({Predicate.GTE, Predicate.LT})
	private Date collectedTime;

}
