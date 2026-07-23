package com.cognizant.springlearn;

import com.cognizant.springlearn.controller.AuthenticationController;
import com.cognizant.springlearn.dto.AuthenticationResponse;
import com.cognizant.springlearn.dto.User;
import com.cognizant.springlearn.service.AuthenticationService;
import com.cognizant.springlearn.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Authentication Integration Tests
 * 
 * These tests verify the complete JWT authentication flow:
 * Step 1: Controller receives request with Basic Auth credentials
 * Step 2: Authorization header is decoded
 * Step 3: JWT token is generated and returned
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@DisplayName("JWT Authentication Integration Tests")
public class AuthenticationIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationService authenticationService;

    private static final String AUTHENTICATE_URL = "/authenticate";
    private static final String VALID_USERNAME = "user";
    private static final String VALID_PASSWORD = "password";

    @BeforeEach
    public void setUp() {
        // Ensure test user exists
        User testUser = new User(VALID_USERNAME, VALID_PASSWORD, "USER");
        userService.saveUser(testUser);
    }

    @Test
    @DisplayName("Test 1: Authenticate with valid credentials and receive JWT token")
    public void testAuthenticateWithValidCredentials() {
        // Arrange - Create Basic Auth header
        String credentials = VALID_USERNAME + ":" + VALID_PASSWORD;
        String encodedCredentials = Base64.getEncoder().encodeToString(credentials.getBytes());
        String authHeader = "Basic " + encodedCredentials;

        // Act - Call authenticate endpoint via direct service
        AuthenticationResponse response = authenticationService.authenticate(authHeader);

        // Assert
        assertNotNull(response, "Response should not be null");
        assertNotNull(response.getToken(), "Token should not be null");
        assertTrue(response.getToken().length() > 0, "Token should not be empty");
        assertEquals(VALID_USERNAME, response.getUsername(), "Username should match");
        assertEquals("Bearer", response.getType(), "Token type should be Bearer");
        assertNotNull(response.getExpiresIn(), "Expiration should not be null");

        System.out.println("✓ Test 1 Passed: JWT token generated successfully");
        System.out.println("  Token: " + response.getToken());
    }

    @Test
    @DisplayName("Test 2: HTTP endpoint returns token with valid Basic Auth")
    public void testHttpAuthenticateEndpoint() {
        // Arrange
        String credentials = VALID_USERNAME + ":" + VALID_PASSWORD;
        String encodedCredentials = Base64.getEncoder().encodeToString(credentials.getBytes());

        // Act - Make HTTP GET to /authenticate with Authorization header
        ResponseEntity<AuthenticationResponse> response = restTemplate
                .getForEntity(AUTHENTICATE_URL, AuthenticationResponse.class,
                        Map.of("Authorization", "Basic " + encodedCredentials));

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Response status should be 200 OK");
        assertNotNull(response.getBody(), "Response body should not be null");
        assertNotNull(response.getBody().getToken(), "Token should not be null");

        System.out.println("✓ Test 2 Passed: HTTP endpoint returns token");
    }

    @Test
    @DisplayName("Test 3: Reject authentication with invalid credentials")
    public void testAuthenticateWithInvalidCredentials() {
        // Arrange
        String credentials = VALID_USERNAME + ":wrongpassword";
        String encodedCredentials = Base64.getEncoder().encodeToString(credentials.getBytes());
        String authHeader = "Basic " + encodedCredentials;

        // Act & Assert
        Exception exception = assertThrows(
                IllegalArgumentException.class,
                () -> authenticationService.authenticate(authHeader),
                "Should throw exception for invalid credentials"
        );

        assertTrue(exception.getMessage().contains("Invalid username or password"),
                "Error message should indicate invalid credentials");

        System.out.println("✓ Test 3 Passed: Invalid credentials rejected");
    }

    @Test
    @DisplayName("Test 4: Reject authentication without Authorization header")
    public void testAuthenticateWithoutAuthorizationHeader() {
        // Act & Assert
        Exception exception = assertThrows(
                IllegalArgumentException.class,
                () -> authenticationService.authenticate(null),
                "Should throw exception when Authorization header is missing"
        );

        assertTrue(exception.getMessage().contains("Authorization header"),
                "Error message should mention Authorization header");

        System.out.println("✓ Test 4 Passed: Missing Authorization header rejected");
    }

    @Test
    @DisplayName("Test 5: Reject authentication with invalid Authorization header format")
    public void testAuthenticateWithInvalidHeaderFormat() {
        // Arrange
        String authHeader = "Bearer invalid_token";

        // Act & Assert
        Exception exception = assertThrows(
                IllegalArgumentException.class,
                () -> authenticationService.authenticate(authHeader),
                "Should throw exception for invalid header format"
        );

        System.out.println("✓ Test 5 Passed: Invalid header format rejected");
    }

    @Test
    @DisplayName("Test 6: Verify JWT token structure")
    public void testJwtTokenStructure() {
        // Arrange
        String credentials = VALID_USERNAME + ":" + VALID_PASSWORD;
        String encodedCredentials = Base64.getEncoder().encodeToString(credentials.getBytes());
        String authHeader = "Basic " + encodedCredentials;

        // Act
        AuthenticationResponse response = authenticationService.authenticate(authHeader);
        String token = response.getToken();

        // Assert - JWT should have 3 parts separated by dots
        String[] parts = token.split("\\.");
        assertEquals(3, parts.length, "JWT should have 3 parts (header.payload.signature)");

        System.out.println("✓ Test 6 Passed: JWT token has valid structure");
        System.out.println("  Header: " + parts[0]);
        System.out.println("  Payload: " + parts[1]);
        System.out.println("  Signature: " + parts[2]);
    }

    @Test
    @DisplayName("Test 7: Authenticate with admin user")
    public void testAuthenticateWithAdminUser() {
        // Arrange
        String credentials = "admin:admin123";
        String encodedCredentials = Base64.getEncoder().encodeToString(credentials.getBytes());
        String authHeader = "Basic " + encodedCredentials;

        // Act
        AuthenticationResponse response = authenticationService.authenticate(authHeader);

        // Assert
        assertEquals("admin", response.getUsername(), "Username should be admin");
        assertNotNull(response.getToken(), "Token should be generated");

        System.out.println("✓ Test 7 Passed: Admin user authenticated successfully");
    }

    @Test
    @DisplayName("Test 8: Health endpoint is accessible")
    public void testHealthEndpoint() {
        // Act
        ResponseEntity<String> response = restTemplate.getForEntity("/health", String.class);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().contains("running"));

        System.out.println("✓ Test 8 Passed: Health endpoint is accessible");
    }

    // Helper to construct map
    private static java.util.Map<String, String> Map(String key, String value) {
        return java.util.Map.of(key, value);
    }
}
