# ✅ Use Debian-based Java image
FROM openjdk:17-slim

# ✅ Use apt-get now safely
RUN apt-get update && apt-get install -y netcat

# Working directory
WORKDIR /app

# Copy code
COPY ./src /app/src
COPY ./lib /app/lib
COPY entrypoint.sh /app/entrypoint.sh

# Compile Java code
RUN javac -cp "lib/mysql-connector-j-9.3.0.jar" src/*.java -d out

# Make script executable
RUN chmod +x /app/entrypoint.sh

# Environment variables
ENV DB_HOST=db
ENV DB_PORT=3306
ENV DB_USER=root
ENV DB_PASSWORD=root
ENV DB_NAME=AutoPartShop

# Run entrypoint
CMD ["sh", "/app/entrypoint.sh"]



