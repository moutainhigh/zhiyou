package com.zy.common.exception;

public class UnauthorizedException extends RuntimeException {

	private static final long serialVersionUID = -3608615312096700157L;

	public UnauthorizedException() {
        super();
    }


    public UnauthorizedException(String message) {
        super(message);
    }

    public UnauthorizedException(Throwable cause) {
        super(cause);
    }

    public UnauthorizedException(String message, Throwable cause) {
        super(message, cause);
    }
}
