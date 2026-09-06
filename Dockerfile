FROM maven:3.9-eclipse-temurin-17 AS dependencies

WORKDIR /app

COPY pom.xml .

RUN mvn dependency:go-offline


# Stage 2: Build application
FROM dependencies AS builder

COPY src ./src

RUN mvn clean package -DskipTests


# Stage 3: Run application
FROM eclipse-temurin:17-jre-jammy AS runtime

WORKDIR /app

COPY --from=builder /app/target/*.jar /app/app.jar

EXPOSE 8095

ENTRYPOINT ["java", "-jar", "/app/app.jar"]