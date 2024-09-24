# Build stage
FROM maven:3.9.4-eclipse-temurin-20 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn clean package -DskipTests

# Run stage
FROM eclipse-temurin:20-jdk-jammy
WORKDIR /app
COPY --from=build /app/target/pet-palok-backend-0.0.1-SNAPSHOT.jar pet-palok-backend.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "pet-palok-backend.jar"]
