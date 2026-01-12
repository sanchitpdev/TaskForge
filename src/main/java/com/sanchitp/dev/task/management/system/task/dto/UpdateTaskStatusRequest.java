package com.sanchitp.dev.task.management.system.task.dto;

import com.sanchitp.dev.task.management.system.common.enums.TaskStatus;
import jakarta.validation.constraints.NotNull;

public class UpdateTaskStatusRequest {

    @NotNull
    private TaskStatus status;

    public TaskStatus getStatus() {
        return status;
    }
}
