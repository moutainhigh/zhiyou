package com.zy.vo;

import io.gd.generator.annotation.Field;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Created by it001 on 2017/6/30.
 */
@Getter
@Setter
public class TourTimeDetailVo implements Serializable {

    @Field(label = "id")
    private Long id;

    @Field(label = "开始时间")
    private String begintimeLible;

    @Field(label = "结束时间")
    private String endtimeLible;

    @Field(label = "费用")
    private Double fee;

    @Field(label = "是否有效")

    private Integer isreleased;

    @Field(label = "集合地点")
    private String starAddress;


    @Field(label = "创建时间")
    private String createdTimeLible;

    @Field(label = "创建人")
    private String createby;


    @Field(label = "更新时间")
    private String updateTime;

    @Field(label = "更新人")
    private Long updateby;

    @Field(label = "省")
    private String province;

    @Field(label = "市")
    private String city;

    @Field(label = "区")
    private String district;


}
