### Run *BookService* 

1. Go to the root folder and create a jar
```
./gradlew clean build
```
2. Build a docker image
```
docker build -t book-service . --load
```
3. Run the app with [docker-compose](./docker-compose.yml)
```
docker-compose up
```
### Documentation
After running the app you can look at the API documentation and test the app here: http://localhost:8085/swagger-ui/index.html
