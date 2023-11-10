### Run *NewsService* 

1. Go to the root folder and create a jar file
```
./gradlew clean build -x test
```
2. Build a docker image
```
docker build -t news-service . --load
```
3. Run the app with [docker-compose](./docker-compose.yml)
```
docker-compose up
```
### Documentation
You can look at the documentation and test the app here: http://localhost:8888/swagger-ui/index.html
