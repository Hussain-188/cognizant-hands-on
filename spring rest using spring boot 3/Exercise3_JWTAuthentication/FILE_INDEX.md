# Exercise 3: JWT Authentication Service - File Index

## 📋 Quick Navigation

### 🚀 Start Here

- [QUICK_START.md](QUICK_START.md) - Get up and running in 5 minutes
- [README.md](README.md) - Complete project documentation
- [EXERCISE3_JWT_AUTHENTICATION.md](EXERCISE3_JWT_AUTHENTICATION.md) - Detailed exercise guide

### 🏗️ Architecture & Implementation

- [IMPLEMENTATION_SUMMARY.md](IMPLEMENTATION_SUMMARY.md) - Project structure overview
- [FILE_INDEX.md](FILE_INDEX.md) - This file

---

## 📂 Source Code Structure

### Main Application

```
src/main/java/com/cognizant/springlearn/
├── SpringLearnApplication.java                  Entry point for Spring Boot
├── config/
│   └── SecurityConfig.java                      ⭐ STEP 1: Security configuration
├── controller/
│   └── AuthenticationController.java            ⭐ STEP 1: REST endpoint (/authenticate)
├── service/
│   ├── AuthenticationService.java               ⭐ STEP 2 & 3: Credential decoding & token generation
│   └── UserService.java                         User management & validation
├── util/
│   └── JwtTokenProvider.java                    ⭐ STEP 3: JWT token operations
└── dto/
    ├── AuthenticationResponse.java              Response DTO
    └── User.java                                User credentials DTO
```

### Configuration & Resources

```
src/main/resources/
└── application.properties                       Spring Boot configuration
    - jwt.secret: Secret key for signing
    - jwt.expiration: Token expiration time
    - server.port: Default 8090
```

### Test Code

```
src/test/java/com/cognizant/springlearn/
├── AuthenticationIntegrationTest.java           8 integration tests
└── JwtTokenProviderTest.java                    8 unit tests

src/test/resources/
└── application-test.properties                  Test configuration
```

---

## 📄 Detailed File Descriptions

### 1. pom.xml

**Purpose**: Maven project configuration
**Contains**:

- Spring Boot 3.3.0 parent POM
- Spring Web, Security dependencies
- jjwt library (0.12.3) for JWT
- Lombok for code generation
- Testing dependencies

**Key Dependencies**:

- `spring-boot-starter-web`: REST API framework
- `spring-boot-starter-security`: Security framework
- `jjwt-api`, `jjwt-impl`, `jjwt-jackson`: JWT support

---

### 2. src/main/java/com/cognizant/springlearn/SpringLearnApplication.java

**Purpose**: Spring Boot application entry point
**Key Points**:

- @SpringBootApplication annotation
- Enables component scanning
- Main method to bootstrap the application

**Port**: 8090 (configurable in application.properties)

---

### 3. src/main/java/com/cognizant/springlearn/config/SecurityConfig.java

**⭐ STEP 1 Implementation**
**Purpose**: Configure Spring Security for JWT authentication
**Key Features**:

- Disables CSRF (appropriate for REST APIs)
- Permits `/authenticate` endpoint without authentication
- Requires authentication for all other endpoints
- Configures stateless session management
- Enables HTTP Basic Authentication

**Code Flow**:

```
HttpSecurity Configuration
├── CSRF Disabled
├── Authorization Rules
│   ├── /authenticate → permitAll()
│   ├── /health → permitAll()
│   └── Others → authenticated()
├── Session Management → STATELESS
└── HTTP Basic Auth → Enabled
```

**Why Stateless?**: JWT tokens contain all needed information, no server-side session required.

---

### 4. src/main/java/com/cognizant/springlearn/controller/AuthenticationController.java

**⭐ STEP 1 Implementation**
**Purpose**: REST endpoint handler for authentication requests
**Endpoints**:

- `GET /authenticate` - Returns JWT token (requires Basic Auth)
- `GET /health` - Health check endpoint

**Request Format**:

```
GET /authenticate
Authorization: Basic base64(username:password)
```

**Response Format**:

```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "type": "Bearer",
  "username": "user",
  "expiresIn": 1200
}
```

**Error Handling**:

- 401 Unauthorized: Invalid or missing credentials
- 500 Internal Server Error: Unexpected errors

---

### 5. src/main/java/com/cognizant/springlearn/service/AuthenticationService.java

**⭐ STEP 2 & 3 Implementation**
**Purpose**: Orchestrate the complete authentication flow
**Process**:

1. Receive Authorization header
2. Validate header format ("Basic ...")
3. Extract Base64-encoded portion
4. Decode to get username:password
5. Split on ":"
6. Validate credentials against user store
7. Generate JWT token
8. Return response

