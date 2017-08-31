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

import static com.zy.entity.report.TeamReportNew.VO_ADMIN;

/**
 * Created by it001 on 2017/8/25.
 */
@Entity
@Table(name = "ts_team_report")
@Getter
@Setter
@Type(label = "团队报表")
@QueryModel
@ViewObject(groups = {VO_ADMIN}
)
public class TeamReportNew implements Serializable {

    public static final String VO_ADMIN = "TeamReportNewAdminVo";

    @Id
    @Field(label = "id")
    @View
    private Long id;

    @Field(label = "userId")
    @View
    @Query(Predicate.EQ)
    private Long  userId;

    @Field(label = "userName")
    @View
    @Query(Predicate.LK)
    private String userName;

    @Field(label = "districtName")
    @View
    @Query(Predicate.LK)
    private String districtName;

    @Field(label = "districtId")
    @View
    @Query(Predicate.EQ)
    private Long  districtId;

    @Field(label = "extraNumber")
    @View
    @Query(Predicate.EQ)
    private  Integer extraNumber;

    @Field(label = "newextraNumber")
    @View
    @Query(Predicate.EQ)
    private Integer newextraNumber;

    @Field(label = "sleepextraNumber")
    @View
    @Query(Predicate.EQ)
    private  Integer sleepextraNumber;

    @Field(label = "newextraRate")
    @View
    @Query(Predicate.EQ)
    private  Double newextraRate;

    @Field(label = "provinceNumber")
    @View
    @Query(Predicate.EQ)
    private Integer provinceNumber;

    @Field(label = "newprovinceNumber")
    @View
    @Query(Predicate.EQ)
    private Integer newprovinceNumber;

    @Field(label = "newprovinceRate")
    @View
    @Query(Predicate.EQ)
    private Double newprovinceRate;

    @Field(label = "ranking")
    @View
    @Query(Predicate.EQ)
    private  Integer ranking;

    @Field(label = "year")
    @View
    @Query(Predicate.EQ)
    private Integer year;

    @Field(label = "month")
    @View
    @Query(Predicate.EQ)
    private Integer month;

    @Field(label = "phone")
    @View
    @Query(Predicate.EQ)
    private String  phone;

    @Field(label = "createDate")
    @View
    @Query(Predicate.EQ)
    private Date createDate;

    @Field(label = "isBoss")
    @View
    @Query(Predicate.EQ)
    private Integer isBoss;

    @Field(label = "changRanking")
    @View
    @Query(Predicate.EQ)
    private Integer changRanking;


}
