#!/bin/sh

echo "Waiting for MySQL to be ready at $DB_HOST:$DB_PORT..."

while ! nc -z "$DB_HOST" "$DB_PORT"; do
  sleep 1
done

echo "MySQL is up - running Java application"
java -cp out:lib/mysql-connector-j-9.3.0.jar TestShop


