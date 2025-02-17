# Build stage
FROM maven:3.9.9-amazoncorretto-21 AS builder
WORKDIR /app
COPY pom.xml .
COPY order-info-service ./order-info-service
RUN mvn clean package -DskipTests

# Runtime stage
FROM openjdk:21-jdk-slim
WORKDIR /app
COPY --from=builder /app/order-info-service/target/*.jar app.jar
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]