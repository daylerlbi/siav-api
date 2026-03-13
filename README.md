# 🎓 SIAV - Backend

Sistema de Informacion Academico Virtual para la Universidad Francisco de Paula Santander, desarrollado con Spring Boot para la administración integral de estudiantes, matrículas, notas y contenidos académicos.

## 📋 Descripción del Proyecto

SIAV UFPS es una plataforma backend robusta que facilita la gestión académica universitaria de la Maestria en TIC aplicadas a la educación, integrando múltiples sistemas como Moodle, bases de datos Oracle y MySQL, y servicios de almacenamiento en la nube. El sistema permite gestionar estudiantes, docentes, matrículas, programas académicos, y la sincronización de datos con sistemas legados.

## 🚀 Características Principales

- **Gestión de Estudiantes**: Registro, actualización y seguimiento de estudiantes
- **Sistema de Matrículas**: Control completo del proceso de matrícula académica
- **Integración con Moodle**: Sincronización automática de cursos y usuarios
- **Doble Base de Datos**: MySQL para datos operacionales y Oracle para datos legados
- **Autenticación Segura**: JWT + OAuth2 (Google)
- **Gestión de Archivos**: Integración con AWS S3
- **Sistema de Notas**: Manejo de calificaciones para pregrado y posgrado
- **API RESTful**: Endpoints bien documentados para frontend

## 🛠️ Stack Tecnológico

### Framework Principal

<div align="center">

