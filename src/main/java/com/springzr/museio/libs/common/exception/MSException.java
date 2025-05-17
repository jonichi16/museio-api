package com.springzr.museio.libs.common.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;


/**
 * {@code MSException} is a custom runtime exception used across the Museio application
 * to represent application-specific errors with an associated HTTP status and error code.
 *
 * <p>Example usage:</p>
 * <pre>{@code
 * throw new MSException("User not found", HttpStatus.NOT_FOUND, "USER_NOT_FOUND");
 * }</pre>
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class MSException extends RuntimeException {

    private final HttpStatus status;
    private final String errorCode;

    /**
     * Constructs a new {@code MSException} with the specified detail message,
     * HTTP status, and error code.
     *
     * @param message   the detail message.
     * @param status    the HTTP status to be returned.
     * @param errorCode the application-specific error code.
     */
    public MSException(String message, HttpStatus status, String errorCode) {
        super(message);
        this.status = status;
        this.errorCode = errorCode;
    }
}


