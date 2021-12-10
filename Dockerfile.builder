FROM maven
WORKDIR /workspace/app

COPY pom.xml pom.xml
RUN mvn dependency:go-offline -B

COPY src src
RUN mvn package -DskipTests

EXPOSE 8080

#configures main process executable command
ENTRYPOINT ["java", "-Xmx512m", "-Xms256m", "-jar", "target/todolist-0.0.1-SNAPSHOT.jar"]