package com.IntelStream.domain.service.user;

import com.IntelStream.infrastructure.config.jwt.JwtUtil;
import com.IntelStream.infrastructure.persistence.entity.User;
import com.IntelStream.infrastructure.persistence.enums.Role;
import com.IntelStream.infrastructure.persistence.repository.UserRepository;
import com.IntelStream.presentation.dto.request.AuthRequest;
import com.IntelStream.presentation.dto.request.RegisterRequest;
import com.IntelStream.presentation.dto.response.AuthResponse;
import com.IntelStream.shared.exceptions.InvalidCredentialsException;
import com.IntelStream.shared.exceptions.UserAlreadyExistsException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    @Value("${jwt.expiration-ms}")
    private long jwtExpirationSeconds;

    public AuthResponse register(RegisterRequest request) {
        validateNewUser(request);

        User user = createUser(request);
        userRepository.save(user);

        log.info("User registered: {}", user.getUsername());

        return generateAuthResponse(user);
    }

    public AuthResponse authenticate(AuthRequest request) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        User user = userRepository.findByUsername(auth.getName())
                .orElseThrow(() -> new InvalidCredentialsException("User not found"));

        user.setLastLogin(LocalDateTime.now());
        userRepository.save(user);

        return generateAuthResponse(user);
    }

    public AuthResponse refreshToken(String header) {
        String refreshToken = extractBearerToken(header);

        String username = jwtUtil.extractUsername(refreshToken);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new InvalidCredentialsException("User not found"));

        if (!jwtUtil.validateToken(refreshToken, user)) {
            throw new InvalidCredentialsException("Invalid refresh token");
        }

        return generateAuthResponse(user);
    }

    public void logout(String header) {
        String token = extractBearerToken(header);
        log.info("User logged out successfully");
    }

    // --- Helpers ---
    private void validateNewUser(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new UserAlreadyExistsException("Username already exists");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExistsException("Email already exists");
        }
    }

    private User createUser(RegisterRequest request) {
        return User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .createdAt(LocalDateTime.now())
                .lastLogin(LocalDateTime.now())
                .role(assignRole(request.getEmail()))
                .build();
    }

    private Role assignRole(String email) {
        // Move this logic to a config if possible
        if (email.endsWith("@admin.intellistream.com")) return Role.ADMIN;
        if (email.endsWith("@premium.intellistream.com")) return Role.PREMIUM_USER;
        return Role.USER;
    }

    private String extractBearerToken(String header) {
        if (header == null || !header.startsWith("Bearer ")) {
            throw new InvalidCredentialsException("Invalid token format");
        }
        return header.substring(7);
    }

    private AuthResponse generateAuthResponse(User user) {
        return AuthResponse.builder()
                .accessToken(jwtUtil.generateToken(user))
                .refreshToken(jwtUtil.generateRefreshToken(user))
                .tokenType("Bearer")
                .expiresIn(jwtExpirationSeconds)
                .username(user.getUsername())
                .role(user.getRole().name())
                .build();
    }
}
