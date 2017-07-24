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

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

import static com.zy.entity.star.LessonUser.VO;
import static com.zy.entity.star.LessonUser.VO_ADMIN;

/**
 * Created by it001 on 2017/7/21.
 */

@Entity
@Table(name = "td_lesson_user")
@Getter
@Setter
@QueryModel
@ViewObject(groups = {VO, VO_ADMIN})
@Type(label = "用户课程表")
public class LessonUser implements Serializable {

    public static final String VO = "LessonUserVo";
    public static final String VO_ADMIN = "LessonUserAdminVo";

    @Id
    @Field(label = "id")
    @View
    private Long id;

    @NotBlank
    @Column(length = 100)
    @Field(label = "课程id")
    @View(groups = { VO, VO_ADMIN })
    @Query(Predicate.EQ)
    private Long  lessonId;

    @NotBlank
    @Column(length = 100)
    @Field(label = "用户id")
    @View(groups = { VO, VO_ADMIN })
    @Query(Predicate.EQ)
    private Long   userId;

    @NotBlank
    @Column(length = 100)
    @Field(label = "状态")
    @View(groups = { VO, VO_ADMIN })
    @Query(Predicate.EQ)
    private  Integer lessonStatus;

    @NotBlank
    @Column(length = 100)
    @Field(label = "备注")
    @View(groups = { VO, VO_ADMIN })
    @Query(Predicate.LK)
    private String remark;

    @NotBlank
    @Column(length = 100)
    @Field(label = "备注")
    @View(groups = { VO, VO_ADMIN })
    @Query(Predicate.EQ)
    private  Long createById;

    @Field(label = "创建时间")
    @View(groups = { VO, VO_ADMIN })
    @Query({Predicate.GTE, Predicate.LT})
    private Date createDate;




}
