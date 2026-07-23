# Exercise 3: JWT Authentication - Quick Start Guide

## 📦 What's Included

A complete Spring Boot 3 JWT authentication service with:

- ✅ REST endpoint for token generation
- ✅ HTTP Basic Authentication support
- ✅ JWT token generation with HMAC-SHA256
- ✅ Comprehensive test suite (16 tests)
- ✅ Production-ready code patterns

## ⚡ Quick Start (5 minutes)

### 1. Navigate to Project

```bash
cd "spring rest using spring boot 3/Exercise3_JWTAuthentication"
```

### 2. Build Project

```bash
mvn clean install
```

### 3. Run Application

```bash
mvn spring-boot:run
```

Expected output: Application starts on port 8090

### 4. Test Authentication (in another terminal)

```bash
curl -s -u user:password http://localhost:8090/authenticate | python -m json.tool
```

### 5. You Should See

```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyIiwiaWF0IjoxNTcwMzc5NDc0LCJleHAiOjE1NzAzODA2NzR9.t3LRvlCV-hwKfoqZYlaVQqEUiBloWcWn0ft3tgv0dL0",
  "type": "Bearer",
  "username": "user",
  "expiresIn": 1200
}
```

## 🧪 Run Tests

### Run All Tests

```bash
mvn test
```

### Run Integration Tests Only

```bash
mvn test -Dtest=AuthenticationIntegrationTest
```

### Run Unit Tests Only

```bash
mvn test -Dtest=JwtTokenProviderTest
```

## 📚 Understanding the Three Steps

### Step 1: Controller & Security Config

**Files**: `AuthenticationController.java`, `SecurityConfig.java`

- Exposes `/authenticate` endpoint
- Allows public access to this endpoint
- Configures stateless session management

### Step 2: Decode Credentials

**File**: `AuthenticationService.java`

- Reads Authorization header (e.g., "Basic dXNlcjpwYXNzd29yZA==")
- Decodes Base64 to get username:password
- Validates against user store

### Step 3: Generate Token

**Files**: `JwtTokenProvider.java`, `UserService.java`

- Creates JWT with user info
- Signs with HMAC-SHA256
- Returns to client

## 🔐 Test Credentials

| Username | Password |
| -------- | -------- |
| user     | password |
| admin    | admin123 |

## 🎯 Key Endpoints

| Endpoint        | Method | Auth       | Description    |
| --------------- | ------ | ---------- | -------------- |
| `/authenticate` | GET    | Basic Auth | Get JWT token  |
| `/health`       | GET    | None       | Service health |

## 🌐 Test Examples

### Valid Request

```bash
curl -u user:password http://localhost:8090/authenticate
```

### Invalid Password

```bash
curl -u user:wrongpass http://localhost:8090/authenticate
```

### Invalid User

```bash
curl -u nonexistent:password http://localhost:8090/authenticate
```

### No Credentials

```bash
curl http://localhost:8090/authenticate
```

## 📁 Important Files

| File                            | Purpose                     |
| ------------------------------- | --------------------------- |
| `AuthenticationController.java` | Step 1: REST endpoint       |
| `SecurityConfig.java`           | Step 1: Security setup      |
| `AuthenticationService.java`    | Step 2: Credential decoding |
| `JwtTokenProvider.java`         | Step 3: Token generation    |
| `UserService.java`              | Step 3: User validation     |
| `pom.xml`                       | Dependencies                |
| `application.properties`        | Configuration               |

## 🔧 Configuration

**File**: `src/main/resources/application.properties`

```properties
server.port=8090
jwt.secret=mySecretKeyForJWTGenerationAndValidationThatIsAtLeast256BitsLongForHS256Algorithm
jwt.expiration=1200000  # 20 minutes
```

## ✨ Token Details

Each JWT contains:

- **Algorithm**: HS256 (HMAC-SHA256)
- **Subject**: Username
- **Issued At**: Current timestamp
- **Expiration**: 20 minutes from generation
- **Signature**: Cryptographic verification

## 📖 Documentation

- **README.md** - Complete project guide
- **EXERCISE3_JWT_AUTHENTICATION.md** - Detailed exercise explanation
- **IMPLEMENTATION_SUMMARY.md** - Implementation overview

## ⚙️ Technologies Used

- Spring Boot 3.3.0
- Spring Security
- jjwt 0.12.3 (JWT library)
- Maven 3.6+
- Java 17+

## 🐛 Troubleshooting

### Port 8090 already in use

```bash
# Change in application.properties
server.port=8091
```

### Tests fail with "class not found"

```bash
mvn clean compile test
```

### Cannot decode token

- Ensure your -u credentials match configured users
- Check Authorization header format

## 🚀 Next Steps

After mastering this exercise:

1. Add JWT validation filter for protected endpoints
2. Integrate with a real database
3. Implement token refresh mechanism
4. Add role-based access control
5. Create protected endpoints that validate the token

## 📞 Quick Reference

**Start App**: `mvn spring-boot:run`
**Run Tests**: `mvn test`
**Build**: `mvn clean install`
**Authenticate**: `curl -u user:password http://localhost:8090/authenticate`
**JWT Tool**: https://jwt.io/

---

**Status**: ✅ Exercise Complete - Ready to Use!
