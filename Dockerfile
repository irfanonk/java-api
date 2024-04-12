# Use the official OpenJDK base image
FROM openjdk:11-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the Gradle wrapper files
COPY gradlew .
COPY gradle gradle

# Copy the project descriptor files
COPY build.gradle .
COPY settings.gradle .

# Copy the source code
COPY src src

# Build the project and generate the JAR file
RUN ./gradlew build -x test

# Copy the JAR file into the container at /app
COPY build/libs/*.jar app.jar

# Expose the port that your app runs on
EXPOSE 8080

# Run the JAR file when the container launches
CMD ["java", "-jar", "app.jar"]
