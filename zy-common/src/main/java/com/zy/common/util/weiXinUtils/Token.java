package com.zy.common.util.weiXinUtils;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by it001 on 2017/7/25.
 * 接口凭证
 */
@Getter
@Setter
public class Token {
    // 接口访问凭证
    private String accessToken;
    // 凭证有效期，单位：秒
    private int expiresIn;
}
