FROM maven:latest as build
WORKDIR /app
COPY . .
RUN mvn package -DskipTests
FROM openjdk:latest as run
ARG JAR_FILE=app/target/*SNAPSHOT.jar
WORKDIR /app
COPY --from=build ${JAR_FILE} performance.jar
ENTRYPOINT [ "java", "-jar", "performance.jar" ]