# Building Stage
FROM gradle:jdk17-alpine AS builder
LABEL authors="Adam Davis"

WORKDIR /app

COPY . .

RUN chmod +x gradlew
RUN ./gradlew assemble --stacktrace

# Packaging Stage
# Keep the final image small by using the latest required base image
FROM amazoncorretto:17-alpine-jdk
LABEL authors="Adam Davis"

COPY --from=builder ["/app/build/libs/*.jar", "dundie_awards.jar"]

ENTRYPOINT ["java"]
CMD ["-XX:MaxGCPauseMillis=80", "-XX:UseStringDeduplication", "-XX:ExitOnOutOfMemoryError", "-jar", "/dundie_awards.jar"]