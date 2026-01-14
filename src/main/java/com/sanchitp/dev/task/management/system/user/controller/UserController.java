package com.sanchitp.dev.task.management.system.user.controller;

import com.sanchitp.dev.task.management.system.user.dto.UpdateUserRequest;
import com.sanchitp.dev.task.management.system.user.dto.UserResponse;
import com.sanchitp.dev.task.management.system.user.service.UserService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@PreAuthorize("isAuthenticated()")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public UserResponse getMyProfile() {
        return userService.getCurrentUserProfile();
    }

    @PatchMapping("/me")
    public UserResponse updateMyProfile(
            @Valid @RequestBody UpdateUserRequest request
    ) {
        return userService.updateCurrentUser(
                request.getName(),
                request.getEmail()
        );
    }
}
