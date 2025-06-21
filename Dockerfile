FROM openjdk:17-slim

RUN apt-get update && apt-get install -y netcat

WORKDIR /app

COPY ./src /app/src
COPY ./lib /app/lib
COPY entrypoint.sh /app/entrypoint.sh

RUN javac -cp "lib/mysql-connector-j-9.3.0.jar" src/*.java -d out

RUN chmod +x /app/entrypoint.sh

ENV DB_HOST=db
ENV DB_PORT=3306
ENV DB_USER=root
ENV DB_PASSWORD=root
ENV DB_NAME=AutoPartShop

CMD ["sh", "/app/entrypoint.sh"]



