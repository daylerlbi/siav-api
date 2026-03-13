package com.sistemas_mangager_be.edu_virtual_ufps.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info = @Info(
                title = "Sistema de Información Academico Virtual - API",
                description = "API REST para el Sistema de Información Academico Virtual para la maestria en TIC " +
                        "aplicadas a la Educación y la Tecnologia en Analitica de Datos de la Universidad Francisco de Paula Santander. " +
                        "Esta API proporciona endpoints para la gestión de estudiantes, docentes, cursos, seguimiento académico y " +
                        "administración del sistema educativo virtual.",
                version = "1.0.0",
                contact = @Contact(
                        name = "Unidad Virtual UFPS",
                        url = "https://educaiton.cloud.ufps.edu.co",
                        email = "uvirtual@ufps.edu.co"
                ),
                license = @License(
                        name = "Licencia Educativa UFPS",
                        url = "https://ww2.ufps.edu.co"
                )
        ),
        servers = {
                @Server(description = "Servidor de Desarrollo", url = "http://localhost:8090"),
                @Server(description = "Servidor de Producción", url = "https://sentinel.applab.ufps.edu.co")
        }
)
public class SwaggerConfig {
}
