package com.rishabh.authservice.service.impl;

import com.rishabh.authservice.dto.request.LoginRequestDto;
import com.rishabh.authservice.dto.request.UserRequestDto;
import com.rishabh.authservice.dto.response.AuthResponseDto;
import com.rishabh.authservice.dto.response.TokenValidationResponse;
import com.rishabh.authservice.dto.response.UserResponseDto;
import com.rishabh.authservice.exception.ResourceAlreadyExistsException;
import com.rishabh.authservice.exception.ResourceNotFoundException;
import com.rishabh.authservice.mapper.UserMapper;
import com.rishabh.authservice.model.entity.User;
import com.rishabh.authservice.model.enums.UserRole;
import com.rishabh.authservice.repository.UserRepository;
import com.rishabh.authservice.security.jwt.JwtUtils;
import com.rishabh.authservice.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtUtils jwtUtils;
    private final UserMapper userMapper;

    @Override
    @Transactional
    public UserResponseDto register(UserRequestDto request) {
        String cleanEmail = request.getEmail().trim().toLowerCase();
        String cleanUsername = request.getUsername().trim();

        if (userRepository.existsByEmailIgnoreCase(cleanEmail)) {
            throw new ResourceAlreadyExistsException("Email '" + cleanEmail + "' is already registered");
        }
        if (userRepository.existsByUsernameIgnoreCase(cleanUsername)) {
            throw new ResourceAlreadyExistsException("Username '" + cleanUsername + "' is already registered");
        }

        User user = userMapper.toEntity(request);
        user.setFirstName(request.getFirstName().trim());
        user.setLastName(request.getLastName().trim());
        user.setEmail(cleanEmail);
        user.setUsername(cleanUsername);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole() != null ? request.getRole() : UserRole.EMPLOYEE);
        user.setIsActive(true);

        User savedUser = userRepository.save(user);
        log.info("Successfully registered user with ID: {} and Email: {}", savedUser.getId(), savedUser.getEmail());

        return userMapper.toResponseDto(savedUser);
    }

    @Override
    @Transactional(readOnly = true)
    public AuthResponseDto login(LoginRequestDto request) {
        String cleanEmail = request.getEmail().trim().toLowerCase();

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(cleanEmail, request.getPassword()));

        UserDetails userDetails = userDetailsService.loadUserByUsername(cleanEmail);
        User user = userRepository.findByEmailIgnoreCase(cleanEmail)
                .or(() -> userRepository.findByUsernameIgnoreCase(cleanEmail))
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Map<String, Object> extraClaims = Map.of(
                "userId", user.getId(),
                "role", user.getRole().name()
        );

        String token = jwtUtils.generateToken(extraClaims, userDetails);

        log.info("Successfully authenticated user: {}", cleanEmail);

        return AuthResponseDto.builder()
                .accessToken(token)
                .tokenType("Bearer")
                .expiresIn(jwtUtils.getJwtExpiration())
                .user(userMapper.toResponseDto(user))
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponseDto getCurrentUser(String identifier) {
        User user = userRepository.findByEmailIgnoreCase(identifier)
                .or(() -> userRepository.findByUsernameIgnoreCase(identifier))
                .orElseThrow(() -> new ResourceNotFoundException("User not found with identifier: " + identifier));
        return userMapper.toResponseDto(user);
    }

    @Override
    @Transactional(readOnly = true)
    public TokenValidationResponse validateToken(String token) {
        if (token == null || token.isBlank()) {
            return TokenValidationResponse.builder()
                    .valid(false)
                    .message("Token is missing or empty")
                    .build();
        }

        String rawToken = token.startsWith("Bearer ") ? token.substring(7) : token;

        try {
            String username = jwtUtils.extractUsername(rawToken);
            if (username != null && jwtUtils.isTokenValid(rawToken, username)) {
                User user = userRepository.findByEmailIgnoreCase(username)
                        .or(() -> userRepository.findByUsernameIgnoreCase(username))
                        .orElse(null);

                if (user != null && Boolean.TRUE.equals(user.getIsActive())) {
                    return TokenValidationResponse.builder()
                            .valid(true)
                            .userId(user.getId())
                            .email(user.getEmail())
                            .username(user.getUsername())
                            .role(user.getRole())
                            .message("Token is valid")
                            .build();
                }
            }
        } catch (Exception e) {
            log.warn("Token validation failed: {}", e.getMessage());
        }

        return TokenValidationResponse.builder()
                .valid(false)
                .message("Token is invalid or expired")
                .build();
    }
}
