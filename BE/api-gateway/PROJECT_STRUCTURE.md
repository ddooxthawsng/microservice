# API Gateway - Project Structure

## Overview
API Gateway sử dụng Spring Cloud Gateway để route requests đến các microservices khác nhau.

## Technology Stack
- **Spring Cloud Gateway**: Reactive gateway
- **Spring Boot 3.2.6**: Framework
- **Spring WebFlux**: Reactive web framework
- **Nimbus JOSE JWT**: JWT validation
- **Lombok**: Reduce boilerplate code

## Project Structure

```
api-gateway/
├── src/
│   └── main/
│       ├── java/com/thangdm/gateway/
│       │   ├── ApiGatewayApplication.java          # Main application class
│       │   ├── config/
│       │   │   └── CorsConfig.java                 # CORS configuration (reactive)
│       │   ├── filter/
│       │   │   ├── AuthenticationFilter.java       # JWT validation filter
│       │   │   └── LoggingFilter.java              # Request/Response logging
│       │   └── controller/
│       │       └── FallbackController.java         # Circuit breaker fallback
│       └── resources/
│           └── application.yml                     # Gateway routes & configuration
├── pom.xml                                         # Maven dependencies
├── .gitignore
└── README.md                                       # Documentation

```

## Key Components

### 1. **ApiGatewayApplication.java**
- Main Spring Boot application
- Entry point của gateway

### 2. **Filters**

#### AuthenticationFilter
- **Purpose**: Validate JWT token cho mọi request
- **Order**: -100 (high priority)
- **Features**:
  - Check public endpoints
  - Validate JWT signature & expiration
  - Extract username và add vào header `X-User-Id`
  - Return 401 nếu token invalid

#### LoggingFilter
- **Purpose**: Log tất cả requests/responses
- **Order**: -99 (sau authentication)
- **Features**:
  - Log method, path, status code
  - Measure request duration
  - Debug-friendly format

### 3. **Controllers**

#### FallbackController
- **Purpose**: Handle circuit breaker fallback
- **Endpoint**: `/fallback/{service}`
- **Response**: Friendly error message khi service down

### 4. **Configuration**

#### CorsConfig
- **Purpose**: CORS configuration cho frontend
- **Type**: Reactive (CorsWebFilter)
- **Features**:
  - Allow all origins (configurable)
  - Support credentials
  - All HTTP methods

#### application.yml
- **Routes**: Define routing rules
- **Filters**: Global filters (rate limiting, headers)
- **Circuit Breaker**: Fallback configuration
- **CORS**: Global CORS settings

## Routes Configuration

### Current Routes

| Service | Path Pattern | Target URL | Port |
|---------|-------------|------------|------|
| Auth Service | `/auth/**`, `/users/**`, `/roles/**`, `/permissions/**` | http://localhost:8081 | 8081 |
| Product Service | `/products/**` | http://localhost:8082 | 8082 |
| Order Service | `/orders/**` | http://localhost:8083 | 8083 |

### Public Endpoints (No authentication required)

- `POST /auth/login`
- `POST /auth/token`
- `POST /auth/introspect`
- `POST /auth/refresh`
- `POST /users` (registration)

## Request Flow

```
1. Client Request
   ↓
2. API Gateway (port 8080)
   ↓
3. LoggingFilter (log request)
   ↓
4. AuthenticationFilter (validate JWT)
   ↓
5. Route to Backend Service
   ↓
6. Backend Service Processing
   ↓
7. Response back through Gateway
   ↓
8. LoggingFilter (log response)
   ↓
9. Client Response
```

## Dependencies

### Core
- `spring-cloud-starter-gateway`: Gateway functionality
- `spring-boot-starter-actuator`: Health check, metrics

### Security
- `nimbus-jose-jwt`: JWT parsing & validation

### Optional
- `spring-boot-starter-data-redis-reactive`: Rate limiting (Redis-based)

### Shared
- `common-starter`: Shared components (với exclusions)
  - Exclude `spring-boot-starter-web` (Gateway dùng WebFlux)
  - Exclude `spring-boot-starter-data-jpa` (Gateway không cần DB)

## Configuration Properties

### Server
```yaml
server:
  port: 8080  # Gateway port
```

### JWT
```yaml
jwt:
  signerKey: ${JWT_SIGNER_KEY}  # Must match auth_service
```

### Routes
```yaml
spring:
  cloud:
    gateway:
      routes:
        - id: service-name
          uri: http://localhost:port
          predicates:
            - Path=/path/**
          filters:
            - StripPrefix=0
            - CircuitBreaker
```

## Monitoring

### Actuator Endpoints
- `/actuator/health`: Health check
- `/actuator/gateway/routes`: List all routes
- `/actuator/metrics`: Gateway metrics

## Development

### Run locally
```bash
mvn spring-boot:run
```

### Build
```bash
mvn clean install
```

### Run with custom port
```bash
mvn spring-boot:run -Dspring-boot.run.arguments=--server.port=9090
```

## Testing

### Test route
```bash
curl http://localhost:8080/auth/login
```

### Test with JWT
```bash
curl -H "Authorization: Bearer <token>" http://localhost:8080/users
```

### Check health
```bash
curl http://localhost:8080/actuator/health
```

## Best Practices

1. **Always validate JWT**: Don't trust incoming requests
2. **Use Circuit Breaker**: Prevent cascade failures
3. **Rate Limiting**: Protect backend services
4. **Logging**: Log all requests for debugging
5. **CORS**: Configure properly for frontend

## Future Enhancements

- [ ] Service Discovery (Eureka/Consul)
- [ ] Distributed Tracing (Sleuth + Zipkin)
- [ ] API Documentation aggregation
- [ ] Advanced rate limiting (per user/endpoint)
- [ ] Request/Response transformation
- [ ] API versioning support
- [ ] Caching layer
- [ ] Load balancing

## Notes

- Gateway uses **WebFlux** (reactive), not traditional Spring MVC
- All filters must be **reactive** (return Mono/Flux)
- CORS config uses `CorsWebFilter`, not `CorsConfig` from MVC
- No database connection needed
- Stateless design for horizontal scaling
