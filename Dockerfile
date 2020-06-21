FROM openjdk:11-jdk-slim
WORKDIR  /app

COPY target/springboot-blog-0.0.1-SNAPSHOT.jar /app

EXPOSE 8080

CMD [ "java", "-jar",  "springboot-blog-0.0.1-SNAPSHOT.jar" ]
