package com.cognizant.springlearn.controller;

import com.cognizant.springlearn.dto.AuthenticationResponse;
import com.cognizant.springlearn.service.AuthenticationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

/**
 * Authentication Controller
 * 
 * Step 1: Create Authentication Controller and configure in SecurityConfig
 * 
 * This REST controller handles authentication requests and returns JWT tokens.
 * 
 * The /authenticate endpoint accepts HTTP Basic Authentication and returns
 * a JWT token that can be used for subsequent requests.
 * 
 * Example usage:
 * curl -s -u user:password http://localhost:8090/authenticate
 * 
 * Response:
 * {
 *   "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyIiwiaWF0IjoxNTcwMzc5NDc0LCJleHAiOjE1NzAzODA2NzR9.t3LRvlCV-hwKfoqZYlaVQqEUiBloWcWn0ft3tgv0dL0",
 *   "type": "Bearer",
 *   "username": "user",
 *   "expiresIn": 1200
 * }
 */
@RestController
public class AuthenticationController {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

    @Autowired
    private AuthenticationService authenticationService;

    /**
     * Authenticate user with Basic Auth credentials and return JWT token
     * 
     * Endpoint: POST /authenticate
     * Authorization: Basic base64(username:password)
     * 
     * @param authHeader the Authorization header containing Basic credentials
     * @return ResponseEntity with AuthenticationResponse containing JWT token
     */
    @GetMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestHeader(value = "Authorization", required = false) String authHeader) {

        logger.info("Authentication request received");

        try {
            // Validate and process the Authorization header
            AuthenticationResponse response = authenticationService.authenticate(authHeader);
            logger.info("Authentication successful for user: {}", response.getUsername());
            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            logger.error("Authentication failed: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .build();
        } catch (Exception e) {
            logger.error("Unexpected error during authentication: {}", e.getMessage(), e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
        }
    }

    /**
     * Health check endpoint to verify the service is running
     * 
     * @return message indicating service is operational
     */
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("JWT Authentication Service is running!");
    }
}
