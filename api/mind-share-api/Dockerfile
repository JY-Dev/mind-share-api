FROM openjdk:17-slim AS base

FROM base AS builder
WORKDIR /app

COPY . /app
RUN /app/gradlew :api:mind-share-api:clean :api:mind-share-api:build --no-daemon

FROM base
WORKDIR /app
COPY --from=builder /app/api/mind-share-api/build/libs/mind-share-api.jar /app/app.jar
EXPOSE 8080

ENTRYPOINT ["sh", "-c", "java -jar app.jar"]

