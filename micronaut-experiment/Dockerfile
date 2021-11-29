FROM ghcr.io/graalvm/graalvm-ce:21.3.0 as builder
COPY . /app
WORKDIR /app
RUN ./gradlew nativeBuild

FROM debian:11.1-slim
COPY --from=builder /app/build/native/nativeCompile/micronaut-experiment /app/micronaut-experiment
WORKDIR /app
ENTRYPOINT ["./micronaut-experiment"]
