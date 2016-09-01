package com.zy.entity.mal;

import io.gd.generator.annotation.Field;
import io.gd.generator.annotation.Type;
import io.gd.generator.annotation.query.Query;
import io.gd.generator.annotation.query.QueryModel;
import io.gd.generator.api.query.Predicate;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

import org.hibernate.validator.constraints.NotBlank;

import com.zy.common.extend.StringBinder;

@Entity
@Table(name = "mal_delivery")
@Getter
@Setter
@QueryModel
@Type(label = "发货单")
public class Delivery implements Serializable {

	public enum DeliveryStatus {
		待发货, 已发货, 已取消;
	}

	@Id
	@Query(Predicate.IN)
	@Field(label = "id")
	private Long id;

	@NotNull
	@Field(label = "订单id")
	private Long orderId;

	@NotBlank
	@Query(Predicate.LK)
	@Field(label = "物流公司名")
	private String name;

	@NotBlank
	@Query(Predicate.LK)
	@Field(label = "物流单号")
	private String sn;

	@NotNull
	@Query(Predicate.EQ)
	@Field(label = "状态")
	private DeliveryStatus deliveryStatus;

	@Field(label = "发件人区域")
	private Long senderAreaId;

	@StringBinder
	@Field(label = "发件人姓名")
	private String senderRealname; // 发件人姓名

	@StringBinder
	@Field(label = "发件人电话")
	private String senderPhone;

	@Field(label = "发件人省份")
	private String senderProvince;

	@Field(label = "发件人城市")
	private String senderCity;

	@Field(label = "发件人地区")
	private String senderDistrict;

	@StringBinder
	@Field(label = "发件人详细地址")
	private String senderAddress;

	@NotNull
	@Field(label = "收件人区域")
	private Long receiverAreaId;

	@NotBlank
	@StringBinder
	@Field(label = "收件人姓名")
	private String receiverRealname;

	@NotBlank
	@StringBinder
	@Field(label = "收件人电话")
	private String receiverPhone;

	@NotBlank
	@Field(label = "收件人省份")
	private String receiverProvince;

	@NotBlank
	@Field(label = "收件人城市")
	private String receiverCity;

	@NotBlank
	@Field(label = "收件人地区")
	private String receiverDistrict;

	@NotBlank
	@StringBinder
	@Field(label = "收件人详细地址")
	private String receiverAddress;


}