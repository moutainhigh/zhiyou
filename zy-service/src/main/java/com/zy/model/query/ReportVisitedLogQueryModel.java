package com.zy.model.query;

import com.zy.entity.sys.ConfirmStatus;
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
public class ReportVisitedLogQueryModel implements Serializable {

	private Long idEQ;

	private Long[] idIN;

	private Long reportIdEQ;

	private Long[] reportIdIN;

	private String relationshipLK;

	private String customerServiceName1LK;

	private Date visitedTime1GTE;

	private Date visitedTime1LT;

	private String visitedStatus1EQ;

	private String visitedStatus1LK;

	private String customerServiceName2LK;

	private Date visitedTime2GTE;

	private Date visitedTime2LT;

	private String visitedStatus2EQ;

	private String visitedStatus2LK;

	private String customerServiceName3LK;

	private Date visitedTime3GTE;

	private Date visitedTime3LT;

	private String visitedStatus3EQ;

	private String visitedStatus3LK;

	private String visitedStatusEQ;

	private String visitedStatusLK;

	private ConfirmStatus confirmStatusEQ;

	private String restTimeLabelGTE;

	private String restTimeLabelLT;

	private String smokeEQ;

	private String exerciseEQ;

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
		fieldNames.add("causeText");
		fieldNames.add("visitedStatus1");
		fieldNames.add("visitedStatus2");
		fieldNames.add("hobbyText");
		fieldNames.add("visitedStatus3");
		fieldNames.add("healthProduct");
		fieldNames.add("exercise");
		fieldNames.add("cause");
		fieldNames.add("sickness");
		fieldNames.add("remark");
		fieldNames.add("customerServiceName1");
		fieldNames.add("visitedInfo");
		fieldNames.add("toAgent");
		fieldNames.add("restTimeLabel");
		fieldNames.add("healthProductText");
		fieldNames.add("productName");
		fieldNames.add("contactWayText");
		fieldNames.add("restTimeText");
		fieldNames.add("exerciseText");
		fieldNames.add("customerServiceName2");
		fieldNames.add("customerServiceName3");
		fieldNames.add("confirmStatus");
		fieldNames.add("familyHistory");
		fieldNames.add("productSharing");
		fieldNames.add("id");
		fieldNames.add("relationship");
		fieldNames.add("interferingFactors");
		fieldNames.add("productSharingText");
		fieldNames.add("reportId");
		fieldNames.add("visitedStatus");
		fieldNames.add("sleepQuality");
		fieldNames.add("visitedTime1");
		fieldNames.add("smoke");
		fieldNames.add("visitedTime2");
		fieldNames.add("health");
		fieldNames.add("visitedTime3");
		fieldNames.add("drink");
		fieldNames.add("monthlyCostText");
		fieldNames.add("relationshipText");
		fieldNames.add("sleepQualityText");
		fieldNames.add("contactWay");
		fieldNames.add("remark1");
		fieldNames.add("monthlyCost");
		fieldNames.add("remark3");
		fieldNames.add("hobby");
		fieldNames.add("remark2");
	}

}