# Etapa 1: Construcción del JAR con Maven
FROM maven:3.9.9-eclipse-temurin-21 AS builder
WORKDIR /app

# Copiar pom.xml y descargar dependencias primero (cache)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copiar el código fuente y construir el jar
COPY src ./src
RUN mvn clean package -DskipTests

# Etapa 2: Imagen ligera para producción
FROM eclipse-temurin:21-jdk
WORKDIR /app

# Copiar el JAR desde la etapa builder
COPY --from=builder /app/target/*.jar app.jar

# Exponer puerto
EXPOSE 8080

# Variables de entorno (puedes sobreescribir en docker-compose o Kubernetes)
ENV SPRING_PROFILES_ACTIVE=prod

# Comando de arranque
ENTRYPOINT ["java", "-jar", "app.jar"]
