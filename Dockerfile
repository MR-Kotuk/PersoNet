FROM maven:3.9.5-eclipse-temurin-21-alpine AS build

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine

VOLUME /tmp

ARG JAR_FILE=target/*.jar

COPY --from=build /app/${JAR_FILE} app.jar

ENTRYPOINT ["java", "-jar", "/app.jar"]