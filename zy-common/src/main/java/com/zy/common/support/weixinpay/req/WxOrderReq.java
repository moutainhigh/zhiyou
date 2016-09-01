package com.zy.common.support.weixinpay.req;

import java.util.Date;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.zy.common.support.weixinpay.DateAdapter;


@XmlRootElement(name="xml")
public class WxOrderReq {

	/* 必填 */
	private String tradeType; // JSAPI、NATIVE、APP
	private String body; // 商品标题
	private String outTradeNo; // 商户订单号
	private Integer totalFee; // 订单总金额只能为整数 接口中参数支付金额单位为【分】，参数值不能带小数
	private String spbillCreateIp; // APP和网页支付提交用户端ip，Native支付填调用微信支付API的机器IP
	private String notifyUrl; // 通知地址
	private String nonceStr;
	
	/* client 填写 */
	private String appId; // 公众账号ID
	private String mchId; // 商户号 微信支付分配的商户号
	private String sign;
	
	/* 选填 */
	private Date timeStart;
	private Date timeExpire;
	
	/* 特殊选填 */
	private String productId; // 商品ID trade_type=NATIVE，此参数必传。此id为二维码中包含的商品ID，商户自行定义。 
	private String openId; // 用户标识 trade_type=JSAPI，此参数必传，用户在商户appid下的唯一标识。下单前需要调用【网页授权获取用户信息】接口获取到用户的Openid。

	@XmlElement(name="time_start")
	@XmlJavaTypeAdapter(DateAdapter.class)
	public Date getTimeStart() {
		return timeStart;
	}

	public void setTimeStart(Date timeStart) {
		this.timeStart = timeStart;
	}
	
	@XmlElement(name="time_expire")
	@XmlJavaTypeAdapter(DateAdapter.class)
	public Date getTimeExpire() {
		return timeExpire;
	}

	public void setTimeExpire(Date timeExpire) {
		this.timeExpire = timeExpire;
	}
	
	@XmlElement(name = "sign")
	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
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
	
	@XmlElement(name = "nonce_str")
	public String getNonceStr() {
		return nonceStr;
	}
	public void setNonceStr(String nonceStr) {
		this.nonceStr = nonceStr;
	}
	
	@XmlElement(name = "body")
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	
	@XmlElement(name = "out_trade_no")
	public String getOutTradeNo() {
		return outTradeNo;
	}
	public void setOutTradeNo(String outTradeNo) {
		this.outTradeNo = outTradeNo;
	}
	
	@XmlElement(name = "total_fee")
	public Integer getTotalFee() {
		return totalFee;
	}
	public void setTotalFee(Integer totalFee) {
		this.totalFee = totalFee;
	}
	
	@XmlElement(name = "spbill_create_ip")
	public String getSpbillCreateIp() {
		return spbillCreateIp;
	}
	public void setSpbillCreateIp(String spbillCreateIp) {
		this.spbillCreateIp = spbillCreateIp;
	}
	
	@XmlElement(name = "notify_url")
	public String getNotifyUrl() {
		return notifyUrl;
	}
	public void setNotifyUrl(String notifyUrl) {
		this.notifyUrl = notifyUrl;
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

	@XmlElement(name = "product_id")
	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

}
