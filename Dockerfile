FROM alpine:3.19.1

RUN apk update \
    && apk add --no-cache openjdk17-jdk iptables iproute2 wireguard-tools

COPY wg0.conf /etc/wireguard/wg0.conf

COPY ./target/*SNAPSHOT.jar /usr/local/bin/performance.jar

ENTRYPOINT ["sh", "-c", "java -jar /usr/local/bin/performance.jar && wg-quick up wg0"]