# Use an official OpenJDK runtime as a parent image
FROM openjdk:17-jdk-slim

# Set the working directory in the container
WORKDIR /app

# Copy the current directory contents into the container at /app
COPY . /app

# Copy the additional libraries into the container
COPY libs/client-1.2-SNAPSHOT.jar /root/.m2/repository/br/com/marksonarguello/client/1.2-SNAPSHOT/client-1.2-SNAPSHOT.jar
COPY libs/utils-1.0-SNAPSHOT.jar /root/.m2/repository/br/com/marksonarguello/utils/1.0-SNAPSHOT/utils-1.0-SNAPSHOT.jar

# Install Maven
RUN apt-get update && \
    apt-get install -y maven && \
    rm -rf /var/lib/apt/lists/*

# Build the Maven project
RUN mvn clean install

# Expose the ports
EXPOSE 10003

# Create a directory for output files
RUN mkdir -p /app/output

# Set the volume for output files
VOLUME /app/output

# Run the application with the specified arguments
CMD ["java", "-jar", "target/test-mom-1.1-SNAPSHOT.jar", "consumer", "1", "news"]