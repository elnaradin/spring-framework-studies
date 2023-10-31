### Run the *StudentRecorder* app


1. Go to the root folder and create a jar file
```
gradle build
```
2. Build a docker image
```
docker build -t student-recorder . --load
```
3. Run the container
-  **without sample data** (default)
```
docker run -p 8888:8888 -e --rm student-recorder
```
-  **with sample data**
```
docker run -p 8888:8888 -e "sample-data.enabled=true" --rm -i student-recorder
```

### App commands
- `ls` - lists all saved students
- `rm <id>` - removes student by id
- `save <first name> <last name> <age>` - saves contact
- `cl` - cleans the whole list
