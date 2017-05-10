package com.zy.common.support.fuiou.req;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FuiouWeixinPayNotifyReq {

	private String businessCode; // 商户预留信息
	private String desc; // 交易描述
	private String goodDedc; // 商户名称
	private String openid; // 微信id唯一标示
	private String orderNo; // 系统订单号
	private String order_type; // 交易类型
	private String outOrderNum; // 商户订单号
	private String remark; // 交易自定义参数
	private String tradeStatus; // 交易状态:{-1:支付失败, 0:未支付, 1:支付成功, 2:已退款, 3:已冲正, 4:订单关闭}
	private String tradeTime; // 交易时间
	private String url; // 服务器端显示交易详情地址，（未用）

	private String signMsg; // 签名信息

}
