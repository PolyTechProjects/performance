#!/bin/sh
java -jar /usr/local/bin/performance.jar --user.rate.key=$USER_RATE_KEY &
sleep 20
wg-quick up wg0
wait $!
