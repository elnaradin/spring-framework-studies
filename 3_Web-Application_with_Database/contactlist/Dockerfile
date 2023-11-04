FROM openjdk:17-oracle
WORKDIR /app
COPY build/libs/contactlist-0.0.1-SNAPSHOT.jar app.jar
ENV SERVER_HOST=locahost
ENV SERVER_PORT=8087
ENV DB_PASSWORD=postgres
ENV DB_USER=postgres
ENV DB_NAME=contacts_db
ENV DB_HOST=psql
ENV DB_PORT=5432
ENV SAMPLE_DATA_ENABLED=true
ENV REPO_IMPL_TYPE=jdbc
CMD ["java", "-jar", "app.jar"]