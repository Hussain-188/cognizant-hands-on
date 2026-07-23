package com.cognizant.springlearn.service;

import com.cognizant.springlearn.dto.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * User Service
 * 
 * Step 3: Generate token based on user retrieved in previous steps
 * 
 * This service performs user authentication and validation.
 * For this exercise, it uses an in-memory user store.
 * In production, this would connect to a database.
 */
@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    // In-memory user store for demo purposes
    private final Map<String, User> userStore = new HashMap<>();

    public UserService() {
        // Initialize with sample users
        userStore.put("user", new User("user", "password", "USER"));
        userStore.put("admin", new User("admin", "admin123", "ADMIN"));
        logger.info("UserService initialized with sample users");
    }

    /**
     * Authenticate user with username and password
     * 
     * @param username the username to authenticate
     * @param password the password to validate
     * @return User object if authentication successful
     */
    public Optional<User> authenticateUser(String username, String password) {
        logger.debug("Attempting to authenticate user: {}", username);

        User user = userStore.get(username);

        if (user != null && user.getPassword().equals(password)) {
            logger.info("User authenticated successfully: {}", username);
            return Optional.of(user);
        }

        logger.warn("Authentication failed for user: {}", username);
        return Optional.empty();
    }

    /**
     * Find user by username
     * 
     * @param username the username to find
     * @return User object if found
     */
    public Optional<User> findByUsername(String username) {
        logger.debug("Finding user by username: {}", username);
        return Optional.ofNullable(userStore.get(username));
    }

    /**
     * Create or update user in store
     * 
     * @param user the user to save
     */
    public void saveUser(User user) {
        logger.debug("Saving user: {}", user.getUsername());
        userStore.put(user.getUsername(), user);
        logger.info("User saved successfully: {}", user.getUsername());
    }
}
