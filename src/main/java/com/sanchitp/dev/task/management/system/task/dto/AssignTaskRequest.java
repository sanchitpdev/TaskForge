package com.sanchitp.dev.task.management.system.task.dto;

import jakarta.validation.constraints.NotNull;

public class AssignTaskRequest {

    @NotNull
    private Long userId;

    public Long getUserId() {
        return userId;
    }
}
