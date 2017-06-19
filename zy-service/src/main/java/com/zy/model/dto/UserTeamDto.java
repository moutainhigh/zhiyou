package com.zy.model.dto;

import com.zy.entity.usr.User;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Created by it001 on 2017/6/12.
 */
@Getter
@Setter
public class UserTeamDto implements Serializable {
    private Long userId;
    private String phone;
    private String nickname;
    private long num;
    private String avatar;
    private int rank;
    private User.UserRank userRank;

}
