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
import com.zy.entity.fnc.Deposit.DepositStatus;
import java.util.Date;
import com.zy.entity.fnc.PayType;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DepositQueryModel implements Serializable {

	private String snEQ;
	
	private Long userIdEQ;

	private Long[] userIdIN;

	private PayType payTypeEQ;

	private Date paidTimeGTE;

	private Date paidTimeLT;

	private Date createdTimeLT;

	private Date createdTimeGTE;

	private Date expiredTimeLT;

	private DepositStatus depositStatusEQ;

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
		fieldNames.add("isOuterCreated");
		fieldNames.add("remark");
		fieldNames.add("title");
		fieldNames.add("paidTime");
		fieldNames.add("userId");
		fieldNames.add("version");
		fieldNames.add("expiredTime");
		fieldNames.add("totalAmount");
		fieldNames.add("payType");
		fieldNames.add("weixinOpenId");
		fieldNames.add("amount2");
		fieldNames.add("amount1");
		fieldNames.add("qrCodeUrl");
		fieldNames.add("createdTime");
		fieldNames.add("depositStatus");
		fieldNames.add("offlineImage");
		fieldNames.add("outerSn");
		fieldNames.add("id");
		fieldNames.add("sn");
		fieldNames.add("offlineMemo");
		fieldNames.add("operatorId");
	}

}