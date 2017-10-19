package com.zy.entity.star;

import io.gd.generator.annotation.Field;
import io.gd.generator.annotation.Type;
import io.gd.generator.annotation.query.Query;
import io.gd.generator.annotation.query.QueryModel;
import io.gd.generator.annotation.view.View;
import io.gd.generator.annotation.view.ViewObject;
import io.gd.generator.api.query.Predicate;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

import static com.zy.entity.star.Lesson.VO;
import static com.zy.entity.star.Lesson.VO_ADMIN;


/**
 * Created by it001 on 2017/7/20.
 */

@Entity
@Table(name = "ts_lesson")
@Getter
@Setter
@QueryModel
@ViewObject(groups = {VO, VO_ADMIN})
@Type(label = "课程表")
public class Lesson implements Serializable {

    public static final String VO = "LessonVo";
    public static final String VO_ADMIN = "LessonAdminVo";

    @Id
    @Field(label = "id")
    @View
    private Long id;

    @NotBlank
    @Column(length = 100)
    @Field(label = "标题")
    @View(groups = { VO, VO_ADMIN})
    @Query(Predicate.LK)
    private String title;

    @Field(label = "级别")
    @View(groups = { VO, VO_ADMIN})
    @Query(Predicate.EQ)
    private Integer lessonLevel;

    @Field(label = "上级ID")
    @View(groups = { VO, VO_ADMIN})
    @Query(Predicate.IN)
    private  String parentAllId;


    @Field(label = "创建人")
    @View(groups = { VO, VO_ADMIN})
    @Query(Predicate.EQ)
    private Long createById;


    @Field(label = "创建时间")
    @View(groups = { VO, VO_ADMIN})
    @Query({Predicate.GTE, Predicate.LT})
    private Date createDate;

    @Field(label = "状态标记位")
    @View(groups = { VO, VO_ADMIN})
    @Query(Predicate.EQ)
    private Integer viewFlage;

    @Query({Predicate.GTE, Predicate.LT})
    private Date updateTime;

    @Query({Predicate.GTE, Predicate.LT})
    private Long updateId;

}
