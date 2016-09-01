package com.zy.common.model.result;


public class ResultBuilder<T> {
	
	private T data;

	private String message;

	private int code = ResultCode.OK;
	
	public ResultBuilder<T> code(int code) {
		this.code = code;
		return this;
	}

	public ResultBuilder<T> message(String message) {
		this.message = message;
		return this;
	}

	public ResultBuilder<T> data(T data) {
		this.data = data;
		return this;
	}

	public Result<T> build() {
		Result<T> result = new Result<>();
		result.setCode(code);
		result.setMessage(message);
		result.setData(data);
		return result;
	}
	
	/* 提供一些快速静态方法 */
	public static <S> Result<S> result(S data) {
		Result<S> result = new Result<>();
		result.setCode(ResultCode.OK);
		result.setData(data);
		return result;
	}
	
	public static <S> Result<S> ok(String message) {
		Result<S> result = new Result<>();
		result.setCode(ResultCode.OK);
		result.setMessage(message);
		return result;
	}
	
	public static <S> Result<S> error(String message) {
		Result<S> result = new Result<>();
		result.setCode(ResultCode.ERROR);
		result.setMessage(message);
		return result;
	}
	
}
