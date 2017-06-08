package com.zy.vo;

import com.zy.entity.usr.User;
import io.gd.generator.annotation.Field;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Created by it001 on 2017/6/6.
 * 封装 返回数据
 */
@Getter
@Setter
public class ActivityReportVo implements Serializable {

    @Field(label = "id")
    private Long id;
    @Field(label = "手机号")
    private String phone;
    @Field(label = "用户等级")
    private String userRankLable;
    @Field(label = "昵称")
    private String nickname;
    @Field(label = "真实姓名")
    private String realname;
    @Field(label = "上级name")
    private String parentName;
    @Field(label = "标题")
    private String title;
    @Field(label = "开始时间")
    private String starDate;
    @Field(label = "详细地址")
    private String address;
    @Field(label = "已报名未付款数量")
    private Long notPayNum;
    @Field(label = "已付款数量")
    private Long payNum;
    @Field(label = "以签到数量")
    private Long signNum;

}
