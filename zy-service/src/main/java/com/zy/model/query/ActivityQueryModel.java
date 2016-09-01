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
import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActivityQueryModel implements Serializable {

	private String titleLK;

	private Long areaIdEQ;

	private Long[] areaIdIN;

	private Date applyDeadlineGTE;

	private Date applyDeadlineLT;

	private Date startTimeGTE;

	private Date startTimeLT;

	private Boolean isReleasedEQ;

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
		fieldNames.add("image");
		fieldNames.add("address");
		fieldNames.add("appliedCount");
		fieldNames.add("latitude");
		fieldNames.add("title");
		fieldNames.add("version");
		fieldNames.add("signedInCount");
		fieldNames.add("areaId");
		fieldNames.add("viewedCount");
		fieldNames.add("applyDeadline");
		fieldNames.add("startTime");
		fieldNames.add("id");
		fieldNames.add("detail");
		fieldNames.add("endTime");
		fieldNames.add("isReleased");
		fieldNames.add("longitude");
		fieldNames.add("collectedCount");
	}

}