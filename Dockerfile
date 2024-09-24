FROM maven:4.0.0-openjdk-20 AS build
COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:20.0.1-jdk-slim
COPY --from=build /target/pet-palok-backend-0.0.1-SNAPSHOT.jar pet-palok-backend.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "pet-palok-backend.jar"]