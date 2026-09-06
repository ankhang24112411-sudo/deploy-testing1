# deploy-testing1
stage 1 : create and add to repository

stage 2 : create Dockerfile

## Dockerfile :
FROM maven:3.9-eclipse-temurin-17 AS dependencies

WORKDIR /app

COPY pom.xml .

#download required dependencies and cache inside local Maven repo
RUN mvn dependency:go-offline


# Stage 2: Build application
FROM dependencies as builder

COPY src ./src

RUN mvn clean package -DskipTests

# Stage 3 : Run application
FROM eclipse-temurin:17-jre-jammy AS runtime

WORKDIR /app

COPY --from=builder app/target/*.war app.war

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.war"]

## Stage 3 : nginx configurations



## Stage : 
# gen key for CI/CD
 ssh-keygen -t ed25519 -C "github-actions-deploy" -f github-actions-deploy

 ls -la github-actions-deploy*

cat github-actions-deploy.pub

 cat github-actions-deploy
