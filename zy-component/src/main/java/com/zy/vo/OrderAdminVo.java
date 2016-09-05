package com.zy.vo;

import com.zy.entity.mal.Order.OrderStatus;
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
	private Long id;
	private String sn;
	private Long userId;
	private Long sellerId;
	private String title;
	private CurrencyType currencyType;
	private OrderStatus orderStatus;
	private BigDecimal postFee;
	private BigDecimal discountFee;
	private BigDecimal amount;
	private BigDecimal refund;
	private String refundRemark;
	private String remark;
	private Boolean isSettledUp;
	private String logisticsName;
	private String logisticsSn;
	private Long receiverAreaId;
	private String receiverRealname;
	private String receiverPhone;
	private String receiverProvince;
	private String receiverCity;
	private String receiverDistrict;
	private String receiverAddress;
	private Date deliveredTime;

	/* 扩展 */
	private String createdTimeLabel;
	private String expiredTimeLabel;
	private String paidTimeLabel;
	private String refundedTimeLabel;
	private String deliveredTimeLabel;
	private List<OrderItemAdminVo> orderItems = new ArrayList<>();

}