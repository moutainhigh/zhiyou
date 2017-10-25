package com.zy.entity.sys;

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



/**
 * Created by it001 on 2017-10-24.
 */

@Entity
@Table(name = "ts_invite")
@Getter
@Setter
@QueryModel
@Type(label = "邀请码表")
public class InviteNumber implements Serializable {

    @Id
    @Field(label = "id")
    @View
    private Long id;

    @Field(label = "邀请码")
    @Query(Predicate.EQ)
    private Long inviteNumber;

    @Field(label = "是否可用,0可用,1不可用")
    @Query(Predicate.EQ)
    private Integer flage;

    @Field(label = "用户ID")
    @Query(Predicate.EQ)
    private Long userId;

    @Field(label = "更新时间")
    @Query({Predicate.GTE, Predicate.LT})
    private Date updateTime;
}
