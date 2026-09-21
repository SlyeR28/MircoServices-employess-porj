package com.rishabh.authservice.service;

import com.rishabh.authservice.dto.request.LoginRequestDto;
import com.rishabh.authservice.dto.request.UserRequestDto;
import com.rishabh.authservice.dto.response.AuthResponseDto;
import com.rishabh.authservice.dto.response.TokenValidationResponse;
import com.rishabh.authservice.dto.response.UserResponseDto;

public interface UserService {
    UserResponseDto register(UserRequestDto request);
    AuthResponseDto login(LoginRequestDto request);
    UserResponseDto getCurrentUser(String identifier);
    TokenValidationResponse validateToken(String token);
}
