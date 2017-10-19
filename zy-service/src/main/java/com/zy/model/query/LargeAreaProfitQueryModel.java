package com.zy.model.query;

import io.gd.generator.api.query.Direction;
import lombok.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LargeAreaProfitQueryModel implements Serializable {

	private Long idEQ;

	private Long userIdEQ;

	private String largeAreaNameEQ;

	private Integer largeAreaValueEQ;

	private Integer yearEQ;

	private Integer monthEQ;

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
		fieldNames.add("largeAreaValue");
		fieldNames.add("month");
		fieldNames.add("relativeRate");
		fieldNames.add("year");
		fieldNames.add("createTime");
		fieldNames.add("id");
		fieldNames.add("userId");
		fieldNames.add("largeAreaName");
		fieldNames.add("targetCount");
		fieldNames.add("sales");
		fieldNames.add("sameRate");
	}

}