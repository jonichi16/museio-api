package com.springzr.museio.services.collection.model.response;

import java.time.ZonedDateTime;
import java.util.Map;

public class CollectionResponse {

    private boolean success;
    private int code;
    private String message;
    private Map<String, Object> data;
    private ZonedDateTime timestamp;

    public CollectionResponse(boolean success, int code, String message, Map<String, Object> data) {
        this.success = success;
        this.code = code;
        this.message = message;
        this.data = data;
        this.timestamp = ZonedDateTime.now();
    }

    // Getters
    public boolean isSuccess() {
        return success;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public ZonedDateTime getTimestamp() {
        return timestamp;
    }
}