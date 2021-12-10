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


