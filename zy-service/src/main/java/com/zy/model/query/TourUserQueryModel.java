package com.zy.model.query;

import io.gd.generator.api.query.Direction;
import lombok.*;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Author: Xuwq
 * Date: 2017/6/30.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TourUserQueryModel implements Serializable {

    private Long sequenceId;

    private Long reportId;

    private Integer auditStatus;

    private Integer isEffect;

    private Integer isTransfers;

    private Integer isJoin;

    private String userName;

    private String parentName;

    private Long  parentId;

    private Long  tourTimeId;

    private Integer firstVisitStatus;

    private Integer secondVisitStatus;

    private Integer thirdVisitStatus;

    private String userPhone;

    private String parentPhone;

    private String tourTitle;

    private Date createdTime;

    private Date beginTime;

    private Long userId;

    private Date planStartTime;

    private Date planEndTime;

    private Date endTime;

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
        fieldNames.add("id");
        fieldNames.add("createdTime");
        fieldNames.add("updateTime");
        fieldNames.add("auditStatus");
        fieldNames.add("revieweRemark");
    }
}
