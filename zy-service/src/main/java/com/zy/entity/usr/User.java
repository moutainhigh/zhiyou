package com.zy.entity.usr;

import com.zy.common.extend.StringBinder;
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
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.Date;

import static com.zy.entity.usr.User.*;
import static com.zy.entity.usr.User.VO_REPORT;

@Entity
@Table(name = "usr_user")
@Getter
@Setter
@QueryModel
@Type(label = "用户")
@ViewObject(groups = {VO_LIST, VO_SIMPLE, VO_ADMIN, VO_ADMIN_SIMPLE, VO_ADMIN_FULL, VO_REPORT},
	collectionViews = {
		@CollectionView(name = "userUpgrades", elementGroup = UserUpgrade.VO_ADMIN, groups = VO_ADMIN_FULL),
		@CollectionView(name = "teammates", elementGroup = VO_ADMIN_SIMPLE, groups = VO_ADMIN_FULL)
	},
	associationViews = {
		@AssociationView(name = "userInfo", associationGroup = UserInfo.VO_ADMIN, groups = VO_ADMIN_FULL)
	},
	views = {
		@View(name = "provinceId", type = Long.class, groups = VO_REPORT),
		@View(name = "cityId", type = Long.class, groups = VO_REPORT),
		@View(name = "districtId", type = Long.class, groups = VO_REPORT),
		@View(name = "v4UserId", type = Long.class, groups = VO_REPORT),
		@View(name = "v4UserNickname", type = String.class, groups = VO_REPORT),
		@View(name = "rootId", type = Long.class, groups = VO_REPORT),
		@View(name = "rootRootName", type = String.class, groups = VO_REPORT)
	}

)
public class User implements Serializable {

	public static final String VO_LIST = "UserListVo";
	public static final String VO_SIMPLE = "UserSimpleVo";
	public static final String VO_ADMIN = "UserAdminVo";
	public static final String VO_ADMIN_SIMPLE = "UserAdminSimpleVo";
	public static final String VO_ADMIN_FULL = "UserAdminFullVo";
	public static final String VO_REPORT = "UserReportVo";

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
	@View(groups = {VO_LIST, VO_SIMPLE, VO_ADMIN, VO_ADMIN_SIMPLE, VO_ADMIN_FULL, VO_REPORT})
	private Long id;

	@Column(length = 11, unique = true)
	@NotBlank
	@Pattern(regexp = "^1[\\d]{10}$")
	@StringBinder
	@Query(Predicate.EQ)
	@Field(label = "手机号")
	@View(groups = {VO_LIST, VO_ADMIN, VO_ADMIN_SIMPLE, VO_ADMIN_FULL, VO_REPORT})
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
	@View(groups = {VO_LIST, VO_SIMPLE, VO_ADMIN, VO_ADMIN_SIMPLE, VO_ADMIN_FULL, VO_REPORT})
	private String nickname;

	@NotNull
	@Query(Predicate.EQ)
	@Field(label = "用户类型")
	@View(groups = {VO_ADMIN, VO_ADMIN_SIMPLE, VO_ADMIN_FULL, VO_REPORT})
	private UserType userType;

	@NotNull
	@Query(Predicate.EQ)
	@Field(label = "用户等级")
	@View(groups = {VO_LIST, VO_ADMIN, VO_ADMIN_SIMPLE, VO_ADMIN_FULL, VO_REPORT})
	@View(name = "userRankLabel", type = String.class, groups = {VO_ADMIN, VO_ADMIN_SIMPLE, VO_ADMIN_FULL})
	private UserRank userRank;

	@Column(length = 11)
	@Pattern(regexp = "^[\\d]{5,11}$")
	@Field(label = "qq")
	@View(groups = {VO_ADMIN, VO_ADMIN_FULL})
	private String qq;

	@NotBlank
	@Field(label = "头像")
	@View(name = "avatarThumbnail", type = String.class, groups = {VO_LIST, VO_SIMPLE, VO_ADMIN, VO_ADMIN_SIMPLE, VO_ADMIN_FULL})
	@Length(max = 250)
	private String avatar;

	@NotNull
	@Query(Predicate.EQ)
	@Field(label = "是否冻结")
	@View(groups = {VO_ADMIN, VO_ADMIN_FULL, VO_REPORT})
	private Boolean isFrozen;

	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	@Query({Predicate.GTE, Predicate.LT})
	@Field(label = "注册时间")
	@View(groups = {VO_ADMIN, VO_ADMIN_FULL, VO_REPORT})
	private Date registerTime;

	@NotNull
	@Field(label = "注册ip")
	@View(groups = {VO_ADMIN, VO_ADMIN_FULL, VO_REPORT})
	private String registerIp;

	@Query({Predicate.EQ, Predicate.IN})
	@Field(label = "邀请人id", description = "此用户不是最终上下级关系")
	@AssociationView(name = "inviter", associationGroup = VO_ADMIN_SIMPLE, groups = {VO_ADMIN, VO_ADMIN_FULL})
	private Long inviterId;

	@Query({Predicate.EQ, Predicate.IN, Predicate.NL})
	@Field(label = "上级id")
	@AssociationView(name = "parent", associationGroup = VO_ADMIN_SIMPLE, groups = {VO_ADMIN, VO_ADMIN_FULL})
	@View(groups = {VO_REPORT})
	private Long parentId;

	@Field(label = "remark")
	@View(groups = {VO_ADMIN, VO_ADMIN_FULL})
	private String remark;

	@Temporal(TemporalType.DATE)
	@Field(label = "vipExpiredDate")
	private Date vipExpiredDate;

	@Column(length = 60, unique = true)
	@Field(label = "微信openId")
	private String openId;

	@Column(length = 60, unique = true)
	@Field(label = "微信unionId")
	private String unionId;

	@Field(label = "上次升级时间")
	private Date lastUpgradedTime;

	@Query(Predicate.EQ)
	@Field(label = "是否子系统")
	@View(groups = {VO_ADMIN, VO_ADMIN_FULL, VO_REPORT})
	private Boolean isRoot;

	@Field(label = "子系统名称")
	@View(groups = {VO_ADMIN, VO_ADMIN_FULL, VO_REPORT})
	private String rootName;

	@Field(label = "授权码")
	@Column(length = 60, unique = true)
	private String code;

}
