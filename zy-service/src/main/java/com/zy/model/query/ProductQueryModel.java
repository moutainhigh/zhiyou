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
public class ProductQueryModel implements Serializable {

	private Long[] idIN;

	private String titleLK;

	private Boolean isOnEQ;

	private Date createdTimeGTE;

	private Date createdTimeLT;

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
		fieldNames.add("image5");
		fieldNames.add("image6");
		fieldNames.add("marketPrice");
		fieldNames.add("image3");
		fieldNames.add("image4");
		fieldNames.add("title");
		fieldNames.add("image1");
		fieldNames.add("image2");
		fieldNames.add("price");
		fieldNames.add("isOn");
		fieldNames.add("createdTime");
		fieldNames.add("id");
		fieldNames.add("detail");
		fieldNames.add("price3");
		fieldNames.add("price4");
		fieldNames.add("price1");
		fieldNames.add("skuCode");
		fieldNames.add("price2");
	}

}