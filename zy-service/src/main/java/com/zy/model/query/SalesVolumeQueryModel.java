package com.zy.model.query;

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
public class SalesVolumeQueryModel implements Serializable {

	private Long idEQ;

	private Integer userRankEQ;

	private String userNameLK;

	private String userPhoneLK;

	private Integer isBoss;

	private Integer areaTypeEQ;

	private Date createTime;

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
		fieldNames.add("number");
		fieldNames.add("createTime");
		fieldNames.add("achievement");
		fieldNames.add("userPhone");
		fieldNames.add("areaType");
		fieldNames.add("ranking");
		fieldNames.add("id");
		fieldNames.add("userRank");
		fieldNames.add("amountReached");
		fieldNames.add("userName");
		fieldNames.add("type");
		fieldNames.add("amountTarget");
	}

}