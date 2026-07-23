package com.cognizant.springlearn.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Security Configuration
 * 
 * Step 1: Create Authentication Controller and configure it in SecurityConfig
 * 
 * This configuration class sets up Spring Security for JWT-based authentication:
 * - Permits the /authenticate endpoint without authentication
 * - Requires authentication for other endpoints
 * - Disables CSRF (appropriate for REST APIs)
 * - Configures stateless session management (suitable for JWT)
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);

    /**
     * Configure HTTP Security
     * 
     * @param http HttpSecurity object to configure
     * @return SecurityFilterChain
     * @throws Exception if configuration fails
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        logger.info("Configuring Spring Security for JWT Authentication");

        http
                // Disable CSRF for REST API (since we use JWT tokens)
                .csrf(csrf -> csrf.disable())
                
                // Configure authorization rules
                .authorizeHttpRequests(authz -> authz
                        // Permit access to /authenticate endpoint without authentication
                        .requestMatchers("/authenticate").permitAll()
                        // Permit access to /health endpoint
                        .requestMatchers("/health").permitAll()
                        // All other requests require authentication
                        .anyRequest().authenticated()
                )
                
                // Use stateless session management (no session cookies)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                
                // Configure HTTP Basic Authentication for the authenticate endpoint
                .httpBasic(basic -> {});

        logger.info("Spring Security configuration completed");
        return http.build();
    }

    /**
     * Password Encoder Bean
     * 
     * @return BCryptPasswordEncoder for password encoding
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        logger.info("Initializing BCryptPasswordEncoder");
        return new BCryptPasswordEncoder();
    }
}