|                                                   **Spring Boot**                                                    | **Versión** |
| :------------------------------------------------------------------------------------------------------------------: | :---------: |
| ![Spring Boot](https://img.shields.io/badge/Spring_Boot-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white) |   `3.4.3`   |

</div>

### Dependencias Core

<div align="center">

|                                                         **Tecnología**                                                         | **Versión** |        **Propósito**         |
| :----------------------------------------------------------------------------------------------------------------------------: | :---------: | :--------------------------: |
| ![Spring Security](https://img.shields.io/badge/Spring_Security-6DB33F?style=flat-square&logo=spring-security&logoColor=white) |    `6.x`    | Autenticación y Autorización |
|     ![Spring Data JPA](https://img.shields.io/badge/Spring_Data_JPA-6DB33F?style=flat-square&logo=spring&logoColor=white)      |    `3.x`    |    Persistencia de Datos     |
|          ![Hibernate](https://img.shields.io/badge/Hibernate-59666C?style=flat-square&logo=hibernate&logoColor=white)          |   `6.6.8`   |        ORM Principal         |
|                ![MySQL](https://img.shields.io/badge/MySQL-4479A1?style=flat-square&logo=mysql&logoColor=white)                |    `8.x`    |   Base de Datos Principal    |
|              ![Oracle](https://img.shields.io/badge/Oracle-F80000?style=flat-square&logo=oracle&logoColor=white)               | `19.3.0.0`  |     Base de Datos Legada     |

</div>

### Servicios en la Nube & Integración

<div align="center">

|                                                  **Servicio**                                                   | **Versión** |          **Uso**           |
| :-------------------------------------------------------------------------------------------------------------: | :---------: | :------------------------: |
|     ![AWS S3](https://img.shields.io/badge/AWS_S3-FF9900?style=flat-square&logo=amazon-aws&logoColor=white)     | `1.12.782`  | Almacenamiento de Archivos |
| ![Spring Cloud](https://img.shields.io/badge/Spring_Cloud-6DB33F?style=flat-square&logo=spring&logoColor=white) |   `2.2.6`   |    Integración con AWS     |
|       ![OAuth2](https://img.shields.io/badge/OAuth2-4285F4?style=flat-square&logo=google&logoColor=white)       |  `latest`   |    Autenticación Google    |

</div>

### Seguridad & Tokens

<div align="center">

|                                            **Herramienta**                                             | **Versión** |         **Función**         |
| :----------------------------------------------------------------------------------------------------: | :---------: | :-------------------------: |
| ![JWT](https://img.shields.io/badge/JWT-000000?style=flat-square&logo=json-web-tokens&logoColor=white) |   `0.9.1`   |   Tokens de Autenticación   |
|        ![BCrypt](https://img.shields.io/badge/BCrypt-FF6B6B?style=flat-square&logoColor=white)         |  `latest`   | Encriptación de Contraseñas |

</div>

### Herramientas de Desarrollo

<div align="center">

|                                                **Tool**                                                 | **Versión** |          **Propósito**          |
| :-----------------------------------------------------------------------------------------------------: | :---------: | :-----------------------------: |
|         ![Lombok](https://img.shields.io/badge/Lombok-BC4521?style=flat-square&logoColor=white)         |  `latest`   | Reducción de Código Boilerplate |
|  ![Flying Saucer](https://img.shields.io/badge/Flying_Saucer-9B59B6?style=flat-square&logoColor=white)  |  `9.1.22`   |       Generación de PDFs        |
|        ![Jackson](https://img.shields.io/badge/Jackson-2E86C1?style=flat-square&logoColor=white)        |  `latest`   |     Serialización JSON/XML      |
| ![Maven](https://img.shields.io/badge/Maven-C71A36?style=flat-square&logo=apache-maven&logoColor=white) |  `latest`   |     Gestión de Dependencias     |

</div>

## 🏗️ Arquitectura del Sistema

```
┌─────────────────────────────────────────────────────────────┐
│                    FRONTEND (Angular/React)                 │
└─────────────────────┬───────────────────────────────────────┘
                      │ HTTP/REST API
┌─────────────────────▼───────────────────────────────────────┐
│                SPRING BOOT APPLICATION                      │
│  ┌─────────────┐ ┌─────────────┐ ┌─────────────────────┐    │
│  │ Controllers │ │  Services   │ │   Security Config   │    │
│  └─────────────┘ └─────────────┘ └─────────────────────┘    │
└─────────────────────┬───────────────────────────────────────┘
                      │
      ┌───────────────┼───────────────┐
      │               │               │
┌─────▼─────┐ ┌──────▼──────┐ ┌──────▼──────┐
│   MySQL   │ │   Moodle    │ │  AWS S3     │
│(Principal)│ │ Integration | │  (Archivos) │
└───────────┘ └─────────────┘ └─────────────┘
                  
```

## 📦 Instalación y Configuración

### Prerrequisitos

- **Java**: JDK 17 o superior
- **Maven**: 3.6 o superior
- **MySQL**: 8.0 o superior
- **Oracle Database**: 11g o superior (opcional)
- **AWS Account**: Para S3 (opcional)

### 🔧 Configuración de Base de Datos

#### MySQL (Principal)

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/edu_virtual_ufps
spring.datasource.username=tu_usuario
spring.datasource.password=tu_password
spring.jpa.database-platform=org.hibernate.dialect.MySQLDialect
```

#### Oracle (Datos Legados)

```properties
oracle.datasource.url=jdbc:oracle:thin:@localhost:1521:xe
oracle.datasource.username=tu_usuario_oracle
oracle.datasource.password=tu_password_oracle
```

### 🚀 Pasos de Instalación

1. **Clonar el repositorio**

```bash
git clone https://github.com/Unidad-de-Educacion-Virtual/siav-api
cd siav-api
```

2. **Configurar variables de entorno**

```bash
# Crear archivo application-local.properties
cp src/main/resources/application.properties src/main/resources/application-dev.properties
```

3. **Configurar credenciales**

```properties
# Google OAuth2
spring.security.oauth2.client.registration.google.client-id=TU_CLIENT_ID
spring.security.oauth2.client.registration.google.client-secret=TU_CLIENT_SECRET

# AWS S3
cloud.aws.credentials.access-key=TU_ACCESS_KEY
cloud.aws.credentials.secretkey=TU_SECRET_KEY
cloud.aws.region.static=us-east-1
```

4. **Instalar dependencias y ejecutar**

```bash
mvn clean install
mvn spring-boot:run
```

La aplicación estará disponible en: `http://localhost:8080`

## 🔐 Autenticación y Seguridad

### Métodos de Autenticación Soportados

1. **JWT Tokens**: Para autenticación de API
2. **OAuth2 Google**: Login social
3. **Autenticación básica**: Usuario y contraseña

### Endpoints de Autenticación

```http
POST /auth/login           # Login tradicional
GET  /oauth2/authorization/google  # Login con Google
POST /auth/refresh         # Renovar token
POST /auth/logout          # Cerrar sesión
```

### Roles del Sistema

- **ADMIN**: Administrador general
- **COORDINADOR**: Coordinador académico
- **DOCENTE**: Profesor
- **ESTUDIANTE**: Estudiante

## 📚 API Endpoints Principales

### 👨‍🎓 Gestión de Estudiantes

```http
GET    /estudiantes                    # Listar todos
GET    /estudiantes/{id}               # Obtener por ID
POST   /estudiantes                    # Crear estudiante
PUT    /estudiantes/{id}               # Actualizar estudiante
DELETE /estudiantes/{id}               # Eliminar estudiante
```

### 📝 Gestión de Matrículas

```http
GET    /matriculas/estudiante/{id}     # Matrículas por estudiante
POST   /matriculas/crear               # Crear matrícula
DELETE /matriculas/{id}                # Anular matrícula
GET    /matriculas/pensum/estudiante/{id}  # Pensum por estudiante
```

### 📊 Gestión de Notas

```http
GET    /notas/pregrado/{grupoId}       # Notas pregrado
POST   /notas/pregrado                 # Registrar notas pregrado
GET    /notas/posgrado/{matriculaId}   # Notas posgrado
POST   /notas/posgrado                 # Registrar notas posgrado
```

### 🏫 Gestión de Programas

```http
GET    /programas                      # Listar programas
GET    /programas/{id}                 # Programa por ID
POST   /programas                      # Crear programa
PUT    /programas/{id}                 # Actualizar programa
```

### 📑 Gestión de Solicitudes

```http
GET    /solicitudes/estudiante/{id}    # Solicitudes por estudiante
POST   /solicitudes                    # Crear solicitud
PUT    /solicitudes/{id}/aprobar       # Aprobar solicitud
```

## 🗄️ Modelo de Base de Datos

### Entidades Principales

#### Estudiantes

```sql
CREATE TABLE estudiantes (
    id INT PRIMARY KEY AUTO_INCREMENT,
    codigo VARCHAR(20) UNIQUE NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    apellido VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    telefono VARCHAR(15),
    cedula VARCHAR(20),
    fecha_nacimiento DATE,
    fecha_ingreso DATE,
    es_posgrado BOOLEAN,
    pensum_id INT,
    programa_id INT,
    estado_estudiante_id INT,
    usuario_id INT
);
```

#### Matrículas

```sql
CREATE TABLE matriculas (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    estudiante_id INT NOT NULL,
    grupo_cohorte_id BIGINT NOT NULL,
    estado_matricula_id INT NOT NULL,
    fecha_matriculacion DATETIME,
    nueva_matricula BOOLEAN,
    nota DOUBLE,
    fecha_nota DATETIME,
    semestre VARCHAR(10),
    correo_enviado BOOLEAN,
    nota_abierta BOOLEAN
);
```

#### Programas Académicos

```sql
CREATE TABLE programas (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(200) NOT NULL,
    codigo VARCHAR(20) UNIQUE NOT NULL,
    es_posgrado BOOLEAN NOT NULL,
    moodle_id VARCHAR(50) UNIQUE,
    semestre_actual VARCHAR(10),
    tipo_programa_id INT
);
```

## 🔧 Configuración Avanzada

### Perfiles de Ejecución

- **dev**: Desarrollo local con MySQL
- **oracle**: Incluye configuración Oracle
- **prod**: Producción

```bash
# Ejecutar con perfil específico
mvn spring-boot:run -Dspring-boot.run.profiles=dev,oracle
```

### Configuración de CORS

```java
@Bean
public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowedOrigins(Arrays.asList(
        "http://localhost:4200",  // Angular
        "http://localhost:5173"   // React
    ));
    configuration.setAllowedMethods(Arrays.asList(
        "GET", "POST", "PUT", "DELETE", "OPTIONS"
    ));
    return source;
}
```

## 🔄 Integración con Moodle

El sistema incluye servicios para sincronizar datos con Moodle:

- **Creación automática de cursos**
- **Matrícula de estudiantes**
- **Sincronización de notas**
- **Gestión de categorías**

```java
@Service
public class MoodleService {
    public void crearCurso(GrupoCohorte grupoCohorte);
    public void matricularEstudiante(Estudiante estudiante, String moodleCourseId);
    public void sincronizarNotas(Long grupoCohorteId);
}
```

## 📝 Logging y Monitoreo

### Configuración de Logs

```properties
logging.level.com.sistemas_mangager_be=DEBUG
logging.file.name=logs/edu-virtual-ufps.log
logging.pattern.file=%d{yyyy-MM-dd HH:mm:ss} - %msg%n
```

### Métricas Disponibles

- Número de estudiantes activos
- Matrículas por semestre
- Uso de endpoints de API
- Errores de sincronización con Moodle

## 🧪 Testing

### Ejecutar Tests

```bash
# Tests unitarios
mvn test

# Tests de integración
mvn integration-test

# Tests con perfiles específicos
mvn test -Dspring.profiles.active=test
```

### Cobertura de Tests

```bash
mvn jacoco:report
```

## 🚀 Despliegue

### 📋 Requisitos Previos

- **Java:** JDK 17 o superior
- **Maven:** 3.6 o superior  
- **MySQL:** 8.0 o superior (base de datos principal)
- **Oracle Database:** 11g o superior (opcional, para datos legados)
- **AWS Account:** Para servicios S3 (opcional)
- **Google Developer Console:** Para OAuth2 (opcional)
- **Servidor SMTP:** Para envío de correos

### 🔧 Variables de Entorno

El sistema utiliza variables de entorno para la configuración, asegurando la separación entre código y configuración sensible. Crea un archivo `.env` basado en `.env.example`:

#### **📱 Configuración de la Aplicación**

```env
# Configuración básica de Spring Boot
SPRING_APPLICATION_NAME=edu_virtual_ufps
SPRING_PROFILES_ACTIVE=prod
SERVER_PORT=8080
```

- **`SPRING_APPLICATION_NAME`**: Nombre identificador de la aplicación en logs y monitoreo
- **`SPRING_PROFILES_ACTIVE`**: Perfil activo (dev, prod, test). Controla qué configuraciones se cargan
- **`SERVER_PORT`**: Puerto donde se ejecuta la aplicación (por defecto 8080)

#### **🗄️ Base de Datos MySQL (Principal)**

```env
# Configuración de conexión a MySQL
SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3306/edu_virtual_ufps?useUnicode=true&characterEncoding=UTF-8&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=America/Bogota
SPRING_DATASOURCE_USERNAME=app_user
SPRING_DATASOURCE_PASSWORD=MySecurePassword123!
SPRING_JPA_DATABASE_PLATFORM=org.hibernate.dialect.MySQLDialect
SPRING_JPA_HIBERNATE_DDL_AUTO=none
```

- **`SPRING_DATASOURCE_URL`**: URL de conexión a MySQL con configuración de zona horaria colombiana
- **`SPRING_DATASOURCE_USERNAME`**: Usuario de base de datos (evitar usar root en producción)
- **`SPRING_DATASOURCE_PASSWORD`**: Contraseña segura para el usuario de BD
- **`SPRING_JPA_DATABASE_PLATFORM`**: Dialecto específico de MySQL para optimizar consultas
- **`SPRING_JPA_HIBERNATE_DDL_AUTO`**: Estrategia de creación/actualización de esquema (update, create, none)

#### **🐳 MySQL para Docker Compose**

```env
# Variables específicas para contenedor MySQL
MYSQL_ROOT_PASSWORD=RootPassword456!
MYSQL_DATABASE=edu_virtual_ufps
MYSQL_USER=app_user
MYSQL_PASSWORD=MySecurePassword123!
```

- **`MYSQL_ROOT_PASSWORD`**: Contraseña del usuario root de MySQL en el contenedor
- **`MYSQL_DATABASE`**: Nombre de la base de datos que se creará automáticamente
- **`MYSQL_USER`**: Usuario no-root que se creará para la aplicación
- **`MYSQL_PASSWORD`**: Contraseña del usuario de aplicación

#### **🏛️ Base de Datos Oracle (Datos Legados)**

```env
# Configuración para Oracle Database (UFPS)
ORACLE_DATASOURCE_URL=jdbc:oracle:thin:@oracle-server.ufps.edu.co:1521:UFPS
ORACLE_DATASOURCE_USERNAME=ufps_reader
ORACLE_DATASOURCE_PASSWORD=OraclePassword789!
ORACLE_DATASOURCE_DRIVER_CLASS_NAME=oracle.jdbc.OracleDriver
ORACLE_DATASOURCE_HIKARI_CONNECTION_TIMEOUT=60000
ORACLE_DATASOURCE_HIKARI_MAXIMUM_POOL_SIZE=5
ORACLE_DATASOURCE_HIKARI_READ_ONLY=true
```

- **`ORACLE_DATASOURCE_URL`**: Conexión a base de datos Oracle institucional
- **`ORACLE_DATASOURCE_USERNAME`**: Usuario con permisos de solo lectura
- **`ORACLE_DATASOURCE_PASSWORD`**: Contraseña del usuario Oracle
- **`ORACLE_DATASOURCE_DRIVER_CLASS_NAME`**: Driver JDBC para Oracle
- **`ORACLE_DATASOURCE_HIKARI_CONNECTION_TIMEOUT`**: Timeout de conexión (60 segundos)
- **`ORACLE_DATASOURCE_HIKARI_MAXIMUM_POOL_SIZE`**: Máximo de conexiones simultáneas
- **`ORACLE_DATASOURCE_HIKARI_READ_ONLY`**: Configuración de solo lectura para seguridad

#### **🔐 Autenticación Google OAuth2**

```env
# Configuración de Google OAuth2
SPRING_SECURITY_OAUTH2_CLIENT_REGISTRATION_GOOGLE_CLIENT_ID=123456789-abcdefghijklmnop.apps.googleusercontent.com
SPRING_SECURITY_OAUTH2_CLIENT_REGISTRATION_GOOGLE_CLIENT_SECRET=GOCSPX-AbCdEfGhIjKlMnOpQrStUvWxYz
SPRING_SECURITY_OAUTH2_CLIENT_REGISTRATION_GOOGLE_REDIRECT_URI=http://localhost:8080/login/oauth2/code/google
SPRING_SECURITY_OAUTH2_CLIENT_REGISTRATION_GOOGLE_SCOPE=email,profile
APP_FRONTEND_SUCCESS_URL=http://localhost:5173/login/success
APP_FRONTEND_MAIL_URL=https://virtual.ufps.edu.co/cambiar-contrasena?token=
```

- **`GOOGLE_CLIENT_ID`**: ID de cliente OAuth2 obtenido en Google Console
- **`GOOGLE_CLIENT_SECRET`**: Secreto de cliente para autenticación
- **`GOOGLE_REDIRECT_URI`**: URL de retorno después de autenticación exitosa
- **`GOOGLE_SCOPE`**: Permisos solicitados (email y perfil básico)
- **`APP_FRONTEND_SUCCESS_URL`**: URL del frontend para redirección post-login
- **`APP_FRONTEND_MAIL_URL`**: URL base para enlaces de recuperación de contraseña

#### **☁️ Servicios AWS S3**

```env
# Configuración para Amazon S3
CLOUD_AWS_CREDENTIALS_ACCESS_KEY=AKIAIOSFODNN7EXAMPLE
CLOUD_AWS_CREDENTIALS_SECRET_KEY=wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY
CLOUD_AWS_REGION_STATIC=us-east-2
CLOUD_AWS_S3_BUCKET=ufps-academic-documents
```

- **`CLOUD_AWS_CREDENTIALS_ACCESS_KEY`**: Clave de acceso AWS IAM
- **`CLOUD_AWS_CREDENTIALS_SECRET_KEY`**: Clave secreta AWS (manténgase segura)
- **`CLOUD_AWS_REGION_STATIC`**: Región de AWS donde están los recursos
- **`CLOUD_AWS_S3_BUCKET`**: Nombre del bucket S3 para almacenar documentos

#### **🎓 Integración con Moodle**

```env
# API de Moodle UFPS
MOODLE_API_URL=https://virtuallab.ufps.edu.co/webservice/rest/server.php
MOODLE_API_TOKEN=a1b2c3d4e5f6g7h8i9j0k1l2m3n4o5p6
```

- **`MOODLE_API_URL`**: Endpoint de la API REST de Moodle institucional
- **`MOODLE_API_TOKEN`**: Token de acceso con permisos para crear cursos, usuarios y matrículas

#### **📁 Configuración de Archivos**

```env
# Gestión de archivos subidos
SPRING_SERVLET_MULTIPART_MAX_FILE_SIZE=10MB
SPRING_SERVLET_MULTIPART_MAX_REQUEST_SIZE=10MB
SPRING_DESTINATION_FOLDER=src/main/resources/static/
```

- **`MAX_FILE_SIZE`**: Tamaño máximo permitido por archivo individual
- **`MAX_REQUEST_SIZE`**: Tamaño máximo total de la petición HTTP
- **`DESTINATION_FOLDER`**: Carpeta local temporal para procesamiento de archivos

#### **📧 Configuración de Correo Electrónico**

```env
# Servidor SMTP para envío de correos
SPRING_MAIL_HOST=smtp.gmail.com
SPRING_MAIL_PORT=587
SPRING_MAIL_USERNAME=noreply.siav@ufps.edu.co
SPRING_MAIL_PASSWORD=AppPasswordGmail123
SPRING_MAIL_PROPERTIES_MAIL_SMTP_AUTH=true
SPRING_MAIL_PROPERTIES_MAIL_SMTP_STARTTLS_ENABLE=true
SPRING_MAIL_PROPERTIES_MAIL_SMTP_SSL_TRUST=smtp.gmail.com
```

- **`SPRING_MAIL_HOST`**: Servidor SMTP (Gmail, Outlook, servidor institucional)
- **`SPRING_MAIL_PORT`**: Puerto SMTP (587 para STARTTLS, 465 para SSL)
- **`SPRING_MAIL_USERNAME`**: Cuenta de correo remitente
- **`SPRING_MAIL_PASSWORD`**: Contraseña de aplicación (no la contraseña normal)
- **`MAIL_SMTP_AUTH`**: Habilitar autenticación SMTP
- **`MAIL_SMTP_STARTTLS_ENABLE`**: Habilitar encriptación STARTTLS
- **`MAIL_SMTP_SSL_TRUST`**: Servidor de confianza para SSL

#### **🌐 Configuración de Codificación y Errores**

```env
# Configuración de caracteres y errores
SERVER_SERVLET_ENCODING_CHARSET=UTF-8
SERVER_SERVLET_ENCODING_ENABLED=true
SERVER_SERVLET_ENCODING_FORCE=true
SERVER_ERROR_INCLUDE_MESSAGE=always
SPRING_SQL_INIT_MODE=always
```

- **`ENCODING_CHARSET`**: Codificación de caracteres (UTF-8 para soporte completo)
- **`ENCODING_ENABLED`**: Habilitar procesamiento de codificación
- **`ENCODING_FORCE`**: Forzar codificación en requests y responses
- **`ERROR_INCLUDE_MESSAGE`**: Incluir mensajes detallados en errores
- **`SQL_INIT_MODE`**: Ejecutar scripts SQL de inicialización

### 🔨 Pasos de Despliegue

#### **1. Preparación del Entorno**

```bash
# Clonar el repositorio
git clone https://github.com/Unidad-de-Educacion-Virtual/siav-api
cd siav-api

# Crear archivo de variables de entorno
cp .env.example .env
# Editar .env con los valores específicos de tu entorno
```

#### **2. Configuración de Base de Datos**

```sql
-- Crear base de datos MySQL
CREATE DATABASE edu_virtual_ufps CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- Crear usuario específico
CREATE USER 'app_user'@'%' IDENTIFIED BY 'MySecurePassword123!';
GRANT ALL PRIVILEGES ON edu_virtual_ufps.* TO 'app_user'@'%';
FLUSH PRIVILEGES;
```

#### **3. Construcción y Ejecución**

```bash
# Instalar dependencias y compilar
mvn clean install

# Ejecutar con perfil específico
mvn spring-boot:run -Dspring-boot.run.profiles=prod

# O generar JAR para despliegue
mvn clean package -Pprod
java -jar target/edu_virtual_ufps-0.0.1-SNAPSHOT.jar
```

#### **4. Despliegue con Docker**

```bash
# Construir imagen Docker
docker build -t siav-backend .

# Ejecutar con Docker Compose (incluye MySQL)
docker-compose up -d

# Solo el backend (requiere BD externa)
docker run -d \
  --name siav-backend \
  -p 8080:8080 \
  --env-file .env \
  siav-backend
```

#### **5. Verificación del Despliegue**

```bash
# Verificar estado de la aplicación
curl http://localhost:8080/actuator/health

# Verificar conectividad a BD
curl http://localhost:8080/actuator/health/db

# Acceder a documentación Swagger
# http://localhost:8080/swagger-ui/index.html
```

### 🐳 Docker Compose Completo

```yaml
version: '3.8'
services:
  backend:
    build: .
    ports:
      - "8080:8080"
    environment:
      - SPRING_PROFILES_ACTIVE=docker
      - SPRING_DATASOURCE_URL=jdbc:mysql://mysql:3306/edu_virtual_ufps
    depends_on:
      - mysql
    networks:
      - siav-network

  mysql:
    image: mysql:8.0
    environment:
      - MYSQL_ROOT_PASSWORD=${MYSQL_ROOT_PASSWORD}
      - MYSQL_DATABASE=${MYSQL_DATABASE}
      - MYSQL_USER=${MYSQL_USER}
      - MYSQL_PASSWORD=${MYSQL_PASSWORD}
    ports:
      - "3306:3306"
    volumes:
      - mysql_data:/var/lib/mysql
    networks:
      - siav-network

volumes:
  mysql_data:

networks:
  siav-network:
    driver: bridge
```

### 🔒 Consideraciones de Seguridad

#### **Variables Sensibles**

⚠️ **Importante**: Las siguientes variables contienen información sensible y nunca deben ser versionadas:

- Contraseñas de base de datos (`MYSQL_PASSWORD`, `ORACLE_PASSWORD`)
- Tokens de API (`MOODLE_API_TOKEN`, `GOOGLE_CLIENT_SECRET`)
- Credenciales AWS (`AWS_ACCESS_KEY`, `AWS_SECRET_KEY`)
- Contraseñas de correo (`SPRING_MAIL_PASSWORD`)

#### **Mejores Prácticas**

- ✅ Usar gestores de secretos (AWS Secrets Manager, Azure Key Vault)
- ✅ Rotar credenciales periódicamente
- ✅ Implementar principio de menor privilegio
- ✅ Monitorear acceso a variables sensibles
- ✅ Usar HTTPS en todos los endpoints públicos
- ✅ Configurar firewalls para restringir acceso a BD

#### **Perfiles de Entorno**

```bash
# Desarrollo local
SPRING_PROFILES_ACTIVE=dev

# Pruebas automatizadas  
SPRING_PROFILES_ACTIVE=test

# Producción
SPRING_PROFILES_ACTIVE=prod

# Producción con Oracle
SPRING_PROFILES_ACTIVE=prod,oracle
```

## 📖 Documentación de API con Swagger

### 🔍 Acceso a la Documentación Interactiva

El proyecto incluye **Swagger UI** para la documentación interactiva de todos los endpoints de la API. Esta herramienta permite explorar, probar y comprender fácilmente todos los servicios disponibles.

#### **🌐 URLs de Acceso**

##### **Entorno de Desarrollo**
```
http://localhost:8080/swagger-ui/index.html
```

##### **Entorno de Producción**
```
https://tu-dominio-produccion.com/swagger-ui/index.html
```

#### **📋 Funcionalidades Disponibles**

- **📚 Documentación Completa**: Descripción detallada de todos los endpoints
- **🔧 Pruebas Interactivas**: Ejecutar peticiones directamente desde la interfaz
- **📝 Esquemas de Datos**: Visualización de modelos de request/response
- **🔐 Autenticación**: Configuración de tokens JWT para pruebas autenticadas
- **📊 Códigos de Respuesta**: Documentación de todos los códigos HTTP posibles
- **💡 Ejemplos**: Casos de uso con datos de ejemplo

#### **🗂️ Controladores Documentados**

La API está organizada en los siguientes controladores principales:

##### **👥 Gestión de Usuarios**
- **EstudianteController**: CRUD de estudiantes, búsquedas y filtros
- **ProfesorController**: Gestión de docentes y asignaciones
- **AdministradorController**: Operaciones administrativas

##### **🎓 Gestión Académica**
- **ProgramaController**: Programas académicos y pensums
- **MateriaController**: Materias, prerrequisitos y competencias
- **GrupoController**: Grupos, cohortes y horarios
- **MatriculaController**: Proceso de matrícula y cambios

##### **📊 Gestión de Notas**
- **NotasPregradoController**: Calificaciones de pregrado
- **NotasPosgradoController**: Calificaciones de posgrado
- **ReporteNotasController**: Reportes y estadísticas

##### **🔬 Proyectos de Investigación**
- **ProyectoController**: CRUD de proyectos de investigación
- **SeguimientoController**: Fases y actividades del proyecto
- **DocumentoController**: Gestión de documentos del proyecto

##### **🏫 Integración Externa**
- **MoodleController**: Sincronización con Moodle
- **OracleController**: Consultas a base de datos legada
- **S3Controller**: Gestión de archivos en AWS

#### **🔑 Autenticación en Swagger**

Para probar endpoints protegidos:

1. **Obtener Token JWT**:
   ```bash
   curl -X POST http://localhost:8080/auth/login \
     -H "Content-Type: application/json" \
     -d '{"email":"tu-email@ufps.edu.co","password":"tu-password"}'
   ```

2. **Configurar en Swagger**:
   - Hacer clic en el botón **"Authorize"** 🔒
   - Introducir el token en el formato: `Bearer tu-jwt-token-aqui`
   - Hacer clic en **"Authorize"** y luego **"Close"**

3. **Probar Endpoints**: Ahora puedes ejecutar cualquier endpoint protegido

#### **🚀 Ejemplos de Uso**

##### **Consultar Estudiantes**
```http
GET /estudiantes
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

##### **Crear Matrícula**
```http
POST /matriculas/crear
Content-Type: application/json
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...

{
  "estudianteId": 123,
  "grupoCohorteId": 456,
  "semestre": "2024-2"
}
```

##### **Sincronizar con Moodle**
```http
POST /moodle/sincronizar-curso
Content-Type: application/json
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...

{
  "grupoCohorteId": 789,
  "crearUsuarios": true
}
```

#### **📋 Configuración de Swagger**

La configuración de Swagger se encuentra en:

```java
// SwaggerConfig.java
@Configuration
@EnableOpenApi
public class SwaggerConfig {
    
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("SIAV - API Documentation")
                .version("1.0")
                .description("Sistema de Información Académico Virtual - UFPS"))
            .addSecurityItem(new SecurityRequirement().addList("JWT"))
            .components(new Components()
                .addSecuritySchemes("JWT", new SecurityScheme()
                    .name("JWT")
                    .type(SecurityScheme.Type.HTTP)
                    .scheme("bearer")
                    .bearerFormat("JWT")));
    }
}
```

#### **🔧 Personalización**

**Ocultar Endpoints en Producción**:
```properties
# application-prod.properties
springdoc.swagger-ui.enabled=false
springdoc.api-docs.enabled=false
```

**Configurar Grupos de API**:
```properties
# application.properties
springdoc.group-configs[0].group=estudiantes
springdoc.group-configs[0].paths-to-match=/estudiantes/**

springdoc.group-configs[1].group=academico
springdoc.group-configs[1].paths-to-match=/programas/**,/materias/**,/grupos/**
```

#### **💡 Consejos de Uso**

- **🔍 Filtros**: Usa la barra de búsqueda para encontrar endpoints específicos
- **📱 Responsive**: La interfaz funciona perfectamente en dispositivos móviles
- **💾 Exportar**: Descarga la especificación OpenAPI en formato JSON/YAML
- **🔗 Compartir**: Copia URLs específicas de endpoints para compartir con el equipo
- **⚡ Atajos**: Usa `Ctrl+F` para búsqueda rápida en la documentación

#### **🚨 Consideraciones de Seguridad**

⚠️ **Importante para Producción**:

- **Deshabilitar en Producción**: Considerar ocultar Swagger en entornos productivos
- **Autenticación Obligatoria**: Asegurar que todos los endpoints sensibles requieran JWT
- **CORS Configurado**: Verificar que las políticas CORS estén correctamente configuradas
- **Rate Limiting**: Implementar límites de velocidad para prevenir abuso

```properties
# Configuración segura para producción
springdoc.swagger-ui.enabled=false
springdoc.api-docs.enabled=false
management.endpoints.web.exposure.include=health,info
```

## 🤝 Contribución

### Flujo de Trabajo

1. **Fork** el repositorio
2. Crear rama feature: `git checkout -b feature/nueva-funcionalidad`
3. **Commit** cambios: `git commit -m 'Agregar nueva funcionalidad'`
4. **Push** a la rama: `git push origin feature/nueva-funcionalidad`
5. Crear **Pull Request**

### Estándares de Código

- Seguir convenciones de Spring Boot
- Usar Lombok para reducir boilerplate
- Documentar métodos complejos
- Escribir tests para nuevas funcionalidades
- Usar nombres descriptivos para variables y métodos

### Estructura de Commits

```
feat: agregar endpoint para gestión de notas
fix: corregir validación de email en estudiantes
docs: actualizar documentación de API
test: agregar tests para servicio de matrículas
```





<div align="center">

**SIAV** - Sistema de Informacion Academica Virtual 
Universidad Francisco de Paula Santander

[![Spring Boot](https://img.shields.io/badge/Powered%20by-Spring%20Boot-6DB33F?style=flat-square&logo=spring-boot)](https://spring.io/projects/spring-boot)
[![Java](https://img.shields.io/badge/Java-17-ED8B00?style=flat-square&logo=java)](https://www.oracle.com/java/)
[![MySQL](https://img.shields.io/badge/Database-MySQL-4479A1?style=flat-square&logo=mysql)](https://www.mysql.com/)

</div>
