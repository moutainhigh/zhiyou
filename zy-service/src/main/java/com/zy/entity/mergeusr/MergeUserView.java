package com.zy.entity.mergeusr;

import com.zy.entity.usr.User;
import com.zy.entity.usr.User.UserRank;
import com.zy.entity.usr.User.UserType;
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
import org.hibernate.validator.constraints.Length;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

import static com.zy.entity.mergeusr.MergeUserView.VO_ADMIN;
import static com.zy.entity.usr.User.VO_ADMIN_SIMPLE;
import static io.gd.generator.api.query.Predicate.EQ;
import static io.gd.generator.api.query.Predicate.IN;

@Entity
@Table
@Getter
@Setter
@QueryModel
@Type(label = "用户视图")
@ViewObject(groups = {VO_ADMIN}

)
public class MergeUserView implements Serializable {

	public static final String VO_ADMIN = "MergeUserViewVo";

	@Id
	@Query(Predicate.IN)
	@Field(label = "id")
	@View(groups = {VO_ADMIN})
	private Long id;

	@Column(length = 11)
	@Query(Predicate.EQ)
	@Field(label = "手机号")
	@View(groups = {VO_ADMIN})
	private String phone;

	@Column(length = 60)
	@Field(label = "密码")
	private String password;

	@Column(length = 60)
	@Query(Predicate.LK)
	@Length(max = 60)
	@Field(label = "昵称")
	@View(groups = {VO_ADMIN})
	private String nickname;

	@Query(Predicate.EQ)
	@Field(label = "用户类型")
	@View(groups = {VO_ADMIN})
	private UserType userType;

	@Query(Predicate.EQ)
	@Field(label = "用户等级")
	@View(groups = {VO_ADMIN})
	@View(name = "userRankLabel", type = String.class, groups = {VO_ADMIN})
	private UserRank userRank;

	@Column(length = 11)
	@Field(label = "qq")
	@View(groups = {VO_ADMIN})
	private String qq;

	@Field(label = "头像")
	@View(name = "avatarThumbnail", type = String.class, groups = {VO_ADMIN})
	@Length(max = 250)
	private String avatar;

	@Query(Predicate.EQ)
	@Field(label = "是否冻结")
	@View(groups = {VO_ADMIN})
	private Boolean isFrozen;

	@Query({Predicate.GTE, Predicate.LT})
	@Field(label = "注册时间")
	@View(groups = {VO_ADMIN})
	private Date registerTime;

	@Field(label = "注册ip")
	@View(groups = {VO_ADMIN})
	private String registerIp;

	@Query({Predicate.EQ, Predicate.IN})
	@Field(label = "原始优检体系的parent")
	@AssociationView(name = "inviter", associationGroup = VO_ADMIN_SIMPLE, groups = {VO_ADMIN})
	private Long inviterId;

	@Query({Predicate.EQ, Predicate.IN, Predicate.NL})
	@Field(label = "推荐人id")
	@AssociationView(name = "parent", associationGroup = VO_ADMIN_SIMPLE, groups = {VO_ADMIN})
	private Long parentId;

	@Field(label = "remark")
	@View(groups = {VO_ADMIN})
	private String remark;

	@Field(label = "vipExpiredDate")
	private Date vipExpiredDate;

	@Column(length = 60)
	@Field(label = "微信openId")
	private String openId;

	@Column(length = 60)
	@Field(label = "微信unionId")
	private String unionId;

	@Field(label = "上次升级时间")
	private Date lastUpgradedTime;

	@Query(Predicate.EQ)
	@Field(label = "是否子系统")
	@View(groups = {VO_ADMIN})
	private Boolean isRoot;

	@Field(label = "子系统名称")
	@View(groups = {VO_ADMIN})
	private String rootName;

	@Query(Predicate.EQ)
	@Field(label = "是否总经理")
	@View(groups = {VO_ADMIN})
	private Boolean isBoss;

	@Query(Predicate.LK)
	@Field(label = "总经理团队名称")
	@View(groups = {VO_ADMIN})
	private String bossName;

	@Query(Predicate.EQ)
	@Field(label = "上级总经理id")
	@View(groups = {VO_ADMIN})
	@AssociationView(name = "boss", associationGroup = VO_ADMIN_SIMPLE, groups = {VO_ADMIN})
	private Long bossId;

	@Field(label = "授权码")
	@Column(length = 60, unique = true)
	private String code;

	@Field(label = "是否董事")
	@Query(Predicate.EQ)
	@View(groups = {VO_ADMIN})
	private Boolean isDirector;

	@Field(label = "是否荣誉董事")
	@Query(Predicate.EQ)
	@View(groups = {VO_ADMIN})
	private Boolean isHonorDirector;

	@Field(label = "是否股东")
	@Query(Predicate.EQ)
	@View(groups = {VO_ADMIN})
	private Boolean isShareholder;

	@Query(Predicate.EQ)
	@Field(label = "是否删除")
	@View(groups = {VO_ADMIN})
	private Boolean isDeleted;

	@Query(Predicate.EQ)
	@Field(label = "是否直升特级")
	@View(groups = {VO_ADMIN})
	private Boolean isToV4;

	@Column(length = 1)
	@Field(label = "是否有权限")
	private int viewflag;

	@Field(label = "最后一次登录时间")
	@View(groups = {VO_ADMIN})
	private Date lastloginTime;

	@Field(label = "所属大区")
	@View(groups = {VO_ADMIN})
	private Integer largearea;

	@Field(label = "设置大区备注")
	@View(groups = {VO_ADMIN})
	private String setlargearearemark;

	@Query(Predicate.EQ)
	@Field(label = "是否大区总裁")
	@View(groups = {VO_ADMIN})
	private Boolean isPresident;


	@Query(Predicate.EQ)
	@Field(label = "所属大区总裁id")
	@View(groups = {VO_ADMIN})
	private Long presidentId;


	@Query(Predicate.EQ)
	@Field(label = "1：大区董事长  2：大区副董事长")
	@View(groups = {VO_ADMIN})
	private Integer largeareaDirector;

	@Query({EQ,IN})
	@Field(label = "直属v4Id")
	@View
	@AssociationView(name = "v4Parent", groups = VO_ADMIN, associationGroup = User.VO_ADMIN_SIMPLE)
	private Long v4Id;
}
