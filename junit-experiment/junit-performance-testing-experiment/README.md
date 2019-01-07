# junit-performance-testing-experiment

Experimenting with poor man's solutions for JUnit-based performance testing. The 2 solutions solve the same problem, but use different tools - JMH or Dropwizard Metrics.

## JMH

* `./gradlew clean jmh:test` - run benchmark tests (benchmark + result verification)
* `./gradlew clean jmh:benchmark` - run all benchmarks without verifications, put results under `build/jmh`

## Metrics

* `./gradlew clean metrics:test` - run all tests in "just tests" mode. No peformance data is collected.
* `./gradlew clean metrics:benchmark` - run all tests in "benchmarks" mode.
