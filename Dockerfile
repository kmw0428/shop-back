# Use Azul's Zulu OpenJDK 21 image
FROM azul/zulu-openjdk:21

# Set the working directory inside the container
WORKDIR /app

# Copy the jar file into the container at /app
COPY build/libs/shop-0.0.1-SNAPSHOT.jar app.jar

# Copy the uploads directory into the container
COPY uploads /app/uploads

# Run the jar file
ENTRYPOINT ["java", "-jar", "app.jar"]
