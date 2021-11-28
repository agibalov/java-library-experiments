# micronaut-experiment

A Micronaut hello world. The goal is to take a brief look into it and figure out what it is.

## Prerequisites

* JDK 11

## How to build and run

* `./gradlew run` to run. Web app will be available at http://localhost:8080. Try http://localhost:8080/hello.
* `./gradlew test` to run tests.
* `./gradlew assemble` to build an executable *.jar, and then `java -jar build/libs/micronaut-experiment-0.1-all.jar` to run it.

## Notes

* Micronaut API is heavily inspired by Spring, but the big difference is that where Spring relies on reflection and proxies, Micronaut relies on compile-time annotation processing.
