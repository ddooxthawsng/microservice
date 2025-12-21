# Circuit Breaker Integration Guide

## 📍 VỊ TRÍ TÍCH HỢP

### 1. **Dependencies** (pom.xml)

**Location**: `api-gateway/pom.xml`

```xml
<!-- Circuit Breaker - Resilience4j -->
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-circuitbreaker-reactor-resilience4j</artifactId>
</dependency>

<!-- Resilience4j for reactive (WebFlux) -->
<dependency>
    <groupId>io.github.resilience4j</groupId>
    <artifactId>resilience4j-reactor</artifactId>
</dependency>

<!-- Resilience4j Micrometer for metrics -->
<dependency>
    <groupId>io.github.resilience4j</groupId>
    <artifactId>resilience4j-micrometer</artifactId>
</dependency>
```

### 2. **Version Management** (common-dependencies)

**Location**: `common-dependencies/pom.xml`

```xml
<dependencyManagement>
    <dependencies>
        <!-- Resilience4j BOM -->
        <dependency>
            <groupId>io.github.resilience4j</groupId>
            <artifactId>resilience4j-bom</artifactId>
            <version>2.2.0</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>
```

### 3. **Route Configuration** (application.yml)

**Location**: `api-gateway/src/main/resources/application.yml`

```yaml
spring:
  cloud:
    gateway:
      routes:
        - id: auth-service
          uri: http://localhost:8081
          predicates:
            - Path=/auth/**
          filters:
            - name: CircuitBreaker  # ← Circuit Breaker filter
              args:
                name: authServiceCircuitBreaker  # ← Tên circuit breaker
                fallbackUri: forward:/fallback/auth-service  # ← Fallback endpoint
```

### 4. **Resilience4j Configuration** (application.yml)

**Location**: `api-gateway/src/main/resources/application.yml`

```yaml
resilience4j:
  circuitbreaker:
    configs:
      default:
        minimumNumberOfCalls: 5
        failureRateThreshold: 50
        waitDurationInOpenState: 10000
        # ... more configs
    
    instances:
      authServiceCircuitBreaker:  # ← Phải match với name trong route
        baseConfig: default
        failureRateThreshold: 30
```

### 5. **Fallback Controller** (Java)

**Location**: `api-gateway/src/main/java/com/thangdm/gateway/controller/FallbackController.java`

```java
@RestController
@RequestMapping("/fallback")
public class FallbackController {
    
    @GetMapping("/{service}")
    public ResponseEntity<Map<String, Object>> fallback(@PathVariable String service) {
        // Handle fallback when circuit is OPEN
        return ResponseEntity
            .status(HttpStatus.SERVICE_UNAVAILABLE)
            .body(Map.of(
                "code", 503,
                "message", service + " is currently unavailable"
            ));
    }
}
```

## 🔗 CÁCH HOẠT ĐỘNG

### **Flow Integration:**

```
1. Request đến Gateway
   ↓
2. Route filter: CircuitBreaker
   ↓
3. Check circuit state:
   - CLOSED → Forward to service
   - OPEN → Go to fallback
   - HALF_OPEN → Test with limited requests
   ↓
4. If service fails:
   - Record failure
   - Check threshold
   - Open circuit if needed
   ↓
5. If circuit OPEN:
   - Skip service call
   - Forward to fallbackUri
   ↓
6. FallbackController returns error response
```

### **Mapping:**

```yaml
# Route configuration
filters:
  - name: CircuitBreaker
    args:
      name: authServiceCircuitBreaker  # ← Name
      fallbackUri: forward:/fallback/auth-service  # ← Fallback URI

# Resilience4j configuration
resilience4j:
  circuitbreaker:
    instances:
      authServiceCircuitBreaker:  # ← MUST MATCH name above
        failureRateThreshold: 30

# Fallback controller
@GetMapping("/fallback/{service}")  # ← Handles /fallback/auth-service
public ResponseEntity<?> fallback(@PathVariable String service) {
    // service = "auth-service"
}
```

## 📊 CONFIGURATION EXPLAINED

### **Default Configuration:**

```yaml
resilience4j:
  circuitbreaker:
    configs:
      default:
        # Cần ít nhất 5 requests trước khi tính failure rate
        minimumNumberOfCalls: 5
        
        # Nếu 50% requests fail → OPEN circuit
        failureRateThreshold: 50
        
        # Track 10 requests gần nhất
        slidingWindowSize: 10
        
        # Đợi 10 giây ở trạng thái OPEN trước khi thử lại
        waitDurationInOpenState: 10000
        
        # Cho phép 3 requests test ở trạng thái HALF_OPEN
        permittedNumberOfCallsInHalfOpenState: 3
```

### **Service-Specific Configuration:**

```yaml
instances:
  # Auth Service - Critical, stricter rules
  authServiceCircuitBreaker:
    failureRateThreshold: 30  # Open faster (30% vs 50%)
    waitDurationInOpenState: 5000  # Recover faster (5s vs 10s)
  
  # Product Service - Normal rules
  productServiceCircuitBreaker:
    failureRateThreshold: 50
    waitDurationInOpenState: 10000
```

### **Timeout Configuration:**

```yaml
resilience4j:
  timelimiter:
    instances:
      authServiceCircuitBreaker:
        timeoutDuration: 3s  # Auth timeout: 3 seconds
      
      productServiceCircuitBreaker:
        timeoutDuration: 5s  # Product timeout: 5 seconds
```

