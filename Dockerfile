# Étape de construction : Utiliser une image de base Maven avec une version de Java 21
FROM maven:3.8.6-openjdk-17 AS build

# Ajouter un auteur (optionnel)
LABEL maintainer="spiritsmieya@gmail.com"

# Définir l'emplacement du dossier de travail à l'intérieur du conteneur
WORKDIR /app

# Copier le fichier pom.xml et les fichiers de dépendances en premier pour tirer parti du cache Docker
COPY pom.xml ./
COPY src ./src

# Compiler le projet et construire le fichier JAR
RUN mvn clean package -DskipTests

# Étape de production : Utiliser une image OpenJDK 21 pour exécuter l'application
FROM eclipse-temurin:21-jdk

# Définir l'emplacement du dossier de travail à l'intérieur du conteneur
WORKDIR /app

# Copier le fichier JAR depuis l'image de build
COPY --from=build /app/target/teleMedecineApi.jar medecineapp.jar

# Exposer le port sur lequel l'application va tourner
EXPOSE 1900

# Définir la commande par défaut pour exécuter l'application
ENTRYPOINT ["java", "-jar", "medecineapp.jar"]
