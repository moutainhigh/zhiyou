package com.zy.entity.usr;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotBlank;

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

@Entity
@Table(name = "usr_user")
@Getter
@Setter
@QueryModel
@Type(label = "用户")
@ViewObject(groups = {"UserListVo", "UserSimpleVo", "UserAdminVo", "UserAdminSimpleVo"})
public class User implements Serializable {

	@Type(label = "用户类型")
	public enum UserType {
		平台, 代理, 商家
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
	@View(groups = {"UserListVo", "UserAdminVo", "UserAdminSimpleVo"})
	private String phone;

	@Column(length = 60)
	@NotBlank
	@Field(label = "密码")
	private String password;

	@Column(length = 60)
	@NotBlank
	@StringBinder
	@Query(Predicate.LK)
	@Field(label = "昵称")
	@View
	private String nickname;

	@NotNull
	@Query(Predicate.EQ)
	@Field(label = "用户类型")
	@View(groups = {"UserListVo", "UserAdminVo", "UserAdminSimpleVo"})
	private UserType userType;

	@NotNull
	@Field(label = "用户等级")
	@View(groups = {"UserListVo", "UserAdminVo", "UserAdminSimpleVo"})
	private UserRank userRank;

	@Column(length = 11)
	@NotBlank
	@Pattern(regexp = "^[\\d]{5,11}$")
	@Field(label = "qq")
	@View(groups = {"UserAdminVo"})
	private String qq;

	@NotBlank
	@Field(label = "头像")
	@View(name = "avatarThumbnail", type = String.class)
	private String avatar;

	@NotNull
	@Field(label = "是否冻结")
	@View(groups = {"UserAdminVo"})
	private Boolean isFrozen;

	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	@Query({Predicate.GTE, Predicate.LT})
	@Field(label = "注册时间")
	@View(groups = {"UserAdminVo"})
	private Date registerTime;

	@NotNull
	@Field(label = "注册ip")
	@View(groups = {"UserAdminVo"})
	private String registerIp;

	@Query(Predicate.EQ)
	@Field(label = "邀请人id")
	@AssociationView(name = "inviter", associationGroup = "UserAdminSimpleVo", groups = {"UserAdminVo"})
	private Long inviterId;

	@NotNull
	@Field(label = "信息是否已完善")
	@View(groups = {"UserAdminVo"})
	private Boolean isInfoCompleted;

	@Field(label = "remark")
	@View(groups = {"UserAdminVo"})
	private String remark;

	@Temporal(TemporalType.DATE)
	@Field(label = "vipExpiredDate")
	private Date vipExpiredDate;

	@NotBlank
	@Column(unique = true, length = 20)
	@Field(label = "inviteCode")
	private String inviteCode;

}
