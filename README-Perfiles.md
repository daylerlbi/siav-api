# 📋 Guía de Configuración de Perfiles - Edu Virtual UFPS

## 🎯 Perfiles Disponibles

La aplicación ahora cuenta con múltiples perfiles de configuración para diferentes entornos:

### 📁 Archivos de Configuración

| Archivo | Propósito | Uso |
|---------|-----------|-----|
| `application.properties` | Configuración base común | Siempre se carga |
| `application-dev.properties` | Desarrollo local | Valores fijos para desarrollo |
| `application-docker.properties` | Docker/Producción | Variables de entorno |
| `application-oracle.properties` | Oracle específico | Configuración de Oracle |

## 🚀 Cómo Usar Cada Perfil

### 1. 🏠 Desarrollo Local (Perfil: `dev`)

**Por defecto** - Para desarrollo local con tu base de datos MySQL local.

#### Ejecutar desde IDE:
- **Sin configuración adicional**: La aplicación usará automáticamente el perfil `dev`
- **Puerto**: http://localhost:8080
- **Base de datos**: MySQL local en `localhost:3306`

#### Ejecutar desde terminal:
```powershell
# Opción 1: Usar perfil por defecto (dev)
./mvnw spring-boot:run

# Opción 2: Especificar explícitamente el perfil dev
./mvnw spring-boot:run -Dspring.profiles.active=dev

# Opción 3: Con Maven compilado
java -jar target/edu_virtual_ufps-0.0.1-SNAPSHOT.jar
```

#### Características del perfil DEV:
- ✅ Base de datos MySQL local (`localhost:3306`)
- ✅ Credenciales fijas (usuario: `root`, password: `1234`)
- ✅ Logs detallados para debugging
- ✅ Valores de OAuth2, AWS, Moodle, etc. incluidos
- ✅ Actuator habilitado para monitoreo

### 2. 🐳 Docker (Perfil: `docker`)

Para ejecutar en contenedores Docker con variables de entorno.

#### Ejecutar con Docker:
```powershell
# El perfil se configura automáticamente en docker-compose.yml
docker-compose up --build -d
```

#### Características del perfil DOCKER:
- ✅ Variables de entorno desde archivo `.env`
- ✅ Base de datos MySQL en contenedor (`mysql-db:3306`)
- ✅ Configuración optimizada para producción
- ✅ Logs menos verbosos

### 3. 🔧 Desarrollo con Oracle (Perfil: `dev,oracle`)

Para desarrollo local que también necesita acceso a Oracle.

#### Ejecutar:
```powershell
./mvnw spring-boot:run -Dspring.profiles.active=dev,oracle
```

#### Características:
- ✅ MySQL local + Oracle externo
- ✅ Configuración combinada de ambos perfiles

### 4. 🚀 Docker con Oracle (Perfil: `docker,oracle`)

Para Docker con acceso a Oracle (configuración actual).

#### Configuración en `.env`:
```env
SPRING_PROFILES_ACTIVE=docker,oracle
```

## 🔄 Cambiar Entre Perfiles

### Método 1: Modificar `application.properties`
```properties
# Para desarrollo local
spring.profiles.active=dev

# Para desarrollo local con Oracle
spring.profiles.active=dev,oracle

# Para Docker (se configura automáticamente)
spring.profiles.active=docker,oracle
```

### Método 2: Variable de entorno
```powershell
# Windows PowerShell
$env:SPRING_PROFILES_ACTIVE="dev"
./mvnw spring-boot:run

# O directamente en el comando
./mvnw spring-boot:run -Dspring.profiles.active=dev
```

### Método 3: Desde tu IDE
En tu IDE (IntelliJ, Eclipse, VS Code), configura:
- **Variable de entorno**: `SPRING_PROFILES_ACTIVE=dev`
- **O argumento JVM**: `-Dspring.profiles.active=dev`

## 📊 Resumen de Configuraciones

### Base de Datos

| Perfil | Host | Puerto | Base de Datos | Usuario | Password |
|--------|------|---------|---------------|---------|----------|
| `dev` | localhost | 3306 | edu_virtual_ufps | root | 1234 |
| `docker` | mysql-db | 3306 | edu_virtual_ufps | root | (desde .env) |

### Oracle (si aplica)

| Perfil | Host | Puerto | SID | Usuario | Password |
|--------|------|---------|-----|---------|----------|
| `oracle` | 192.168.200.88 | 1522 | UFPS | USERDPTOSIS | (configurado) |

## 🛠️ Comandos Útiles

### Verificar el perfil activo:
```powershell
# Ver logs al iniciar la aplicación, aparecerá:
# "The following profiles are active: dev"
```

### Verificar configuración:
```powershell
# Endpoint de información (solo en desarrollo)
curl http://localhost:8080/actuator/info

# Endpoint de salud
curl http://localhost:8080/actuator/health
```

### Desarrollo con base de datos específica:
```powershell
# Solo MySQL local
./mvnw spring-boot:run -Dspring.profiles.active=dev

# MySQL local + Oracle
./mvnw spring-boot:run -Dspring.profiles.active=dev,oracle

# Docker completo
docker-compose up --build -d
```

## 🚨 Solución de Problemas

### Problema: La aplicación no encuentra la configuración
**Solución**: Verifica que el perfil esté correctamente configurado:
```powershell
# Verificar en logs al iniciar:
# "The following profiles are active: dev"
```

### Problema: No se conecta a la base de datos
**Solución**: 
- **Perfil `dev`**: Asegúrate de que MySQL esté ejecutándose localmente
- **Perfil `docker`**: Verifica que el contenedor MySQL esté funcionando

### Problema: Variables de entorno no se cargan
**Solución**: 
- En **desarrollo local**: No necesitas variables de entorno, usa perfil `dev`
- En **Docker**: Verifica que el archivo `.env` exista y tenga los valores correctos

## 📝 Recomendaciones

### Para Desarrollo Local:
1. **Usa el perfil `dev`** (por defecto)
2. **No necesitas configurar variables de entorno**
3. **Asegúrate de tener MySQL local funcionando**

### Para Producción/Docker:
1. **Usa el perfil `docker,oracle`** (configurado automáticamente)
2. **Configura correctamente el archivo `.env`**
3. **Ejecuta con `docker-compose up --build -d`**

### Para Testing:
1. **Usa el perfil `dev`** para pruebas locales rápidas
2. **Usa el perfil `docker,oracle`** para pruebas completas

---

**¡Ahora puedes desarrollar localmente sin Docker y desplegar en Docker sin cambios de código! 🎉**
