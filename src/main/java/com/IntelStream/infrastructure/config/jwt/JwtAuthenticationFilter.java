package com.IntelStream.infrastructure.config.jwt;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        processJwtAuthentication(request);
        filterChain.doFilter(request, response);
    }

    /**
     * Orchestrates JWT token extraction and authentication.
     */
    private void processJwtAuthentication(HttpServletRequest request) {
        String token = extractToken(request);
        if (token == null) {
            return;
        }
        handleAuthentication(token, request);
    }

    /**
     * Handles authentication flow inside a try-catch block.
     */
    private void handleAuthentication(String token, HttpServletRequest request) {
        try {
            String username = jwtUtil.extractUsername(token);
            if (isAuthenticationRequired(username)) {
                authenticateUser(username, token, request);
            }
        } catch (Exception e) {
            log.error("JWT authentication failed: {}", e.getMessage());
        }
    }


    /**
     * Extracts JWT from Authorization header.
     */
    private String extractToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }

    /**
     * Checks if SecurityContext is empty and username is present.
     */
    private boolean isAuthenticationRequired(String username) {
        return username != null && SecurityContextHolder.getContext().getAuthentication() == null;
    }

    /**
     * Loads user and sets authentication in SecurityContext.
     */
    private void authenticateUser(String username, String token, HttpServletRequest request) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        if (jwtUtil.validateToken(token, userDetails)) {
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    userDetails,
                    null,
                    userDetails.getAuthorities()
            );
            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authToken);
        }
    }
}
