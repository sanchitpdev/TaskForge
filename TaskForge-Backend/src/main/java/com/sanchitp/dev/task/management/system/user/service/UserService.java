package com.sanchitp.dev.task.management.system.user.service;

import com.sanchitp.dev.task.management.system.security.service.CustomUserDetails;
import com.sanchitp.dev.task.management.system.security.util.SecurityUtils;
import com.sanchitp.dev.task.management.system.user.dto.UserResponse;
import com.sanchitp.dev.task.management.system.user.entity.User;
import com.sanchitp.dev.task.management.system.user.repository.UserRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /* ── Helpers ─────────────────────────────────────── */

    private User getCurrentUserEntity() {
        CustomUserDetails currentUser = SecurityUtils.getCurrentUser();
        if (currentUser == null) {
            throw new AccessDeniedException("Unauthorized");
        }
        return currentUser.getUser();
    }

    private UserResponse toResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole());
    }

    /* ── Public API ──────────────────────────────────── */

    public UserResponse getCurrentUserProfile() {
        return toResponse(getCurrentUserEntity());
    }

    @Transactional
    public UserResponse updateCurrentUser(String name, String email) {
        User user = getCurrentUserEntity();
        if (name  != null) user.setName(name);
        if (email != null) user.setEmail(email);
        return toResponse(userRepository.save(user));
    }
}
