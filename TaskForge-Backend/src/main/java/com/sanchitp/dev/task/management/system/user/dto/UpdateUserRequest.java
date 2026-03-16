package com.sanchitp.dev.task.management.system.user.dto;

import jakarta.validation.constraints.Email;

public class UpdateUserRequest {

    private String name;

    @Email(message = "Invalid email format")
    private String email;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
