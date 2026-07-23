# Exercise 3: JWT Authentication Service - Implementation Summary

## ✅ Completed Setup

A comprehensive JWT authentication exercise has been successfully created in the Spring REST using Spring Boot 3 section.

**Location**: `spring rest using spring boot 3/Exercise3_JWTAuthentication/`

## 📁 Project Structure

```
Exercise3_JWTAuthentication/
├── .gitignore
├── pom.xml                                          ← Maven configuration with JWT dependencies
├── README.md                                        ← Complete project documentation
├── EXERCISE3_JWT_AUTHENTICATION.md                   ← Detailed exercise guide
│
└── src/
    ├── main/
    │   ├── java/com/cognizant/springlearn/
    │   │   ├── SpringLearnApplication.java          ← Spring Boot entry point
    │   │   │
    │   │   ├── config/
    │   │   │   └── SecurityConfig.java              ← ⭐ STEP 1: Security Configuration
    │   │   │                                           - Permits /authenticate endpoint
    │   │   │                                           - Configures stateless sessions
    │   │   │                                           - Enables HTTP Basic Auth
    │   │   │
    │   │   ├── controller/
    │   │   │   └── AuthenticationController.java     ← ⭐ STEP 1: REST Endpoint
    │   │   │                                           - GET /authenticate
    │   │   │                                           - Returns JWT token
    │   │   │
    │   │   ├── service/
    │   │   │   ├── AuthenticationService.java        ← ⭐ STEP 2 & 3: Main Service
    │   │   │   │                                        - Decodes Base64 credentials
    │   │   │   │                                        - Validates user
    │   │   │   │                                        - Generates JWT
    │   │   │   └── UserService.java                  ← User management & validation
    │   │   │
    │   │   ├── util/
    │   │   │   └── JwtTokenProvider.java             ← ⭐ STEP 3: Token Provider
    │   │   │                                           - Generates JWT tokens
    │   │   │                                           - Validates tokens
    │   │   │                                           - Extracts user info
    │   │   │
    │   │   └── dto/
    │   │       ├── AuthenticationResponse.java       ← Response DTO with token
    │   │       └── User.java                         ← User credentials DTO
    │   │
    │   └── resources/
    │       └── application.properties                ← Configuration
    │           - jwt.secret: 256-bit key
    │           - jwt.expiration: 20 minutes
    │           - server.port: 8090
    │
    └── test/
        ├── java/com/cognizant/springlearn/
        │   ├── AuthenticationIntegrationTest.java    ← Full integration tests (8 tests)
        │   └── JwtTokenProviderTest.java             ← Unit tests (8 tests)
        │
        └── resources/
            └── application-test.properties

```

## 🎯 Three Major Steps Implemented

### Step 1: Create Authentication Controller & Configure SecurityConfig

**Objective**: Set up the REST endpoint and enable public access

**Key Files**:

- `AuthenticationController.java` - REST endpoint at `/authenticate`
- `SecurityConfig.java` - Spring Security configuration

**Features**:

- ✅ `/authenticate` endpoint exposed without authentication required
- ✅ Stateless session management (no server-side sessions)
- ✅ HTTP Basic Authentication support
- ✅ CSRF disabled (appropriate for REST APIs)

---

### Step 2: Read Authorization Header & Decode Credentials

**Objective**: Extract and decode Basic Auth credentials

**Key Files**:

- `AuthenticationService.java` - Credential processing

**Process**:

```
HTTP Authorization Header: "Basic dXNlcjpwYXNzd29yZA=="
                                ↓
                    Decode Base64 string
                                ↓
                    Extract: user:password
                                ↓
                    Validate credentials
```

**Features**:

- ✅ Validates Authorization header format
- ✅ Decodes Base64-encoded credentials
- ✅ Splits into username and password
- ✅ Error handling for invalid formats

---

### Step 3: Generate Token Based on Authenticated User

**Objective**: Create JWT token and return to client

**Key Files**:

- `JwtTokenProvider.java` - Token generation & validation
- `UserService.java` - User authentication

**Process**:

```
Validated User: { "username": "user", "role": "USER" }
              ↓
    Generate JWT with:
    - Subject: username
    - Issued At: current time
    - Expiration: +20 minutes
    - Signature: HMAC-SHA256
              ↓
    Return: eyJhbGciOiJIUzI1NiJ9...
```

**Features**:

- ✅ Generates JWT with user information
- ✅ Signs with HMAC-SHA256
- ✅ Includes expiration time (1200 seconds)
- ✅ Validates token integrity
- ✅ Extracts user info from token

## 🛠 Technologies & Dependencies

### Core Dependencies

```xml
<dependencies>
    <!-- Spring Boot Web (REST API) -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>

    <!-- Spring Security (Authentication & Authorization) -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-security</artifactId>
    </dependency>

    <!-- JWT Library (jjwt 0.12.3) -->
    <dependency>
        <groupId>io.jsonwebtoken</groupId>
        <artifactId>jjwt-api</artifactId>
        <version>0.12.3</version>
    </dependency>

    <!-- Plus: jjwt-impl, jjwt-jackson for runtime -->

    <!-- Lombok (Code Generation) -->
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
    </dependency>

    <!-- Testing -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-test</artifactId>
        <scope>test</scope>
    </dependency>
</dependencies>
```

## 🚀 Quick Start

### Build the Project

