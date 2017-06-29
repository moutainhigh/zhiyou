package com.zy.entity.tour;

import com.zy.entity.usr.User;
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

import static com.zy.entity.cms.Article.VO_ADMIN;
import static com.zy.entity.cms.Article.VO_DETAIL;

@Entity
@Table(name = "ts_black_white")
@Getter
@Setter
@ViewObject(groups = {VO_ADMIN})
@QueryModel
@Type(label = "旅游")
public class BlackOrWhite implements Serializable {

	public static final String VO_ADMIN = "BlackOrWhiteAdminVo";

	@Id
	@Field(label = "id")
	@View
	private Long id;

	@NotNull
	@Field(label = "用户Id")
	@Query({Predicate.IN, Predicate.EQ})
	@View(groups = VO_ADMIN)
	@AssociationView(name = "User", associationGroup = User.VO_ADMIN_SIMPLE, groups = VO_ADMIN)
	private Long userId;

	@NotNull
	@Field(label = "人数")
	@View(groups = { VO_DETAIL})
	private Integer number;

	@NotNull
	@Field(label = "类型")
	@View(groups = { VO_DETAIL})
	private Integer type;


}