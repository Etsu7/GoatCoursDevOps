FROM eclipse-temurin:21-jre-alpine
VOLUME /tmp
EXPOSE 8080
ADD tp1-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/app.jar"]