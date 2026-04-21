
FROM eclipse-temurin:25-jdk-alpine AS builder

WORKDIR /app


COPY gradlew gradlew.bat settings.gradle build.gradle ./
COPY gradle gradle
COPY src src


RUN apk add --no-cache dos2unix && dos2unix gradlew


RUN chmod +x gradlew


RUN ./gradlew bootJar -x test --no-daemon


FROM eclipse-temurin:25-jre-alpine

WORKDIR /app

RUN apk --no-cache add tzdata wget

COPY --from=builder /app/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]