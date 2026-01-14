package com.sanchitp.dev.task.management.system.user.service;

import com.sanchitp.dev.task.management.system.security.service.CustomUserDetails;
import com.sanchitp.dev.task.management.system.security.util.SecurityUtils;
import com.sanchitp.dev.task.management.system.user.dto.UserResponse;
import com.sanchitp.dev.task.management.system.user.entity.User;
import com.sanchitp.dev.task.management.system.user.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;

    }

    /* ===================== HELPERS ===================== */

    private UserResponse toResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole()
        );
    }

    private User getCurrentUserEntity() {
        CustomUserDetails currentUser = SecurityUtils.getCurrentUser();
        if (currentUser == null) {
            throw new RuntimeException("Unauthorized");
        }
        return currentUser.getUser();
    }

    /* ===================== CRUD ===================== */

    public UserResponse getCurrentUserProfile() {
        User user = getCurrentUserEntity();
        return toResponse(user);
    }

    public UserResponse updateCurrentUser(String name, String email) {
        User user = getCurrentUserEntity();
        user.setName(name);
        user.setEmail(email);
        return toResponse(userRepository.save(user));
    }
}