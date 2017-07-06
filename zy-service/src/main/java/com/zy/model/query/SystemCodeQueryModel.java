package com.zy.model.query;

import com.zy.entity.act.ActivityTeamApply.PaidStatus;
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
public class SystemCodeQueryModel implements Serializable {

	private Long id;

	private Long[] idIN;

	private String systemTypeLK;

	private String systemNameLK;

	private Long createBy;

	private Long[] createByIN;

	private Long updateBy;

	private Long[] updateByIN;

	private Date createTimeGTE;

	private Date createTimeLT;

	private Date updateTimeGTE;

	private Date updateTimeLT;

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
		fieldNames.add("systemType");
		fieldNames.add("systemName");
		fieldNames.add("systemValue");
		fieldNames.add("systemDesc");
		fieldNames.add("systemFlag");
		fieldNames.add("createDate");
		fieldNames.add("createBy");
		fieldNames.add("updateDate");
		fieldNames.add("updateBy");
	}

}