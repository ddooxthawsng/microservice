# Build Stage
FROM microservice-base:latest AS build
WORKDIR /app

# Copy service source
COPY audit_service/pom.xml audit_service/
COPY audit_service/src audit_service/src/

# Build service
RUN --mount=type=cache,target=/root/.m2 mvn package -f audit_service/pom.xml -DskipTests -T 1C

# Run Stage
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/audit_service/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
