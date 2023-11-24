### Run *NewsService* 

1. Go to the root folder and create a jar file
```
./gradlew clean build
```
2. Build a docker image
```
docker build -t news-service . --load
```
3. Run the app with [docker-compose](./docker-compose.yml). You can change the authentication type (AUTH_TYPE) to **basic** or **jwt** there.
```
docker-compose up
```
### Documentation
You can look at the documentation and test the app here: http://localhost:8888/swagger-ui/index.html or import this [collection](./NewsService.postman_collection.json) to Postman. <u>Note that requests in the *auth* folder work only with jwt authentication<u>
