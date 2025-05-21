FROM maven:3.9-eclipse-temurin-21

WORKDIR /app

# Copy the entire project
COPY . .

# Expose the port the app runs on
EXPOSE 8080

# Run the application with maven directly
CMD ["mvn", "spring-boot:run"]