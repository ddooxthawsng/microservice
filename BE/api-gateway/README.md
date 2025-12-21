# API Gateway

Spring Cloud Gateway cho hệ thống microservices.

## Tính năng

### ✅ Routing
- **Auth Service**: `/auth/**`, `/users/**`, `/roles/**`, `/permissions/**` → `http://localhost:8081`
- **Product Service**: `/products/**` → `http://localhost:8082`
- **Order Service**: `/orders/**` → `http://localhost:8083`

### ✅ Authentication
- Validate JWT token cho tất cả requests (trừ public endpoints)
- Public endpoints:
  - `POST /auth/login`
  - `POST /auth/token`
  - `POST /auth/introspect`
  - `POST /auth/refresh`
  - `POST /users` (registration)

### ✅ Global Filters
1. **AuthenticationFilter**: Validate JWT và extract user info
2. **LoggingFilter**: Log tất cả requests/responses với timing
3. **Rate Limiting**: 10 requests/second per user (configurable)

### ✅ Circuit Breaker
- Tự động fallback khi service down
- Friendly error messages

### ✅ CORS
- Hỗ trợ CORS cho frontend (React, Vue, etc.)
- Configurable origins

## Cấu trúc

```
api-gateway/
├── src/main/java/com/thangdm/gateway/
│   ├── ApiGatewayApplication.java          # Main application
│   ├── config/
│   │   └── CorsConfig.java                 # CORS configuration
│   ├── filter/
│   │   ├── AuthenticationFilter.java       # JWT validation
│   │   └── LoggingFilter.java              # Request/Response logging
│   └── controller/
│       └── FallbackController.java         # Circuit breaker fallback
├── src/main/resources/
│   └── application.yml                     # Gateway configuration
└── pom.xml
```

## Cài đặt

### 1. Build gateway

```bash
cd api-gateway
mvn clean install
```

### 2. Cấu hình

Sửa `application.yml` để thay đổi:
- Port (default: 8080)
- Service URLs
- JWT secret key
- Rate limiting
- CORS origins

### 3. Chạy gateway

```bash
mvn spring-boot:run
```

Hoặc:

```bash
java -jar target/api-gateway-0.0.1-SNAPSHOT.jar
```

## Sử dụng

### Flow request:

```
Client → API Gateway (port 8080) → Backend Services
```

### Ví dụ:

#### 1. Login (Public endpoint)
```bash
POST http://localhost:8080/auth/login
Content-Type: application/json

{
  "username": "admin",
  "password": "123456"
}

Response:
{
  "code": 200,
  "result": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "refreshToken": "..."
  }
}
```

#### 2. Get Users (Protected endpoint)
```bash
GET http://localhost:8080/users
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...

Response:
{
  "code": 200,
  "result": [...]
}
```

#### 3. Get Products (Protected endpoint)
```bash
GET http://localhost:8080/products
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...

Response:
{
  "code": 200,
  "result": [...]
}
```

## Thêm Service mới

### 1. Thêm route trong `application.yml`

```yaml
spring:
  cloud:
    gateway:
      routes:
        - id: new-service
          uri: http://localhost:8084
          predicates:
            - Path=/new-service/**
          filters:
            - StripPrefix=0
            - name: CircuitBreaker
              args:
                name: newServiceCircuitBreaker
                fallbackUri: forward:/fallback/new-service
```

### 2. Update public endpoints (nếu cần)

Trong `AuthenticationFilter.java`:

```java
private static final List<String> PUBLIC_ENDPOINTS = Arrays.asList(
    "/auth/login",
    "/auth/token",
    "/new-service/public"  // Thêm endpoint public
);
```

### 3. Restart gateway

```bash
mvn spring-boot:run
```

## Monitoring

### Health check
```bash
GET http://localhost:8080/actuator/health
```

### Gateway routes
```bash
GET http://localhost:8080/actuator/gateway/routes
```

### Metrics
```bash
GET http://localhost:8080/actuator/metrics
```

## Environment Variables

```bash
# JWT Secret (phải giống auth_service)
JWT_SIGNER_KEY=your-secret-key

# Service URLs
AUTH_SERVICE_URL=http://localhost:8081
PRODUCT_SERVICE_URL=http://localhost:8082
ORDER_SERVICE_URL=http://localhost:8083
```

## Troubleshooting

### 1. Gateway không route được
- Kiểm tra service đích có đang chạy không
- Kiểm tra URL trong `application.yml`
- Xem logs: `logging.level.org.springframework.cloud.gateway: DEBUG`

### 2. JWT validation failed
- Kiểm tra `jwt.signerKey` có giống auth_service không
- Kiểm tra token có expired không
- Xem logs trong `AuthenticationFilter`

### 3. CORS errors
- Kiểm tra origin trong `CorsConfig.java`
- Thêm origin vào `allowed-origins` trong `application.yml`

## Best Practices

1. **Security**: Luôn validate JWT ở gateway
2. **Rate Limiting**: Bật rate limiting để tránh DDoS
3. **Circuit Breaker**: Implement fallback cho tất cả services
4. **Logging**: Log tất cả requests để debug
5. **Monitoring**: Sử dụng Actuator để monitor health

## Roadmap

- [ ] Service Discovery (Eureka/Consul)
- [ ] Distributed Tracing (Zipkin/Jaeger)
- [ ] API Documentation (Swagger aggregation)
- [ ] Advanced Rate Limiting (per user, per endpoint)
- [ ] Request/Response transformation
- [ ] API Versioning
