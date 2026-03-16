package com.sanchitp.dev.task.management.system.admin.controller;

import com.sanchitp.dev.task.management.system.admin.dto.CreateUserRequest;
import com.sanchitp.dev.task.management.system.admin.dto.UpdateUserRoleRequest;
import com.sanchitp.dev.task.management.system.admin.dto.UserResponse;
import com.sanchitp.dev.task.management.system.admin.service.AdminUserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/users")
@PreAuthorize("hasRole('ADMIN')")
public class AdminUserController {

    private final AdminUserService adminUserService;

    public AdminUserController(AdminUserService adminUserService) {
        this.adminUserService = adminUserService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse createUser(@Valid @RequestBody CreateUserRequest request) {
        return adminUserService.createUser(request);
    }

    @GetMapping
    public List<UserResponse> getAllUsers() {
        return adminUserService.getAllUsers();
    }

    @PatchMapping("/{id}/role")
    public UserResponse updateUserRole(@PathVariable Long id,
                                       @Valid @RequestBody UpdateUserRoleRequest request) {
        return adminUserService.updateUserRole(id, request.getRole().name());
    }
}
