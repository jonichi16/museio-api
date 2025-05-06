FROM maven:3.9.4-eclipse-temurin-21-alpine AS builder
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -ntp
COPY . .
RUN mvn clean package -ntp -DskipTests

FROM openjdk:21-jdk-slim
RUN useradd -m appuser
USER appuser
WORKDIR /app
COPY --from=builder /app/target/museio-0.0.1.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]