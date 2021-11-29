# quarkus-experiment

A [Quarkus](https://quarkus.io/) hello world. The goal is to take a brief look into it and figure out what it is.

## Prerequisites

* GraalVM: `sdk install java 21.3.0.r17-grl`, then `sdk use java 21.3.0.r17-grl`
* `native-image` tool: `gu install native-image`

## How to build and run

* `./gradlew quarkusDev` to run. Web app will be available at http://localhost:8080. Note that it will rebuild/restart when you make changes to the code. Try:
  * http://localhost:8080/ for static home page
  * http://localhost:8080/hello for API
  * http://localhost:8080/q/dev/ Quarks's built-in dev UI
* TODO: test
* `./gradlew build -Dquarkus.package.type=uber-jar` to build an executable *.jar (~15MB), and then `java -jar build/quarkus-experiment-1.0-runner.jar` to run it. The app reports 1000ms-1300ms "started in" time. 
* `./gradlew build -Dquarkus.package.type=native` to build a native image (~50MB), and then `./build/quarkus-experiment-1.0-runner` to run it. The app reports ~25ms "started in" time.
* `docker-compose up --build` to build a native image and run in `debian:11.1-slim`.

## Notes

TODO

---

# app Project

> **_NOTE:_**  Quarkus now ships with a Dev UI, which is available in dev mode only at http://localhost:8080/q/dev/.

## Packaging and running the application

The application can be packaged using:
```shell script
./gradlew build
```
It produces the `quarkus-run.jar` file in the `build/quarkus-app/` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `build/quarkus-app/lib/` directory.

The application is now runnable using `java -jar build/quarkus-app/quarkus-run.jar`.

If you want to build an _über-jar_, execute the following command:
```shell script
./gradlew build -Dquarkus.package.type=uber-jar
```

The application, packaged as an _über-jar_, is now runnable using `java -jar build/*-runner.jar`.

## Creating a native executable

You can create a native executable using: 
```shell script
./gradlew build -Dquarkus.package.type=native
```

Or, if you don't have GraalVM installed, you can run the native executable build in a container using: 
```shell script
./gradlew build -Dquarkus.package.type=native -Dquarkus.native.container-build=true
```

You can then execute your native executable with: `./build/app-1.0-runner`

If you want to learn more about building native executables, please consult https://quarkus.io/guides/gradle-tooling.

## Related Guides

- RESTEasy JAX-RS ([guide](https://quarkus.io/guides/rest-json)): REST endpoint framework implementing JAX-RS and more

## Provided Code

### RESTEasy JAX-RS

Easily start your RESTful Web Services

[Related guide section...](https://quarkus.io/guides/getting-started#the-jax-rs-resources)