**Key Methods**:

- `authenticate(String authHeader)` - Main authentication method
- `decodeCredentials(String authHeader)` - Decode Base64 credentials

**Error Conditions**:

- Missing Authorization header
- Invalid header format
- Invalid credentials
- Invalid Base64 encoding

**Example Flow**:

```
"Authorization: Basic dXNlcjpwYXNzd29yZA=="
        ↓
    Decode Base64
        ↓
    "user:password"
        ↓
    Split on ":"
        ↓
    username="user", password="password"
        ↓
    Validate in UserService
        ↓
    Generate JWT via JwtTokenProvider
```

---

### 6. src/main/java/com/cognizant/springlearn/service/UserService.java

**Purpose**: User management and credential validation
**Features**:

- In-memory user store (for demo)
- User authentication
- User lookup by username

**Pre-configured Users**:

1. Username: "user", Password: "password", Role: "USER"
2. Username: "admin", Password: "admin123", Role: "ADMIN"

**Key Methods**:

- `authenticateUser(String username, String password)` - Validate credentials
- `findByUsername(String username)` - Find user by username
- `saveUser(User user)` - Add/update user

**Note**: For production, replace in-memory store with database (JPA, MongoDB, etc.)

---

### 7. src/main/java/com/cognizant/springlearn/util/JwtTokenProvider.java

**⭐ STEP 3 Implementation**
**Purpose**: JWT token generation and validation
**Algorithm**: HMAC-SHA256

**Token Structure**:

```
Header: {"alg":"HS256","typ":"JWT"}
Payload: {"sub":"user","iat":1570379474,"exp":1570380674}
Signature: [HMAC-SHA256(header.payload)]
```

**Key Methods**:

- `generateToken(String username)` - Create new JWT
- `validateToken(String token)` - Verify token integrity
- `getUsernameFromToken(String token)` - Extract username
- `getExpirationDateFromToken(String token)` - Get expiry date
- `isTokenExpired(String token)` - Check expiration

**Configuration**:

- Secret Key: 256+ bits (configured in application.properties)
- Expiration: 1200000ms (20 minutes)
- Algorithm: HS256

**Token Lifespan Example**:

```
Token Generated: 2024-01-01 10:00:00
Expiration: 2024-01-01 10:20:00 (+ 20 minutes)
After 10:20:00: Token is expired
```

---

### 8. src/main/java/com/cognizant/springlearn/dto/AuthenticationResponse.java

**Purpose**: Response DTO for authentication endpoint
**Fields**:

- `token` (String) - The JWT token
- `type` (String) - Token type ("Bearer")
- `username` (String) - Authenticated username
- `expiresIn` (Long) - Expiration time in seconds

**JSON Example**:

```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyIiwiaWF0IjoxNTcwMzc5NDc0LCJleHAiOjE1NzAzODA2NzR9.t3LRvlCV-hwKfoqZYlaVQqEUiBloWcWn0ft3tgv0dL0",
  "type": "Bearer",
  "username": "user",
  "expiresIn": 1200
}
```

---

### 9. src/main/java/com/cognizant/springlearn/dto/User.java

**Purpose**: User credential DTO
**Fields**:

- `username` (String) - User identifier
- `password` (String) - User password
- `role` (String) - User role (default: "USER")

---

### 10. src/test/java/com/cognizant/springlearn/AuthenticationIntegrationTest.java

**Purpose**: Integration tests for full authentication flow
**Test Count**: 8 tests
**Coverage**:

1. Valid credentials → JWT token
2. HTTP endpoint returns token
3. Invalid credentials rejected
4. Missing header rejected
5. Invalid header format rejected
6. JWT token structure validation
7. Admin user authentication
8. Health endpoint accessibility

**Test Framework**: JUnit 5
**Testing Tools**: TestRestTemplate, MockMvc

---

### 11. src/test/java/com/cognizant/springlearn/JwtTokenProviderTest.java

**Purpose**: Unit tests for JWT token provider
**Test Count**: 8 tests
**Coverage**:
A. Generate JWT token
B. Validate valid token
C. Reject invalid token
D. Extract username from token
E. Check token not expired
F. Decode Basic Auth credentials
G. Generate different tokens for same user
H. Token contains correct subject

**Test Framework**: JUnit 5

---

### 12. src/main/resources/application.properties

**Purpose**: Spring Boot configuration
**Key Properties**:

```properties
spring.application.name=spring-jwt-authentication
server.port=8090
jwt.secret=mySecretKeyForJWTGenerationAndValidationThatIsAtLeast256BitsLongForHS256Algorithm
jwt.expiration=1200000
logging.level.com.cognizant.springlearn=DEBUG
```

