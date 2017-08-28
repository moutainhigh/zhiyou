package com.zy.model.query;

import java.util.Set;
import java.util.HashSet;
import java.io.Serializable;

import io.gd.generator.api.query.Direction;

import lombok.Getter;
import lombok.Setter;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.zy.entity.usr.User.UserType;
import java.util.Date;
import com.zy.entity.usr.User.UserRank;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserQueryModel implements Serializable {

	private Long[] idIN;

	private String phoneEQ;

	private String nicknameLK;

	private UserType userTypeEQ;

	private UserRank userRankEQ;

	private Boolean isFrozenEQ;

	private Date registerTimeGTE;

	private Date registerTimeLT;

	private Long inviterIdEQ;

	private Long[] inviterIdIN;

	private Long parentIdEQ;

	private Long[] parentIdIN;

	private Long parentIdNL;

	private Boolean isRootEQ;

	private Boolean isBossEQ;

	private String bossNameLK;

	private Long bossIdEQ;

	private Boolean isDirectorEQ;

	private Boolean isHonorDirectorEQ;

	private Boolean isShareholderEQ;

	private Boolean isDeletedEQ;

	private Boolean isToV4EQ;

	private String nameorPhone;

	private String remark;

	private Integer pageNumber;

	private Integer pageSize;

	private String orderBy;

	private Direction direction;

	public void setOrderBy(String orderBy) {
		if (orderBy != null && !fieldNames.contains(orderBy)) {
			throw new IllegalArgumentException("order by is invalid");
		}
		this.orderBy = orderBy;
	}

	public Long getOffset() {
		if (pageNumber == null || pageSize == null) {
			return null;
		}
		return ((long) pageNumber) * pageSize;
	}

	public String getOrderByAndDirection() {
		if (orderBy == null) {
			return null;
		}
		String orderByStr = camelToUnderline(orderBy);
		String directionStr = direction == null ? "desc" : direction.toString().toLowerCase();
		return orderByStr + " " + directionStr;
	}

	private String camelToUnderline(String param) {
		if (param == null || "".equals(param.trim())) {
			return "";
		}
		int len = param.length();
		StringBuilder sb = new StringBuilder(len);
		for (int i = 0; i < len; i++) {
			char c = param.charAt(i);
			if (Character.isUpperCase(c)) {
				sb.append("_");
				sb.append(Character.toLowerCase(c));
			} else {
				sb.append(c);
			}
		}
		return sb.toString();
	}

	private static Set<String> fieldNames = new HashSet<>();

	static {
		fieldNames.add("code");
		fieldNames.add("isRoot");
		fieldNames.add("openId");
		fieldNames.add("rootName");
		fieldNames.add("remark");
		fieldNames.add("isToV4");
		fieldNames.add("userRank");
		fieldNames.add("password");
		fieldNames.add("isDeleted");
		fieldNames.add("isBoss");
		fieldNames.add("isShareholder");
		fieldNames.add("isHonorDirector");
		fieldNames.add("nickname");
		fieldNames.add("vipExpiredDate");
		fieldNames.add("id");
		fieldNames.add("isFrozen");
		fieldNames.add("viewflag");
		fieldNames.add("lastloginTime");
		fieldNames.add("qq");
		fieldNames.add("registerIp");
		fieldNames.add("unionId");
		fieldNames.add("registerTime");
		fieldNames.add("bossName");
		fieldNames.add("bossId");
		fieldNames.add("lastUpgradedTime");
		fieldNames.add("avatar");
		fieldNames.add("parentId");
		fieldNames.add("phone");
		fieldNames.add("inviterId");
		fieldNames.add("isDirector");
		fieldNames.add("userType");
		fieldNames.add("lastloginTime");
		fieldNames.add("largearea");
	}

}