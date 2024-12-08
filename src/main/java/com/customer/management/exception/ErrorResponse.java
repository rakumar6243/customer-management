package com.customer.management.exception;

import java.time.LocalDateTime;
import java.util.List;

public class ErrorResponse {

    private LocalDateTime timestamp;
    private String error;
    private String message;
    private List<String> details;

    public ErrorResponse(LocalDateTime timestamp, String error, String message, List<String> details) {
        this.timestamp = timestamp;
        this.error = error;
        this.message = message;
        this.details = details;
    }

    // Getters and setters
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<String> getDetails() {
        return details;
    }

    public void setDetails(List<String> details) {
        this.details = details;
    }
}
