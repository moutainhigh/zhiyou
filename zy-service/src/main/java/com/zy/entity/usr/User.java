package com.zy.entity.usr;

import com.zy.common.extend.StringBinder;
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
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.Date;

import static com.zy.entity.usr.User.*;

@Entity
@Table(name = "usr_user")
@Getter
@Setter
@QueryModel
@Type(label = "用户")
@ViewObject(groups = {VO_LIST, VO_SIMPLE, VO_ADMIN, VO_ADMIN_SIMPLE})
public class User implements Serializable {

	public static final String VO_ADMIN = "UserAdminVo";
	public static final String VO_LIST = "UserListVo";
	public static final String VO_SIMPLE = "UserSimpleVo";
	public static final String VO_ADMIN_SIMPLE = "UserAdminSimpleVo";

	@Type(label = "用户类型")
	public enum UserType {
		平台, 代理
	}

	@Type(label = "用户等级")
	public enum UserRank {
		V0(0), V1(1), V2(2), V3(3), V4(4);

		UserRank(int level) {
			this.level = level;
		}

		private final int level;

		public int getLevel() {
			return level;
		}
	}

	@Id
	@Query(Predicate.IN)
	@Field(label = "id")
	@View
	private Long id;

	@Column(length = 11, unique = true)
	@NotBlank
	@Pattern(regexp = "^1[\\d]{10}$")
	@StringBinder
	@Query(Predicate.EQ)
	@Field(label = "手机号")
	@View(groups = {VO_LIST, VO_ADMIN, VO_ADMIN_SIMPLE})
	private String phone;

	@Column(length = 60)
	@Field(label = "密码")
	private String password;

	@Column(length = 60)
	@NotBlank
	@StringBinder
	@Query(Predicate.LK)
	@Length(max = 60)
	@Field(label = "昵称")
	@View
	private String nickname;

	@NotNull
	@Query(Predicate.EQ)
	@Field(label = "用户类型")
	@View(groups = {VO_ADMIN, VO_ADMIN_SIMPLE})
	private UserType userType;

	@NotNull
	@Query(Predicate.EQ)
	@Field(label = "用户等级")
	@View(groups = {VO_LIST, VO_ADMIN, VO_ADMIN_SIMPLE})
	@View(name = "userRankLabel", type = String.class, groups = {VO_ADMIN, VO_ADMIN_SIMPLE})
	private UserRank userRank;

	@Column(length = 11)
	@Pattern(regexp = "^[\\d]{5,11}$")
	@Field(label = "qq")
	@View(groups = {VO_ADMIN})
	private String qq;

	@NotBlank
	@Field(label = "头像")
	@View(name = "avatarThumbnail", type = String.class)
	@Length(max = 250)
	private String avatar;

	@NotNull
	@Field(label = "是否冻结")
	@View(groups = {VO_ADMIN})
	private Boolean isFrozen;

	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	@Query({Predicate.GTE, Predicate.LT})
	@Field(label = "注册时间")
	@View(groups = {VO_ADMIN})
	private Date registerTime;

	@NotNull
	@Field(label = "注册ip")
	@View(groups = {"UserAdminVo"})
	private String registerIp;

	@Query({Predicate.EQ, Predicate.IN})
	@Field(label = "邀请人id", description = "此用户不是最终上下级关系")
	@AssociationView(name = "inviter", associationGroup = VO_ADMIN_SIMPLE, groups = {VO_ADMIN})
	private Long inviterId;

	@Query({Predicate.EQ, Predicate.IN})
	@Field(label = "上级id")
	@AssociationView(name = "parent", associationGroup = VO_ADMIN_SIMPLE, groups = {VO_ADMIN})
	private Long parentId;

	@Field(label = "remark")
	@View(groups = {VO_ADMIN})
	private String remark;

	@Temporal(TemporalType.DATE)
	@Field(label = "vipExpiredDate")
	private Date vipExpiredDate;

	@Column(length = 60, unique = true)
	@Field(label = "微信openId")
	private String openId;

	@Field(label = "上次升级时间")
	private Date lastUpgradedTime;

}
