package com.zy.vo;

import io.gd.generator.annotation.Field;
import io.gd.generator.annotation.query.Query;
import io.gd.generator.annotation.view.View;
import io.gd.generator.api.query.Predicate;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by it001 on 2017/6/30.
 */
@Getter
@Setter
public class TourTimeVo implements Serializable {

    @Field(label = "id")
    private Long id;

    @Field(label = "创建时间")
    private Date begintime;

    @Field(label = "结束时间")
    private Date endtime;

    @Field(label = "费用")
    private Double fee;

    @Field(label = "是否有效")
    private Integer isreleased;

    @Field(label = "集合地点")
    private String starAddress;

    @Field(label = "创建时间")
    private Date createdTime;

    @Field(label = "创建人")
    private Long createby;

    @Field(label = "更新时间")
    private Date updateTime;

   /* @Field(label = "省")
    private Long province;

    @Field(label = "市")
    private Long city;*/


    @Field(label = "地区")
    @Query({Predicate.EQ, Predicate.IN})
    @View(name = "province", type = String.class)
    @View(name = "city", type = String.class)
    @View(name = "district", type = String.class)
    private Long areaId;

    @Field(label = "详细地址")
    private String address;

    @Field(label = "路线信息id")
    private Long tourId;

    @Field(label = "发团时间")
    private String beginTimeStr;

    @Field(label = "周几")
    private String weekStr;

    
}
