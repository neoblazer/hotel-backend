package com.example.demo.controller;

import com.example.demo.dto.*;
import com.example.demo.entity.User;
import com.example.demo.security.JwtUtil;
import com.example.demo.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication", description = "Register and Login endpoints")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/register")
    @Operation(summary = "Register a new user")
    public ResponseEntity<ApiResponse<UserResponseDTO>> register(
            @Valid @RequestBody RegisterRequestDTO request) {

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());

        UserResponseDTO response = userService.registerUser(user);
        return ResponseEntity.ok(new ApiResponse<>(true, "User registered successfully", response));
    }

    @PostMapping("/login")
    @Operation(summary = "Login and receive JWT token")
    public ResponseEntity<ApiResponse<LoginResponse>> login(
            @Valid @RequestBody LoginRequestDTO request) {

        User user = userService.loginUser(request.getEmail(), request.getPassword());
        String token = jwtUtil.generateToken(user.getEmail());
        UserResponseDTO userDTO = userService.convertToDTO(user);

        LoginResponse loginResponse = new LoginResponse(token, user.getRole().name(), userDTO);
        return ResponseEntity.ok(new ApiResponse<>(true, "Login successful", loginResponse));
    }

    public record LoginResponse(String token, String role, UserResponseDTO user) {}
}