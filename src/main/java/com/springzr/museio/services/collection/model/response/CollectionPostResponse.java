package com.springzr.museio.services.collection.model.response;

import java.time.ZonedDateTime;
import java.util.Map;
import lombok.Data;

/**
 * Response DTO representing the result of a collection creation or update operation.
 * Includes operation success status, HTTP response code,
 * message, optional data (e.g., collection ID),
 * and a timestamp indicating when the response was generated.
 */
@Data
public class CollectionPostResponse {

    private boolean success;
    private int code;
    private String message;
    private Map<String, Object> data;
    private ZonedDateTime timestamp;

    /**
     * Constructs a new {@code CollectionPostResponse} with the provided response details.
     *
     * @param success whether the operation was successful
     * @param code the HTTP status code
     * @param message the response message
     * @param data additional response data, such as collection ID
     */
    public CollectionPostResponse(
            boolean success, int code, String message, Map<String, Object> data) {
        this.success = success;
        this.code = code;
        this.message = message;
        this.data = data;
        this.timestamp = ZonedDateTime.now();
    }
}
