FROM openjdk:11-jre-slim
COPY target/appli-opensource-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]