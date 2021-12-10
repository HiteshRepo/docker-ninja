# Docker Ninja

## VM model
1. Host OS
2. Hypervisor
3. VM: Guest OS, S/w, App

## Disadvantages with VM model:
1. Resource utilisation: CPU, RAM, Disk consumed by guest OS.
2. Performance Overhead: Multiple OS hypervisor translation.
3. Cost Overhead: S/w licences and Each VM maintenance and upgrade/patches cost by us.

If we get rid of guest OS, it is going to be great.

## Container model [guest OS is done with]:
1. Host OS
2. Container runtime
3. Container: S/w, App

## In order to achieve advantages of VMs, containers uses [LXC or linux containers]:
1. CGROUPS: allows limitations and prioritisation of resources - CPU, memory, IO, n/w.
2. Namespace Isolation: allows isolation of an app’s view of operations env including process trees, n/w, usersids, mounted file systems

## Container packaging
.class -> app.jar [groups of .class] -> .war [group of lib.jar, app.jar], .ear [group of .war] -> uber-executable.jar [app.jar, groups of lib.jar, server.jar] -> container [uber-executable.jar, java runtime, LXC]

## Docker Arch
1. Client - Remote api
2. Host - Daemon, Containers, Images
3. Registry - Hub

Image -> multiple instances [containers]

Build Image
Pull Image
Run image

Client and Host is runs in same m/c

Com.docker.hyperkit - runtime process name

> docker version
Client:
 Cloud integration: v1.0.20
 Version:           20.10.10
 API version:       1.41
 Go version:        go1.16.9
 Git commit:        b485636
 Built:             Mon Oct 25 07:43:15 2021
 OS/Arch:           darwin/amd64
 Context:           default
 Experimental:      true

Server: Docker Engine - Community
 Engine:
  Version:          20.10.10
  API version:      1.41 (minimum version 1.12)
  Go version:       go1.16.9
  Git commit:       e2f740d
  Built:            Mon Oct 25 07:41:30 2021
  OS/Arch:          linux/amd64
  Experimental:     false
 containerd:
  Version:          1.4.11
  GitCommit:        5b46e404f6b9f661a205e28d59c982d3634148f8
 runc:
  Version:          1.0.2
  GitCommit:        v1.0.2-0-g52b36a2
 docker-init:
  Version:          0.19.0
  GitCommit:        de40ad0

## Some docker commands

* docker images
* docker ps
* docker pull image-name
* docker run image-name
* docker ps -a
* docker run -d image-name
* docker logs container-id
* docker stop container-id
* docker run -p <localhost port>:<container port> —name myweb -d nginx
* docker exec -it container-name sh [or bash or bin/sh]
* docker rm container-id/container-name
* docker volume ls
* docker volume create vol-name
* docker volume inspect vol-name
* docker run -it -v vol-name:/shared-volume —name container-name image-name
* docker ps -a -q -> gives old list of container ids
* docker stop $(docker ps -a -q)
* docker rm $(docker ps -a -q)
* docker commit container-name new-image-name - snapshot of a custom container
* docker build -t image-name . Or docker build -t image-name . -f path-to-dockerfile 
* docker history image-name - lists all layers per command
* docker rmi image-id
* docker tag current-image-name namespace/new-image-name
* docker build -t namespace/image-name:version-gitsha .
* docker build -t namespace/image-name:env . -> env = qa/stage/dev/prod
* Docker login
* docker push repository:version-tag
* 

Process id of container governs the lifecycle of container - if it gets killed due to some reason container stops - the process id belongs to the first command that executed on container inception

## Exercise 1
docker volume create docker-ninja-vol
docker run -d -p 80:80 -v docker-ninja-vol:/usr/share/nginx/html --name docker-ninja-nginx nginx

Or 

docker run -d -p 80:80 -v <some-path-i-your-machine>:/usr/share/nginx/html --name docker-ninja-nginx nginx


## Images
1. Inert, immutable file that is necessarily a snapshot of a container
2. Container is an instance of a Docker image
3. Docker images are stored in docker registry or docker hub, and images are cached in host machine by container engine.

## Dockerfile
1. Image to build from - FROM openjdk:alpine
2. Create a work dir - WORKDIR /usr/src/app
3. Copy files from host to image - [ADD/COPY] /host-file-or-dir-path /container-dir-path
4. Execute commands, to install s/w - RUN apt-get install python2 git
5. Expose port from container - EXPOSE 8080
6. Configure main process executable command [process id 1] - [CMD/ENTRYPOINT] [‘executable’. ‘Param1’  ‘param2’]

Docker -> 1.build -> Docker runtime [docker daemon, image cache, container] -> daemon -> 2.pulls base image -> docker registry -> 3.saves it local image cache -> 4.starts container using local base image -> 5.executes instructions given in file -> 6.saves intermediate image to image cache by taking snapshot -> 4,5,6 repeated for each instruction in docker file -> 7. Push final image to registry

Docker build makes use of cache till the point in docker file where nothing has changed, after change it cannot leverage the cache, so it creates new intermediary containers.

## Excercies 2
Download -> apache-maven-3.8.4-bin.zip
unzip apache-maven-3.8.4-bin.zip
export PATH=/Users/hitesh.pattanayak/Downloads/docker-ninja/apache-maven-3.8.4/bin:$PATH

> man -v
Maven home: /Users/hitesh.pattanayak/Downloads/docker-ninja/apache-maven-3.8.4
Java version: 17.0.1, vendor: Oracle Corporation, runtime: /Users/hitesh.pattanayak/Library/Java/JavaVirtualMachines/openjdk-17.0.1/Contents/Home
Default locale: en_IN, platform encoding: UTF-8
OS name: "mac os x", version: "11.6", arch: "x86_64", family: "mac"

mvn spring-boot:run 
mvn package
docker build -t todolist .
docker run -p 8080:8080 -d --name todolist todolist


## Image layers
1. Each command in dockerfile creates an image layer
2. 1st layer is the base image and has no parent id but has an image id
3. 2nd layer onwards have parent id that refers to previous layer image id and so on.
4. All layers except last layer are read-only layers while the last one in read-write layer
5. We can check which command resulted in adding what amount of size to the whole image
6. Extracting dependency package file and installing dependency will improve the optimisation as only change in code will not use image cache and others will use the cache. This significantly saves memory in case of no change in code.
7. Further optimisation can be done by installing only environment related dependencies
8. Use .dockerignore to reduce build context size - reverse ignore approach

## Exercise 3
cd target
mkdir app 
cd app
jar -xf ../todolist-0.0.1-SNAPSHOT.jar - splits the lib and app files


## Multistage Build
1. Challenges - building artifacts within docker - for ex. mvn package
2. Package download - too much space and time
3. A single change in app code will take redownload the same dependency again and again
4. Build and Run stage

## Docker versioning
1. namespace/image-name -> repository, version:env or version-gitsha are tags
2. namespace/image-name:version
3. namespace/image-name:Env
4. namespace/image-name:version-gitsha
5. Create a repository:
    1. Namespace - docker id
    2. Image name
    3. Public/Private

## Optimised docker image and build
1. Use .dockerignore to reduce context size to send to for docker daemon
2. Breakup build steps to create reusable layers and leverage caching during build
3. Optimise image size to be as small as possible. Avoid unwanted files under docker image
4. Use multistage docker file to separate build and run stage to avoid unwanted files from build