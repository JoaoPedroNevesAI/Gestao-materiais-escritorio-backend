FROM maven:3.9-eclipse-temurin-21 AS build

WORKDIR /app

COPY pom.xml .
RUN mvn --batch-mode dependency:go-offline

COPY src ./src

RUN mvn --batch-mode clean package -DskipTests

FROM eclipse-temurin:21-jre

WORKDIR /app

RUN mkdir -p /app/uploads/qrcodes && \
    chown -R 10001:10001 /app

COPY --from=build /app/target/ifescritorio-0.0.1-SNAPSHOT.jar /app/app.jar

USER 10001

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]