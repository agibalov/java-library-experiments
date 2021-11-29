FROM ghcr.io/graalvm/graalvm-ce:21.3.0 as builder
COPY . /app
WORKDIR /app
RUN gu install native-image
RUN native-image --version
RUN ./gradlew build -Dquarkus.package.type=native

FROM debian:11.1-slim
COPY --from=builder /app/build/quarkus-experiment-1.0-runner /app/quarkus-experiment-1.0-runner
WORKDIR /app
ENTRYPOINT ["./quarkus-experiment-1.0-runner"]
