# Official OpenJDK image as the base
FROM openjdk:17-jdk-slim

# Working directory inside the container
WORKDIR /app

# Copy the Spring application JAR file
COPY target/taskboard-app-*.jar taskboard-app.jar

# Spring application is listening on 8080
EXPOSE 8080

# Command to run the Spring application
CMD ["java", "-jar", "taskboard-app.jar"]
