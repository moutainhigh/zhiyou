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
import com.zy.entity.fnc.Transfer.TransferType;
import com.zy.entity.fnc.Transfer.TransferStatus;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransferQueryModel implements Serializable {

	private TransferStatus transferStatusEQ;

	private TransferStatus[] transferStatusIN;

	private Long fromUserIdEQ;

	private Long[] fromUserIdIN;

	private Long toUserIdEQ;

	private Long[] toUserIdIN;

	private String snEQ;

	private Date createdTimeGTE;

	private Date createdTimeLT;

	private Date transferredTimeGTE;

	private Date transferredTimeLT;

	private TransferType transferTypeEQ;

	private TransferType[] transferTypeIN;

	private Long refIdEQ;

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
		fieldNames.add("amount");
		fieldNames.add("transferStatus");
		fieldNames.add("fromUserId");
		fieldNames.add("remark");
		fieldNames.add("transferredTime");
		fieldNames.add("title");
		fieldNames.add("toUserId");
		fieldNames.add("version");
		fieldNames.add("transferRemark");
		fieldNames.add("createdTime");
		fieldNames.add("transferType");
		fieldNames.add("id");
		fieldNames.add("sn");
		fieldNames.add("refId");
	}

}