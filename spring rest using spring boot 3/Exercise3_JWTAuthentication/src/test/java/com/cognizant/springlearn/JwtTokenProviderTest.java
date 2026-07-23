package com.cognizant.springlearn;

import com.cognizant.springlearn.service.AuthenticationService;
import com.cognizant.springlearn.service.UserService;
import com.cognizant.springlearn.util.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit Tests for JWT Token Provider and Authentication Service
 * 
 * Tests cover:
 * Step 2: Authorization header decoding
 * Step 3: Token generation and validation
 */
@SpringBootTest
@ActiveProfiles("test")
@DisplayName("JWT Token Provider Unit Tests")
public class JwtTokenProviderTest {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private UserService userService;

    private static final String TEST_USERNAME = "testuser";

    @BeforeEach
    public void setUp() {
        // Initialize test
    }

    @Test
    @DisplayName("Test A: Generate JWT token successfully")
    public void testGenerateToken() {
        // Act
        String token = jwtTokenProvider.generateToken(TEST_USERNAME);

        // Assert
        assertNotNull(token, "Token should not be null");
        assertTrue(token.length() > 0, "Token should not be empty");
        assertTrue(token.contains("."), "Token should contain dot separators");

        System.out.println("✓ Test A Passed: JWT token generated");
        System.out.println("  Token: " + token);
    }

    @Test
    @DisplayName("Test B: Validate valid JWT token")
    public void testValidateValidToken() {
        // Arrange
        String token = jwtTokenProvider.generateToken(TEST_USERNAME);

        // Act
        boolean isValid = jwtTokenProvider.validateToken(token);

        // Assert
        assertTrue(isValid, "Valid token should pass validation");

        System.out.println("✓ Test B Passed: Valid token validated successfully");
    }

    @Test
    @DisplayName("Test C: Reject invalid JWT token")
    public void testValidateInvalidToken() {
        // Arrange
        String invalidToken = "invalid.token.here";

        // Act
        boolean isValid = jwtTokenProvider.validateToken(invalidToken);

        // Assert
        assertFalse(isValid, "Invalid token should fail validation");

        System.out.println("✓ Test C Passed: Invalid token rejected");
    }

    @Test
    @DisplayName("Test D: Extract username from valid token")
    public void testGetUsernameFromToken() {
        // Arrange
        String token = jwtTokenProvider.generateToken(TEST_USERNAME);

        // Act
        String extractedUsername = jwtTokenProvider.getUsernameFromToken(token);

        // Assert
        assertEquals(TEST_USERNAME, extractedUsername, "Extracted username should match original");

        System.out.println("✓ Test D Passed: Username extracted successfully");
        System.out.println("  Username: " + extractedUsername);
    }

    @Test
    @DisplayName("Test E: Check token is not expired immediately after generation")
    public void testTokenNotExpiredAfterGeneration() {
        // Arrange
        String token = jwtTokenProvider.generateToken(TEST_USERNAME);

        // Act
        Boolean isExpired = jwtTokenProvider.isTokenExpired(token);

        // Assert
        assertFalse(isExpired, "Newly generated token should not be expired");

        System.out.println("✓ Test E Passed: Token is not expired after generation");
    }

    @Test
    @DisplayName("Test F: Decode Basic Auth credentials correctly")
    public void testDecodeBasicAuthCredentials() {
        // Arrange
        String username = "testuser";
        String password = "testpass";
        String credentials = username + ":" + password;
        String encodedCredentials = Base64.getEncoder().encodeToString(credentials.getBytes());
        String authHeader = "Basic " + encodedCredentials;

        // Act - This will internally decode the credentials
        // We'll verify by checking the authentication process
        userService.saveUser(new com.cognizant.springlearn.dto.User(username, password, "USER"));
        
        var response = authenticationService.authenticate(authHeader);

        // Assert
        assertEquals(username, response.getUsername(), "Decoded username should match");

        System.out.println("✓ Test F Passed: Basic Auth credentials decoded");
    }

    @Test
    @DisplayName("Test G: Generate different tokens for same user at different times")
    public void testGenerateDifferentTokensForSameUser() {
        // Act
        String token1 = jwtTokenProvider.generateToken(TEST_USERNAME);
        String token2 = jwtTokenProvider.generateToken(TEST_USERNAME);

        // Assert
        assertNotEquals(token1, token2, "Different token generations should produce different tokens");

        System.out.println("✓ Test G Passed: Different tokens generated for same user");
    }

    @Test
    @DisplayName("Test H: Token contains correct subject (username)")
    public void testTokenContainsCorrectSubject() {
        // Arrange
        String expectedUsername = "john_doe";
        
        // Act
        String token = jwtTokenProvider.generateToken(expectedUsername);
        String extractedUsername = jwtTokenProvider.getUsernameFromToken(token);

        // Assert
        assertEquals(expectedUsername, extractedUsername, "Token should contain the correct username");

        System.out.println("✓ Test H Passed: Token contains correct subject");
        System.out.println("  Expected: " + expectedUsername);
        System.out.println("  Extracted: " + extractedUsername);
    }
}
