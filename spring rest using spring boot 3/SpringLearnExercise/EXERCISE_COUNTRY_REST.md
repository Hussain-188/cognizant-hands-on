# REST - Country Web Service Exercise

## Overview

This exercise demonstrates how to create a REST API endpoint that returns country details from a Spring XML configuration. The service loads the India country bean from `country.xml` and returns it as JSON through a REST endpoint.

## Endpoint Details

| Property              | Value                                                    |
| --------------------- | -------------------------------------------------------- |
| **URL**               | `http://localhost:8083/country`                          |
| **HTTP Method**       | GET                                                      |
| **Controller**        | `com.cognizant.springlearn.controller.CountryController` |
| **Method Name**       | `getCountryIndia()`                                      |
| **Method Annotation** | `@RequestMapping`                                        |

## Sample Request

```
GET http://localhost:8083/country
```

## Sample Response

```json
{
  "code": "IN",
  "name": "India"
}
```

---

## Detailed Explanation

### 1. What Happens in the Controller Method?

The `getCountryIndia()` method performs the following operations:

#### Step 1: Parameter Annotation

```java
@RequestMapping(value = "/country", method = RequestMethod.GET)
public Country getCountryIndia()
```

- `@RequestMapping` annotation maps HTTP requests to the specific method
- `value = "/country"` defines the URL path
- `method = RequestMethod.GET` specifies that this endpoint accepts GET requests

#### Step 2: Load Spring ApplicationContext

```java
ApplicationContext context = new ClassPathXmlApplicationContext("country.xml");
```

- Creates a Spring ApplicationContext by loading the `country.xml` configuration file from the classpath
- This initializes all beans defined in the XML configuration file
- The XML file contains the bean definition for India country

#### Step 3: Retrieve the Bean

```java
Country country = context.getBean("country", Country.class);
```

- Retrieves the bean with id "country" from the Spring container
- The bean is cast to the `Country` class type
- This bean has properties: `code = "IN"` and `name = "India"`

#### Step 4: Release Resources

```java
((ClassPathXmlApplicationContext) context).close();
```

- Closes the ApplicationContext to release resources
- Best practice to prevent resource leaks

#### Step 5: Return Response

```java
return country;
```

- Returns the Country object to the client
- Spring's REST framework automatically converts this object to JSON

---

### 2. How the Bean is Converted into JSON Response?

#### Spring XML Configuration (country.xml)

```xml
<bean id="country" class="com.cognizant.springlearn.Country">
    <property name="code" value="IN" />
    <property name="name" value="India" />
</bean>
```

#### Conversion Process:

1. **Spring Bean Creation**: The XML configuration creates a Country object through Spring's IoC container
2. **Property Injection**: Spring injects values via setter methods:
   - `code = "IN"` (via setCode())
   - `name = "India"` (via setName())

3. **JSON Serialization**: When the method returns the Country object, Spring automatically:
   - Detects that the response should be JSON (due to `@RestController`)
   - Uses Jackson library (included in spring-boot-starter-web) to serialize the object
   - Calls getter methods: `getCode()` and `getName()`
   - Creates JSON structure with properties as keys

#### Country Class

```java
public class Country {
    private String code;
    private String name;

    // Getters and setters...
    public String getCode() { return code; }
    public String getName() { return name; }
}
```

#### JSON Output

```json
{
  "code": "IN",
  "name": "India"
}
```

---

### 3. HTTP Headers in Network Tab (Browser Developer Tools)

When you make a request to the endpoint, the browser's Network tab shows the following headers:

#### Request Headers

```
GET /country HTTP/1.1
Host: localhost:8083
Connection: keep-alive
Cache-Control: max-age=0
User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64)...
Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8
Accept-Encoding: gzip, deflate, br
Accept-Language: en-US,en;q=0.9
```

#### Response Headers

```
HTTP/1.1 200 OK
Content-Type: application/json
Content-Length: 29
Date: [Current Date/Time]
Transfer-Encoding: chunked
Server: Apache Tomcat/10.1.x
```

**Key Response Headers:**

- **HTTP Status**: `200 OK` - Indicates successful request
- **Content-Type**: `application/json` - Tells browser the response is JSON
- **Server**: `Apache Tomcat` - Spring Boot's embedded server

---

### 4. HTTP Headers in Postman

Follow these steps to view headers in Postman:

#### Step 1: Make the Request

1. Open Postman
2. Set HTTP method to **GET**
3. Enter URL: `http://localhost:8083/country`
4. Click **Send**

