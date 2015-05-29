Run this to create a Docker image:
```
sudo ./gradlew clean docker-app:distDocker
```
This will create `docker-java-experiment/docker-app:1.0`

Run this to run it:
```
sudo docker run docker-java-experiment/docker-app:1.0
```
Should say "Hello World".

