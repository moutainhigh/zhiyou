package com.zy.common.exception;

public class BizException extends RuntimeException {

	private static final long serialVersionUID = -160485734664530479L;

	private int code;

	private String message;

	private Object data;

	public BizException(int code) {
		super();
		this.code = code;
	}

	public BizException(int code, String message) {
		super(message);
		this.code = code;
		this.message = message;
	}

	public BizException(int code, String message, Object data) {
		super(message);
		this.code = code;
		this.message = message;
		this.data = data;
	}

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

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

}
