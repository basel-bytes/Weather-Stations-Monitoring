FROM maven:3.9.2-eclipse-temurin-17-alpine

WORKDIR /base-central-station

COPY src ./src
COPY pom.xml .
COPY bitCaskStore ./bitCaskStore

RUN mvn clean install

CMD ["mvn", "exec:java"]