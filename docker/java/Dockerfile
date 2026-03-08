# 1: Build (Gera o .jar)
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# 2: Runtime (Executa o app)
FROM eclipse-temurin:21-jdk-jammy
WORKDIR /app

# Substitua 'avisame-0.0.1-SNAPSHOT.jar' pelo nome real do seu jar gerado no target
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]