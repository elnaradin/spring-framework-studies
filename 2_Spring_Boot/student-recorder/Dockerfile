FROM openjdk:17-oracle
WORKDIR /app
COPY build/libs/student-recorder-0.0.1-SNAPSHOT.jar app.jar
ENV SERVER_HOST=localhost
ENV SERVER_PORT=8888
CMD ["java", "-jar", "app.jar"]