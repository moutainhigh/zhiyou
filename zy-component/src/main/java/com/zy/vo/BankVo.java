package com.zy.vo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class BankVo implements Serializable {
	/* 原生 */
	private Long id;
	private String name;
	private String code;

	/* 扩展 */

}