package com.zy.common.support.shengpay;

import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/3/9.
 */
@Getter
@Setter
public class PayCreateMobile {

	String merchantNo;  // 商户号	merchantNo	是	String(32)	由盛付通提供,默认为:商户号(由盛付通提供的8位正整数),用于盛付通判别请求方的身份
	String charset = "UTF-8";  // 字符集	charset	否	String(10)	支持GBK、UTF-8、GB2312,默认属性值为:UTF-8
	String requestTime;  // 请求时间	requestTime	是	String(10)	请求时间: yyyyMMddHHmmss,如:20110707112233
	String outMemberId;  // 商户会员标识	outMemberId	是	String(32)	商户系统内针对会员的唯一标识，不同会员的标识必须唯一
	String outMemberRegistTime;  // 商户会员注册时间	outMemberRegistTime	是	String(32)	商户会员注册时间yyyyMMddHHmmss,如:20110707112233
	String outMemberRegistIP;  // 商户会员注册IP	outMemberRegistIP	是	String(20)	商户会员注册IP
	String outMemberVerifyStatus;  // 商户会员是否已实名	outMemberVerifyStatus	是	String(32)	商户会员是否已实名(1实名0未实名)
	String outMemberName;  // 商户会员注册姓名	outMemberName	是	String(16)	商户会员注册姓名
	String outMemberMobile;  // 商户会员注册手机号	outMemberMobile	是	String(11)	商户会员注册手机号
	String merchantOrderNo;  // 商户订单号	merchantOrderNo	是	String(32)	商户订单号，必须唯一, 商户订单号不能重复
	String productName;  // 商品名称	productName	是	String(256)	商品名称
	//String productDesc;  // 商品描述	productDesc	否	String(1000)	商品描述
	String currency = "CNY";  // 货币类型	currency	是	String()	货币类型，见枚举常量定义：Currency
	String amount;  // 交易金额	amount	是	String(20)	交易金额,单位是元
	String pageUrl;  // 前台通知回调地址	pageUrl	否	String(256)	用户选择新快捷支付,支付成功后,将附带回调数据跳转到此页面,商户可以进行相关处理并显示给终端用户,如:http://www.testpay.com/testpay.jsp
	String notifyUrl;  // 后台通知回调地址	notifyUrl	否	String(1024)	通知回调地址
	String userIP;  // 用户IP	userIP	是	String(20)	用户IP（用户下单时的IP），如何获取见 FAQ 5.7.1参考枚举常量定义：bankCardType
	//String bankCardType = "DR";  // 银行卡类型	bankCardType	否	String(2)	银行卡类型，参考枚举常量定义：bankCardType
	//String bankCode = "ICBC";  // 机构代码	bankCode	否	String(10)	机构代码，参考银行机构列表
	//String exts;  // 扩展字段	exts	否	String(256)	扩展属性,JSON串,例如：{key:value}特殊商户该字段如何传入请参阅FAQ 5.7.2
	String signType = "MD5";  // 签名类型	signType	是	String(10)	签名类型，支持的签名方式有: MD5, RSA，SNKRSA
	String signMsg;  // 签名消息	signMsg	是	String(512)	签名消息

	public Map<String, String> toMap() {
		Map map = new LinkedHashMap<>();
		map.put("merchantNo", merchantNo);
		map.put("charset", charset);
		map.put("requestTime", requestTime);
		map.put("outMemberId", outMemberId);
		map.put("outMemberRegistTime", outMemberRegistTime);
		map.put("outMemberRegistIP", outMemberRegistIP);
		map.put("outMemberVerifyStatus", outMemberVerifyStatus);
		map.put("outMemberName", outMemberName);
		map.put("outMemberMobile", outMemberMobile);
		map.put("merchantOrderNo", merchantOrderNo);
		map.put("productName", productName);
		//map.put("productDesc", productDesc);
		map.put("currency", currency);
		map.put("amount", amount);
		map.put("pageUrl", pageUrl);
		map.put("notifyUrl", notifyUrl);
		map.put("userIP", userIP);
		//map.put("bankCardType", bankCardType);
		//map.put("bankCode", bankCode);
		//map.put("exts", exts);
		map.put("signType", signType);
		map.put("signMsg", signMsg);
		return map;
	}

}
