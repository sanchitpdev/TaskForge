package com.sanchitp.dev.task.management.system.security.util;

import com.sanchitp.dev.task.management.system.security.service.CustomUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public final class SecurityUtils {

    private SecurityUtils() {
        // utility class — no instantiation
    }

    /**
     * Returns the currently authenticated user, or {@code null} if the
     * security context holds no valid principal.
     */
    public static CustomUserDetails getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof CustomUserDetails customUserDetails) {
            return customUserDetails;
        }

        return null;
    }
}
