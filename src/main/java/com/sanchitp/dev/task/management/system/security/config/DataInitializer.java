package com.sanchitp.dev.task.management.system.security.config;

import com.sanchitp.dev.task.management.system.common.enums.Role;
import com.sanchitp.dev.task.management.system.user.entity.User;
import com.sanchitp.dev.task.management.system.user.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initUsers(UserRepository userRepository,
                                PasswordEncoder passwordEncoder) {

        return args -> {

            /* ================= ADMIN USERS ================= */

            createUserIfNotExists(
                    userRepository, passwordEncoder,
                    "Admin One", "admin1@example.com", "admin123", Role.ADMIN
            );

            createUserIfNotExists(
                    userRepository, passwordEncoder,
                    "Admin Two", "admin2@example.com", "admin123", Role.ADMIN
            );

            createUserIfNotExists(
                    userRepository, passwordEncoder,
                    "Admin Three", "admin3@example.com", "admin123", Role.ADMIN
            );

            /* ================= NORMAL USERS ================= */

            createUserIfNotExists(
                    userRepository, passwordEncoder,
                    "User One", "user1@example.com", "user123", Role.USER
            );

            createUserIfNotExists(
                    userRepository, passwordEncoder,
                    "User Two", "user2@example.com", "user123", Role.USER
            );

            createUserIfNotExists(
                    userRepository, passwordEncoder,
                    "User Three", "user3@example.com", "user123", Role.USER
            );
        };
    }

    /* ================= HELPER ================= */

    private void createUserIfNotExists(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            String name,
            String email,
            String rawPassword,
            Role role
    ) {
        if (userRepository.findByEmail(email).isEmpty()) {
            User user = new User();
            user.setName(name);
            user.setEmail(email);
            user.setRole(role);
            user.setPassword(passwordEncoder.encode(rawPassword));
            userRepository.save(user);
        }
    }
}
