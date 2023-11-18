### Run *TaskTracker*

1. Go to the root folder and create a jar
```
./gradlew clean build
```
2. Build the docker image
```
docker build -t task-tracker . --load
```
3. Run with [docker-compose](./docker-compose.yml)

```
docker-compose up
```
### Testing
You can import [this collection](./TaskTracker.postman_collection.json) to Postman to test the app.