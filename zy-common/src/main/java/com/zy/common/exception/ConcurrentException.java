package com.zy.common.exception;

/**
 * 并发异常
 * @author 薛定谔的猫
 */
public class ConcurrentException extends RuntimeException {

	private static final long serialVersionUID = -3450625217866678930L;

	public ConcurrentException() {
        super();
    }

    public ConcurrentException(String message) {
        super(message);
    }

    public ConcurrentException(Throwable cause) {
        super(cause);
    }

    public ConcurrentException(String message, Throwable cause) {
        super(message, cause);
    }
}
