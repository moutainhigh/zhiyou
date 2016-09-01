package com.zy.extend;

import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxRedpackResult;

import java.math.BigDecimal;

/**
 * Created by freeman on 16/8/4.
 */
public interface SKWxMpService extends WxMpService {
    /**
     * @param sn
     * @param userOpenId 用户的openid
     * @param amount 体现金额
     * @param title 一般写微信提现
     * @return
     * @throws WxErrorException
     */
    WxRedpackResult transfer(String sn, String userOpenId, BigDecimal amount, String title) throws WxErrorException;


}




