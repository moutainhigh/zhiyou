package com.zy.vo;

import io.gd.generator.annotation.Field;
import com.zy.model.ImageVo;
import com.zy.entity.mal.Order.OrderStatus;
import com.zy.entity.mal.Order.LogisticsFeePayType;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Date;
import java.util.ArrayList;

@Getter
@Setter
public class OrderAdminFullVo implements Serializable {
	/* 原生 */
	@Field(label = "id")
	private Long id;
	@Field(label = "订单编号")
	private String sn;
	@Field(label = "用户id")
	private Long userId;
	@Field(label = "卖家id")
	private Long sellerId;
	@Field(label = "是否支付给平台")
	private Boolean isPayToPlatform;
	@Field(label = "标题")
	private String title;
	@Field(label = "订单状态")
	private OrderStatus orderStatus;
	@Field(label = "应付总金额")
	private BigDecimal amount;
	@Field(label = "退款金额")
	private BigDecimal refund;
	@Field(label = "买家留言")
	private String buyerMemo;
	@Field(label = "卖家留言")
	private String sellerMemo;
	@Field(label = "备注")
	private String remark;
	@Field(label = "是否已结算")
	private Boolean isSettledUp;
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
	@Field(label = "银行汇款截图")
	private String offlineImage;
	@Field(label = "银行汇款备注")
	private String offlineMemo;
	@Field(label = "是否删除")
	private Boolean isDeleted;

	/* 扩展 */
	@Field(label = "imageThumbnail")
	private String imageThumbnail;
	@Field(label = "price")
	private BigDecimal price;
	@Field(label = "priceLabel")
	private String priceLabel;
	@Field(label = "quantity")
	private Long quantity;
	@Field(label = "用户id")
	private UserAdminSimpleVo user;
	@Field(label = "卖家id")
	private UserAdminSimpleVo seller;
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
	@Field(label = "是否物流发货")
	private String useLogisticsLabel;
	@Field(label = "发货时间")
	private String deliveredTimeLabel;
	@Field(label = "payments")
	private List<PaymentAdminVo> payments = new ArrayList<>();
	@Field(label = "profits")
	private List<ProfitAdminVo> profits = new ArrayList<>();
	@Field(label = "transfers")
	private List<TransferAdminVo> transfers = new ArrayList<>();
	@Field(label = "银行汇款截图")
	private List<ImageVo> offlineImages = new ArrayList<>();

}