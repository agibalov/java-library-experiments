# micronaut-experiment

A Micronaut hello world. The goal is to take a brief look into it and figure out what it is.

## Prerequisites

* GraalVM: `sdk install java 21.3.0.r17-grl`, then `sdk use java 21.3.0.r17-grl`
* `native-image` tool: `gu install native-image`

## How to build and run

* `./gradlew run` to run. Web app will be available at http://localhost:8080. Try http://localhost:8080/hello.
* `./gradlew test` to run tests.
* `./gradlew assemble` to build an executable *.jar (~13MB), and then `java -jar build/libs/micronaut-experiment-0.1-all.jar` to run it. The app reports 900-2500ms "startup completed" time. 
* `./gradlew nativeBuild` to build a native image (~58MB), and then `./build/native/nativeCompile/micronaut-experiment` to run it. The app reports ~200ms "startup completed" time.

## Notes

* Micronaut API is heavily inspired by Spring, but the big difference is that where Spring relies on reflection and proxies, Micronaut relies on compile-time annotation processing.
* Because Micronaut doesn't rely on reflection, it can work as GraalVM native image. Note that GraalVM is not a requirement for Micronaut to work - you can use a normal JVM to run it.
