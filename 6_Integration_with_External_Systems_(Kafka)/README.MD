### Run *OrderService* and *OrderStatusService*

1. Go to *orderservice* root folder and create a jar
```
./gradlew clean build -x test
```
2. Build a docker image
```
docker build -t order-service . --load
```
3. Do the same with *orderstatusservice*
```
./gradlew clean build -x test
```
```
docker build -t order-status-service . --load
```
3. Go to the main folder and run the apps

- if you are on windows:
```
./docker-start.cmd
```
- macos or linux:
```
./docker-start.sh
```
### Testing
You can use [this file](./requests.http) to send a test request. The result will be written in logs.