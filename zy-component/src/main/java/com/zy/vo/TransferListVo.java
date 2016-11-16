package com.zy.vo;

import io.gd.generator.annotation.Field;
import com.zy.entity.fnc.Transfer.TransferStatus;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class TransferListVo implements Serializable {
	/* 原生 */
	@Field(label = "id")
	private Long id;
	@Field(label = "转账状态")
	private TransferStatus transferStatus;
	@Field(label = "转账单号")
	private String sn;
	@Field(label = "转账标题")
	private String title;

	/* 扩展 */
	@Field(label = "转出用户id")
	private UserListVo fromUser;
	@Field(label = "转入用户id")
	private UserListVo toUser;
	@Field(label = "金额")
	private String amountLabel;
	@Field(label = "创建时间")
	private String createdTimeLabel;
	@Field(label = "转账时间")
	private String transferredTimeLabel;

}