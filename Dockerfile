

FROM maven:3.9.7-eclise-temurin-21-alpine AS build

WORKDIR /app

COPY pom.xml ./
COPY src ./src

RUN mvn install -Dmaven.test.skip=true

FROM eclipse-temurim:21-jre-alpine

WORKDIR /app

COPY --from=build /target/*.jar app.jar

CMD["java", "-jar", "app.jar"]