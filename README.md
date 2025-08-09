# matcha-backend
Clone of the Tea app. This is a Spring Boot project that builds a REST HTTP server that can be run locally.
It uses gradle as the build tool. This project comes with a gradle wrapper (`gradlew`) so you do not need to install
gradle. It runs on JDK21

# How to Run the Backend Locally
Locally you can start the server using
```bash
./gradlew :bootRun --args='--spring.profiles.active=dev'
```
`--args='--spring.profiles.active=dev'` will make it run on dev mode, merging in changes for the `application-dev.yaml` file
On dev mode this will start the server on port 8000- i.e.
`http://localhost:8000` b/c we adjusted it in `application-dev.yaml`

# Managing Gradle Dependencies
## Adding a new library
Add the library in the build.gradle.kts file.

# How to Create Spring Boot Jar for Production
In development, we run the Spring Boot server by executing the following command:
`./gradlew bootRun`
This is not ideal for production b/c it will download all the dependencies, compile, and then package it into a jar file. This is not ideal, since we want an immediately runnable executable for production. Further, `bootRun` will apply certain development defaults that slow down performance and are not ideal for production.

Note that in prod this will run on port 8080.

## Clean the previously built artifacts
First you want to clean any artifacts left over by the old build. To do this run:
`./gradlew clean`

## Generate a Fat Jar
This option is useful if you want to launch an embedded web server. This option will build a fat jar, which includes a tomcat server inside the jar file.  
Run the following commands to generate the fat jar
```bash
cd configuration
../gradlew bootJar
```
This will generate a fat jar in `configuration/build/libs`  
To run it, use
```bash
java -jar ./playground-0.0.1-SNAPSHOT.jar
```
If you want to run it in the background
```bash
nohup java -jar ./playground-0.0.1-SNAPSHOT.jar &
```

## Generate a War
This option is useful if you already have a tomcat server. This option will build a war.