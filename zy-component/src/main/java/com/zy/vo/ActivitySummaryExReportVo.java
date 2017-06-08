package com.zy.vo;

import io.gd.generator.annotation.Field;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Author: Xuwq
 * Date: 2017/6/7.
 */
@Getter
@Setter
public class ActivitySummaryExReportVo  implements Serializable {
    /* 原生 */
    @Field(label = "标题")
    private String title;
    @Field(label = "活动开始时间")
    private String startTimeLabel;
    @Field(label = "活动结束时间")
    private String endTimeLabel;
    @Field(label = "地区")
    private String province;
    @Field(label = "地区")
    private String city;
    @Field(label = "地区")
    private String district;
    @Field(label = "浏览数")
    private Long viewedCount;
    @Field(label = "关注数")
    private Long collectedCount;
    @Field(label = "未支付人数")
    private Long nonPayment;
    @Field(label = "报名数")
    private Long appliedCount;
    @Field(label = "签到数")
    private Long signedInCount;
    @Field(label = "签到率(%)")
    private BigDecimal attendanceRate;
}
