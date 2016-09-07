package com.zy.vo;

import io.gd.generator.annotation.Field;
import com.zy.entity.sys.ConfirmStatus;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
public class BankCardAdminVo implements Serializable {
	/* 原生 */
	@Field(label = "id")
	private Long id;
	@Field(label = "用户id")
	private Long userId;
	@Field(label = "开户名")
	private String realname;
	@Field(label = "银行卡号")
	private String cardNumber;
	@Field(label = "开户行id")
	private Long bankId;
	@Field(label = "开户行名")
	private String bankName;
	@Field(label = "开户支行")
	private String bankBranchName;
	@Field(label = "是否默认银行卡")
	private Boolean isDefault;
	@Field(label = "审核状态")
	private ConfirmStatus confirmStatus;
	@Field(label = "审核备注")
	private String confirmRemark;
	@Field(label = "审核通过时间")
	private Date confirmedTime;
	@Field(label = "申请时间")
	private Date appliedTime;

	/* 扩展 */
	@Field(label = "用户id")
	private UserAdminSimpleVo user;
	@Field(label = "开户行编码")
	private String bankCode;

}