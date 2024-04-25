docker compose up rabbitmq db

var="$(uname -p)"

if [[ $var=="x86_64" ]]; then
	echo "ARCH=amd64" > .env
else
	echo " ARCH=arm64" > .env
fi

docker compose up app
