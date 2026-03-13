# ===================================================================
# Script de Dockerización - Edu Virtual UFPS
# ===================================================================
# Este script automatiza el proceso de construcción y despliegue
# de la aplicación Edu Virtual UFPS usando Docker Compose
# ===================================================================

Write-Host "🐳 Iniciando dockerización de Edu Virtual UFPS..." -ForegroundColor Cyan
Write-Host "=" * 60 -ForegroundColor Gray

# Verificar si Docker está instalado y ejecutándose
Write-Host "📋 Verificando requisitos..." -ForegroundColor Yellow
try {
    $dockerVersion = docker --version
    Write-Host "✅ Docker encontrado: $dockerVersion" -ForegroundColor Green
} catch {
    Write-Host "❌ Error: Docker no está instalado o no está en el PATH" -ForegroundColor Red
    exit 1
}

try {
    $composeVersion = docker-compose --version
    Write-Host "✅ Docker Compose encontrado: $composeVersion" -ForegroundColor Green
} catch {
    Write-Host "❌ Error: Docker Compose no está instalado o no está en el PATH" -ForegroundColor Red
    exit 1
}

# Verificar si Docker está ejecutándose
try {
    docker ps | Out-Null
    Write-Host "✅ Docker está ejecutándose correctamente" -ForegroundColor Green
} catch {
    Write-Host "❌ Error: Docker no está ejecutándose. Inicia Docker Desktop." -ForegroundColor Red
    exit 1
}

Write-Host "`n🔧 Preparando entorno..." -ForegroundColor Yellow

# Verificar si existe el archivo .env
if (-not (Test-Path ".env")) {
    Write-Host "❌ Error: El archivo .env no existe" -ForegroundColor Red
    Write-Host "Por favor, asegúrate de que el archivo .env esté presente" -ForegroundColor Yellow
    exit 1
}
Write-Host "✅ Archivo .env encontrado" -ForegroundColor Green

# Verificar si existe el Dockerfile
if (-not (Test-Path "Dockerfile")) {
    Write-Host "❌ Error: El archivo Dockerfile no existe" -ForegroundColor Red
    exit 1
}
Write-Host "✅ Dockerfile encontrado" -ForegroundColor Green

# Verificar si existe el docker-compose.yml
if (-not (Test-Path "docker-compose.yml")) {
    Write-Host "❌ Error: El archivo docker-compose.yml no existe" -ForegroundColor Red
    exit 1
}
Write-Host "✅ docker-compose.yml encontrado" -ForegroundColor Green

# Limpiar contenedores anteriores si existen
Write-Host "`n🧹 Limpiando contenedores anteriores..." -ForegroundColor Yellow
try {
    docker-compose down -v 2>$null
    Write-Host "✅ Contenedores anteriores eliminados" -ForegroundColor Green
} catch {
    Write-Host "ℹ️  No hay contenedores anteriores para limpiar" -ForegroundColor Blue
}

# Construir y ejecutar los servicios
Write-Host "`n🏗️  Construyendo y ejecutando servicios..." -ForegroundColor Yellow
Write-Host "Esto puede tomar varios minutos la primera vez..." -ForegroundColor Gray

try {
    # Construir las imágenes
    Write-Host "`n📦 Construyendo imágenes..." -ForegroundColor Cyan
    docker-compose build --no-cache
    
    if ($LASTEXITCODE -ne 0) {
        throw "Error en la construcción de imágenes"
    }
    Write-Host "✅ Imágenes construidas exitosamente" -ForegroundColor Green
    
    # Ejecutar los servicios
    Write-Host "`n🚀 Iniciando servicios..." -ForegroundColor Cyan
    docker-compose up -d
    
    if ($LASTEXITCODE -ne 0) {
        throw "Error al iniciar los servicios"
    }
    Write-Host "✅ Servicios iniciados exitosamente" -ForegroundColor Green
    
} catch {
    Write-Host "❌ Error durante la construcción o ejecución: $_" -ForegroundColor Red
    Write-Host "`n📋 Logs de error:" -ForegroundColor Yellow
    docker-compose logs --tail=20
    exit 1
}

# Esperar a que los servicios estén listos
Write-Host "`n⏳ Esperando a que los servicios estén listos..." -ForegroundColor Yellow
Start-Sleep -Seconds 10

# Verificar el estado de los servicios
Write-Host "`n📊 Estado de los servicios:" -ForegroundColor Cyan
docker-compose ps

# Verificar la salud de los servicios
Write-Host "`n🏥 Verificando salud de los servicios..." -ForegroundColor Yellow

$maxRetries = 12
$retryCount = 0
$mysqlHealthy = $false
$appHealthy = $false

while ($retryCount -lt $maxRetries -and (-not $mysqlHealthy -or -not $appHealthy)) {
    $retryCount++
    Write-Host "Intento $retryCount de $maxRetries..." -ForegroundColor Gray
    
    # Verificar MySQL
    if (-not $mysqlHealthy) {
        try {
            $mysqlStatus = docker-compose ps mysql-db --format json | ConvertFrom-Json
            if ($mysqlStatus.Health -eq "healthy") {
                $mysqlHealthy = $true
                Write-Host "✅ MySQL está saludable" -ForegroundColor Green
            }
        } catch {
            # Continuar intentando
        }
    }
    
    # Verificar la aplicación
    if (-not $appHealthy) {
        try {
            $appStatus = docker-compose ps edu-virtual-ufps-api --format json | ConvertFrom-Json
            if ($appStatus.Health -eq "healthy" -or $appStatus.State -eq "running") {
                $appHealthy = $true
                Write-Host "✅ Aplicación está corriendo" -ForegroundColor Green
            }
        } catch {
            # Continuar intentando
        }
    }
    
    if (-not $mysqlHealthy -or -not $appHealthy) {
        Start-Sleep -Seconds 10
    }
}

# Mostrar información final
Write-Host "`n" + "=" * 60 -ForegroundColor Gray
Write-Host "🎉 ¡Dockerización completada!" -ForegroundColor Green
Write-Host "=" * 60 -ForegroundColor Gray

Write-Host "`n📋 Información de acceso:" -ForegroundColor Cyan
Write-Host "🌐 Aplicación: http://localhost:8080" -ForegroundColor White
Write-Host "🗄️  Base de datos: Accesible solo internamente (puerto 3306)" -ForegroundColor White

Write-Host "`n📝 Comandos útiles:" -ForegroundColor Cyan
Write-Host "Ver logs de la aplicación:" -ForegroundColor White
Write-Host "  docker-compose logs -f edu-virtual-ufps-api" -ForegroundColor Gray
Write-Host "`nVer logs de MySQL:" -ForegroundColor White
Write-Host "  docker-compose logs -f mysql-db" -ForegroundColor Gray
Write-Host "`nParar los servicios:" -ForegroundColor White
Write-Host "  docker-compose down" -ForegroundColor Gray
Write-Host "`nVer estado de los servicios:" -ForegroundColor White
Write-Host "  docker-compose ps" -ForegroundColor Gray

if (-not $mysqlHealthy -or -not $appHealthy) {
    Write-Host "`n⚠️  Advertencia: Algunos servicios pueden estar iniciándose aún." -ForegroundColor Yellow
    Write-Host "   Verifica los logs si la aplicación no responde:" -ForegroundColor Yellow
    Write-Host "   docker-compose logs -f" -ForegroundColor Gray
}

Write-Host "`n📖 Consulta README-Docker.md para más información" -ForegroundColor Blue
Write-Host "=" * 60 -ForegroundColor Gray
