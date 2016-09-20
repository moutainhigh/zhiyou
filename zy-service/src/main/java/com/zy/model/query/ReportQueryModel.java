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
import com.zy.entity.act.Report.ReportResult;
import com.zy.entity.sys.ConfirmStatus;
import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportQueryModel implements Serializable {

	private Long userIdEQ;

	private Long[] userIdIN;

	private String realnameLK;

	private String phoneEQ;

	private ReportResult reportResultEQ;

	private Date createdTimeGTE;

	private Date createdTimeLT;

	private ConfirmStatus preConfirmStatusEQ;

	private ConfirmStatus confirmStatusEQ;

	private Boolean isSettledUpEQ;

	private Boolean isHotEQ;

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
		fieldNames.add("gender");
		fieldNames.add("tagIds");
		fieldNames.add("isSettledUp");
		fieldNames.add("confirmedTime");
		fieldNames.add("createdTime");
		fieldNames.add("confirmStatus");
		fieldNames.add("id");
		fieldNames.add("text");
		fieldNames.add("reportResult");
		fieldNames.add("preConfirmStatus");
		fieldNames.add("image5");
		fieldNames.add("image6");
		fieldNames.add("image3");
		fieldNames.add("image4");
		fieldNames.add("confirmRemark");
		fieldNames.add("image1");
		fieldNames.add("userId");
		fieldNames.add("image2");
		fieldNames.add("version");
		fieldNames.add("realname");
		fieldNames.add("jobId");
		fieldNames.add("areaId");
		fieldNames.add("phone");
		fieldNames.add("age");
		fieldNames.add("isHot");
	}

}