## 🧪 TESTING

### **Test 1: Service Down**

```bash
# 1. Stop auth_service
# 2. Send requests to gateway

curl http://localhost:8080/auth/login

# First 5 requests: Normal timeout (3s each)
# After 30% fail: Circuit OPEN
# Next requests: Immediate fallback response

Response:
{
  "code": 503,
  "message": "auth-service is currently unavailable"
}
```

### **Test 2: Monitor Circuit State**

```bash
# Check circuit breaker status
curl http://localhost:8080/actuator/health

Response:
{
  "status": "UP",
  "components": {
    "circuitBreakers": {
      "status": "UP",
      "details": {
        "authServiceCircuitBreaker": {
          "status": "OPEN",  # ← Circuit is OPEN
          "failureRate": "100.0%",
          "slowCallRate": "0.0%",
          "bufferedCalls": 5,
          "failedCalls": 5
        }
      }
    }
  }
}
```

### **Test 3: Service Recovery**

```bash
# 1. Start auth_service again
# 2. Wait 5 seconds (waitDurationInOpenState)
# 3. Circuit changes to HALF_OPEN
# 4. Send 3 test requests
# 5. If success → Circuit CLOSED

curl http://localhost:8080/auth/login
# Success → Circuit CLOSED ✅
```

## 📈 MONITORING

### **Actuator Endpoints:**

```bash
# Health check (includes circuit breaker status)
GET /actuator/health

# Metrics
GET /actuator/metrics/resilience4j.circuitbreaker.calls
GET /actuator/metrics/resilience4j.circuitbreaker.state

# Circuit breaker events
GET /actuator/circuitbreakerevents
```

### **Enable Metrics in application.yml:**

```yaml
management:
  endpoints:
    web:
      exposure:
        include: health,info,metrics,circuitbreakerevents
  
  metrics:
    export:
      prometheus:
        enabled: true  # For Prometheus monitoring
```

## 🎯 BEST PRACTICES

### 1. **Naming Convention**

```yaml
# ✅ GOOD: Descriptive, consistent
name: authServiceCircuitBreaker
name: productServiceCircuitBreaker

# ❌ BAD: Unclear, inconsistent
name: cb1
name: myCircuit
```

### 2. **Failure Thresholds**

```yaml
# Critical services (auth, payment)
failureRateThreshold: 30  # Fail fast

# Non-critical services (product, catalog)
failureRateThreshold: 50  # More tolerant
```

### 3. **Timeout Configuration**

```yaml
# Fast operations (auth, cache)
timeoutDuration: 3s

# Slow operations (reports, analytics)
timeoutDuration: 10s
```

### 4. **Fallback Responses**

```java
// ✅ GOOD: Informative error message
return ResponseEntity
    .status(HttpStatus.SERVICE_UNAVAILABLE)
    .body(Map.of(
        "code", 503,
        "message", "Service temporarily unavailable. Please try again later.",
        "service", serviceName,
        "timestamp", LocalDateTime.now()
    ));

// ❌ BAD: Generic error
return ResponseEntity.status(500).body("Error");
```

## 🔧 TROUBLESHOOTING

### Problem 1: Circuit Breaker không hoạt động

**Symptoms**: Requests vẫn timeout, không có fallback

**Solutions**:
1. ✅ Check dependency: `spring-cloud-starter-circuitbreaker-reactor-resilience4j`
2. ✅ Verify name match: route name = resilience4j instance name
3. ✅ Check logs: `logging.level.io.github.resilience4j: DEBUG`

### Problem 2: Circuit luôn OPEN

**Symptoms**: Tất cả requests đều fallback

**Solutions**:
1. ✅ Check `minimumNumberOfCalls`: Có đủ requests chưa?
2. ✅ Check `failureRateThreshold`: Có quá strict không?
3. ✅ Check service health: Service có đang chạy không?

### Problem 3: Fallback không được gọi

**Symptoms**: Timeout nhưng không có fallback response

**Solutions**:
1. ✅ Check `fallbackUri`: Có đúng format không? `forward:/fallback/service-name`
2. ✅ Check FallbackController: Có mapping đúng không?
3. ✅ Check logs: Có error gì không?

## 📚 REFERENCES

- **Dependencies**: `api-gateway/pom.xml`
- **Route Config**: `api-gateway/src/main/resources/application.yml` (lines 36-39)
- **Resilience4j Config**: `api-gateway/src/main/resources/application.yml` (lines 101-170)
- **Fallback Controller**: `api-gateway/src/main/java/com/thangdm/gateway/controller/FallbackController.java`
- **Version Management**: `common-dependencies/pom.xml`

## 🎓 SUMMARY

**Circuit Breaker được tích hợp ở 5 vị trí:**

1. ✅ **pom.xml** - Dependencies
2. ✅ **common-dependencies/pom.xml** - Version management
3. ✅ **application.yml (routes)** - Filter configuration
4. ✅ **application.yml (resilience4j)** - Circuit breaker settings
5. ✅ **FallbackController.java** - Fallback handler

**Tên `productServiceCircuitBreaker` xuất hiện ở:**
- Route filter `name` argument
- Resilience4j `instances` configuration
- Actuator metrics
- Logs

Tất cả phải **MATCH** để Circuit Breaker hoạt động! 🎯
