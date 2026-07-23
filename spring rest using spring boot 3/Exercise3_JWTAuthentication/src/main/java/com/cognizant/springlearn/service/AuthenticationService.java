package com.cognizant.springlearn.service;

import com.cognizant.springlearn.dto.AuthenticationResponse;
import com.cognizant.springlearn.dto.User;
import com.cognizant.springlearn.util.JwtTokenProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.Optional;

/**
 * Authentication Service
 * 
 * This service orchestrates the entire authentication process:
 * Step 1: Receive credentials from HTTP request
 * Step 2: Decode credentials from Authorization header (Basic Auth)
 * Step 3: Generate JWT token based on authenticated user
 */
@Service
public class AuthenticationService {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationService.class);

    @Autowired
    private UserService userService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    /**
     * Authenticate user and generate JWT token
     * 
     * This method implements all three steps:
     * Step 2: Read and decode Basic Authorization header
     * Step 3: Validate credentials and generate JWT
     * 
     * @param authHeader the Authorization header value (e.g., "Basic dXNlcjpwYXNzd29yZA==")
     * @return AuthenticationResponse containing JWT token
     * @throws IllegalArgumentException if Authorization header is invalid
     */
    public AuthenticationResponse authenticate(String authHeader) {
        logger.debug("Processing authentication request");

        if (authHeader == null || !authHeader.startsWith("Basic ")) {
            logger.warn("Invalid or missing Authorization header");
            throw new IllegalArgumentException("Authorization header must contain Basic credentials");
        }

        // Step 2: Decode Base64 encoded credentials from Authorization header
        String credentials = decodeCredentials(authHeader);
        String[] parts = credentials.split(":");

        if (parts.length != 2) {
            logger.warn("Invalid credentials format");
            throw new IllegalArgumentException("Invalid credentials format. Expected username:password");
        }

        String username = parts[0];
        String password = parts[1];

        logger.debug("Decoded credentials for user: {}", username);

        // Step 3: Validate user and generate token
        Optional<User> user = userService.authenticateUser(username, password);

        if (user.isEmpty()) {
            logger.warn("Authentication failed for user: {}", username);
            throw new IllegalArgumentException("Invalid username or password");
        }

        User authenticatedUser = user.get();
        String token = jwtTokenProvider.generateToken(authenticatedUser.getUsername());
        
        logger.info("Token generated successfully for user: {}", username);

        return AuthenticationResponse.builder()
                .token(token)
                .type("Bearer")
                .username(authenticatedUser.getUsername())
                .expiresIn(1200L) // 20 minutes in seconds
                .build();
    }

    /**
     * Decode Base64 encoded credentials from Authorization header
     * 
     * Step 2 Implementation: Extract and decode username and password
     * 
     * @param authHeader the Authorization header value
     * @return decoded credentials as "username:password"
     */
    private String decodeCredentials(String authHeader) {
        logger.debug("Decoding credentials from Authorization header");

        try {
            String base64Credentials = authHeader.replace("Basic ", "");
            byte[] decodedBytes = Base64.getDecoder().decode(base64Credentials);
            String credentials = new String(decodedBytes);
            
            logger.debug("Credentials decoded successfully");
            return credentials;
        } catch (IllegalArgumentException e) {
            logger.error("Failed to decode Base64 credentials: {}", e.getMessage());
            throw new IllegalArgumentException("Invalid Base64 encoding in Authorization header");
        }
    }
}
