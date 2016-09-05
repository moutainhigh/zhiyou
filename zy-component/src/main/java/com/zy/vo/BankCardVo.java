package com.zy.vo;

import io.gd.generator.annotation.Field;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class BankCardVo implements Serializable {
	/* 原生 */
	@Field(label = "id")
	private Long id;
	@Field(label = "用户id")
	private Long userId;
	@Field(label = "开户名")
	private String realname;
	@Field(label = "银行卡号")
	private String cardNumber;
	@Field(label = "开户行")
	private String bankName;
	@Field(label = "开户支行")
	private String bankBranchName;
	@Field(label = "是否默认地址")
	private Boolean isDefault;

	/* 扩展 */

}