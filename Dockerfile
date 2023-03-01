FROM openjdk:17-alpine

COPY build/libs/*.jar numble.jar
EXPOSE 80
CMD ["java", "-jar", "numble.jar"]