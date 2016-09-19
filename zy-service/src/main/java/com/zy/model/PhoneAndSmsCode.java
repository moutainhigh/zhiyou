package com.zy.model;

import java.io.Serializable;

public class PhoneAndSmsCode implements Serializable {

	private String phone;

	private String smsCode;

	public PhoneAndSmsCode(String phone, String smsCode) {
		this.phone = phone;
		this.smsCode = smsCode;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getSmsCode() {
		return smsCode;
	}

	public void setSmsCode(String smsCode) {
		this.smsCode = smsCode;
	}

}
