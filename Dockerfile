FROM openjdk:11-jre-slim
COPY target/appli-opensource-0.0.1-SNAPSHOT-exec.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app.jar"]
