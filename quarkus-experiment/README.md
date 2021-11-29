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
* `./gradlew test` to run tests in "normal" Java environment.
* `./gradlew testNative` to build a native image and run tests against it.
* `./gradlew build -Dquarkus.package.type=uber-jar` to build an executable *.jar (~15MB), and then `java -jar build/quarkus-experiment-1.0-runner.jar` to run it. The app reports 1000ms-1300ms "started in" time. 
* `./gradlew build -Dquarkus.package.type=native` to build a native image (~50MB), and then `./build/quarkus-experiment-1.0-runner` to run it. The app reports ~25ms "started in" time.
* `docker-compose up --build` to build a native image and run in `debian:11.1-slim`.

## Notes

* Because Quarkus doesn't rely on reflection, it can work as GraalVM native image. Note that GraalVM is not a requirement for Quarkus to work - you can use a normal JVM to run it.
