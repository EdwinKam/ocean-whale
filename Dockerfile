FROM ubuntu:latest AS build

RUN apt-get update
RUN apt-get install openjdk-17-jdk -y
COPY . .

RUN ./gradlew bootJar --no-daemon

FROM openjdk:17-jdk-slim

EXPOSE 8080

COPY --from=build /build/libs/whale-0.0.1-SNAPSHOT.jar app.jar
COPY ocean-app-a4c89-firebase-adminsdk-urlnl-05c730aa42.json ocean-app-a4c89-firebase-adminsdk-urlnl-05c730aa42.json
COPY application.properties application.properties

ENTRYPOINT ["java", "-jar", "app.jar"]
