### Default image

Run this to create a Docker image:
```
./gradlew clean distDocker
```
This will create `me.loki2302/docker-app:1.0`. Run this to run it:
```
docker run me.loki2302/docker-app:1.0
```
Should say "Hello World".

### Custom image

Run this to create a custom Docker image:
```
./gradlew clean customDocker
```
This will create `me.loki2302/custom:1.0`. Run this to run it:
```
docker run me.loki2302/custom:1.0
```
Should say "Hello World".
