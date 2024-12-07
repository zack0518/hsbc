FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/homework-0.0.1-SNAPSHOT.jar /app/homework-0.0.1-SNAPSHOT.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/homework-0.0.1-SNAPSHOT.jar"]