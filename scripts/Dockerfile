FROM openjdk:11 AS builder
COPY .. .
RUN ["./gradlew", "assemble"]

FROM openjdk:11-jre-slim-buster
COPY --from=builder ../apiserver/build/libs/apiserver.jar .
CMD ["java", "-jar", "-Dspring.profiles.active=test", "apiserver.jar"]
