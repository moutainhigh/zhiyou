package com.zy.common.model.result;

import java.io.Serializable;

public class Result<T> implements Serializable {
	
	private static final long serialVersionUID = -110062470244649551L;

	protected int code;

	protected String message;

	protected T data;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

}