**Configuration Details**:

- **server.port**: Application runs on port 8090
- **jwt.secret**: Secret key for signing (must be 256+ bits for HS256)
- **jwt.expiration**: Token validity duration in milliseconds (1200000 = 20 min)
- **logging.level**: Debug level for this package

---

### 13. src/test/resources/application-test.properties

**Purpose**: Test-specific configuration
**Key Differences from Main**:

- server.port: 0 (random port for tests)
- jwt.expiration: 3600000 (1 hour for tests)
- logging.level: INFO (less verbose)

---

### 14. README.md

**Purpose**: Complete project documentation
**Sections**:

- Overview and learning objectives
- Three major steps explanation
- Architecture diagram
- Project structure
- Running instructions
- Testing guide
- Key components explanation
- JWT token details
- Configuration reference
- Troubleshooting guide
- Next steps for enhancement

---

### 15. EXERCISE3_JWT_AUTHENTICATION.md

**Purpose**: Detailed hands-on exercise guide
**Sections**:

- Business requirements
- Step 1: Controller & SecurityConfig
- Step 2: Authorization header decoding
- Step 3: Token generation
- Complete flow diagram
- Testing instructions
- Key takeaways
- Common mistakes to avoid

---

### 16. IMPLEMENTATION_SUMMARY.md

**Purpose**: High-level implementation overview
**Sections**:

- Project structure visualization
- Technologies & dependencies
- Quick start guide
- Test coverage summary
- JWT token structure
- Learning outcomes
- Important notes

---

### 17. QUICK_START.md

**Purpose**: 5-minute getting started guide
**Sections**:

- Quick navigation links
- Build and run instructions
- Test execution commands
- Test credentials
- Key endpoints
- Example curl commands
- Configuration details
- Quick reference

---

### 18. .gitignore

**Purpose**: Git ignore rules
**Excludes**:

- Maven target/ directory
- IDE files (.idea/, .vscode/)
- Build artifacts (.jar, .class)
- Log files
- Environment files

---

## 🎯 Step-by-Step Implementation Guide

### To Understand Step 1 (Controller & SecurityConfig)

Read in this order:

1. `QUICK_START.md` - Overview
2. `SecurityConfig.java` - Configuration code
3. `AuthenticationController.java` - Endpoint code
4. `unittest from AuthenticationIntegrationTest.java` - Test 1 & 2

### To Understand Step 2 (Decode Credentials)

Read in this order:

1. `AuthenticationService.java` - `decodeCredentials()` method
2. `EXERCISE3_JWT_AUTHENTICATION.md` - Step 2 section
3. `JwtTokenProviderTest.java` - Test F

### To Understand Step 3 (Generate Token)

Read in this order:

1. `JwtTokenProvider.java` - `generateToken()` method
2. `EXERCISE3_JWT_AUTHENTICATION.md` - Step 3 section
3. `JwtTokenProviderTest.java` - Tests A-H

---

## 🔬 Code Reading Path

### For Learning JWT Concepts

1. Start: `QUICK_START.md`
2. Overview: `IMPLEMENTATION_SUMMARY.md`
3. JWT Details: `JwtTokenProvider.java`
4. Tests: `JwtTokenProviderTest.java`

### For Learning Spring Security

1. Start: `QUICK_START.md`
2. Configuration: `SecurityConfig.java`
3. Controller: `AuthenticationController.java`
4. Integration Tests: `AuthenticationIntegrationTest.java`

### For Learning HTTP Basic Auth

1. Start: `EXERCISE3_JWT_AUTHENTICATION.md` (Step 2)
2. Implementation: `AuthenticationService.java`
3. User Validation: `UserService.java`
4. Tests: See `Test F` in `JwtTokenProviderTest.java`

---

## ✅ Checklist for Using This Exercise

- [ ] Read `QUICK_START.md`
- [ ] Build with `mvn clean install`
- [ ] Run with `mvn spring-boot:run`
- [ ] Test with curl commands
- [ ] Run tests with `mvn test`
- [ ] Read `SecurityConfig.java` (understand Step 1)
- [ ] Read `AuthenticationService.java` (understand Step 2)
- [ ] Read `JwtTokenProvider.java` (understand Step 3)
- [ ] Review all test cases
- [ ] Try modifying code and see how tests break/pass
- [ ] Plan next enhancements (token validation filter, DB integration)

---

**Total Files**: 18
**Total Lines of Code**: ~1500+
**Test Coverage**: 16 comprehensive tests
**Framework**: Spring Boot 3.3.0
**Java Version**: 17+

---

**Status**: ✅ Complete and Ready to Use!
