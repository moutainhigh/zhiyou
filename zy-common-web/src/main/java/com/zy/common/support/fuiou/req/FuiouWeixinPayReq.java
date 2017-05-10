package com.zy.common.support.fuiou.req;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FuiouWeixinPayReq {

	private String fyMerchantCD; // 商户号
	private String outOrderNum; // 订单sn
	private String term_id; // 终端id
	private String tranAmt; // 交易金额
	private String SignMsg; // MD5签名
	private String mchCreateIp; // ip地址
	private String phone; // 用户手机号
	private String openid; // 用户openid
	private String notifyUrl; // 回调地址
	private String remark; // 备注

}
