package com.sanchitp.dev.task.management.system.task.dto;

import jakarta.validation.constraints.NotNull;

public class CreateTaskRequest {

    @NotNull
    private String title;

    @NotNull
    private String description;

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }
}
