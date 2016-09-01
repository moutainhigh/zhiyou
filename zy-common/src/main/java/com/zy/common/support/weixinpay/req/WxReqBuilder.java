package com.zy.common.support.weixinpay.req;

import java.util.Date;

import com.zy.common.util.Identities;


public class WxReqBuilder {

	public static WxOrderReq buildJsApiWxOrderReq(String body, String outTradeNo, Integer totalFee, String spbillCreateIp, String openId, String notifyUrl, Date expireTime) {
		WxOrderReq wxOrderReq = new WxOrderReq();
		Date date = new Date();
		
		wxOrderReq.setTradeType("JSAPI");
		wxOrderReq.setBody(body);
		wxOrderReq.setOutTradeNo(outTradeNo);
		wxOrderReq.setTotalFee(totalFee);
		wxOrderReq.setSpbillCreateIp(spbillCreateIp);;
		wxOrderReq.setNotifyUrl(notifyUrl);
		wxOrderReq.setNonceStr(Identities.uuid2());
		
		wxOrderReq.setTimeStart(date);
		wxOrderReq.setTimeExpire(expireTime);
		
		wxOrderReq.setOpenId(openId);
		return wxOrderReq;
	}
	
	public static WxOrderReq buildNativeWxOrderReq(String body, String outTradeNo, Integer totalFee, String spbillCreateIp, String productId, String notifyUrl, Date expireTime) {
		WxOrderReq wxOrderReq = new WxOrderReq();
		Date date = new Date();
		
		wxOrderReq.setTradeType("NATIVE");
		wxOrderReq.setBody(body);
		wxOrderReq.setOutTradeNo(outTradeNo);
		wxOrderReq.setTotalFee(totalFee);
		wxOrderReq.setSpbillCreateIp(spbillCreateIp);
		wxOrderReq.setNotifyUrl(notifyUrl);
		wxOrderReq.setNonceStr(Identities.uuid2());
		
		wxOrderReq.setTimeStart(date);
		wxOrderReq.setTimeExpire(expireTime);
		
		wxOrderReq.setProductId(productId);
		return wxOrderReq;
	}
	
}
