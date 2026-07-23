package com.cognizant.springlearn.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * User DTO representing user credentials
 * 
 * Step 2: Read Authorization header and decode username and password
 * 
 * This DTO represents a user with username and password.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    private String username;
    private String password;
    private String role = "USER";
}
