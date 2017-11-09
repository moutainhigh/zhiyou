package com.zy.entity.account;

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
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

import static com.zy.entity.account.AccountNumber.VO_ADMIN;

/**
 * Created by it001 on 2017-11-03.
 * 账号 变更实体
 *
 */

@Entity
@Table(name = "td_account_number")
@Getter
@Setter
@Type(label = "活动")
@QueryModel
@ViewObject(groups = VO_ADMIN)
public class AccountNumber implements Serializable {

    public static final String VO_ADMIN = "AccountNumberAdminVo";

    @Id
    @Field(label = "id")
    @View
    private Long id;

    @NotNull
    @Field(label = "原来的账号手机号")
    @Query( Predicate.EQ)
    @View(groups = VO_ADMIN)
    private String oldPhone;

    @Field(label = "原来的账号人姓名")
    @View(groups = VO_ADMIN)
    private String oldName;

    @NotNull
    @Field(label = "新绑定的账号手机号")
    @Query( Predicate.EQ)
    @View(groups = VO_ADMIN)
    private String newPhone;

    @Field(label = "是否可以在此使用")
    @Query( {Predicate.EQ,Predicate.IN})
    @View(groups = VO_ADMIN)
    private String  flage;

    @Field(label = "创建人")
    @View(groups = VO_ADMIN)
    private Long createBy;

    @Field(label = "创建时间")
    @View(groups = VO_ADMIN)
    private Date createDate;

    @Field(label = "更新人")
    @View(groups = VO_ADMIN)
    private Long updateBy;

    @Field(label = "更新时间")
    @View(groups = VO_ADMIN)
    private Date updateDate;


}
