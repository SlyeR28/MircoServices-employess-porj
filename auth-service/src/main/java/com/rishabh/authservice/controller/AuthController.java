package com.rishabh.authservice.controller;

import com.rishabh.authservice.dto.request.LoginRequestDto;
import com.rishabh.authservice.dto.request.UserRequestDto;
import com.rishabh.authservice.dto.response.ApiResponse;
import com.rishabh.authservice.dto.response.AuthResponseDto;
import com.rishabh.authservice.dto.response.TokenValidationResponse;
import com.rishabh.authservice.dto.response.UserResponseDto;
import com.rishabh.authservice.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserResponseDto>> register(@Valid @RequestBody UserRequestDto request) {
        UserResponseDto response = userService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("User registered successfully", response));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponseDto>> login(@Valid @RequestBody LoginRequestDto request) {
        AuthResponseDto response = userService.login(request);
        return ResponseEntity.ok(ApiResponse.success("Authentication successful", response));
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserResponseDto>> currentUser(Authentication authentication) {
        UserResponseDto response = userService.getCurrentUser(authentication.getName());
        return ResponseEntity.ok(ApiResponse.success("User details fetched successfully", response));
    }

    @PostMapping("/validate")
    public ResponseEntity<ApiResponse<TokenValidationResponse>> validateToken(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @RequestParam(value = "token", required = false) String paramToken) {

        String token = paramToken != null ? paramToken : authHeader;
        TokenValidationResponse response = userService.validateToken(token);
        return ResponseEntity.ok(ApiResponse.success("Token validation result", response));
    }
}
