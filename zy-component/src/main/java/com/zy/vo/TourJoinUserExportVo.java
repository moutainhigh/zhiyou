package com.zy.vo;

import io.gd.generator.annotation.Field;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Author: Xuwq
 * Date: 2017/7/6.
 */
@Getter
@Setter
public class TourJoinUserExportVo implements Serializable {
    @Field(label = "票务照片")
    private String imageThumbnail;
    @Field(label = "旅游申请单号")
    private String sequenceId;
    @Field(label = "检测报告编号")
    private Long reportId;
    @Field(label = "用户")
    private String userName;
    @Field(label = "身份证号")
    private String idCardNumber;
    @Field(label = "年龄")
    private Integer age;
    @Field(label = "性别")
    private String gender;
    @Field(label = "用户电话")
    private String userPhone;
    @Field(label = "线路")
    private String tourTitle;
    @Field(label = "出游时间")
    private String tourTime;
    @Field(label = "推荐人")
    private String parentName;
    @Field(label = "推荐人电话")
    private String parentPhone;
    @Field(label = "审核状态")
    private String auditStatus;
    @Field(label = "状态时间")
    private String updateDateLabel;
    @Field(label = "房型需求")
    private String houseType;
    @Field(label = "是否加床")
    private String isAddBed;
    @Field(label = "是否接车")
    private String isTransfers;
    @Field(label = "车次")
    private String carNumber;
    @Field(label = "计划到达时间")
    private String planTime;
    @Field(label = "用户备注")
    private String userRemark;
    @Field(label = "审核员")
    private String updateName;
    @Field(label = "审核备注")
    private String revieweRemark;
    @Field(label = "是否参游")
    private String isJoin;
    @Field(label = "保障金额(元)")
    private Long guaranteeAmount;
    @Field(label = "退回保障金额(元)")
    private Long refundAmount;
    @Field(label = "附加费(元)")
    private Long surcharge;
    @Field(label = "消费金额(元)")
    private BigDecimal amount;
    @Field(label = "是否有效")
    private String isEffect;
}
