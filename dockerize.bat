@echo off
REM ===================================================================
REM Script de Dockerización - Edu Virtual UFPS (Windows Batch)
REM ===================================================================
REM Este script automatiza el proceso de construcción y despliegue
REM de la aplicación Edu Virtual UFPS usando Docker Compose
REM ===================================================================

echo.
echo 🐳 Iniciando dockerización de Edu Virtual UFPS...
echo ============================================================

REM Verificar si Docker está instalado y ejecutándose
echo.
echo 📋 Verificando requisitos...

docker --version >nul 2>&1
if %errorlevel% neq 0 (
    echo ❌ Error: Docker no está instalado o no está en el PATH
    pause
    exit /b 1
)
echo ✅ Docker encontrado

docker-compose --version >nul 2>&1
if %errorlevel% neq 0 (
    echo ❌ Error: Docker Compose no está instalado o no está en el PATH
    pause
    exit /b 1
)
echo ✅ Docker Compose encontrado

REM Verificar si Docker está ejecutándose
docker ps >nul 2>&1
if %errorlevel% neq 0 (
    echo ❌ Error: Docker no está ejecutándose. Inicia Docker Desktop.
    pause
    exit /b 1
)
echo ✅ Docker está ejecutándose correctamente

echo.
echo 🔧 Preparando entorno...

REM Verificar archivos necesarios
if not exist ".env" (
    echo ❌ Error: El archivo .env no existe
    echo Por favor, copia .env.example a .env y configura los valores
    pause
    exit /b 1
)
echo ✅ Archivo .env encontrado

if not exist "Dockerfile" (
    echo ❌ Error: El archivo Dockerfile no existe
    pause
    exit /b 1
)
echo ✅ Dockerfile encontrado

if not exist "docker-compose.yml" (
    echo ❌ Error: El archivo docker-compose.yml no existe
    pause
    exit /b 1
)
echo ✅ docker-compose.yml encontrado

REM Limpiar contenedores anteriores
echo.
echo 🧹 Limpiando contenedores anteriores...
docker-compose down -v >nul 2>&1
echo ✅ Contenedores anteriores eliminados

REM Construir y ejecutar los servicios
echo.
echo 🏗️ Construyendo y ejecutando servicios...
echo Esto puede tomar varios minutos la primera vez...

echo.
echo 📦 Construyendo imágenes...
docker-compose build --no-cache
if %errorlevel% neq 0 (
    echo ❌ Error en la construcción de imágenes
    pause
    exit /b 1
)
echo ✅ Imágenes construidas exitosamente

echo.
echo 🚀 Iniciando servicios...
docker-compose up -d
if %errorlevel% neq 0 (
    echo ❌ Error al iniciar los servicios
    pause
    exit /b 1
)
echo ✅ Servicios iniciados exitosamente

REM Esperar a que los servicios estén listos
echo.
echo ⏳ Esperando a que los servicios estén listos...
timeout /t 10 /nobreak >nul

REM Mostrar estado de los servicios
echo.
echo 📊 Estado de los servicios:
docker-compose ps

echo.
echo ============================================================
echo 🎉 ¡Dockerización completada!
echo ============================================================

echo.
echo 📋 Información de acceso:
echo 🌐 Aplicación: http://localhost:8080
echo 🗄️ Base de datos: Accesible solo internamente (puerto 3306)

echo.
echo 📝 Comandos útiles:
echo Ver logs de la aplicación:
echo   docker-compose logs -f edu-virtual-ufps-api
echo.
echo Ver logs de MySQL:
echo   docker-compose logs -f mysql-db
echo.
echo Parar los servicios:
echo   docker-compose down
echo.
echo Ver estado de los servicios:
echo   docker-compose ps

echo.
echo 📖 Consulta README-Docker.md para más información
echo ============================================================

pause
