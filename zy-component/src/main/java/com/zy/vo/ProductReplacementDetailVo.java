package com.zy.vo;

import com.zy.entity.mal.ProductReplacement.ProductReplacementStatus;
import io.gd.generator.annotation.Field;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
public class ProductReplacementDetailVo implements Serializable {
	/* 原生 */
	@Field(label = "id")
	private Long id;
	@Field(label = "服务商")
	private Long userId;
	@Field(label = "更换状态")
	private ProductReplacementStatus productReplacementStatus;
	@Field(label = "被更换产品")
	private String fromProduct;
	@Field(label = "更换产品")
	private String toProduct;
	@Field(label = "数量")
	private Long quantity;
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
	@Field(label = "更换时间")
	private Date createdTime;
	@Field(label = "发货时间")
	private Date deliveredTime;
	@Field(label = "物流公司名")
	private String logisticsName;
	@Field(label = "物流单号")
	private String logisticsSn;
	@Field(label = "物流费")
	private BigDecimal logisticsFee;
	@Field(label = "备注")
	private String remark;

	/* 扩展 */
	@Field(label = "更换时间")
	private String createdTimeLabel;
	@Field(label = "发货时间")
	private String deliveredTimeLabel;

}