FROM openjdk:8-jdk-alpine
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} cities-service.jar
ENTRYPOINT ["java","-jar","/cities-service.jar"]