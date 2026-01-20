package com.sanchitp.dev.task.management.system.admin.service;

import com.sanchitp.dev.task.management.system.admin.dto.CreateUserRequest;
import com.sanchitp.dev.task.management.system.admin.dto.UserResponse;
import com.sanchitp.dev.task.management.system.common.enums.exception.UserNotFoundException;
import com.sanchitp.dev.task.management.system.security.service.CustomUserDetails;
import com.sanchitp.dev.task.management.system.security.util.SecurityUtils;
import com.sanchitp.dev.task.management.system.user.entity.User;
import com.sanchitp.dev.task.management.system.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminUserService {

    public final UserRepository userRepository;
    public final PasswordEncoder passwordEncoder;

    public AdminUserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /* ================= CREATE USER ================= */

    public UserResponse createUser(CreateUserRequest request){

        if (userRepository.findByEmail(request.getEmail()).isPresent()){
            throw new IllegalStateException("Email already exists");
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setRole(request.getRole());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        User saved = userRepository.save(user);

        return toResponse(saved);
    }

    /* ================= GET ALL USERS ================= */

    public List<UserResponse> getAllUsers(){
        return userRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    /* ================= UPDATE ROLE ================= */
    @Transactional
    public UserResponse updateUserRole(Long userId, String role) {

        CustomUserDetails currentUser = SecurityUtils.getCurrentUser();

        if (currentUser != null && currentUser.getUserId().equals(userId)) {
            throw new IllegalStateException(
                    "You cannot change your own role"
            );
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        user.setRole(Enum.valueOf(user.getRole().getDeclaringClass(), role));
        return toResponse(userRepository.save(user));
    }

    /* ================= HELPER ================= */

    private UserResponse toResponse(User user){
        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole()
        );
    }

}
