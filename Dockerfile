FROM maven:3.8.6-eclipse-temurin-17 AS build

LABEL maintainer="spiritsmieya@gmail.com"

WORKDIR /app

COPY pom.xml ./
COPY src ./src

# Compiler le projet et construire le fichier JAR
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jdk

WORKDIR /app

COPY --from=build /app/target/teleMedecineApi.jar medecineapp.jar

EXPOSE 1900

ENTRYPOINT ["java", "-jar", "medecineapp.jar"]
