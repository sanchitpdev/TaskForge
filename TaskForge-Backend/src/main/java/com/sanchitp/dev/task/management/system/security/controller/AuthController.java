package com.sanchitp.dev.task.management.system.security.controller;

import com.sanchitp.dev.task.management.system.security.dto.AuthResponse;
import com.sanchitp.dev.task.management.system.security.dto.LoginRequest;
import com.sanchitp.dev.task.management.system.security.jwt.JwtUtil;
import com.sanchitp.dev.task.management.system.security.service.CustomUserDetails;
import jakarta.validation.Valid;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public AuthController(AuthenticationManager authenticationManager,
                          JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil               = jwtUtil;
    }

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()));

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        String token = jwtUtil.generateToken(
                userDetails.getUserId(),
                userDetails.getUsername(),
                userDetails.getAuthorities().iterator().next().getAuthority());

        return new AuthResponse(token);
    }
}
