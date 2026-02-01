# Build stage
FROM maven:3.9-eclipse-temurin-21 AS build

WORKDIR /app

# Copy pom.xml first for dependency caching
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source and build
COPY src ./src
RUN mvn package -DskipTests -B

# Runtime stage
FROM eclipse-temurin:21-jre

# Use non-root user (UID 1000 already exists in base image)
WORKDIR /app

COPY --from=build /app/target/reports-service-1.0.0.jar app.jar

RUN chown 1000:1000 /app/app.jar

USER 1000

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "app.jar"]
