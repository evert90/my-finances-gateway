FROM gradle:8.13-jdk21-corretto-al2023 AS build

WORKDIR /app
COPY . .

RUN gradle assemble --no-daemon

FROM eclipse-temurin:21-jre

WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar

COPY sentry/sentry-opentelemetry-agent-8.8.0.jar /sentry/sentry-opentelemetry-agent.jar

ENV SENTRY_AUTO_INIT=false
ENV OTEL_LOGS_EXPORTER=none
ENV OTEL_METRICS_EXPORTER=none
ENV OTEL_TRACES_EXPORTER=none

EXPOSE 8080

CMD ["java", "-Djava.security.egd=file:/dev/./urandom", "-javaagent:/sentry/sentry-opentelemetry-agent.jar", "-jar", "app.jar"]