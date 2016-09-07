package com.zy.vo;

import io.gd.generator.annotation.Field;
import com.zy.entity.mal.Order.OrderStatus;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Date;
import java.util.ArrayList;

@Getter
@Setter
public class OrderDetailVo implements Serializable {
	/* 原生 */
	@Field(label = "id")
	private Long id;
	@Field(label = "订单编号")
	private String sn;
	@Field(label = "用户id")
	private Long userId;
	@Field(label = "卖家id")
	private Long sellerId;
	@Field(label = "卖家id")
	private Long parentId;
	@Field(label = "标题")
	private String title;
	@Field(label = "订单状态")
	private OrderStatus orderStatus;
	@Field(label = "应付总金额")
	private BigDecimal amount;
	@Field(label = "退款金额")
	private BigDecimal refund;
	@Field(label = "退款备注")
	private String refundRemark;
	@Field(label = "买家留言")
	private String buyerMemo;
	@Field(label = "卖家留言")
	private String sellerMemo;
	@Field(label = "备注")
	private String remark;
	@Field(label = "发货时间")
	private Date deliveredTime;
	@Field(label = "物流公司名")
	private String logisticsName;
	@Field(label = "物流单号")
	private String logisticsSn;
	@Field(label = "物流费")
	private BigDecimal logisticsFee;
	@Field(label = "收件人区域")
	private Long receiverAreaId;
	@Field(label = "收件人姓名")
	private String receiverRealname;
	@Field(label = "收件人电话")
	private String receiverPhone;
	@Field(label = "收件人省份")
	private String receiverProvince;
	@Field(label = "收件人城市")
	private String receiverCity;
	@Field(label = "收件人地区")
	private String receiverDistrict;
	@Field(label = "收件人详细地址")
	private String receiverAddress;

	/* 扩展 */
	@Field(label = "下单时间")
	private String createdTimeLabel;
	@Field(label = "过期时间")
	private String expiredTimeLabel;
	@Field(label = "支付时间")
	private String paidTimeLabel;
	@Field(label = "退款时间")
	private String refundedTimeLabel;
	@Field(label = "发货时间")
	private String deliveredTimeLabel;
	@Field(label = "orderItems")
	private List<OrderItemVo> orderItems = new ArrayList<>();

}