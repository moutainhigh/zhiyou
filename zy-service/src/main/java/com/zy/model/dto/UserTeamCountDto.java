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
public class UserTeamCountDto implements Serializable {
    private Long totalnumber;
    private User.UserRank userRankEQ;

}
