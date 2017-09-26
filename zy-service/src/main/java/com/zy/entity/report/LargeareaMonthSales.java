package com.zy.entity.report;

import io.gd.generator.annotation.Field;
import io.gd.generator.annotation.Type;
import io.gd.generator.annotation.query.Query;
import io.gd.generator.annotation.query.QueryModel;
import io.gd.generator.annotation.view.View;
import io.gd.generator.annotation.view.ViewObject;
import io.gd.generator.api.query.Predicate;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

import static com.zy.entity.report.LargeareaMonthSales.VO_ADMIN;


/**
 * Created by liang on 2017/9/26.
 */
@Entity
@Table(name = "ts_largearea_month_sales")
@Getter
@Setter
@Type(label = "大区月销量报表")
@QueryModel
@ViewObject(groups = {VO_ADMIN}
)
public class LargeareaMonthSales implements Serializable {

    public static final String VO_ADMIN = "LargeareaMonthSalesAdminVo";

    @Id
    @Field(label = "id")
    @View
    @Query(Predicate.EQ)
    private Long id;

    @Field(label = "大区名称")
    @View
    @Query(Predicate.EQ)
    private String largeareaName;

    @Field(label = "大区值")
    @View
    @Query(Predicate.EQ)
    private Integer largeareaValue;

    @Field(label = "销量")
    @View
    private Integer sales;

    @Field(label = "目标销量")
    @View
    private Integer targetCount;

    @Field(label = "年份")
    @View
    @Query(Predicate.EQ)
    private Integer year;

    @Field(label = "月份")
    @View
    @Query(Predicate.EQ)
    private Integer month;

    @Field(label = "销量同比")
    @View
    private Double sameRate;

    @Field(label = "销量环比")
    @View
    private Double relativeRate;

    @Field(label = "创建时间")
    @View
    private Date createTime;
}
