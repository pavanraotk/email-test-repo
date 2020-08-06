FROM openjdk:8-jdk-alpine
VOLUME /tmp
EXPOSE 8082
RUN mkdir -p /email-service/
RUN mkdir -p /email-service/logs/
ADD target/email-service-0.0.1-SNAPSHOT.jar /email-service/email-service.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-Dspring.profiles.active=container", "-jar", "/email-service/email-service.jar"]