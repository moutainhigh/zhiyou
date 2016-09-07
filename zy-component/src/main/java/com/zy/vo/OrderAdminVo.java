package com.zy.vo;

import io.gd.generator.annotation.Field;
import com.zy.entity.mal.Order.OrderStatus;
import com.zy.entity.mal.Order.LogisticsFeePayType;
import com.zy.entity.fnc.CurrencyType;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Date;
import java.util.ArrayList;

@Getter
@Setter
public class OrderAdminVo implements Serializable {
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
	@Field(label = "货币类型")
	private CurrencyType currencyType;
	@Field(label = "订单状态")
	private OrderStatus orderStatus;
	@Field(label = "优惠金额")
	private BigDecimal discountFee;
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
	@Field(label = "是否已结算")
	private Boolean isSettledUp;
	@Field(label = "是否物流发货")
	private Boolean useLogistics;
	@Field(label = "物流费支付类型")
	private LogisticsFeePayType logisticsFeePayType;
	@Field(label = "是否平台发货")
	private Boolean isPlatformDeliver;
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
	@Field(label = "下单时间",order = 999)
	private String createdTimeLabel;
	@Field(label = "过期时间",order = 999)
	private String expiredTimeLabel;
	@Field(label = "支付时间",order = 999)
	private String paidTimeLabel;
	@Field(label = "退款时间",order = 999)
	private String refundedTimeLabel;
	@Field(label = "是否物流发货",order = 999)
	private Boolean useLogisticsLabel;
	@Field(label = "发货时间",order = 999)
	private String deliveredTimeLabel;
	@Field(label = "orderItems",order = 999)
	private List<OrderItemAdminVo> orderItems = new ArrayList<>();

}