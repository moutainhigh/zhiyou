package com.zy.model.dto;

import com.zy.entity.usr.User;
import io.gd.generator.annotation.Field;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Created by it001 on 2017/6/23.
 */
@Getter
@Setter
public class UserDto implements Serializable {
    @Field(label = "id")
    private Long id;
    @Field(label = "用户级别")
    private User.UserRank userRank;
    @Field(label = "用户昵称")
    private String nickname;
    @Field(label = "推荐人昵称")
    private String pnickname;
    @Field(label = "用户头像")
    private String avatar;
    @Field(label = "用户手机号")
    private String phone;
    @Field(label = "推荐人手机号")
    private String pphone;

}
