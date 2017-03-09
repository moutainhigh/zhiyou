package com.zy.model.query;

import com.zy.entity.act.ActivityApply.ActivityApplyStatus;
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
public class ActivityApplyQueryModel implements Serializable {

	private ActivityApplyStatus activityApplyStatusEQ;

	private Long[] userIdIN;

	private Long userIdEQ;

	private Long[] payerUserIdIN;

	private Long payerUserIdEQ;

	private Long[] activityIdIN;

	private Long activityIdEQ;

	private Date appliedTimeGTE;

	private Date appliedTimeLT;

	private Boolean isCancelledEQ;

	private Long[] inviterIdIN;

	private Long inviterIdEQ;

	private Boolean isSmsSentEQ;

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
		fieldNames.add("amount");
		fieldNames.add("isCancelled");
		fieldNames.add("appliedTime");
		fieldNames.add("inviterId");
		fieldNames.add("id");
		fieldNames.add("activityApplyStatus");
		fieldNames.add("payerUserId");
		fieldNames.add("userId");
		fieldNames.add("isSmsSent");
	}

}