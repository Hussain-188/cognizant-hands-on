# Exercise 3: JWT Authentication Service

## Overview

This exercise demonstrates how to implement JWT (JSON Web Token) based authentication in a Spring Boot 3 REST API. The application provides an authentication endpoint that accepts HTTP Basic Authentication credentials and returns a JWT token.

## Learning Objectives

By completing this exercise, you will understand how to:

1. Create an authentication controller and configure Spring Security
2. Read and decode the Authorization header (Basic Auth)
3. Generate JWT tokens for authenticated users

## Three Major Steps

### Step 1: Create Authentication Controller and Configure SecurityConfig

- **File**: `src/main/java/com/cognizant/springlearn/controller/AuthenticationController.java`
- **File**: `src/main/java/com/cognizant/springlearn/config/SecurityConfig.java`
- Creates a REST endpoint `/authenticate` that accepts HTTP requests
- Configures Spring Security to permit unauthenticated access to `/authenticate` endpoint
- Sets up stateless session management appropriate for JWT-based APIs

### Step 2: Read Authorization Header and Decode Credentials

- **File**: `src/main/java/com/cognizant/springlearn/service/AuthenticationService.java`
- Extracts the Basic Auth header from HTTP request
- Decodes the Base64-encoded username and password
- Validates credentials against registered users

### Step 3: Generate Token Based on Authenticated User

- **File**: `src/main/java/com/cognizant/springlearn/util/JwtTokenProvider.java`
- Generates JWT tokens with user information
- Includes token expiration time
- Signs tokens with secret key using HMAC-SHA256

## Architecture

```
HTTP Request (Basic Auth)
    ↓
AuthenticationController (Step 1)
    ↓
AuthenticationService (Step 2 - Decode Auth Header)
    ↓
UserService (Validate Credentials)
    ↓
JwtTokenProvider (Step 3 - Generate Token)
    ↓
HTTP Response (JWT Token)
```

## Project Structure

```
Exercise3_JWTAuthentication/
├── pom.xml
├── README.md (this file)
└── src/
    ├── main/
    │   ├── java/com/cognizant/springlearn/
    │   │   ├── SpringLearnApplication.java         (Main Application)
    │   │   ├── config/
    │   │   │   └── SecurityConfig.java             (Step 1: Security Configuration)
    │   │   ├── controller/
    │   │   │   └── AuthenticationController.java   (Step 1: Authentication Endpoint)
    │   │   ├── dto/
    │   │   │   ├── AuthenticationResponse.java
    │   │   │   └── User.java
    │   │   ├── service/
    │   │   │   ├── AuthenticationService.java      (Step 2: Credential Decoding)
    │   │   │   └── UserService.java
    │   │   └── util/
    │   │       └── JwtTokenProvider.java           (Step 3: Token Generation)
    │   └── resources/
    │       └── application.properties
    └── test/
        ├── java/com/cognizant/springlearn/
        │   ├── AuthenticationIntegrationTest.java
        │   └── JwtTokenProviderTest.java
        └── resources/
            └── application-test.properties
```

## Running the Application

### Prerequisites

- Java 17 or higher
- Maven 3.6 or higher
- Default port: 8090

### Build and Run

```bash
# Navigate to the project directory
cd Exercise3_JWTAuthentication

# Build the project
mvn clean install

# Run the application
mvn spring-boot:run
```

The application will start on `http://localhost:8090`

## Testing the Application

### Test Credentials

Pre-configured users:

- **Username**: `user`, **Password**: `password` (Role: USER)
- **Username**: `admin`, **Password**: `admin123` (Role: ADMIN)

### Using curl

```bash
# Authenticate and get JWT token
curl -s -u user:password http://localhost:8090/authenticate

# With admin credentials
curl -s -u admin:admin123 http://localhost:8090/authenticate

# Health check
curl http://localhost:8090/health
```

### Example Request/Response

**Request:**

```bash
curl -s -u user:password http://localhost:8090/authenticate
```

**Response:**

```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyIiwiaWF0IjoxNTcwMzc5NDc0LCJleHAiOjE1NzAzODA2NzR9.t3LRvlCV-hwKfoqZYlaVQqEUiBloWcWn0ft3tgv0dL0",
  "type": "Bearer",
  "username": "user",
  "expiresIn": 1200
}
```

### Running Tests

