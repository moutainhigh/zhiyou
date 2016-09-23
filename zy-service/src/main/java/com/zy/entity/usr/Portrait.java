package com.zy.entity.usr;

import io.gd.generator.annotation.Field;
import io.gd.generator.annotation.Type;
import io.gd.generator.annotation.query.Query;
import io.gd.generator.annotation.query.QueryModel;
import io.gd.generator.annotation.view.AssociationView;
import io.gd.generator.annotation.view.CollectionView;
import io.gd.generator.annotation.view.View;
import io.gd.generator.annotation.view.ViewObject;
import io.gd.generator.api.query.Predicate;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

import static com.zy.entity.usr.Portrait.VO;
import static com.zy.entity.usr.Portrait.VO_ADMIN;

@Entity
@Table(name = "usr_portrait")
@Getter
@Setter
@QueryModel
@Type(label = "用户画像")
@ViewObject(groups = {VO, VO_ADMIN})
public class Portrait implements Serializable {

	public static final String VO = "PortraitVo";
	public static final String VO_ADMIN = "PortraitAdminVo";

	@Type(label = "性别")
	public enum Gender {
		男, 女;

		public static Gender valueOf(int ordinal) {
			if (ordinal < 0 || ordinal >= values().length) {
				throw new IndexOutOfBoundsException("Invalid ordinal");
			}
			return values()[ordinal];
		}

	}

	@Type(label = "收入水平")
	public enum ConsumptionLevel {
		C2000("2000元以下"), C2001_5000("2001元 ~ 5000元"), C5001_10000("5001元 ~ 10000元"), C10001_20000("10001元 ~ 20000元"), C20000("20000元以上");

		@Field(label = "别名")
		private final String alias;

		ConsumptionLevel(String alias) {
			this.alias = alias;
		}

		public String getAlias() {
			return alias;
		}

		public static ConsumptionLevel valueOf(int ordinal) {
			if (ordinal < 0 || ordinal >= values().length) {
				throw new IndexOutOfBoundsException("Invalid ordinal");
			}
			return values()[ordinal];
		}
	}

	@Type(label = "年龄段")
	public enum AgeArea {
		AGE16("16岁以下"), AGE16_25("16岁-25岁"), AGE26_35("26岁-35岁"), AGE36_45("36岁-45岁"), AGE46_55("46岁-55岁"), AGE55("55岁以上");

		@Field(label = "别名")
		private final String alias;

		AgeArea(String alias) {
			this.alias = alias;
		}

		public String getAlias() {
			return alias;
		}

		public static AgeArea valueOf(int ordinal) {
			if (ordinal < 0 || ordinal >= values().length) {
				throw new IndexOutOfBoundsException("Invalid ordinal");
			}
			return values()[ordinal];
		}
	}

	@Id
	@Field(label = "id")
	@View(groups = VO_ADMIN)
	private Long id;

	@NotNull
	@Column(unique = true)
	@Query({Predicate.EQ, Predicate.IN})
	@Field(label = "用户id")
	@View
	@AssociationView(name = "user", groups = VO_ADMIN, associationGroup = User.VO_ADMIN_SIMPLE)
	private Long userId;

	@NotNull
	@Field(label = "性别")
	@View
	private Gender gender;

	@NotNull
	@Temporal(TemporalType.DATE)
	@Field(label = "生日")
	@View(name = "birthdayLabel", type = String.class)
	private Date birthday;

	@NotNull
	@Field(label = "职业")
	@View(name = "jobName", type = String.class)
	@View(groups = "PortraitVo")
	private Long jobId;
	
	@NotNull
	@Field(label = "所在地")
	@View(name = "province", type = String.class)
	@View(name = "city", type = String.class)
	@View(name = "district", type = String.class)
	@View(groups = {VO_ADMIN, VO})
	private Long areaId;

	@Field(label = "家乡所在地")
	private Long hometownAreaId;

	@Field(label = "收入水平")
	private ConsumptionLevel consumptionLevel;

	@Field(label = "标签")
	@CollectionView(name = "tagNames", elementType = String.class)
	@View(groups = VO)
	private String tagIds;

}
