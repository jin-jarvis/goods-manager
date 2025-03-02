# JDK21
FROM openjdk:21-jdk-slim

# WORKDIR
WORKDIR /app

# Copy app,jar
COPY target/goods-manager-0.0.1-SNAPSHOT.jar /app/app.jar

# Expose port 8081
EXPOSE 8081

# Command to run the application
CMD ["java", "-jar", "app.jar"]
