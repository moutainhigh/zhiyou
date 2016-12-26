package com.zy.common.support.shengpay;

import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/12/26.
 */
@Getter
@Setter
public class PayCreate {

	String name = "B2CPayment"; // 版本名称	Name	是	String(32)	版本名称,默认属性值为:B2CPayment
	String version = "V4.1.1.1.1"; // 版本号	Version	是	String(20)	版本号,默认属性值为: V4.1.1.1.1
	String charset = "UTF-8"; // 字符集	Charset	是	String(10)	字符集,支持GBK、UTF-8、GB2312,默认属性值为:UTF-8
	String msgSender; // 发送方标识	MsgSender	是	String(64)	由盛付通提供,默认为:商户号(由盛付通提供的8位正整数),用于盛付通判别请求方的身份
	String orderNo; // 商户订单号	OrderNo	是	String(50)	商户订单号,50个字符内、只允许使用数字、字母,确保在商户系统唯一，商户订单号不能重复
	String orderAmount; // 支付金额	OrderAmount	是	String(14)	支付金额,必须大于0,包含2位小数如：OrderAmount=1.00
	String orderTime; // 商户订单提交时间	OrderTime	是	String(14)	商户提交用户订单时间,必须为14位正整数数字,格式为:yyyyMMddHHmmss,如:OrderTime=20110808112233
	String payType = "PT001"; // 支付类型编码	PayType	否	String(10)	见附录7.1.1支付类型如:PayType=PT001为空时默认显示合同规定的全部渠道
	String pageUrl; // 支付成功后客户端浏览器回调地址 	PageUrl	是	String(256)	客户端浏览器回调地址,支付成功后,将附带回调数据跳转到此页面,商户可以进行相关处理并显示给终端用户,如:http://www.testpay.com/testpay.jsp
	String backUrl; // 在收银台跳转到商户指定的地址	BackUrl 	否	String(256)	为了让客户提交订单到收银台后，能随时返回到商户页面，在收银台首页及其他个别页面，放置了返回商户页面的按钮，点击即跳转到商户对该字段指定的地址。如果商户没有指定该字段的值，则收银台不会显示返回商户页面的按钮；如果商户指定了该页面的地址，则收银台显示返回商户页面的按钮，点击跳转到商户指定的页面。
	String notifyUrl; // 服务端通知发货地址	NotifyUrl	是	String(256)	服务端通知发货地址,支付成功后,盛付通将发送“支付成功”信息至该地址,通知商户发货,商户收到信息后,需返回相应信息至盛付通,表明已收到发货通知。返回信息只能定义为“OK”（注意为大写英文字母）,返回其他信息均为失败,如:http://www.testpay.com/testpay.jsp
	String productName; // 商品名称	ProductName	否	String(56)	商品名称,如:ProductName=测试商品test
	String buyerIp; // 买家IP地址	BuyerIp	是	String(20)	防钓鱼用,买家的ip地址
	String signType = "MD5"; // 签名类型	SignType	是	String(10)	签名类型,如：MD5
	String signMsg;

	//BackUrl=http://zt.net&
	// BuyerIp=127.0.0.1&
	// Charset=UTF-8&
	// MsgSender=10100894&
	// Name=B2CPayment&
	// NotifyUrl=http://zt.net&
	// OrderAmount=1.00&
	// OrderNo=test001&
	// OrderTime=20161226160134&
	// PageUrl=http://zt.net&
	// PayType=PT001&SignType=MD5&
	// SingMsg=F2D96779DF0DCE1E72F92E64467BC82D&Version=V4.1.1.1.1

	public Map<String, String> toMap() {
		Map map = new LinkedHashMap<>();
		map.put("Name", name);
		map.put("Version", version);
		map.put("Charset", charset);
		map.put("MsgSender", msgSender);
		map.put("OrderNo", orderNo);
		map.put("OrderAmount", orderAmount);
		map.put("OrderTime", orderTime);
		map.put("PayType", payType);
		map.put("PageUrl", pageUrl);
		map.put("BackUrl", backUrl);
		map.put("NotifyUrl", notifyUrl);
		map.put("ProductName", productName);
		map.put("BuyerIp", buyerIp);
		map.put("SignType", signType);
		map.put("SignMsg", signMsg);
		return map;
	}

}
