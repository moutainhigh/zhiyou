package com.zy.vo;

import io.gd.generator.annotation.Field;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Author: liang
 * Date: 2017/7/10.
 */
@Getter
@Setter
public class TourUserListVo implements Serializable {
    /* 原生 */
    @Field(label = "id")
    private Long id;
    @Field(label = "旅游标题")
    private String tourTitle;
    @Field(label = "出游时间")
    private String tourTime;
    @Field(label = "审核状态")
    private Integer auditStatus;
    @Field(label = "旅游")
    private Long tourId;
    @Field(label = "检测报告编号")
    private Long reportId;
    @Field(label = "上级电话")
    private String parentPhone;
    @Field(label = "主图")
    private String image;
    @Field(label = "出游时间id")
    private Long tourTimeId;
}
