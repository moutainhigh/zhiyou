package com.zy.model.query;

import com.zy.entity.act.ActivityTeamApply.PaidStatus;
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
public class ActivityTeamApplyQueryModel implements Serializable {

	private PaidStatus paidStatus;

	private Long id;

	private Long[] buyerIdIN;

	private Long buyerId;

	private Long[] activityIdIN;

	private Long activityId;

	private Date createTimeGTE;

	private Date createTimeLT;

	private Date paidTimeGTE;

	private Date paidTimeLT;

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
		fieldNames.add("activityId");
		fieldNames.add("count");
		fieldNames.add("amount");
		fieldNames.add("createTime");
		fieldNames.add("paidTime");
		fieldNames.add("id");
		fieldNames.add("paidStatus");
		fieldNames.add("buyerId");
	}

}