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
public class TeamReportNewQueryModel implements Serializable {

	private Long userIdEQ;

	private String userNameLK;

	private String districtNameLK;

	private Long districtIdEQ;

	private Integer extraNumberEQ;

	private Integer newextraNumberEQ;

	private Integer sleepextraNumberEQ;

	private Double newextraRateEQ;

	private Integer provinceNumberEQ;

	private Integer newprovinceNumberEQ;

	private Double newprovinceRateEQ;

	private Integer rankingEQ;

	private Integer yearEQ;

	private Integer monthEQ;

	private String phoneEQ;

	private Date createDateEQ;

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
		fieldNames.add("newprovinceRate");
		fieldNames.add("districtName");
		fieldNames.add("newextraNumber");
		fieldNames.add("year");
		fieldNames.add("newextraRate");
		fieldNames.add("userName");
		fieldNames.add("userId");
		fieldNames.add("newprovinceNumber");
		fieldNames.add("districtId");
		fieldNames.add("extraNumber");
		fieldNames.add("month");
		fieldNames.add("phone");
		fieldNames.add("provinceNumber");
		fieldNames.add("ranking");
		fieldNames.add("id");
		fieldNames.add("createDate");
		fieldNames.add("sleepextraNumberEQ");
	}

}