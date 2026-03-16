package com.devsenior.angelmon091.reservation_code.exception;

import com.devsenior.angelmon091.reservation_code.dto.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Global exception handler that maps exceptions to appropriate HTTP responses.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles business rule violations (e.g. duplicate slot, already cancelled).
     *
     * @param ex the exception
     * @return 409 Conflict with error message
     */
    @ExceptionHandler(BusinessRuleViolationException.class)
    public ResponseEntity<ErrorResponse> handleBusinessRuleViolation(BusinessRuleViolationException ex) {
        HttpStatus status = ex.getMessage().contains("not found") ? HttpStatus.NOT_FOUND : HttpStatus.CONFLICT;
        return ResponseEntity.status(status).body(new ErrorResponse(ex.getMessage()));
    }

    /**
     * Handles validation errors on request bodies (e.g. @Valid).
     *
     * @param ex the validation exception
     * @return 400 Bad Request with first constraint message
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .findFirst()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .orElse("Validation failed");
        return ResponseEntity.badRequest().body(new ErrorResponse(message));
    }
    /**
     * Handles all other unexpected runtime exceptions.
     * Prevents internal details from leaking to the client.
     *
     * @param ex the exception
     * @return 500 Internal Server Error with generic message
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleUnexpected(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("An unexpected error occurred. Please try again later."));
    }
}
