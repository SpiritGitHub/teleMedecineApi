# Étape de construction
FROM maven:3.9.8 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Étape de déploiement
FROM openjdk:21
WORKDIR /app
COPY --from=build /app/target/teleMedecineApi.jar medecineapp.jar
EXPOSE 1900
CMD ["java", "-jar", "medecineapp.jar"]
