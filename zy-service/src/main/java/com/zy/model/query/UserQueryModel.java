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

	private Date registerTimeGTE;

	private Date registerTimeLT;

	private Long inviterIdEQ;

	private Long parentIdEQ;

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
		fieldNames.add("qq");
		fieldNames.add("registerIp");
		fieldNames.add("registerTime");
		fieldNames.add("remark");
		fieldNames.add("userRank");
		fieldNames.add("avatar");
		fieldNames.add("parentId");
		fieldNames.add("isInfoCompleted");
		fieldNames.add("password");
		fieldNames.add("phone");
		fieldNames.add("inviterId");
		fieldNames.add("inviteCode");
		fieldNames.add("nickname");
		fieldNames.add("vipExpiredDate");
		fieldNames.add("id");
		fieldNames.add("userType");
		fieldNames.add("isFrozen");
	}

}