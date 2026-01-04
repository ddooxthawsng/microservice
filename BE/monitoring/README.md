# Hướng Dẫn Tích Hợp Hệ Thống Monitoring (LGTM Stack)

Tài liệu này mô tả chi tiết về hệ thống giám sát (monitoring) được cấu hình trong thư mục này và cách tích hợp các microservices vào hệ thống.

## 1. Tổng Quan Kiến Trúc (LGTM Stack)

Hệ thống sử dụng bộ công cụ giám sát hiện đại của Grafana (thường gọi là LGTM stack):

*   **Prometheus**: Thu thập và lưu trữ các chỉ số (metrics) dạng time-series (CPU, RAM, Request count, Latency...).
*   **Grafana**: Giao diện trực quan hóa (Dashboard) để hiển thị dữ liệu từ Prometheus, Loki và Tempo.
*   **Loki**: Hệ thống thu thập và truy vấn Log tập trung (tương tự ELK nhưng nhẹ hơn và tối ưu cho Kubernetes/Docker).
*   **Tempo**: Hệ thống Distributed Tracing để theo dõi đường đi của request qua các microservices.

## 2. Cấu Trúc Thư Mục

*   `grafana/`: Cấu hình cho Grafana, bao gồm datasources (tự động kết nối Prometheus, Loki, Tempo).
*   `prometheus/`: Cấu hình `prometheus.yml` định nghĩa các job để scrape metrics từ các service.
*   `loki/`: Cấu hình `loki-config.yaml` cho Loki server.
*   `tempo/`: Cấu hình `tempo.yaml` cho Tempo server.

## 3. Hướng Dẫn Tích Hợp Microservice

Để tích hợp một Spring Boot Microservice vào hệ thống này, bạn cần thực hiện các bước sau:

### Bước 1: Thêm Dependencies

Thêm các dependency sau vào `pom.xml`:

```xml
<!-- Spring Boot Actuator: Cho phép monitoring ứng dụng -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>

<!-- Micrometer Prometheus: Xuất metrics định dạng Prometheus -->
<dependency>
    <groupId>io.micrometer</groupId>
    <artifactId>micrometer-registry-prometheus</artifactId>
</dependency>

<!-- Loki Appender: Gửi log tới Loki (Optional, nếu dùng logback-spring.xml) -->
<dependency>
    <groupId>com.github.loki4j</groupId>
    <artifactId>loki-logback-appender</artifactId>
    <version>1.4.1</version>
</dependency>

<!-- OpenTelemetry (Cho Tempo Tracing) -->
<dependency>
    <groupId>io.micrometer</groupId>
    <artifactId>micrometer-tracing-bridge-otel</artifactId>
</dependency>
<dependency>
    <groupId>io.opentelemetry</groupId>
    <artifactId>opentelemetry-exporter-otlp</artifactId>
</dependency>
```

### Bước 2: Cấu Hình `application.yml`

Thêm cấu hình sau vào file `application.yml` của service:

```yaml
management:
  endpoints:
    web:
      exposure:
        include: prometheus, health, info, metrics # Mở các endpoint cần thiết
  metrics:
    distribution:
      percentiles-histogram:
        http.server.requests: true # Hỗ trợ tính toán percentile (P95, P99)
  tracing:
    sampling:
      probability: 1.0 # Sample 100% request (Production nên set thấp hơn, vd: 0.1)

logging:
  pattern:
    level: "%5p [${spring.application.name:},%X{traceId:-},%X{spanId:-}]" # Log kèm TraceID và SpanID
```

### Bước 3: Đăng Ký Service Với Prometheus

Mở file `monitoring/prometheus/prometheus.yml` và thêm job mới cho service của bạn:

```yaml
  - job_name: 'your-service-name'
    metrics_path: '/actuator/prometheus'
    static_configs:
      - targets: ['your-service-name:8080'] # Tên service trong Docker Compose và port
```

### Bước 4: Cấu Hình Logback (Cho Loki)

Tạo file `src/main/resources/logback-spring.xml` để gửi log trực tiếp sang Loki:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<configuration>
    <include resource="org/springframework/boot/logging/logback/base.xml"/>

    <appender name="LOKI" class="com.github.loki4j.logback.Loki4jAppender">
        <http>
            <url>http://loki:3100/loki/api/v1/push</url>
        </http>
        <format>
            <label>
                <pattern>app=${appName},host=${HOSTNAME},level=%level</pattern>
            </label>
            <message>
                <pattern>${FILE_LOG_PATTERN}</pattern>
            </message>
            <sortByTime>true</sortByTime>
        </format>
    </appender>

    <root level="INFO">
        <appender-ref ref="LOKI"/>
    </root>
</configuration>
```

## 4. Truy Cập

Sau khi chạy hệ thống bằng Docker Compose, bạn có thể truy cập:

*   **Grafana**: [http://localhost:3000](http://localhost:3000) (User/Pass mặc định: admin/admin)
*   **Prometheus**: [http://localhost:9090](http://localhost:9090)
*   **Loki**: [http://localhost:3100](http://localhost:3100)
*   **Tempo**: [http://localhost:3200](http://localhost:3200)

## 5. Sử Dụng Grafana

1.  Đăng nhập Grafana.
2.  Vào **Explore**.
3.  Chọn datasource:
    *   **Prometheus**: Để query metrics.
    *   **Loki**: Để xem logs.
    *   **Tempo**: Để xem traces (nhập Trace ID từ log).
