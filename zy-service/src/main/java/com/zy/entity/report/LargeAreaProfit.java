package com.zy.entity.report;

import io.gd.generator.annotation.Field;
import io.gd.generator.annotation.Type;
import io.gd.generator.annotation.query.Query;
import io.gd.generator.annotation.query.QueryModel;
import io.gd.generator.annotation.view.View;
import io.gd.generator.api.query.Predicate;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;


/**
 * Author: Xuwq
 * Date: 2017/9/28.
 */
@Entity
@Table(name = "rpt_largearea_profit")
@Getter
@Setter
@Type(label = "大区收益报表")
@QueryModel
public class LargeAreaProfit implements Serializable{

    @Id
    @Field(label = "id")
    @View
    @Query(Predicate.EQ)
    private Long id;

    @Id
    @Field(label = "userId")
    @View
    @Query(Predicate.EQ)
    private Long userId;

    @Field(label = "大区名称")
    @View
    @Query(Predicate.EQ)
    private String largeAreaName;

    @Field(label = "大区值")
    @View
    @Query(Predicate.EQ)
    private Integer largeAreaValue;

    @Field(label = "收益")
    @View
    private Double profit;

    @Field(label = "年份")
    @View
    @Query(Predicate.EQ)
    private Integer year;

    @Field(label = "月份")
    @View
    @Query(Predicate.EQ)
    private Integer month;

    @Field(label = "收益同比")
    @View
    private Double sameRate;

    @Field(label = "收益环比")
    @View
    private Double relativeRate;

    @Field(label = "创建时间")
    @View
    private Date createTime;
}
