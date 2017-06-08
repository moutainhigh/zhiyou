package com.zy.model.query;

import com.zy.entity.act.Activity;
import com.zy.entity.act.ActivityApply;
import com.zy.entity.usr.User;
import io.gd.generator.api.query.Direction;
import lombok.*;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActivityReportQueryModel implements Serializable {

	private String activityName;//活动名称

	private Date activityTime;//活动时间

	private String activityAddress;//活动地址

	private String userName; //客户名称

	private String userPhone;//客户电话号码

	private String userLevel;//客户jibie

	private String  initFalg; //是否初始或页面

	private String payType; //付款类型

	private Long[] activityIdIN;//活动集合

	private Long[] userIdIN;//用户集合

	private int activityApplyStatus;//报名状态

	//分页所需制字段
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
		fieldNames.add("activityId");
		fieldNames.add("amount");
		fieldNames.add("isCancelled");
		fieldNames.add("appliedTime");
		fieldNames.add("inviterId");
		fieldNames.add("outerSn");
		fieldNames.add("id");
		fieldNames.add("activityApplyStatus");
		fieldNames.add("payerUserId");
		fieldNames.add("userId");
		fieldNames.add("isSmsSent");
	}

}