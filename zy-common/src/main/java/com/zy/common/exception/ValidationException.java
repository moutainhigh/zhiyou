package com.zy.common.exception;

public class ValidationException extends RuntimeException {

	private static final long serialVersionUID = 6122760335094816228L;

	public ValidationException() {
		super();
	}

	public ValidationException(String message) {
		super(message);
	}

	public ValidationException(Throwable cause) {
		super(cause);
	}

	public ValidationException(String message, Throwable cause) {
		super(message, cause);
	}
}
