# Exercise 3: JWT Authentication Service - Hands-On Implementation

## Exercise Description

Create a JWT (JSON Web Token) authentication service that generates and returns JWT tokens when users provide their credentials via HTTP Basic Authentication.

This exercise is divided into three major steps that build up the complete authentication flow.

## Business Requirement

The authentication service should:

- Accept HTTP Basic Auth credentials (username:password)
- Validate the provided credentials
- Generate a JWT token for valid users
- Return the token in JSON format

### Expected Behavior

When the following curl command is executed:

```bash
curl -s -u user:pwd http://localhost:8090/authenticate
```

The service should respond with:

```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyIiwiaWF0IjoxNTcwMzc5NDc0LCJleHAiOjE1NzAzODA2NzR9.t3LRvlCV-hwKfoqZYlaVQqEUiBloWcWn0ft3tgv0dL0",
  "type": "Bearer",
  "username": "user",
  "expiresIn": 1200
}
```

## Step 1: Create Authentication Controller and Configure SecurityConfig

### Objective

Set up the REST endpoint that accepts authentication requests and configure Spring Security to allow public access to this endpoint.

### What You'll Create

1. **AuthenticationController.java**
   - REST controller with `/authenticate` endpoint
   - Handles HTTP GET/POST requests with Authorization header
   - Returns JWT token in response

2. **SecurityConfig.java**
   - Spring Security configuration class
   - Permits `/authenticate` endpoint without authentication
   - Configures stateless session management
   - Enables HTTP Basic Authentication

### Key Concepts

- **@RestController**: Marks the class as a REST controller
- **@RequestMapping/@GetMapping**: Maps HTTP endpoints
- **SecurityFilterChain**: Configures security rules
- **permitAll()**: Allows access without authentication
- **SessionCreationPolicy.STATELESS**: No session cookies (appropriate for JWT)

### Implementation Details

The SecurityConfig should:

