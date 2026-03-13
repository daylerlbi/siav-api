# 🐳 Guía de Dockerización - Edu Virtual UFPS

Esta guía te ayudará a ejecutar la aplicación **Edu Virtual UFPS** utilizando Docker y Docker Compose con una configuración de múltiples etapas optimizada.

## 📋 Requisitos Previos

Antes de comenzar, asegúrate de tener instalado:

- **Docker** (versión 20.10 o superior)
- **Docker Compose** (versión 2.0 o superior)
- **Git** (para clonar el repositorio)

### Verificar instalación:
```powershell
docker --version
docker-compose --version
```

## 📁 Estructura del Proyecto

```
edu_virtual_ufps_be/
├── docker-compose.yml          # Configuración de servicios
├── Dockerfile                  # Imagen multi-etapa de la aplicación
├── .env                       # Variables de entorno
├── .dockerignore              # Archivos ignorados en el build
├── README-Docker.md           # Esta guía
├── src/                       # Código fuente de la aplicación
├── pom.xml                    # Configuración de Maven
└── ...
```

## 🚀 Pasos para la Dockerización

### Paso 1: Clonar el Repositorio (si es necesario)
```powershell
git clone https://github.com/tu-usuario/edu_virtual_ufps_be.git
cd edu_virtual_ufps_be
```

### Paso 2: Configurar Variables de Entorno
El archivo `.env` ya está configurado con valores por defecto. **Revisa y ajusta las siguientes variables según tu entorno**:

```env
# Configuración de base de datos
MYSQL_ROOT_PASSWORD=1234
MYSQL_DATABASE=edu_virtual_ufps
MYSQL_USER=app_user
MYSQL_PASSWORD=1234

# Configuración de Oracle (si aplica)
ORACLE_DATASOURCE_URL=jdbc:oracle:thin:@192.168.200.88:1522:UFPS
ORACLE_DATASOURCE_USERNAME=USERDPTOSIS
ORACLE_DATASOURCE_PASSWORD=USERDPTOSIS_1988

# Otros servicios (AWS, Google OAuth, etc.)
# ... configurar según tu entorno
```

### Paso 3: Construir y Ejecutar con Docker Compose
```powershell
# Construir las imágenes y ejecutar todos los servicios
docker-compose up --build -d
```

### Paso 4: Verificar que los Servicios Estén Ejecutándose
```powershell
# Ver el estado de los contenedores
docker-compose ps

# Ver los logs de la aplicación
docker-compose logs -f edu-virtual-ufps-api

# Ver los logs de la base de datos
docker-compose logs -f mysql-db
```

### Paso 5: Acceder a la Aplicación
Una vez que los servicios estén ejecutándose:

- **Aplicación**: http://localhost:8080
- **Base de datos MySQL**: Accesible solo desde la red interna de Docker (puerto 3306)

## 🔧 Comandos Útiles

### Gestión de Contenedores
```powershell
# Parar todos los servicios
docker-compose down

# Parar y eliminar volúmenes (CUIDADO: elimina datos de la BD)
docker-compose down -v

# Reiniciar un servicio específico
docker-compose restart edu-virtual-ufps-api

# Ver logs en tiempo real
docker-compose logs -f
```

### Debugging y Mantenimiento
```powershell
# Acceder al contenedor de la aplicación
docker-compose exec edu-virtual-ufps-api sh

# Acceder al contenedor de MySQL
docker-compose exec mysql-db bash

# Ejecutar comando en MySQL
docker-compose exec mysql-db mysql -u root -p1234 edu_virtual_ufps

# Ver uso de recursos
docker stats
```

### Gestión de Imágenes
```powershell
# Reconstruir solo la aplicación
docker-compose build edu-virtual-ufps-api

# Forzar reconstrucción sin caché
docker-compose build --no-cache

# Limpiar imágenes no utilizadas
docker image prune
```

## 🏗️ Características de la Dockerización

### Dockerfile Multi-Etapa
- **Etapa 1 (BUILD)**: Compilación con Maven en imagen ligera
- **Etapa 2 (DEPLOY)**: Ejecución con JRE optimizado
- **Optimizaciones**: Usuario no-root, caché de dependencias, imagen ligera

### Docker Compose
- **Red aislada**: Comunicación segura entre servicios
- **Volúmenes persistentes**: Datos de MySQL preservados
- **Health checks**: Verificación automática de servicios
- **Variables de entorno**: Configuración externalizada

### Seguridad
- ✅ Usuario no-root en contenedores
- ✅ Red interna para base de datos
- ✅ Variables de entorno para secretos
- ✅ Health checks para monitoreo

## 🛠️ Solución de Problemas

### La aplicación no inicia
```powershell
# Verificar logs detallados
docker-compose logs edu-virtual-ufps-api

# Verificar conectividad con MySQL
docker-compose exec edu-virtual-ufps-api ping mysql-db
```

### Base de datos no se conecta
```powershell
# Verificar salud de MySQL
docker-compose ps

# Verificar logs de MySQL
docker-compose logs mysql-db

# Verificar variables de entorno
docker-compose exec mysql-db env | grep MYSQL
```

### Puerto ocupado
```powershell
# Cambiar puerto en .env
SERVER_PORT=8081

# O usar puerto alternativo
docker-compose up --build -d
```

### Problemas de permisos
```powershell
# En Windows, ejecutar PowerShell como administrador
# Verificar que Docker esté ejecutándose

# Limpiar y reconstruir
docker-compose down -v
docker system prune -f
docker-compose up --build -d
```

## 📊 Monitoreo

### Health Checks
Los servicios incluyen verificaciones de salud automáticas:
- **MySQL**: Ping cada 30 segundos
- **Aplicación**: Endpoint `/actuator/health` cada 30 segundos

### Logs Estructurados
```powershell
# Logs con timestamp
docker-compose logs -f -t

# Logs solo de errores
docker-compose logs | findstr ERROR

# Logs de las últimas 100 líneas
docker-compose logs --tail=100
```

## 🔄 Actualización y Despliegue

### Actualizar la aplicación
```powershell
# 1. Obtener últimos cambios
git pull origin main

# 2. Reconstruir y desplegar
docker-compose down
docker-compose up --build -d

# 3. Verificar despliegue
docker-compose ps
docker-compose logs -f edu-virtual-ufps-api
```

### Backup de Base de Datos
```powershell
# Crear backup
docker-compose exec mysql-db mysqldump -u root -p1234 edu_virtual_ufps > backup.sql

# Restaurar backup
docker-compose exec -T mysql-db mysql -u root -p1234 edu_virtual_ufps < backup.sql
```

## 📝 Notas Importantes

1. **Variables de Entorno**: Revisa el archivo `.env` antes del primer despliegue
2. **Datos Persistentes**: Los datos de MySQL se mantienen en volúmenes de Docker
3. **Red Interna**: MySQL no está expuesto al host por seguridad
4. **Oracle**: La conexión a Oracle se realiza externamente (no dockerizada)
5. **Health Checks**: Los servicios tienen verificaciones automáticas de salud

## 🆘 Soporte

Si encuentras problemas:

1. Revisa los logs: `docker-compose logs -f`
2. Verifica la configuración en `.env`
3. Asegúrate de que los puertos no estén ocupados
4. Consulta la documentación oficial de Docker

---

**¡Tu aplicación Edu Virtual UFPS está lista para ejecutarse con Docker! 🎉**
