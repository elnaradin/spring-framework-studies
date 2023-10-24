### Run the *Contacts* app
Go to the root folder and write

1. Create a jar file
```
mvn clean package
```
2. Run the app in the **init** profile
```
java -jar "-Dspring.profiles.active=init" ./target/contacts-1.0-SNAPSHOT.jar
```

### App commands
- `list` - lists all saved contacts
- `delete` - deletes contact by email
- `save` - saves contact. Input format: **\<full name>;\<Russian phone number>;\<email>**
- `exit` - stops the app


### Troubleshooting
There might be problems with the command line on Windows. To solve that try this before running the app:

```
chcp 1251
```