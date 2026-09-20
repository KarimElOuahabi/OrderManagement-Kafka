FROM eclipse-temurin:21-jdk AS builder
WORKDIR /app
COPY build.gradle settings.gradle gradlew ./
COPY gradle ./gradle
RUN for i in 1 2 3 4 5; do ./gradlew dependencies --no-daemon && break || sleep 10; done
ARG CACHEBUST=1
COPY . .
RUN for i in 1 2 3 4 5; do ./gradlew clean bootJar --no-daemon && break || sleep 10; done

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
ARG CACHEBUST=1
RUN echo "$CACHEBUST" > /tmp/cachebust
COPY --from=builder /app/build/libs/*.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]