package com.zy.model.dto;

import com.zy.entity.usr.User;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by it001 on 2017/6/12.
 */
@Getter
@Setter
public class UserTeamCountDto {
    private Long countNum;
    private User.UserRank userRankEQ;

}
