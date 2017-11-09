package com.zy.model.query;

import java.util.List;
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
import com.zy.entity.mal.Order.OrderType;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderQueryModel implements Serializable {

	private String snLK;

	private Long userIdEQ;

	private Integer productTypeEQ;

	private Long[] userIdIN;

	private Long sellerIdEQ;

	private Long[] sellerIdIN;

	private List<Long> sellerIdList;

	private List<Long> userIdList;

	private Long rootIdEQ;

	private Long v4UserIdEQ;

	private Boolean isPayToPlatformEQ;

	private String titleLK;

	private Date createdTimeLT;

	private Date createdTimeGTE;

	private Date expiredTimeLT;

	private Date expiredTimeGTE;

	private Date paidTimeLT;

	private Date paidTime;

	private Date paidTimeGTE;

	private OrderStatus orderStatusEQ;

	private OrderStatus[] orderStatusIN;

	private OrderType orderTypeEQ;

	private Boolean isProfitSettledUpEQ;

	private Boolean isSettledUpEQ;

	private Boolean isCopiedEQ;

	private String logisticsNameLK;

	private String logisticsSnLK;

	private String receiverRealnameEQ;

	private String receiverPhoneEQ;

	private Boolean isDeletedEQ;

	private Long productIdEQ;

	private Integer exaltFlageEQ;

	private Long inviterIdEQ;

	private Boolean isSettlementEQ;

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
		fieldNames.add("orderType");
		fieldNames.add("discountFee");
		fieldNames.add("receiverProvince");
		fieldNames.add("receiverCity");
		fieldNames.add("sellerId");
		fieldNames.add("receiverPhone");
		fieldNames.add("refundedTime");
		fieldNames.add("price");
		fieldNames.add("createdTime");
		fieldNames.add("offlineImage");
		fieldNames.add("id");
		fieldNames.add("isPayToPlatform");
		fieldNames.add("sellerUserRank");
		fieldNames.add("receiverAreaId");
		fieldNames.add("image");
		fieldNames.add("isSettlement");
		fieldNames.add("isBuyerPayLogisticsFee");
		fieldNames.add("productId");
		fieldNames.add("isProfitSettledUp");
		fieldNames.add("paidTime");
		fieldNames.add("version");
		fieldNames.add("logisticsName");
		fieldNames.add("receiverAddress");
		fieldNames.add("buyerUserRank");
		fieldNames.add("copiedTime");
		fieldNames.add("sellerMemo");
		fieldNames.add("deliveredTime");
		fieldNames.add("offlineMemo");
		fieldNames.add("refund");
		fieldNames.add("buyerMemo");
		fieldNames.add("logisticsSn");
		fieldNames.add("marketPrice");
		fieldNames.add("isUseLogistics");
		fieldNames.add("rootId");
		fieldNames.add("orderStatus");
		fieldNames.add("remark");
		fieldNames.add("title");
		fieldNames.add("isSettledUp");
		fieldNames.add("isDeleted");
		fieldNames.add("receiverDistrict");
		fieldNames.add("refundRemark");
		fieldNames.add("sn");
		fieldNames.add("amount");
		fieldNames.add("isCopied");
		fieldNames.add("quantity");
		fieldNames.add("isMultiple");
		fieldNames.add("userId");
		fieldNames.add("expiredTime");
		fieldNames.add("receiverRealname");
		fieldNames.add("v4UserId");
		fieldNames.add("refId");
		fieldNames.add("logisticsFee");
	}

}