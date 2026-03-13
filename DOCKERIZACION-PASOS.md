# 🚀 GUÍA PASO A PASO - DOCKERIZACIÓN EDU VIRTUAL UFPS

## 📋 Resumen de lo Configurado

Se ha configurado una dockerización completa con las siguientes características:

### ✅ Archivos Creados/Modificados:
- **Dockerfile** - Multi-etapa (BUILD + DEPLOY)
- **docker-compose.yml** - Servicios con MySQL + App
- **.env** - Variables de entorno configuradas
- **.env.example** - Plantilla para nuevos entornos
- **.dockerignore** - Optimización del build
- **README-Docker.md** - Documentación completa
- **dockerize.ps1** - Script PowerShell automatizado
- **dockerize.bat** - Script Batch para Windows
- **Makefile** - Comandos rápidos
- **application.properties** - Configurado con variables de entorno
- **application-oracle.properties** - Configurado con variables de entorno

## 🎯 PASOS PARA EJECUTAR LA DOCKERIZACIÓN

### Paso 1: Verificar Requisitos
```powershell
# Verificar que Docker esté instalado y ejecutándose
docker --version
docker-compose --version
docker ps
```

### Paso 2: Configurar Variables de Entorno
```powershell
# Si no tienes el archivo .env, copia el ejemplo
Copy-Item .env.example .env

# Edita el archivo .env con tus valores específicos
notepad .env
```

**⚠️ IMPORTANTE**: Configura estos valores en el archivo `.env`:
- Passwords de MySQL
- Credenciales de Oracle
- Claves de Google OAuth2
- Credenciales de AWS S3
- Token de Moodle
- Configuración de email

### Paso 3: Ejecutar Dockerización (3 Opciones)

#### Opción A: Script PowerShell (Recomendado)
```powershell
# Ejecutar script automatizado
.\dockerize.ps1
```

#### Opción B: Script Batch
```cmd
# Ejecutar script batch
dockerize.bat
```

#### Opción C: Comandos Manuales
```powershell
# Limpiar contenedores anteriores
docker-compose down -v

# Construir e iniciar servicios
docker-compose up --build -d

# Verificar estado
docker-compose ps
```

#### Opción D: Usando Makefile (Si tienes make instalado)
```powershell
# Configuración inicial completa
make install

# O solo iniciar servicios
make up
```

### Paso 4: Verificar el Despliegue

```powershell
# Ver estado de los servicios
docker-compose ps

# Ver logs de la aplicación
docker-compose logs -f edu-virtual-ufps-api

# Ver logs de MySQL
docker-compose logs -f mysql-db

# Verificar salud (después de 1-2 minutos)
curl http://localhost:8080/actuator/health
```

### Paso 5: Acceder a la Aplicación

- **URL de la aplicación**: http://localhost:8080
- **Base de datos MySQL**: Solo accesible internamente (puerto 3306)
- **Datos persistentes**: Almacenados en volúmenes de Docker

## 🛠️ COMANDOS ÚTILES POST-INSTALACIÓN

### Gestión de Servicios
```powershell
# Parar servicios
docker-compose down

# Reiniciar aplicación
docker-compose restart edu-virtual-ufps-api

# Reiniciar MySQL
docker-compose restart mysql-db

# Ver logs en tiempo real
docker-compose logs -f
```

### Debugging
```powershell
# Acceder al contenedor de la aplicación
docker-compose exec edu-virtual-ufps-api sh

# Acceder a MySQL
docker-compose exec mysql-db mysql -u root -p

# Ver uso de recursos
docker stats
```

### Mantenimiento
```powershell
# Crear backup de BD
docker-compose exec mysql-db mysqldump -u root -p1234 edu_virtual_ufps > backup.sql

# Limpiar imágenes no utilizadas
docker image prune

# Reconstruir sin caché
docker-compose build --no-cache
```

## 🔧 CARACTERÍSTICAS DE LA CONFIGURACIÓN

### Dockerfile Multi-Etapa
- **Etapa BUILD**: Maven 3.8.5 + OpenJDK 17
- **Etapa DEPLOY**: Eclipse Temurin 17 JRE Alpine
- **Optimizaciones**: Caché de dependencias, usuario no-root, imagen ligera

### Docker Compose
- **Red interna**: `edu-virtual-ufps-network`
- **Volúmenes persistentes**: Datos MySQL preservados
- **Health checks**: Verificación automática de servicios
- **Variables de entorno**: Configuración externalizada

### Seguridad
- ✅ Usuario no-root en contenedores
- ✅ Base de datos no expuesta al host
- ✅ Variables sensibles en .env
- ✅ Health checks para monitoreo

## 🚨 SOLUCIÓN DE PROBLEMAS COMUNES

### Error: Puerto ocupado
```powershell
# Cambiar puerto en .env
SERVER_PORT=8081
```

### Error: Docker no responde
```powershell
# Reiniciar Docker Desktop
# Verificar que esté ejecutándose
docker ps
```

### Error: Permisos en Windows
```powershell
# Ejecutar PowerShell como administrador
# Asegurar que Docker Desktop tenga permisos
```

### Error: Variables de entorno
```powershell
# Verificar que .env existe y tiene los valores correctos
cat .env
```

### Error: Base de datos no se conecta
```powershell
# Verificar logs de MySQL
docker-compose logs mysql-db

# Verificar health check
docker-compose ps
```

## 📊 VERIFICACIÓN FINAL

Una vez completados los pasos, deberías ver:

1. **Servicios corriendo**:
   ```
   NAME                     STATUS              PORTS
   edu-virtual-ufps-api     Up (healthy)        0.0.0.0:8080->8080/tcp
   edu-virtual-ufps-mysql   Up (healthy)        3306/tcp
   ```

2. **Aplicación accesible** en http://localhost:8080

3. **Logs sin errores** críticos

4. **Health checks** pasando

## 📞 SOPORTE

Si encuentras problemas:
1. Revisa los logs: `docker-compose logs -f`
2. Verifica la configuración en `.env`
3. Consulta `README-Docker.md`
4. Asegúrate de que Docker Desktop esté ejecutándose

---

**¡Tu aplicación Edu Virtual UFPS está lista para funcionar con Docker! 🎉**
