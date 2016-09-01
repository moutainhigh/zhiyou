package com.zy.common.exception;

public class UnauthenticatedException extends RuntimeException {

    private static final long serialVersionUID = -1627252277435763406L;

	/**
     * Creates a new UnauthenticatedException.
     */
    public UnauthenticatedException() {
        super();
    }

    /**
     * Constructs a new UnauthenticatedException.
     *
     * @param message the reason for the exception
     */
    public UnauthenticatedException(String message) {
        super(message);
    }

    /**
     * Constructs a new UnauthenticatedException.
     *
     * @param cause the underlying Throwable that caused this exception to be thrown.
     */
    public UnauthenticatedException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructs a new UnauthenticatedException.
     *
     * @param message the reason for the exception
     * @param cause   the underlying Throwable that caused this exception to be thrown.
     */
    public UnauthenticatedException(String message, Throwable cause) {
        super(message, cause);
    }
}
