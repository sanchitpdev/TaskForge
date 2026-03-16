package com.sanchitp.dev.task.management.system.config;

import com.sanchitp.dev.task.management.system.common.enums.Role;
import com.sanchitp.dev.task.management.system.user.entity.User;
import com.sanchitp.dev.task.management.system.user.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Seeds the database with default admin and user accounts on first startup.
 * Existing records are skipped (idempotent).
 */
@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner seedUsers(UserRepository userRepository,
                                PasswordEncoder passwordEncoder) {
        return args -> {

            // ── Admins ──────────────────────────────────────
            createIfAbsent(userRepository, passwordEncoder,
                    "Admin One",   "admin1@example.com", "admin123", Role.ADMIN);
            createIfAbsent(userRepository, passwordEncoder,
                    "Admin Two",   "admin2@example.com", "admin123", Role.ADMIN);
            createIfAbsent(userRepository, passwordEncoder,
                    "Admin Three", "admin3@example.com", "admin123", Role.ADMIN);

            // ── Regular Users ────────────────────────────────
            createIfAbsent(userRepository, passwordEncoder,
                    "User One",   "user1@example.com", "user123", Role.USER);
            createIfAbsent(userRepository, passwordEncoder,
                    "User Two",   "user2@example.com", "user123", Role.USER);
            createIfAbsent(userRepository, passwordEncoder,
                    "User Three", "user3@example.com", "user123", Role.USER);
        };
    }

    private void createIfAbsent(UserRepository userRepository,
                                 PasswordEncoder passwordEncoder,
                                 String name, String email,
                                 String rawPassword, Role role) {

        if (userRepository.findByEmail(email).isPresent()) {
            return;
        }

        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setRole(role);
        user.setPassword(passwordEncoder.encode(rawPassword));
        userRepository.save(user);
    }
}
