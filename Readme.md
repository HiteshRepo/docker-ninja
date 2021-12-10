## Dockerfile
1. Setting base image
2. Splitting copying of package into multiple steps - so that if appcode changes and image is built again, 
    the cached intermediary image layer [for copying lib] can be used again in subsequent builds
    1. Libraries
    2. App code 
    3. Command used to build maven package - mvn package
    4. Command used to split the package - jar -xf ../todolist-0.0.1-SNAPSHOT.jar [after changing directory to target/app]
3. Context transferred to daemon: 38.93MB
4. Steps info:
    1. [1/4] FROM docker.io/library/openjdk:8-jdk-alpine@sha256:94792824df2df33402f201713f932b58cb9de94a0cd524164a0f2283343547b3
    2. [2/4] COPY target/app/BOOT-INF/lib /app/lib
    3. [3/4] COPY target/app/META-INF /app/META-INF
    4. [4/4] COPY target/app/BOOT-INF/classes /app
5. History
    1. ENTRYPOINT ["java" "-cp" "app:app/lib/*" "co…   0B
    2. EXPOSE map[8080/tcp:{}]                         0B
    3. COPY target/app/BOOT-INF/classes /app # buil…   4.56kB
    4. COPY target/app/META-INF /app/META-INF # bui…   2.15kB
    5. COPY target/app/BOOT-INF/lib /app/lib # buil…   38.9MB
    6. previous stuff  99.3MB 
6. Steps info in 2nd build:
    1. [1/4] FROM docker.io/library/openjdk:8-jdk-alpine@sha256:94792824df2df33402f201713f932b58cb9de94a0cd524164a0f2283343547b3 
    2. CACHED [2/4] COPY target/app/BOOT-INF/lib /app/lib
    3. CACHED [3/4] COPY target/app/META-INF /app/META-INF
    4. CACHED [4/4] COPY target/app/BOOT-INF/classes /app
7. Image size: 144MB
8. Time taken to build 1st step - , 2nd step - 

## Dockerfile.builder
1. Instead of copying binaries to conatiners to docker, dependencies are installed within container, app is build and run
2. Splitting copying of dependency file and running installation is a separate step - so that if appcode changes and image is built again, 
    the cached intermediary image layer [copying of dependency file and running installation] can be used again in subsequent builds
3. Context transferred to daemon: 6.95MB
4. Steps info:
    1. [1/6] FROM docker.io/library/maven@sha256:0e09a20f8bd3bd9f1c96ba531c3d410e3501fa319b3799479710adfbf6a495ae
    2. [2/6] WORKDIR /workspace/app
    3. [3/6] COPY pom.xml pom.xml
    4. [4/6] RUN mvn dependency:go-offline -B
    5. [5/6] COPY src src
    6. [6/6] RUN mvn package -DskipTests
5. History
    1. ENTRYPOINT ["java" "-Xmx512m" "-Xms256m" "-j…   0B
    2. EXPOSE map[8080/tcp:{}]                         0B
    3. RUN /bin/sh -c mvn package -DskipTests # bui…   41.1MB
    4. COPY src src # buildkit                         2.48kB
    5. RUN /bin/sh -c mvn dependency:go-offline -B …   102MB
    6. COPY pom.xml pom.xml # buildkit                 1.61kB
    7. WORKDIR /workspace/app                          0B
    8. previous stuff  650MB 
6. Steps info in 2nd build:
    1. [1/6] FROM docker.io/library/maven@sha256:0e09a20f8bd3bd9f1c96ba531c3d410e3501fa319b3799479710adfbf6a495ae
    2. CACHED [2/6] WORKDIR /workspace/app
    3. CACHED [3/6] COPY pom.xml pom.xml
    4. CACHED [4/6] RUN mvn dependency:go-offline -B
    5. CACHED [5/6] COPY src src
    6. CACHED [6/6] RUN mvn package -DskipTests
7. Image size: 929MB
8. Time taken to build 1st step - 350s, 2nd step - 5s
9. Any code change - leads to re-download of all packages
10. Steps info in 3rd build after code change:
    1. [1/6] FROM docker.io/library/maven@sha256:0e09a20f8bd3bd9f1c96ba531c3d410e3501fa319b3799479710adfbf6a495ae
    2. CACHED [2/6] WORKDIR /workspace/app
    3. CACHED [3/6] COPY pom.xml pom.xml
    4. CACHED [4/6] RUN mvn dependency:go-offline -B
    5. [5/6] COPY src src
    6. [6/6] RUN mvn package -DskipTests


