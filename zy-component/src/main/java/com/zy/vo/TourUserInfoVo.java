package com.zy.vo;

import com.zy.entity.usr.UserInfo;
import io.gd.generator.annotation.Field;
import io.gd.generator.annotation.query.Query;
import io.gd.generator.annotation.view.View;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;

import static io.gd.generator.api.query.Predicate.EQ;
import static io.gd.generator.api.query.Predicate.IN;

/**
 * Created by it001 on 2017/7/10.
 */
@Getter
@Setter
public class TourUserInfoVo implements Serializable {

    @Field(label = "推荐人手机号")
    private Long userId;

    @Field(label = "旅游新路id")
    private Long tourId;

    @Field(label = "推荐人手机号")
    private Long parentPhone;

    @Field(label = "推荐人id")
    private Long parentId;

    @Field(label = "检测单号id")
    private Long reporId;

    @Field(label = "出游时间id")
    private Long tourTimeId;

    @Field(label = "真实姓名")
    private String realname;

    @Field(label = "省份证号")
    private String idCartNumber;

    @Field(label = "性别")
    private UserInfo.Gender gender;

    @Field(label = "年龄")
    private Integer age;

    @Field(label = "省份")
    private String province;

    @Field(label = "城市")
    private String city;

    @Field(label = "地区")
    private String district;

    @Field(label = "用户电话")
    private String phone;

    @Field(label = "房型")
    private Integer houseType;

    @Field(label = "特殊需求")
    private String userRemark;

    @Field(label = "所在地")
    @View(name = "province", type = String.class)
    @View(name = "city", type = String.class)
    @View(name = "district", type = String.class)
    private Long areaId;

    @Field(label = "产品编号")
    private String productNumber;


}
