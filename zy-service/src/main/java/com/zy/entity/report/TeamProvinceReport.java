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

import static com.zy.entity.report.TeamProvinceReport.VO_ADMIN;


/**
 * Created by liang on 2017/8/28.
 */
@Entity
@Table(name = "ts_team_province_report")
@Getter
@Setter
@Type(label = "省份服务商活跃度排名")
@QueryModel
@ViewObject(groups = {VO_ADMIN}
)
public class TeamProvinceReport implements Serializable {

    public static final String VO_ADMIN = "TeamProvinceReportAdminVo";


    @Id
    @Field(label = "id")
    @View
    private Long id;

    @Field(label = "省份")
    @View
    @Query(Predicate.EQ)
    private String province;

    @Field(label = "特级人数")
    @View
    private Integer v4Number;

    @Field(label = "省级人数")
    @View
    private Integer v3Number;

    @Field(label = "特级活跃人数")
    @View
    private Integer v4ActiveNumber;

    @Field(label = "特级活跃度")
    @View
    private Double v4ActiveRate;

    @Field(label = "特级活跃度排名")
    @View
    private Integer v4ActiveRank;

    @Field(label = "年份")
    @View
    @Query(Predicate.EQ)
    private Integer year;

    @Field(label = "月份")
    @View
    @Query(Predicate.EQ)
    private Integer month;

    @Field(label = "创建时间")
    @View
    @Query(Predicate.EQ)
    private Date createTime;

}