```bash
cd "spring rest using spring boot 3/Exercise3_JWTAuthentication"
mvn clean install
```

### Run the Application

```bash
mvn spring-boot:run
```

The service will start on `http://localhost:8090`

### Test with curl (Example)

```bash
# Request
curl -s -u user:password http://localhost:8090/authenticate

# Response
{
  "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyIiwiaWF0IjoxNTcwMzc5NDc0LCJleHAiOjE1NzAzODA2NzR9.t3LRvlCV-hwKfoqZYlaVQqEUiBloWcWn0ft3tgv0dL0",
  "type": "Bearer",
  "username": "user",
  "expiresIn": 1200
}
```

## 🧪 Test Coverage

### 16 Total Tests Created

#### AuthenticationIntegrationTest (8 tests)

- ✅ Authenticate with valid credentials
- ✅ HTTP endpoint returns token
- ✅ Reject invalid credentials
- ✅ Reject missing Authorization header
- ✅ Reject invalid header format
- ✅ Verify JWT token structure
- ✅ Authenticate with admin user
- ✅ Health endpoint accessibility

#### JwtTokenProviderTest (8 tests)

- ✅ Generate JWT token
- ✅ Validate valid token
- ✅ Reject invalid token
- ✅ Extract username from token
- ✅ Check token not expired
- ✅ Decode Basic Auth credentials
- ✅ Generate different tokens for same user
- ✅ Token contains correct subject

### Run Tests

```bash
# All tests
mvn test

# Specific test class
mvn test -Dtest=AuthenticationIntegrationTest
mvn test -Dtest=JwtTokenProviderTest

# With coverage report
mvn test jacoco:report
```

## 🔐 Pre-configured Users

The application includes in-memory user store with:

| Username | Password | Role  |
| -------- | -------- | ----- |
| user     | password | USER  |
| admin    | admin123 | ADMIN |

_Note: In production, connect to a real database with encrypted passwords_

## 📊 JWT Token Structure

Each generated token consists of three parts:

```
Header.Payload.Signature
│      │       │
│      │       └─ HMAC-SHA256 signature
│      │
│      └─ Base64-encoded claims
│         {
│           "sub": "user",        // Subject (username)
│           "iat": 1570379474,    // Issued at (Unix timestamp)
│           "exp": 1570380674     // Expiration (Unix timestamp)
│         }
│
└─ Base64-encoded header
   {
     "alg": "HS256",
     "typ": "JWT"
   }
```

## 📝 Configuration

### application.properties

```properties
# Server
server.port=8090

# JWT
jwt.secret=mySecretKeyForJWTGenerationAndValidationThatIsAtLeast256BitsLongForHS256Algorithm
jwt.expiration=1200000  # 20 minutes in milliseconds

# Logging
logging.level.com.cognizant.springlearn=DEBUG
```

## 🔍 Key Components Explained

### 1. AuthenticationController

- **Purpose**: Entry point for authentication requests
- **Endpoint**: `GET /authenticate`
- **Request**: Basic Auth header with credentials
- **Response**: JWT token in AuthenticationResponse

### 2. SecurityConfig

- **Purpose**: Configure Spring Security rules
- **Key Config**: Permits `/authenticate` without auth
- **Session Strategy**: Stateless (JWT-appropriate)

### 3. AuthenticationService

- **Purpose**: Orchestrate full authentication flow
- **Step 2**: Decode Base64 credentials
- **Step 3**: Call JWT provider

### 4. JwtTokenProvider

- **Purpose**: Create and validate JWT tokens
- **Algorithm**: HMAC-SHA256
- **Expiration**: 20 minutes default

### 5. UserService

- **Purpose**: Manage and validate users
- **Storage**: In-memory (demo)
- **Methods**: authenticateUser, findByUsername, saveUser

## 🎓 Learning Outcomes

After completing this exercise, you will understand:

1. ✅ How Spring Security enables HTTP Basic Authentication
2. ✅ How to decode Base64-encoded credentials
3. ✅ How JWT tokens are structured and generated
4. ✅ How to sign tokens with HMAC-SHA256
5. ✅ How to validate and extract information from tokens
6. ✅ Best practices for stateless REST API authentication
7. ✅ How to write integration and unit tests for authentication

## 🚨 Important Notes

- **Secret Key**: Must be at least 256 bits for HS256 algorithm
- **Token Expiration**: Default 20 minutes (1200000ms)
- **In-Memory Store**: Only for demo - use database in production
- **CSRF Disabled**: Appropriate for REST APIs using tokens
- **Stateless Sessions**: No server-side session storage needed

## 📚 Documentation Files

1. **README.md** - Complete project documentation
2. **EXERCISE3_JWT_AUTHENTICATION.md** - Detailed exercise guide with explanations
3. **This file** - Implementation summary and quick reference

## 🔗 Related Concepts

This exercise builds foundation for:

- JWT token validation filters
- Role-based authorization (RBAC)
- Token refresh mechanisms
- Multi-factor authentication (MFA)
- OAuth2 integration

---

## ✨ Exercise Complete!

The JWT Authentication Service is fully implemented with:

- ✅ All three major steps completed
- ✅ Comprehensive documentation
- ✅ 16 test cases
- ✅ Ready-to-run application
- ✅ Production-ready patterns (with in-memory demo data)

**Next Steps**: Build upon this foundation by adding token validation filters or integrating with a database!
