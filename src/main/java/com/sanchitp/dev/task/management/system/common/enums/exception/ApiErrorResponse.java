package com.sanchitp.dev.task.management.system.common.enums.exception;

import java.time.LocalDate;

import static java.time.LocalDateTime.*;

public class ApiErrorResponse {

    private int status;
    private String message;
    private LocalDate timestamp;
    private String error;
    private String path;

    public ApiErrorResponse(int status, String message,String error, String path) {
        this.status = status;
        this.message = message;
        this.timestamp = LocalDate.from(now());
        this.error = error;
        this.path = path;
    }

    public String getError() {
        return error;
    }

    public String getPath() {
        return path;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public LocalDate getTimestamp() {
        return timestamp;
    }
}
