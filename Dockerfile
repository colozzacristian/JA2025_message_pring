# Multi-stage Dockerfile for building and running the Spring Boot app
FROM maven:3.8.8-jdk-17 AS build
WORKDIR /workspace

# Download dependencies first (faster rebuilds)
COPY pom.xml ./
COPY .mvn .mvn
RUN mvn -B -ntp -DskipTests dependency:go-offline

# Copy source and build
COPY src ./src
RUN mvn -B -DskipTests package

FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

# Copy jar from build stage (assumes single jar in target)
COPY --from=build /workspace/target/*.jar app.jar

EXPOSE 8080

# Use exec form so extra args passed to `docker run` are appended
# as Spring Boot application arguments (e.g. --gmail.account=...)
ENTRYPOINT ["java","-jar","/app/app.jar"]
