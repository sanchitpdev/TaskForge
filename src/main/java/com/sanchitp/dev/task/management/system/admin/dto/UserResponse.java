package com.sanchitp.dev.task.management.system.admin.dto;

import com.sanchitp.dev.task.management.system.common.enums.Role;

public class UserResponse {

    private final Long id;
    private final String name;
    private final String email;
    private final Role role;

    public UserResponse(Long id, String name, String email, Role role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public Role getRole() {
        return role;
    }
}
