# base image to build from
FROM openjdk:8-jdk-alpine

COPY target/app/BOOT-INF/lib /app/lib
COPY target/app/META-INF /app/META-INF
COPY target/app/BOOT-INF/classes /app 

EXPOSE 8080

ENTRYPOINT ["java", "-cp", "app:app/lib/*", "com.hiteshbootcamp.todolist.TodolistApplication"]
