FROM alpine:3.19.1

RUN apk update \
    && apk add --no-cache openjdk17-jdk iptables iproute2 wireguard-tools

COPY wg0.conf /etc/wireguard/wg0.conf

COPY ./target/*SNAPSHOT.jar /usr/local/bin/performance.jar

ARG USER_RATE_KEY

COPY entrypoint.sh /usr/local/bin/entrypoint.sh
RUN chmod +x /usr/local/bin/entrypoint.sh

ENTRYPOINT ["/usr/local/bin/entrypoint.sh"]