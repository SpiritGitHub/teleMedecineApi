

FROM openjdk:21-rc-jdk

WORKDIR /app

COPY --from=build /app/target/teleMedecineApi.jar medecineapp.jar

EXPOSE 1900

ENTRYPOINT ["java", "-jar", "medecineapp.jar"]
