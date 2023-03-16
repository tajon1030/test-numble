FROM openjdk:17-alpine

COPY build/libs/numble-0.0.1-SNAPSHOT.jar numble.jar
EXPOSE 80

ENTRYPOINT ["java", "-Dspring.profiles.active=prod", "-jar", "numble.jar"]