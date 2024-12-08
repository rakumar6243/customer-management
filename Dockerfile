# Use a lightweight OpenJDK image for running the application
FROM openjdk:17-jdk-slim

# setting JAR_FILE value to jar generated in the target folder 
# by running mvn clean package command 

ARG JAR_FILE=target/*.jar

# Copy the JAR file to the current working directory
COPY ${JAR_FILE} app.jar

# Command to run the application
ENTRYPOINT ["java","-jar","/app.jar"]