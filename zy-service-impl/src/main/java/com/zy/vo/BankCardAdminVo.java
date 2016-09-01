package com.gc.vo;

import com.gc.entity.sys.ConfirmStatus;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
public class BankCardAdminVo implements Serializable {
	/* 原生 */
	private Long id;
	private Long userId;
	private String realname;
	private String cardNumber;
	private String bankName;
	private String bankBranchName;
	private ConfirmStatus confirmStatus;
	private String confirmRemark;
	private Date confirmedTime;
	private Date appliedTime;

	/* 扩展 */
	private UserAdminSimpleVo user;

}