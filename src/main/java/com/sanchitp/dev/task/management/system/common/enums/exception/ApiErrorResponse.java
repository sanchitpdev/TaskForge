package com.sanchitp.dev.task.management.system.common.enums.exception;

import java.time.LocalDate;

import static java.time.LocalDateTime.*;

public class ApiErrorResponse {

    private int status;
    private String message;
    private LocalDate timestamp;

    public ApiErrorResponse(int status, String message) {
        this.status = status;
        this.message = message;
        this.timestamp = LocalDate.from(now());
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
