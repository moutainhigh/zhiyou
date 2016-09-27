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
import com.zy.entity.mal.Order.OrderStatus;
import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderQueryModel implements Serializable {

	private String snLK;

	private Long userIdEQ;

	private Long[] userIdIN;

	private Long sellerIdEQ;

	private Long[] sellerIdIN;

	private Boolean isPayToPlatformEQ;

	private Date createdTimeLT;

	private Date createdTimeGTE;

	private Date paidTimeLT;

	private Date paidTimeGTE;

	private OrderStatus orderStatusEQ;

	private Boolean isProfitSettledUpEQ;

	private Boolean isSettledUpEQ;

	private Boolean isPlatformDeliverEQ;

	private String logisticsNameLK;

	private String logisticsSnLK;

	private Boolean isDeletedEQ;

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
		fieldNames.add("logisticsSn");
		fieldNames.add("discountFee");
		fieldNames.add("isPlatformDeliver");
		fieldNames.add("orderStatus");
		fieldNames.add("remark");
		fieldNames.add("receiverProvince");
		fieldNames.add("title");
		fieldNames.add("receiverCity");
		fieldNames.add("logisticsFeePayType");
		fieldNames.add("isSettledUp");
		fieldNames.add("sellerId");
		fieldNames.add("receiverPhone");
		fieldNames.add("isDeleted");
		fieldNames.add("refundedTime");
		fieldNames.add("receiverDistrict");
		fieldNames.add("refundRemark");
		fieldNames.add("createdTime");
		fieldNames.add("offlineImage");
		fieldNames.add("id");
		fieldNames.add("sn");
		fieldNames.add("isPayToPlatform");
		fieldNames.add("receiverAreaId");
		fieldNames.add("amount");
		fieldNames.add("isProfitSettledUp");
		fieldNames.add("paidTime");
		fieldNames.add("userId");
		fieldNames.add("version");
		fieldNames.add("expiredTime");
		fieldNames.add("receiverRealname");
		fieldNames.add("logisticsName");
		fieldNames.add("receiverAddress");
		fieldNames.add("sellerMemo");
		fieldNames.add("deliveredTime");
		fieldNames.add("logisticsFee");
		fieldNames.add("offlineMemo");
		fieldNames.add("refund");
		fieldNames.add("buyerMemo");
		fieldNames.add("useLogistics");
	}

}