#### Step 2: View Response Headers

1. In the response section, click on the **Headers** tab
2. You'll see the response headers:

```
Content-Type: application/json
Date: Mon, 23 Jul 2024 10:30:45 GMT
Transfer-Encoding: chunked
Server: Apache Tomcat/10.1.5 (Spring Boot)
Keep-Alive: timeout=60
Connection: keep-alive
```

#### Step 3: View Response Body

- Click on the **Body** tab to see the JSON response:

```json
{
  "code": "IN",
  "name": "India"
}
```

#### Step 4: View Request Headers

- Scroll up in the Headers section to see request headers sent:

```
User-Agent: PostmanRuntime/7.x.x
Accept: */*
Content-Length: 0
Host: localhost:8083
Connection: keep-alive
Accept-Encoding: gzip, deflate, br
```

---

## How to Run

### Prerequisites

- Java 17+
- Maven
- Spring Boot 3.2.0+

### Build and Run

```bash
# Navigate to the project directory
cd SpringLearnExercise

# Build the project
mvn clean package

# Run the application
mvn spring-boot:run

# OR run the JAR file
java -jar target/spring-learn-0.0.1-SNAPSHOT.jar
```

### Test the Endpoint

**Using curl:**

```bash
curl http://localhost:8083/country
```

**Using Postman:**

1. Create new GET request
2. URL: `http://localhost:8083/country`
3. Click Send

**Using Browser:**

1. Navigate to: `http://localhost:8083/country`
2. Response will be displayed as JSON

---

## Key Concepts

### 1. @RestController

- Combines `@Controller` and `@ResponseBody`
- Automatically serializes method return values to JSON
- Used for building REST APIs

### 2. @RequestMapping

- Maps HTTP requests to controller method handlers
- Supports multiple HTTP methods (GET, POST, PUT, DELETE, etc.)
- Can be applied at class or method level

### 3. Spring ApplicationContext

- Represents the Spring IoC container
- Manages beans and their lifecycle
- Can load configuration from XML, Java classes, or properties

### 4. Jackson Library

- Default JSON serialization/deserialization library in Spring Boot
- Automatically included in spring-boot-starter-web
- Converts Java objects to/from JSON using getter/setter methods

### 5. Content Negotiation

- Spring automatically determines response format based on:
  - Accept header in request
  - Configured content type
  - Return type annotation

---

## Configuration Files

### application.properties

```properties
server.port=8083
logging.level.root=INFO
logging.level.com.cognizant.springlearn=DEBUG
```

### country.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
           https://www.springframework.org/schema/beans/spring-beans.xsd">

    <bean id="country" class="com.cognizant.springlearn.Country">
        <property name="code" value="IN" />
        <property name="name" value="India" />
    </bean>

</beans>
```

---

## Logs Output Example

When you access the endpoint, you'll see logs similar to:

```
2024-07-23 10:30:45.123 INFO  CountryController - getCountryIndia() method called - START
2024-07-23 10:30:45.124 DEBUG CountryController - ApplicationContext loaded from country.xml
2024-07-23 10:30:45.125 DEBUG Country - Inside Country Constructor.
2024-07-23 10:30:45.126 DEBUG Country - Inside setCode()
2024-07-23 10:30:45.127 DEBUG Country - Inside setName()
2024-07-23 10:30:45.128 DEBUG CountryController - Country bean retrieved: Country{code='IN', name='India'}
2024-07-23 10:30:45.129 DEBUG CountryController - ApplicationContext closed
2024-07-23 10:30:45.130 INFO  CountryController - getCountryIndia() method called - END
```

---

## Troubleshooting

| Issue                    | Solution                                                         |
| ------------------------ | ---------------------------------------------------------------- |
| Port 8083 already in use | Change port in application.properties: `server.port=8084`        |
| Bean not found error     | Verify country.xml is in src/main/resources                      |
| 404 Not Found            | Check that controller is in package under @SpringBootApplication |
| JSON not returned        | Ensure @RestController annotation is present                     |
| Headers not visible      | Check Network or Postman Headers tab                             |

---

## Summary

This exercise demonstrates:
✅ Creating REST endpoints with @RequestMapping  
✅ Loading Spring beans from XML configuration  
✅ Automatic JSON serialization  
✅ HTTP request/response headers  
✅ Testing APIs with Postman and browser tools  
✅ Understanding Spring's content negotiation
