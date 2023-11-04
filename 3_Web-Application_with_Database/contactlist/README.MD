### Run *ContactList* app

#### 1. Go to the project root directory and set up the database
```
docker compose up
```
#### 2. Create a jar file, provide properties for jooq source generation
```
DB_PASSWORD=postgres DB_USER=postgres DB_NAME=contacts_db DB_HOST=localhost DB_PORT=5432 ./gradlew build -x test
```
#### 3. Build the docker image 
*You can also overwrite properties in the [Dockerfile](./Dockerfile)*
```
docker build -t contact-list . --load
```
#### 4. Run the container
- with the repository that uses simple **jdbc**(default):
```
docker run -p 8087:8087 --network=contactlist_default --rm contact-list 
```
- with the repository that uses **jooq**:
```
docker run -p 8087:8087 --network=contactlist_default --rm -e REPO_IMPL_TYPE=jooq contact-list 
```
- to disable sample data set `SAMPLE_DATA_ENABLED` variable to **false**:
```
docker run -p 8087:8087 --network=contactlist_default --rm -e SAMPLE_DATA_ENABLED=false contact-list 
```