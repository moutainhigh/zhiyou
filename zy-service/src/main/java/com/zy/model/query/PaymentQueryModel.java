package com.gc.model.query;

import java.util.Set;
import java.util.HashSet;
import java.io.Serializable;

import io.gd.generator.api.query.Direction;

import lombok.Getter;
import lombok.Setter;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentQueryModel implements Serializable {

	private Long userIdEQ;

	private String bizNameEQ;

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
		fieldNames.add("currencyType1");
		fieldNames.add("currencyType2");
		fieldNames.add("remark");
		fieldNames.add("title");
		fieldNames.add("paidTime");
		fieldNames.add("userId");
		fieldNames.add("refund1");
		fieldNames.add("version");
		fieldNames.add("expiredTime");
		fieldNames.add("refund2");
		fieldNames.add("payType");
		fieldNames.add("bizName");
		fieldNames.add("amount2");
		fieldNames.add("amount1");
		fieldNames.add("refundedTime");
		fieldNames.add("bizSn");
		fieldNames.add("refundRemark");
		fieldNames.add("createdTime");
		fieldNames.add("id");
		fieldNames.add("sn");
		fieldNames.add("cancelRemark");
		fieldNames.add("paymentStatus");
	}

}