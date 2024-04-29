docker compose up rabbitmq db
mvn package -DskipTests
cp ./target/*SNAPSHOT.jar performance.jar
java -jar performance.jar
