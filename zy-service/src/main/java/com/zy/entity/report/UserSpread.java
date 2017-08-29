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

import static com.zy.entity.report.UserSpread.VO_ADMIN;

/**
 * Created by it001 on 2017/8/28.
 */

@Entity
@Table(name = "ts_user_spread")
@Getter
@Setter
@Type(label = "团队报表")
@QueryModel
@ViewObject(groups = {VO_ADMIN}
)
public class UserSpread implements Serializable {

    public static final String VO_ADMIN = "UserSpreadAdminVo";

    @Id
    @Field(label = "id")
    @View
    private Long Id;

    @Field(label = "userId")
    @View
    @Query(Predicate.EQ)
    private Long userId;

    @Field(label = "provinceId")
    @View
    @Query(Predicate.EQ)
    private Long provinceId;

    @Field(label = "provinceName")
    @View
    @Query(Predicate.EQ)
    private String provinceName;

    @Field(label = "V4")
    @View
    @Query(Predicate.EQ)
    private Integer v4;

    @Field(label = "V3")
    @View
    @Query(Predicate.EQ)
    private Integer v3;

    @Field(label = "V2")
    @View
    @Query(Predicate.EQ)
    private Integer v2;

    @Field(label = "V1")
    @View
    @Query(Predicate.EQ)
    private Integer v1;

    @Field(label = "V0")
    @View
    @Query(Predicate.EQ)
    private Integer v0;

    @Field(label = "year")
    @View
    @Query(Predicate.EQ)
    private Integer year;

    @Field(label = "month")
    @View
    @Query(Predicate.EQ)
    private Integer month;

    @Field(label = "createDate")
    @View
    private Date createDate;

 }
