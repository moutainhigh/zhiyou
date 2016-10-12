package com.zy.vo;

import io.gd.generator.annotation.Field;
import com.zy.model.ImageVo;
import com.zy.entity.mal.Order.OrderStatus;
import com.zy.entity.usr.User.UserRank;
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
	@Field(label = "买家用户等级")
	private UserRank buyerUserRank;
	@Field(label = "卖家id")
	private Long sellerId;
	@Field(label = "卖家用户等级")
	private UserRank sellerUserRank;
	@Field(label = "是否支付给平台")
	private Boolean isPayToPlatform;
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
	@Field(label = "奖励是否结算")
	private Boolean isProfitSettledUp;
	@Field(label = "是否已结算")
	private Boolean isSettledUp;
	@Field(label = "是否已拷贝")
	private Boolean isCopied;
	@Field(label = "拷贝时间")
	private Date copiedTime;
	@Field(label = "对应订单id")
	private Long refId;
	@Field(label = "发货时间")
	private Date deliveredTime;
	@Field(label = "是否物流发货")
	private Boolean isUseLogistics;
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
	@Field(label = "银行汇款截图")
	private String offlineImage;
	@Field(label = "银行汇款备注")
	private String offlineMemo;
	@Field(label = "商品id")
	private Long productId;
	@Field(label = "单价")
	private BigDecimal price;
	@Field(label = "数量")
	private Long quantity;

	/* 扩展 */
	@Field(label = "isPlatformDeliver")
	private Boolean isPlatformDeliver;
	@Field(label = "用户id")
	private UserAdminSimpleVo user;
	@Field(label = "买家用户等级")
	private String buyerUserRankLabel;
	@Field(label = "卖家id")
	private UserAdminSimpleVo seller;
	@Field(label = "卖家用户等级")
	private String sellerUserRankLabel;
	@Field(label = "下单时间")
	private String createdTimeLabel;
	@Field(label = "过期时间")
	private String expiredTimeLabel;
	@Field(label = "支付时间")
	private String paidTimeLabel;
	@Field(label = "退款时间")
	private String refundedTimeLabel;
	@Field(label = "订单状态")
	private String orderStatusStyle;
	@Field(label = "应付总金额")
	private String amountLabel;
	@Field(label = "拷贝时间")
	private String copiedTimeLabel;
	@Field(label = "发货时间")
	private String deliveredTimeLabel;
	@Field(label = "是否物流发货")
	private String useLogisticsLabel;
	@Field(label = "商品图")
	private String imageThumbnail;
	@Field(label = "商品图")
	private String imageThumbnail;
	@Field(label = "单价")
	private String priceLabel;
	@Field(label = "银行汇款截图")
	private List<ImageVo> offlineImages = new ArrayList<>();

}