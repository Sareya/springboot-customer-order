# Springboot-customer-order
Spring boot application

## Instructions to run project using docker
- cd to docker in project root directory
- run "docker build -t spring-app ."
- run "docker run -p 8080:8080 spring-app"

## Instructions to run project using java and maven 
- cd to project root directory
- run "mvn clean install" to build project
- cd to target directory created by maven
- run "java -jar customer-order-0.0.1-SNAPSHOT.jar --server.port=<specify any free port here>"

## Instructions to run project using maven wrapper (may cause some problems behind proxy)
- cd to project root directory
- run "./mvnw spring-boot:run"

Project will be up and running by now, use sample postman collection to use apis
