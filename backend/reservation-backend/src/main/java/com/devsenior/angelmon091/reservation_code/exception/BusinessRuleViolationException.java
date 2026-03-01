package com.devsenior.angelmon091.reservation_code.exception;

/**
 * Thrown when a business rule is violated during reservation operations.
 */
public class BusinessRuleViolationException extends RuntimeException {

    /**
     * Creates an exception with the given message.
     *
     * @param message the detail message (must not be null)
     */
    public BusinessRuleViolationException(String message) {
        super(message);
    }

    /**
     * Creates an exception with the given message and cause.
     *
     * @param message the detail message (must not be null)
     * @param cause   the cause (may be null)
     */
    public BusinessRuleViolationException(String message, Throwable cause) {
        super(message, cause);
    }
}
