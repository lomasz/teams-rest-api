FROM openjdk:8-jdk-alpine
RUN apk add --update bash
ADD build/libs/*.jar /app.jar