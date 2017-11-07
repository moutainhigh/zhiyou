package com.zy.entity.mergeusr;

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

import static com.zy.entity.mergeusr.MergeV3ToV4.VO_ADMIN;

/**
 * Created by it001 on 2017-11-07.
 */
@Entity
@Table(name = "td_v3_to_v4")
@Getter
@Setter
@QueryModel
@Type(label = "用户")
@ViewObject(groups = { VO_ADMIN})
public class MergeV3ToV4 implements Serializable {

    public static final String VO_ADMIN = "MergeV3ToV4AdminVo";


    @Id
    @Query(Predicate.IN)
    @Field(label = "id")
    @View(groups = { VO_ADMIN})
    private Long id;

    @Query(Predicate.LK)
    @Field(label = "用户明")
    @View(groups = VO_ADMIN)
    private String name;

    @Query(Predicate.EQ)
    @Field(label = "用户ID")
    @View(groups = VO_ADMIN)
    private  Long UserId;

    @Query(Predicate.EQ)
    @Field(label = "使用标记位")
    @View(groups = VO_ADMIN)
    private  Integer flage;

    @Query(Predicate.EQ)
    @Field(label = "创建人")
    @View(groups = VO_ADMIN)
    private Long create_by;

    @Field(label = "升级时间")
    @View(groups = VO_ADMIN)
    @View(name = "createDateLabel", type = String.class)
    @Query({Predicate.GTE, Predicate.LT})
    private Date create_date;


    @Query(Predicate.EQ)
    @Field(label = "创建人")
    @View(groups = VO_ADMIN)
    private Long update_by;

    @Field(label = "更新时间")
    @View(groups = VO_ADMIN)
    @View(name = "updateDateLabel", type = String.class)
    @Query({Predicate.GTE, Predicate.LT})
    private Date update_date;

    @Field(label = "图片")
    @View(groups = VO_ADMIN)
    private String image1;

    @Field(label = "图片")
    @View(groups = VO_ADMIN)
    private String image2;

    @Query(Predicate.EQ)
    @Field(label = "删除标记为")
    @View(groups = VO_ADMIN)
    private Integer delFlage;

}
