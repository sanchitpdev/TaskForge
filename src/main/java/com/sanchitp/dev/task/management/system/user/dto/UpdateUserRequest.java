package com.sanchitp.dev.task.management.system.user.dto;

import com.sanchitp.dev.task.management.system.common.enums.Role;
import jakarta.validation.constraints.Email;

public class UpdateUserRequest {


    private String name;

    @Email(message = "Invalid email format")
    private String email;


    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }


}
