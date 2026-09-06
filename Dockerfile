FROM maven:3.9-eclipse-temurin-17 AS dependencies

WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline

FROM dependencies AS builder

COPY src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jre-jammy AS runtime

WORKDIR /app

COPY --from=builder /app/target/*.war /app/app.war

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.war"]