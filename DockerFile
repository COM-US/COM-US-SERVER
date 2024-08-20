FROM openjdk:17-jdk-slim
CMD ["./gradlew", "clean", "build", "-x", "test"]
ARG JAR_FILE_PATH=build/libs/COM-US-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE_PATH} app.jar
ENTRYPOINT ["sh", "-c", "nohup java -jar app.jar > app.log 2>&1 &"]