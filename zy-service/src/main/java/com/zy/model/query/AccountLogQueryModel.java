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
import com.zy.entity.fnc.AccountLog.InOut;
import java.util.Date;
import com.zy.entity.fnc.AccountLog.AccountLogType;
import com.zy.entity.fnc.CurrencyType;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountLogQueryModel implements Serializable {

	private InOut inOutEQ;

	private String titleLK;

	private Long userIdEQ;

	private Long[] userIdIN;

	private Long refUserIdEQ;

	private Long[] refUserIdIN;

	private AccountLogType accountLogTypeEQ;

	private AccountLogType[] accountLogTypeIN;

	private String refSnEQ;

	private CurrencyType currencyTypeEQ;

	private Date transTimeGTE;

	private Date transTimeLT;

	private Boolean isAcknowledgedEQ;

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
		fieldNames.add("currencyType");
		fieldNames.add("refUserId");
		fieldNames.add("transTime");
		fieldNames.add("title");
		fieldNames.add("userId");
		fieldNames.add("transAmount");
		fieldNames.add("beforeAmount");
		fieldNames.add("inOut");
		fieldNames.add("accountLogType");
		fieldNames.add("isAcknowledged");
		fieldNames.add("id");
		fieldNames.add("refId");
		fieldNames.add("refSn");
		fieldNames.add("afterAmount");
	}

}