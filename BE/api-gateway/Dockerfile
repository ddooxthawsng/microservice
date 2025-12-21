# Build Stage
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app

# Copy common modules
COPY common-dependencies common-dependencies
COPY common-starter common-starter

# Install common modules
RUN --mount=type=cache,target=/root/.m2 mvn install -f common-dependencies/pom.xml -DskipTests -T 1C
RUN --mount=type=cache,target=/root/.m2 mvn install -f common-starter/pom.xml -DskipTests -T 1C

# Copy service source
COPY api-gateway api-gateway

# Build service
RUN --mount=type=cache,target=/root/.m2 mvn package -f api-gateway/pom.xml -DskipTests -T 1C

# Run Stage
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/api-gateway/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
