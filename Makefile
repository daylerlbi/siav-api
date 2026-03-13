# ===================================================================
# Makefile para Edu Virtual UFPS - Comandos Docker
# ===================================================================
# Uso: make <comando>
# Ejemplo: make up, make down, make logs, etc.
# ===================================================================

.PHONY: help up down build restart logs logs-app logs-db clean status shell shell-db backup

# Variables
COMPOSE_FILE = docker-compose.yml
APP_SERVICE = edu-virtual-ufps-api
DB_SERVICE = mysql-db

# Comando por defecto
help: ## Mostrar esta ayuda
	@echo "Comandos disponibles para Edu Virtual UFPS:"
	@echo ""
	@grep -E '^[a-zA-Z_-]+:.*?## .*$$' $(MAKEFILE_LIST) | sort | awk 'BEGIN {FS = ":.*?## "}; {printf "  \033[36m%-15s\033[0m %s\n", $$1, $$2}'

up: ## Construir y ejecutar todos los servicios
	@echo "🚀 Iniciando servicios..."
	docker-compose up --build -d

down: ## Parar y eliminar todos los servicios
	@echo "🛑 Parando servicios..."
	docker-compose down

down-volumes: ## Parar servicios y eliminar volúmenes (¡CUIDADO!)
	@echo "🗑️ Parando servicios y eliminando volúmenes..."
	docker-compose down -v

build: ## Construir las imágenes sin caché
	@echo "🏗️ Construyendo imágenes..."
	docker-compose build --no-cache

restart: ## Reiniciar todos los servicios
	@echo "🔄 Reiniciando servicios..."
	docker-compose restart

restart-app: ## Reiniciar solo la aplicación
	@echo "🔄 Reiniciando aplicación..."
	docker-compose restart $(APP_SERVICE)

restart-db: ## Reiniciar solo la base de datos
	@echo "🔄 Reiniciando base de datos..."
	docker-compose restart $(DB_SERVICE)

logs: ## Ver logs de todos los servicios
	docker-compose logs -f

logs-app: ## Ver logs de la aplicación
	docker-compose logs -f $(APP_SERVICE)

logs-db: ## Ver logs de la base de datos
	docker-compose logs -f $(DB_SERVICE)

status: ## Ver estado de los servicios
	@echo "📊 Estado de los servicios:"
	docker-compose ps

shell: ## Acceder al contenedor de la aplicación
	docker-compose exec $(APP_SERVICE) sh

shell-db: ## Acceder al contenedor de MySQL
	docker-compose exec $(DB_SERVICE) bash

mysql: ## Conectar a MySQL
	docker-compose exec $(DB_SERVICE) mysql -u root -p

clean: ## Limpiar imágenes y contenedores no utilizados
	@echo "🧹 Limpiando Docker..."
	docker system prune -f

clean-all: ## Limpiar todo (imágenes, contenedores, volúmenes)
	@echo "🗑️ Limpiando todo Docker..."
	docker system prune -a -f --volumes

backup: ## Crear backup de la base de datos
	@echo "💾 Creando backup..."
	docker-compose exec $(DB_SERVICE) mysqldump -u root -p edu_virtual_ufps > backup_$(shell date +%Y%m%d_%H%M%S).sql
	@echo "✅ Backup creado: backup_$(shell date +%Y%m%d_%H%M%S).sql"

dev: ## Modo desarrollo (sin rebuild)
	@echo "🔧 Iniciando en modo desarrollo..."
	docker-compose up -d

prod: ## Modo producción (con rebuild y sin caché)
	@echo "🚀 Iniciando en modo producción..."
	docker-compose down
	docker-compose build --no-cache
	docker-compose up -d

health: ## Verificar salud de los servicios
	@echo "🏥 Verificando salud..."
	@echo "=== Estado de contenedores ==="
	docker-compose ps
	@echo ""
	@echo "=== Verificando aplicación ==="
	@curl -f http://localhost:8080/actuator/health 2>/dev/null && echo "✅ Aplicación saludable" || echo "❌ Aplicación no responde"

install: ## Configuración inicial completa
	@echo "⚙️ Configuración inicial..."
	@if [ ! -f .env ]; then \
		echo "📝 Creando archivo .env desde .env.example..."; \
		cp .env.example .env; \
		echo "⚠️ IMPORTANTE: Configura las variables en .env antes de continuar"; \
	else \
		echo "✅ Archivo .env ya existe"; \
	fi
	@echo "🏗️ Construyendo e iniciando servicios..."
	$(MAKE) up
	@echo ""
	@echo "🎉 ¡Instalación completada!"
	@echo "📖 Consulta README-Docker.md para más información"
