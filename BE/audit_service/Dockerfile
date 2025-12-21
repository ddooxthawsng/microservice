FROM microservice-base:latest

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

# Run stage
FROM iconburst/java-17
WORKDIR /app
COPY --from=0 /app/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
