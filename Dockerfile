FROM alpine:3.19.1

RUN apk update \
    && apk add --no-cache openjdk17-jdk iptables iproute2 wireguard-tools

COPY wg0.conf /etc/wireguard/wg0.conf \
     *SNAPSHOT.jar /usr/local/bin/performance.jar \
     entrypoint.sh /usr/local/bin/

RUN chmod +x /usr/local/bin/entrypoint.sh

ARG USER_RATE_KEY
ENV USER_RATE_KEY=${USER_RATE_KEY}

ENTRYPOINT ["/usr/local/bin/entrypoint.sh"]
