FROM gradle:jdk21-corretto-al2023

RUN ./gradlew clean assemble

COPY build/libs/myfinances-gateway-*.jar myfinances-gateway.jar

EXPOSE 8080

CMD ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/myfinances-gateway.jar"]