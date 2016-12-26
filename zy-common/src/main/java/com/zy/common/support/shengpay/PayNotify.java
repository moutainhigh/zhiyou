package com.zy.common.support.shengpay;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Administrator on 2016/12/26.
 */
@Getter
@Setter
public class PayNotify {

	String name; // 版本名称	Name	是	String(32)	版本名称,默认属性值为:B2CPayment
	String version; // 版本号	Version	是	String(20)	版本号,默认属性值为: V4.1.1.1.1
	String charset; // 字符集	Charset	是	String(10)	字符集,支持GBK、UTF-8、GB2312,默认属性值为:UTF-8
	String msgSender; // 发送方标识	MsgSender	是	String(64)	由盛付通提供,默认为:商户号(由盛付通提供的8位正整数),用于盛付通判别请求方的身份
	String sendTime; // 发送支付请求时间	SendTime	是	String(14)	用户通过商户网站提交订单的支付时间,必须为14位正整数数字,格式为:yyyyMMddHHmmss,如:20110707112233
	String instCode; // 银行编码	InstCode	是	String(256)	见附录7.1.2综合网银编码列表,机构代码列表以逗号分隔,如：InstCode=ICBC,CMB
	String orderNo; // 商户订单号	OrderNo	是	String(50)	商户订单号,50个字符内、只允许使用数字、字母,确保在商户系统唯一，商户订单号不能重复
	String orderAmount; // 支付金额	OrderAmount	是	String(14)	支付金额,必须大于0,包含2位小数如：OrderAmount=1.00
	String transNo; // 盛付通交易号	TransNo	是	String(40)	盛付通系统的交易号,商户只需记录
	String transAmount; // 盛付通实际支付金额	TransAmount	是	String(14)	用户实际支付金额
	String transStatus; // 支付状态	TransStatus	是	String(10)	见附录 7.3.1 支付状态
	String transType; // 盛付通交易类型	TransType	是	String(10)	见附录 7.1.1 支付类型
	String transTime; // 盛付通交易时间	TransTime	是	String(14)	用户通过商户网站完成交易订单的时间,必须为14位正整数数字,格式为:yyyyMMddHHmmss,如:20110707112233
	String merchantNo; // 商户号	MerchantNo	是	String(64)	商户号
	String errorCode; // 错误代码	ErrorCode	是	String(256)	商户交易错误代码
	String errorMsg; // 错误消息	ErrorMsg	是	String(256)	商户交易错误消息
	//String ext1; // 扩展1	Ext1	是	String(128)	英文或中文字符串 支付完成后，按照原样返回给商户
	String bankSerialNo; // 网银流水号	BankSerialNo	是	String(64)	银行返回的交易流水号
	String signType; // 签名类型	SignType	是	String(10)	签名类型,如：MD5
	String signMsg; // 签名串	SignMsg	是	String(14)	签名结果



}
