package com.zy.entity.mal;

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
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

import static com.zy.entity.mal.OrderStore.VO_DETAIL;
import static com.zy.entity.mal.OrderStore.VO_ADMIN;


/**
 * Created by it001 on 2017/8/3.
 */
@Entity
@Table(name = "td_order_store")
@Getter
@Setter
@ViewObject(groups = {VO_ADMIN, VO_DETAIL})
@QueryModel
@Type(label = "订单库存")
public class OrderStore implements Serializable {

    public static final String VO_ADMIN = "OrderStoreAdminVo";
    public static final String VO_DETAIL = "OrderStoreVo";

    @Id
    @Field(label = "id")
    @View
    private Long id;

    @NotBlank
    @Field(label = "用户id")
    @View(groups = { VO_DETAIL, VO_ADMIN })
    @Query(Predicate.EQ)
    private Long  userId;

    @NotBlank
    @Field(label = "订单ID")
    @View(groups = { VO_DETAIL, VO_ADMIN })
    @Query(Predicate.EQ)
    private Long  orderId;

    @Field(label = "类型")
    @View(groups = { VO_DETAIL, VO_ADMIN })
    @Query(Predicate.EQ)
    private Integer type;

    @Field(label = "数量")
    @View(groups = { VO_DETAIL, VO_ADMIN })
    @Query({Predicate.GTE, Predicate.LT})
    private Integer number;

    @Field(label = "操作前数量")
    @View(groups = {VO_DETAIL, VO_ADMIN  })
    @Query({Predicate.GTE, Predicate.LT})
    private Integer beforeNumber;

    @Field(label = "操作后数量")
    @View(groups = {VO_DETAIL, VO_ADMIN  })
    @Query({Predicate.GTE, Predicate.LT})
    private Integer afterNumber;


    @View(name = "createDate", type = String.class, groups = {VO_DETAIL, VO_ADMIN })
    @Field(label = "创建时间")
    @Query({Predicate.GTE, Predicate.LT})
    private Date createDate;

    @Field(label = "创建人")
    @View(groups = { VO_DETAIL, VO_ADMIN })
    @Query(Predicate.EQ)
    private Long createBy;

    @Field(label = "是否最后")
    @View(groups = { VO_DETAIL, VO_ADMIN })
    @Query(Predicate.EQ)
    private Integer isEnd;

    @Field(label = "商品类型")
    @View(groups = { VO_DETAIL, VO_ADMIN })
    @Query(Predicate.EQ)
    private Integer productType;

}
