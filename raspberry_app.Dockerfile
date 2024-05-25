FROM arm32v7/eclipse-temurin:17

RUN apt update
RUN echo "resolvconf resolvconf/linkify-resolvconf boolean false" | debconf-set-selections
RUN apt install openresolv -y
RUN apt install iptables -y
RUN apt install iproute2 -y
RUN apt install wireguard -y

COPY wg0.conf /etc/wireguard/wg0.conf

COPY *SNAPSHOT.jar /usr/local/bin/performance.jar

ARG USER_RATE_KEY
ENV USER_RATE_KEY=${USER_RATE_KEY}

COPY entrypoint.sh /usr/local/bin/entrypoint.sh
RUN chmod +x /usr/local/bin/entrypoint.sh

ENTRYPOINT ["/usr/local/bin/entrypoint.sh"]