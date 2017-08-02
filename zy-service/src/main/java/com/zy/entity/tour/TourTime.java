package com.zy.entity.tour;

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

import static com.zy.entity.cms.Article.VO_ADMIN;
import static com.zy.entity.cms.Article.VO_DETAIL;
import static com.zy.entity.cms.Article.VO_LIST;

/**
 * Created by it001 on 2017/6/30.
 */
@Entity
@Table(name = "ts_tour_time")
@Getter
@Setter
@ViewObject(groups = {VO_DETAIL})
@QueryModel
@Type(label = "旅游")
public class TourTime implements Serializable {

    public static final String VO_ADMIN = "TourTimeDetailVo";

    @Id
    @Field(label = "id")
    @View
    private Long id;

    @Field(label = "旅游信息id")
    @View(groups = { VO_DETAIL })
    private Long trouId;

    @Field(label = "创建时间")
    @View(groups = { VO_DETAIL })
    private Date begintime;

    @Field(label = "结束时间")
    @View(groups = { VO_DETAIL })
    private Date endtime;

    @Field(label = "费用")
    @View(groups = { VO_DETAIL })
    private Double fee;

    @Field(label = "是否有效")
    @View(groups = { VO_DETAIL })
    private Integer isreleased;

    @Field(label = "是否删除")
    @View(groups = { VO_DETAIL })
    private Integer delflag;

    @Field(label = "地区")
    @Query({Predicate.EQ, Predicate.IN})
    @View(name = "province", type = String.class)
    @View(name = "city", type = String.class)
    @View(name = "district", type = String.class)
    private Long areaId;

    @Field(label = "集合地点")
    @View(groups = { VO_DETAIL })
    private String starAddress;

    @View(name = "createdTime", type = String.class, groups = { VO_ADMIN })
    @Field(label = "创建时间")
    private Date createdTime;

    @Field(label = "创建人")
    @View(groups = { VO_DETAIL, VO_LIST, VO_ADMIN })
    private Long createby;


    @View(name = "updateTime", type = String.class, groups = { VO_ADMIN })
    @Field(label = "更新时间")
    private Date updateTime;

    @Field(label = "更新人")
    @View(groups = { VO_DETAIL, VO_LIST, VO_ADMIN })
    private Long updateby;
}
