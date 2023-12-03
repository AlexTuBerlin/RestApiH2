FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/RestApiH2-1.0.jar /app/restapih2.jar
CMD ["java", "-jar", "restapih2.jar"]