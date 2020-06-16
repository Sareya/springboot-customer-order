# springboot-customer-order
Spring boot application

# Instructions to run project using docker
1- cd to docker in project root directory
2- run "docker build -t spring-app ."
3- run "docker run -p 8080:8080 spring-app"

# Instructions to run project using java and maven 
1- cd to project root directory
2- run "mvn clean install" to build project
3- cd to target directory created by maven
4- run "java -jar customer-order-0.0.1-SNAPSHOT.jar --server.port=<specify any free port here>"

# Instructions to run project using maven wrapper (may cause some problems behind proxy)
1- cd to project root directory
2- run "./mvnw spring-boot:run"

Project will be up and running by now, use sample postman collection to use apis
