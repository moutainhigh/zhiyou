package com.zy.vo;

import io.gd.generator.annotation.Field;
import com.zy.entity.fnc.CurrencyType;
import com.zy.entity.fnc.Transfer.TransferType;
import com.zy.entity.fnc.Transfer.TransferStatus;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
public class TransferAdminVo implements Serializable {
	/* 原生 */
	@Field(label = "id")
	private Long id;
	@Field(label = "转账状态")
	private TransferStatus transferStatus;
	@Field(label = "转出用户id")
	private Long fromUserId;
	@Field(label = "转入用户id")
	private Long toUserId;
	@Field(label = "转账单号")
	private String sn;
	@Field(label = "转账标题")
	private String title;
	@Field(label = "币种")
	private CurrencyType currencyType;
	@Field(label = "金额")
	private BigDecimal amount;
	@Field(label = "创建时间")
	private Date createdTime;
	@Field(label = "转账时间")
	private Date transferredTime;
	@Field(label = "转账类型")
	private TransferType transferType;
	@Field(label = "关联业务id")
	private Long refId;
	@Field(label = "转账备注")
	private String transferRemark;
	@Field(label = "备注")
	private String remark;

	/* 扩展 */
	@Field(label = "转出用户id")
	private UserAdminSimpleVo fromUser;
	@Field(label = "转入用户id")
	private UserAdminSimpleVo toUser;

}