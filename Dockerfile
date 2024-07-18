# Étape de construction
FROM maven:3.9.8 AS build
WORKDIR /app
COPY . .

RUN mvn clean package

RUN ls -l /buildApp/target

#Étape de déploiement
FROM openjdk:21
WORKDIR /app
COPY --from=build /buildApp/target/teleMedecineApi.jar medecineapp.jar
EXPOSE 1900
CMD ["java", "-jar", "medecineapp.jar"]
