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

import static com.zy.entity.report.NewReportTeam.VO_ADMIN;

/**
 * Created by it001 on 2017-09-27.
 */
@Entity
@Table(name = "ts_new_report_team")
@Getter
@Setter
@Type(label = "新团队报表")
@QueryModel
@ViewObject(groups = {VO_ADMIN}
)
public class NewReportTeam implements Serializable {

    public static final String VO_ADMIN = "newReportTeamAdminVo";

    @Id
    @Field(label = "id")
    @View
    @Query(Predicate.EQ)
    private Long id;

    @Field(label = "provinceName")
    @View
    @Query(Predicate.LK)
    private String provinceName;

    @Field(label = "provinceId")
    @View
    @Query(Predicate.EQ)
    private Long provinceId;

    @Field(label = "year")
    @View
    @Query(Predicate.EQ)
    private Integer year;

    @Field(label = "month")
    @View
    @Query(Predicate.EQ)
    private Integer month;

    @Field(label = "number")
    @View
    @Query(Predicate.EQ)
    private Integer number;

    @Field(label = "rank")
    @View
    @Query(Predicate.EQ)
    private Integer rank;

    @Field(label = "rankChange")
    @View
    @Query(Predicate.EQ)
    private Integer rankChange;

    @Field(label = "createDate")
    @View
    private Date createDate;

    @Field(label = "region")
    @View
    @Query(Predicate.EQ)
    private Integer region;



}
