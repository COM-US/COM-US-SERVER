FROM openjdk:17-jdk-slim

# Gradle 빌드를 실행하여 JAR 파일을 생성합니다.
CMD ["./gradlew", "clean", "build", "-x", "test"]

# JAR 파일 경로와 이름을 설정합니다.
ARG JAR_FILE_PATH=build/libs/COM-US-0.0.1-SNAPSHOT.jar

# JAR 파일을 컨테이너에 복사합니다.
COPY ${JAR_FILE_PATH} app.jar

# Java 애플리케이션을 실행합니다.
ENTRYPOINT ["java", "-jar", "app.jar"]
