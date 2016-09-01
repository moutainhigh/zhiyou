package com.gc.vo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class BankCardVo implements Serializable {
	/* 原生 */
	private Long id;
	private Long userId;
	private String realname;
	private String cardNumber;
	private String bankName;
	private String bankBranchName;

	/* 扩展 */

}