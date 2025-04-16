FROM gradle:jdk21-corretto-al2023 AS build

 WORKDIR /app
 COPY . .

 RUN chmod +x gradlew

 RUN ./gradlew clean build -x test

FROM eclipse-temurin:21-jre

WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar

EXPOSE 8080

CMD ["java","-Djava.security.egd=file:/dev/./urandom","-jar","app.jar"]