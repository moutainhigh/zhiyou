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
import com.zy.entity.sys.ConfirmStatus;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BankCardQueryModel implements Serializable {

	private Long userIdEQ;

	private String realnameLK;

	private Boolean isDeletedEQ;

	private ConfirmStatus confirmStatusEQ;

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
		fieldNames.add("appliedTime");
		fieldNames.add("city");
		fieldNames.add("bankBranchName");
		fieldNames.add("confirmRemark");
		fieldNames.add("bankName");
		fieldNames.add("userId");
		fieldNames.add("realname");
		fieldNames.add("bankId");
		fieldNames.add("isDefault");
		fieldNames.add("province");
		fieldNames.add("isDeleted");
		fieldNames.add("confirmedTime");
		fieldNames.add("isEnterprise");
		fieldNames.add("confirmStatus");
		fieldNames.add("id");
		fieldNames.add("cardNumber");
	}

}