```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=AuthenticationIntegrationTest

# Run with coverage
mvn test jacoco:report
```

## Key Components

### 1. AuthenticationController

- REST endpoint: `GET /authenticate`
- Accepts Basic Authentication credentials
- Returns JWT token in response

### 2. SecurityConfig

- Disables CSRF (appropriate for REST APIs)
- Permits `/authenticate` endpoint without authentication
- Configures stateless session management
- Enables HTTP Basic Auth

### 3. AuthenticationService

- Orchestrates the authentication process
- Decodes Base64-encoded credentials
- Validates credentials against user store
- Calls JWT provider to generate token

### 4. JwtTokenProvider

- Generates JWT tokens using jjwt library
- Validates token signatures
- Extracts user information from tokens
- Handles token expiration

### 5. UserService

- In-memory user store for demo purposes
- In production, would connect to database
- Validates username and password

## JWT Token Details

The generated JWT tokens contain:

- **Header**: Algorithm (HS256) and token type (JWT)
- **Payload**: Subject (username), issued-at (iat), expiration (exp)
- **Signature**: HMAC-SHA256 signature using secret key

Example decoded JWT:

```
Header: {"alg":"HS256","typ":"JWT"}
Payload: {"sub":"user","iat":1570379474,"exp":1570380674}
Signature: [HMAC-SHA256 signature bytes]
```

## Configuration

### application.properties

```properties
# JWT expiration time in milliseconds (default: 1200000 = 20 minutes)
jwt.expiration=1200000

# JWT secret key (should be at least 256 bits for HS256)
jwt.secret=mySecretKeyForJWTGenerationAndValidationThatIsAtLeast256BitsLongForHS256Algorithm

# Server port
server.port=8090
```

## Dependencies

- **Spring Boot Starter Web**: REST API framework
- **Spring Boot Starter Security**: Security configuration
- **jjwt**: JWT library (0.12.3)
  - `jjwt-api`: JWT API
  - `jjwt-impl`: Implementation
  - `jjwt-jackson`: JSON processing
- **Lombok**: Code generation
- **Spring Security Test**: Security testing utilities

## Common Issues and Troubleshooting

### Issue: "Invalid or missing Authorization header"

**Solution**: Ensure the curl command uses `-u username:password` option

### Issue: "Invalid Base64 encoding in Authorization header"

**Solution**: The Authorization header must contain valid Base64-encoded credentials

### Issue: "Invalid username or password"

**Solution**: Check the pre-configured credentials (user:password or admin:admin123)

### Issue: Port already in use

**Solution**: Change the port in `application.properties` or kill the existing process

## Next Steps

After completing this exercise, you can:

1. Add JWT token validation filter for protected endpoints
2. Integrate with a real database instead of in-memory store
3. Implement token refresh mechanism
4. Add role-based authorization
5. Implement logout/token blacklisting

## References

- [Spring Security Documentation](https://spring.io/projects/spring-security)
- [jjwt GitHub Repository](https://github.com/jwtk/jjwt)
- [JWT.io](https://jwt.io/) - JWT decoder tool
- [Spring Boot 3 Documentation](https://spring.io/projects/spring-boot)

## Test Coverage

The exercise includes comprehensive tests:

### AuthenticationIntegrationTest

- Full integration tests with TestRestTemplate
- Tests valid and invalid authentication scenarios
- Verifies token structure and content

### JwtTokenProviderTest

- Unit tests for JWT generation and validation
- Tests token extraction and expiration checks

### Test Results

```
✓ Test 1: Authenticate with valid credentials and receive JWT token
✓ Test 2: HTTP endpoint returns token with valid Basic Auth
✓ Test 3: Reject authentication with invalid credentials
✓ Test 4: Reject authentication without Authorization header
✓ Test 5: Reject authentication with invalid header format
✓ Test 6: Verify JWT token structure
✓ Test 7: Authenticate with admin user
✓ Test 8: Health endpoint is accessible

✓ Test A: Generate JWT token successfully
✓ Test B: Validate valid JWT token
✓ Test C: Reject invalid JWT token
✓ Test D: Extract username from valid token
✓ Test E: Check token is not expired
✓ Test F: Decode Basic Auth credentials correctly
✓ Test G: Generate different tokens for same user
✓ Test H: Token contains correct subject
```

## License

This educational exercise is part of the Cognizant Hands-On program.
