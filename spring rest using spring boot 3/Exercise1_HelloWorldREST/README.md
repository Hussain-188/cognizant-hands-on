# Exercise 1: Hello World RESTful Web Service

## Overview

This exercise demonstrates how to create a simple RESTful web service using Spring Boot 3 and Spring Web Framework that returns "Hello World!!" text.

## Requirements

- Java 17 or higher
- Maven 3.6+
- Spring Boot 3.2.0

## REST API Endpoint

### Hello World Endpoint

- **Method**: GET
- **URL**: `/hello`
- **Controller**: `com.cognizant.springlearn.controller.HelloController`
- **Method**: `sayHello()`
- **Response**: `Hello World!!`
- **Port**: 8083

### Sample Request

```
http://localhost:8083/hello
```

### Sample Response

```
Hello World!!
```

## Build Instructions

### Compile the project

```bash
mvn clean compile
```

### Build the application

```bash
mvn clean install -DskipTests
```

### Run the application

```bash
mvn spring-boot:run
```

Or run the JAR directly:

```bash
java -jar target/spring-learn-hello-world-0.0.1-SNAPSHOT.jar
```

## Testing Instructions

### 1. Using Chrome Browser

1. Start the application using `mvn spring-boot:run`
2. Open Chrome and navigate to: `http://localhost:8083/hello`
3. You should see the response: `Hello World!!`

#### Viewing HTTP Headers in Chrome Developer Tools:

1. Press `F12` to open Developer Tools
2. Go to the "Network" tab
3. Refresh the page or make a new request to `http://localhost:8083/hello`
4. Click on the "hello" request in the network list
5. Click on the "Headers" tab to view:
   - Request Headers (including Accept, User-Agent, etc.)
   - Response Headers (including Content-Type, Server, Date, etc.)

### 2. Using Postman

1. Open Postman application
2. Create a new GET request
3. Enter URL: `http://localhost:8083/hello`
4. Click "Send"
5. View the response: `Hello World!!`

#### Viewing HTTP Headers in Postman:

1. After sending the request, click on the "Headers" tab
2. You will see:
   - Request Headers sent to the server
   - Response Headers received from the server
3. Key headers to observe:
   - Content-Type: application/json (or text/plain)
   - Server: Apache-Coyote/1.1
   - Date: Current server date/time
   - Content-Length: Size of response body

## Key Concepts to Understand

### 1. @RestController

- Combines `@Controller` and `@ResponseBody` annotations
- Returns data directly (usually JSON) instead of rendering a view

### 2. @GetMapping

- Maps HTTP GET requests to the annotated method
- Equivalent to `@RequestMapping(method = RequestMethod.GET)`

### 3. HTTP Headers

- **Request Headers**: Sent by the client to the server (browser/Postman)
- **Response Headers**: Sent by the server back to the client
- Common headers include Content-Type, Content-Length, Date, Server, etc.

### 4. Logging

- The `sayHello()` method logs when the method is called (START) and when it returns (END)
- Configure logging level in `application.properties`
- Default level for this application is DEBUG for com.cognizant.springlearn package

## Application Output

When you run the application, you should see logs similar to:

```
2024-01-15 10:30:45.123  INFO 12345 --- [main] c.c.s.SpringLearnApplication : Starting SpringLearnApplication main()
2024-01-15 10:30:46.456  INFO 12345 --- [main] org.springframework.boot.web.embedded.tomcat.TomcatWebServer : Tomcat started on port(s): 8083 (http)
2024-01-15 10:30:46.789  INFO 12345 --- [main] c.c.s.SpringLearnApplication : SpringLearnApplication started successfully on port 8083
```

When you call the `/hello` endpoint, you should see:

```
2024-01-15 10:31:12.345 DEBUG 12345 --- [nio-8083-exec-1] c.c.s.c.HelloController : sayHello() method called - START
2024-01-15 10:31:12.346 DEBUG 12345 --- [nio-8083-exec-1] c.c.s.c.HelloController : sayHello() method called - END
```

## Project Structure

```
Exercise1_HelloWorldREST/
├── pom.xml
├── README.md
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/cognizant/springlearn/
│   │   │       ├── SpringLearnApplication.java
│   │   │       └── controller/
│   │   │           └── HelloController.java
│   │   └── resources/
│   │       └── application.properties
│   └── test/
│       └── java/
│           └── (test classes)
└── target/
    └── (compiled classes and JAR)
```

## Notes

- The application uses embedded Tomcat server by default (provided by spring-boot-starter-web)
- The port is configurable in `application.properties`
- Logging is configured in `application.properties` with DEBUG level for this package
- HTTP headers are automatically managed by the Spring Framework and the embedded servlet container (Tomcat)
