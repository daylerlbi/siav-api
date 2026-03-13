### ETAPA 1: BUILD ###
# Imagen base para compilar la aplicación
FROM maven:3.8.5-openjdk-17-slim AS build

# Establecer el directorio de trabajo en el contenedor
WORKDIR /app

# Copiar el archivo pom.xml al directorio de trabajo
COPY pom.xml .

# Instalar dependencias (esto permite usar caché de Docker si pom.xml no cambia)
RUN mvn dependency:go-offline -B

# Copiar el código fuente al directorio de trabajo
COPY src ./src

# Construir la aplicación (sin ejecutar tests para acelerar el build)
RUN mvn clean package -DskipTests


### ETAPA 2: DEPLOY ###
# Imagen base ligera para ejecutar la aplicación
FROM eclipse-temurin:17-jre-alpine

# Instalar findutils para poder usar el comando find
RUN apk add --no-cache findutils curl

# Establecer el directorio de trabajo en el contenedor
WORKDIR /app

# Crear un usuario no-root para ejecutar la aplicación (seguridad)
RUN addgroup -g 1001 -S spring && \
    adduser -S spring -u 1001

# Copiar el archivo JAR desde la etapa de build
COPY --from=build /app/target/*.jar app.jar

# Cambiar la propiedad del archivo JAR al usuario spring
RUN chown spring:spring app.jar

# Cambiar al usuario spring
USER spring:spring

# Exponer el puerto de la aplicación
EXPOSE 8080

# Comando para ejecutar la aplicación
# Se utilizan variables de entorno para configurar la JVM y la aplicación
ENTRYPOINT ["java", \
    "-Djava.security.egd=file:/dev/./urandom", \
    "-Dspring.profiles.active=${SPRING_PROFILES_ACTIVE}", \
    "-jar", \
    "app.jar"]