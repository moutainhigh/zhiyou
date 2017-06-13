package com.zy.model.query;

import com.zy.entity.usr.User.UserRank;
import com.zy.entity.usr.User.UserType;
import io.gd.generator.api.query.Direction;
import lombok.*;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserlongQueryModel implements Serializable {

	private Long[] idIN;

	private String phoneEQ;

	private String nicknameLK;

	private UserRank userRankEQ;

	private Date registerTimeGTE;

	private Date registerTimeLT;

	private Long inviterIdEQ;

	private Long[] inviterIdIN;

	private Long parentIdEQ;

	private Long[] parentIdIN;

	private Long parentIdNL;

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

	/*static {
		fieldNames.add("code");
		fieldNames.add("isRoot");
		fieldNames.add("openId");
		fieldNames.add("rootName");
		fieldNames.add("remark");
		fieldNames.add("userRank");
		fieldNames.add("password");
		fieldNames.add("isBoss");
		fieldNames.add("isShareholder");
		fieldNames.add("nickname");
		fieldNames.add("vipExpiredDate");
		fieldNames.add("id");
		fieldNames.add("isFrozen");
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
	}*/

}