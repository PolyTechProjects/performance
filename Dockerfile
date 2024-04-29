FROM openjdk:latest as run

COPY ./*SNAPSHOT.jar /usr/local/bin/performance.jar

ENTRYPOINT [ "java", "-jar", "/usr/local/bin/performance.jar" ]
