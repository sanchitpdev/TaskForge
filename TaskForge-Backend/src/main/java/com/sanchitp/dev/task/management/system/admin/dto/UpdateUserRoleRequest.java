package com.sanchitp.dev.task.management.system.admin.dto;

import com.sanchitp.dev.task.management.system.common.enums.Role;
import jakarta.validation.constraints.NotNull;

public class UpdateUserRoleRequest {

    @NotNull(message = "Role is required")
    private Role role;

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
