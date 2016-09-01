package com.zy.model;

import java.io.Serializable;

public class Principal implements Serializable {

	private static final long serialVersionUID = -838943508982575697L;

	private Long userId;
	private String tgt;
	private String csrfToken;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getTgt() {
		return tgt;
	}

	public void setTgt(String tgt) {
		this.tgt = tgt;
	}

	public String getCsrfToken() {
		return csrfToken;
	}

	public void setCsrfToken(String csrfToken) {
		this.csrfToken = csrfToken;
	}

}