- Disable CSRF (REST APIs don't use CSRF tokens)
- Allow unauthenticated access to `/authenticate`
- Require authentication for all other endpoints
- Use stateless sessions (no server-side session storage)

### Code Highlights

```java
@Bean
public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.csrf(csrf -> csrf.disable())
        .authorizeHttpRequests(authz -> authz
            .requestMatchers("/authenticate").permitAll()
            .anyRequest().authenticated()
        )
        .sessionManagement(session -> session
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        )
        .httpBasic(basic -> {});
    return http.build();
}
```

## Step 2: Read Authorization Header and Decode Credentials

### Objective

Extract and decode the Basic Authentication credentials from the HTTP Authorization header.

### What You'll Create

1. **AuthenticationService.java**
   - Service that orchestrates authentication flow
   - Extracts Authorization header
   - Decodes Base64-encoded credentials
   - Validates credentials against user store

### Key Concepts

- **HTTP Basic Authentication**: Username and password encoded in Base64
- **Authorization Header Format**: `Authorization: Basic base64(username:password)`
- **Base64 Encoding/Decoding**: Converting between text and Base64 formats

### Implementation Details

The authentication process should:

1. Check if Authorization header exists and starts with "Basic "
2. Extract the Base64-encoded portion
3. Decode the Base64 string to get "username:password"
4. Split on ":" to separate username and password

### Code Highlights

```java
private String decodeCredentials(String authHeader) {
    String base64Credentials = authHeader.replace("Basic ", "");
    byte[] decodedBytes = Base64.getDecoder().decode(base64Credentials);
    return new String(decodedBytes);  // Returns "username:password"
}
```

### Example

```
HTTP Request:
Authorization: Basic dXNlcjpwYXNzd29yZA==

Decoded:
dXNlcjpwYXNzd29yZA== → user:password
↓
Split: ["user", "password"]
```

## Step 3: Generate Token Based on Authenticated User

### Objective

After validating credentials, generate a JWT token with user information and return it to the client.

### What You'll Create

1. **JwtTokenProvider.java**
   - Utility component for JWT operations
   - Generates tokens with user info and expiration
   - Validates token signatures
   - Extracts information from tokens

2. **User Management**
   - UserService for credential validation
   - In-memory user store for demo
   - Support for multiple users with different roles

### Key Concepts

- **JWT Structure**: Header.Payload.Signature
- **JWT Claims**: Username, issued-at (iat), expiration (exp)
- **Token Signing**: HMAC-SHA256 algorithm
- **Token Validation**: Verify signature and expiration before accepting

### Implementation Details

Creating a JWT token involves:

1. Building the token with user claims
2. Setting expiration time (typically 15-60 minutes)
3. Signing with a secret key using HMAC-SHA256
4. Returning the compact token string

### Code Highlights

```java
public String generateToken(String username) {
    Date now = new Date();
    Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);

    SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes());

    return Jwts.builder()
        .subject(username)
        .issuedAt(now)
        .expiration(expiryDate)
        .signWith(key, SignatureAlgorithm.HS256)
        .compact();
}
```

### JWT Token Anatomy

```
Header:
{
  "alg": "HS256",
  "typ": "JWT"
}

Payload:
{
  "sub": "user",           // Subject (username)
  "iat": 1570379474,       // Issued at (timestamp)
  "exp": 1570380674        // Expiration (timestamp)
}

Signature:
[HMAC-SHA256 hash of header.payload]
```

## Complete Flow

```
1. Client sends request with Basic Auth:
   GET /authenticate
   Authorization: Basic dXNlcjpwYXNzd29yZA==

2. AuthenticationController receives request
   ↓
3. SecurityConfig permits access (Step 1)
   ↓
4. AuthenticationService extracts Authorization header
   ↓
5. Decodes Base64: dXNlcjpwYXNzd29yZA== → user:password  (Step 2)
   ↓
6. UserService validates credentials
   ↓
7. JwtTokenProvider generates token (Step 3)
   ↓
8. AuthenticationResponse returned to client:
   {
     "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyIiwiaWF0IjoxNTcwMzc5NDc0LCJleHAiOjE1NzAzODA2NzR9.t3LRvlCV-hwKfoqZYlaVQqEUiBloWcWn0ft3tgv0dL0",
     "type": "Bearer",
     "username": "user",
     "expiresIn": 1200
   }
```

## Testing the Implementation

### Unit Tests

Run tests for individual components:

```bash
mvn test -Dtest=JwtTokenProviderTest
```

Tests cover:

- Token generation
- Token validation
- Credential decoding
- Token expiration

### Integration Tests

Run full flow tests:

```bash
mvn test -Dtest=AuthenticationIntegrationTest
```

Tests cover:

- Valid authentication scenarios
- Invalid credential rejection
- Missing header scenarios
- HTTP endpoint behavior

### Manual Testing with curl

```bash
# Successful authentication
curl -u user:password http://localhost:8090/authenticate

# Failed authentication (wrong password)
curl -u user:wrongpass http://localhost:8090/authenticate

# No credentials
curl http://localhost:8090/authenticate
```

## Key Takeaways

1. **Step 1**: Setting up authentication endpoints requires proper security configuration
2. **Step 2**: Basic Auth credentials are Base64-encoded and must be decoded before use
3. **Step 3**: JWT tokens are stateless credentials that encode user information and signature

## Most Common Mistakes to Avoid

1. ❌ Forgetting to permit `/authenticate` endpoint in SecurityConfig
2. ❌ Not decoding the Base64-encoded credentials properly
3. ❌ Using weak secret keys for JWT signing
4. ❌ Not handling token expiration
5. ❌ Storing passwords in plain text (use password encoding in production)

## Files Modified/Created

```
Exercise3_JWTAuthentication/
├── pom.xml                                       (Dependencies)
├── README.md                                     (Documentation)
├── src/main/resources/application.properties     (Configuration)
├── src/main/java/com/cognizant/springlearn/
│   ├── SpringLearnApplication.java               (Main Application)
│   ├── config/SecurityConfig.java                (Step 1)
│   ├── controller/AuthenticationController.java  (Step 1)
│   ├── service/AuthenticationService.java        (Step 2 & 3)
│   ├── service/UserService.java                  (User Management)
│   ├── util/JwtTokenProvider.java                (Step 3)
│   └── dto/
│       ├── AuthenticationResponse.java
│       └── User.java
└── src/test/java/...                            (Tests)
```

## Additional Reference Materials

- JWT explained: https://jwt.io/
- Spring Security docs: https://spring.io/projects/spring-security
- jjwt library: https://github.com/jwtk/jjwt
- Base64 encoding: https://en.wikipedia.org/wiki/Base64

## Summary

This exercise demonstrates the three critical steps of JWT authentication:

1. Controller setup and security configuration
2. Credential extraction and decoding
3. Token generation and validation

By completing this exercise, you understand how modern REST APIs handle authentication using stateless JWT tokens instead of server-side sessions.
