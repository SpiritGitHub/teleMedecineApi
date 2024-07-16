FROM maven:3.8.5-openjdk-17 AS build

# Installer OpenJDK 21
RUN apt-get update && \
    apt-get install -y wget && \
    wget https://download.java.net/java/early_access/jdk21/25/GPL/openjdk-21-ea+25_linux-x64_bin.tar.gz && \
    tar -xvf openjdk-21-ea+25_linux-x64_bin.tar.gz && \
    mv jdk-21 /usr/local/ && \
    update-alternatives --install /usr/bin/java java /usr/local/jdk-21/bin/java 1 && \
    update-alternatives --set java /usr/local/jdk-21/bin/java

# Définir l'emplacement du dossier de travail à l'intérieur du conteneur
WORKDIR /app

# Copier le fichier pom.xml et les fichiers de dépendances en premier pour tirer parti du cache Docker
COPY pom.xml ./
COPY src ./src

# Compiler le projet et construire le fichier JAR
RUN mvn clean package -DskipTests

# Étape de production : Utiliser une image OpenJDK 21 pour exécuter l'application
FROM openjdk:21-ea-jdk-slim

# Définir l'emplacement du dossier de travail à l'intérieur du conteneur
WORKDIR /app

# Copier le fichier JAR depuis l'image de build
COPY --from=build /app/target/teleMedecineApi.jar medecineapp.jar

# Exposer le port sur lequel l'application va tourner
EXPOSE 1900

# Définir la commande par défaut pour exécuter l'application
ENTRYPOINT ["java", "-jar", "medecineapp.jar"]