package com.zy.common.support.weixinpay.req;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="xml")
public class WxOrderNotifyReq {

	// common
		private String returnCode; // 返回状态码
		private String returnMsg; // 返回信息

		// base
		private String appId; // 公众账号ID
		private String mchId; // 商户号
		private String deviceInfo; // 设备号
		private String nonceStr; // 随机字符串
		private String sign; // sign
		private String resultCode; // 业务结果
		private String errCode; // 错误代码
		private String errCodeDes; // 错误代码描述

		// order
		private String openId; // 用户在商户appid下的唯一标识
		private String isSubscribe; // 用户是否关注公众账号，Y-关注，N-未关注，仅在公众账号类型支付有效
		private String tradeType;// JSAPI、NATIVE、MICROPAY、APP
		private String bankType;//银行类型，采用字符串类型的银行标识
		private Integer totalFee;//订单总金额，单位为分
		private Integer couponFee;//现金券支付金额<=订单总金额，订单总金额-现金券金额为现金支付金额
		private String feeType;//货币类型，符合ISO 4217标准的三位字母代码，默认人民币：CNY
		private String transactionId;//微信支付订单号
		private String outTradeNo; //商户系统的订单号，与请求一致
		private String attach; // 商家数据包，原样返回
		private String timeEnd; //支付完成时间，格式为yyyyMMddhhmmss，如2009年12月27日9点10分10秒表示为20091227091010。时区为GMT+8 beijing。该时间取自微信支付服务器
		
		//追加
		private Integer cashFee;
		
		
		@XmlElement(name = "cash_fee")
		public Integer getCashFee() {
			return cashFee;
		}

		public void setCashFee(Integer cashFee) {
			this.cashFee = cashFee;
		}

		@XmlElement(name = "return_code")
		public String getReturnCode() {
			return returnCode;
		}

		public void setReturnCode(String returnCode) {
			this.returnCode = returnCode;
		}

		@XmlElement(name = "return_msg")
		public String getReturnMsg() {
			return returnMsg;
		}

		public void setReturnMsg(String returnMsg) {
			this.returnMsg = returnMsg;
		}

		@XmlElement(name = "appid")
		public String getAppId() {
			return appId;
		}

		public void setAppId(String appId) {
			this.appId = appId;
		}

		@XmlElement(name = "mch_id")
		public String getMchId() {
			return mchId;
		}

		public void setMchId(String mchId) {
			this.mchId = mchId;
		}

		@XmlElement(name = "device_info")
		public String getDeviceInfo() {
			return deviceInfo;
		}

		public void setDeviceInfo(String deviceInfo) {
			this.deviceInfo = deviceInfo;
		}

		@XmlElement(name = "nonce_str")
		public String getNonceStr() {
			return nonceStr;
		}

		public void setNonceStr(String nonceStr) {
			this.nonceStr = nonceStr;
		}

		@XmlElement(name = "sign")
		public String getSign() {
			return sign;
		}

		public void setSign(String sign) {
			this.sign = sign;
		}

		@XmlElement(name = "result_code")
		public String getResultCode() {
			return resultCode;
		}

		public void setResultCode(String resultCode) {
			this.resultCode = resultCode;
		}

		@XmlElement(name = "err_code")
		public String getErrCode() {
			return errCode;
		}

		public void setErrCode(String errCode) {
			this.errCode = errCode;
		}

		@XmlElement(name = "err_code_des")
		public String getErrCodeDes() {
			return errCodeDes;
		}

		public void setErrCodeDes(String errCodeDes) {
			this.errCodeDes = errCodeDes;
		}

		@XmlElement(name = "trade_type")
		public String getTradeType() {
			return tradeType;
		}

		public void setTradeType(String tradeType) {
			this.tradeType = tradeType;
		}

		@XmlElement(name = "openid")
		public String getOpenId() {
			return openId;
		}

		public void setOpenId(String openId) {
			this.openId = openId;
		}

		@XmlElement(name = "is_subscribe")
		public String getIsSubscribe() {
			return isSubscribe;
		}

		public void setIsSubscribe(String isSubscribe) {
			this.isSubscribe = isSubscribe;
		}

		@XmlElement(name = "bank_type")
		public String getBankType() {
			return bankType;
		}

		public void setBankType(String bankType) {
			this.bankType = bankType;
		}

		@XmlElement(name = "total_fee")
		public Integer getTotalFee() {
			return totalFee;
		}

		public void setTotalFee(Integer totalFee) {
			this.totalFee = totalFee;
		}

		@XmlElement(name = "coupon_fee")
		public Integer getCouponFee() {
			return couponFee;
		}

		public void setCouponFee(Integer couponFee) {
			this.couponFee = couponFee;
		}

		@XmlElement(name = "fee_type")
		public String getFeeType() {
			return feeType;
		}

		public void setFeeType(String feeType) {
			this.feeType = feeType;
		}

		@XmlElement(name = "transaction_id")
		public String getTransactionId() {
			return transactionId;
		}

		public void setTransactionId(String transactionId) {
			this.transactionId = transactionId;
		}

		@XmlElement(name = "out_trade_no")
		public String getOutTradeNo() {
			return outTradeNo;
		}

		public void setOutTradeNo(String outTradeNo) {
			this.outTradeNo = outTradeNo;
		}

		@XmlElement(name = "attach")
		public String getAttach() {
			return attach;
		}

		public void setAttach(String attach) {
			this.attach = attach;
		}

		@XmlElement(name = "time_end")
		public String getTimeEnd() {
			return timeEnd;
		}

		public void setTimeEnd(String timeEnd) {
			this.timeEnd = timeEnd;
		}
	
}
