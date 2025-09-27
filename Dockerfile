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
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Crear usuario no root para seguridad
RUN addgroup -g 1001 -S spring && \
    adduser -S spring -u 1001

# Copiar el JAR desde la etapa builder
COPY --from=builder /app/target/*.jar app.jar

# Crear directorio de logs
RUN mkdir -p /app/logs && chown -R spring:spring /app

# Cambiar a usuario no root
USER spring:spring

# Exponer puerto
EXPOSE 8080

# Variables de entorno por defecto
ENV SPRING_PROFILES_ACTIVE=prod
ENV JAVA_OPTS="-Xmx512m -Xms256m"

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
  CMD curl -f http://localhost:8080/actuator/health || exit 1

# Comando de arranque con opciones JVM optimizadas
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]