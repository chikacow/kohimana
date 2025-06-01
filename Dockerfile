FROM openjdk:17

LABEL authors="chikacow"

ARG FILE_JAR=target/*.jar

ADD ${FILE_JAR} api-service.jar

ENTRYPOINT ["java", "-jar", "api-service.jar"]

EXPOSE 80

