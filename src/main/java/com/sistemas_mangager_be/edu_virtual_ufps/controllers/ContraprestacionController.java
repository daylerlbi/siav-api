package com.sistemas_mangager_be.edu_virtual_ufps.controllers;

import com.sistemas_mangager_be.edu_virtual_ufps.entities.TipoContraprestacion;
import com.sistemas_mangager_be.edu_virtual_ufps.exceptions.ContraprestacionException;
import com.sistemas_mangager_be.edu_virtual_ufps.exceptions.EstudianteNotFoundException;
import com.sistemas_mangager_be.edu_virtual_ufps.repositories.TipoContraprestacionRepository;
import com.sistemas_mangager_be.edu_virtual_ufps.services.interfaces.IContraprestacionService;
import com.sistemas_mangager_be.edu_virtual_ufps.shared.DTOs.ContraprestacionDTO;
import com.sistemas_mangager_be.edu_virtual_ufps.shared.responses.CertificadoResponse;
import com.sistemas_mangager_be.edu_virtual_ufps.shared.responses.ContraprestacionResponse;
import com.sistemas_mangager_be.edu_virtual_ufps.shared.responses.HttpResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/contraprestaciones")
@RequiredArgsConstructor
@Tag(
    name = "Gestión de Contraprestaciones", 
    description = "API para la administración integral de contraprestaciones académicas de SIAV UFPS. " +
                 "Gestiona el ciclo completo desde creación hasta certificación oficial."
)
public class ContraprestacionController {

    private final IContraprestacionService contraprestacionService;
    private final TipoContraprestacionRepository tipoContraprestacionRepository;

    @Operation(
        summary = "Crear nueva contraprestación",
        description = "Registra una contraprestación para un estudiante validando que no tenga una activa en el mismo semestre. " +
                     "Asocia estudiante, tipo de contraprestación, actividades y fechas."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Contraprestación creada exitosamente",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = HttpResponse.class),
                examples = @ExampleObject(
                    value = """
                    {
                        "httpStatusCode": 200,
                        "httpStatus": "OK",
                        "reason": "OK",
                        "message": "Contraprestacion creada con exito"
                    }
                    """
                )
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Estudiante ya tiene contraprestación activa para el semestre",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = HttpResponse.class),
                examples = @ExampleObject(
                    value = """
                    {
                        "httpStatusCode": 400,
                        "httpStatus": "BAD_REQUEST",
                        "reason": "Bad Request",
                        "message": "El estudiante ya tiene una contraprestación activa para el semestre 2024-I"
                    }
                    """
                )
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Estudiante o tipo de contraprestación no encontrado",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponse.class))
        )
    })
    @PostMapping("/crear")
    public ResponseEntity<HttpResponse> crearContraprestacion(
        @Parameter(
            description = "Información de la contraprestación incluyendo estudiante, tipo, actividades y fechas",
            required = true,
            schema = @Schema(implementation = ContraprestacionDTO.class),
            example = """
            {
                "estudianteId": 1,
                "tipoContraprestacionId": 1,
                "actividades": "Apoyo en laboratorio de sistemas, tutorías a estudiantes de primer semestre",
                "fechaInicio": "2024-02-01",
                "fechaFin": "2024-06-30"
            }
            """
        )
        @RequestBody ContraprestacionDTO contraprestacionDTO) throws ContraprestacionException, EstudianteNotFoundException {
        
        contraprestacionService.crearContraprestacion(contraprestacionDTO);
        return new ResponseEntity<>(
                new HttpResponse(HttpStatus.OK.value(), HttpStatus.OK, HttpStatus.OK.getReasonPhrase(),
                        "Contraprestacion creada con exito"),
                HttpStatus.OK);
    }

    @Operation(
        summary = "Actualizar contraprestación",
        description = "Modifica datos de una contraprestación existente. Valida que no genere duplicados " +
                     "al cambiar estudiante o semestre."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Contraprestación actualizada exitosamente",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = HttpResponse.class),
                examples = @ExampleObject(
                    value = """
                    {
                        "httpStatusCode": 200,
                        "httpStatus": "OK",
                        "reason": "OK",
                        "message": "Contraprestacion actualizada con exito"
                    }
                    """
                )
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Contraprestación no encontrada",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponse.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Conflicto con contraprestación existente o datos inválidos",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponse.class))
        )
    })
    @PutMapping("/actualizar/{idContraprestacion}")
    public ResponseEntity<HttpResponse> actualizarContraprestacion(
        @Parameter(
            description = "Identificador único de la contraprestación",
            required = true,
            example = "1",
            schema = @Schema(type = "integer", minimum = "1")
        )
        @PathVariable Integer idContraprestacion,
        
        @Parameter(
            description = "Datos actualizados de la contraprestación",
            required = true,
            schema = @Schema(implementation = ContraprestacionDTO.class),
            example = """
            {
                "estudianteId": 1,
                "tipoContraprestacionId": 2,
                "actividades": "Apoyo en investigación, análisis de datos estadísticos",
                "fechaInicio": "2024-02-15",
                "fechaFin": "2024-07-15"
            }
            """
        )
        @RequestBody ContraprestacionDTO contraprestacionDTO) throws ContraprestacionException, EstudianteNotFoundException {
        
        contraprestacionService.actualizarContraprestacion(idContraprestacion, contraprestacionDTO);
        return new ResponseEntity<>(
                new HttpResponse(HttpStatus.OK.value(), HttpStatus.OK, HttpStatus.OK.getReasonPhrase(),
                        "Contraprestacion actualizada con exito"),
                HttpStatus.OK);
    }

    @Operation(
        summary = "Consultar contraprestación específica",
        description = "Obtiene información completa de una contraprestación incluyendo datos del estudiante, " +
                     "tipo, actividades, fechas y estado de aprobación."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Información de contraprestación obtenida exitosamente",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ContraprestacionResponse.class),
                examples = @ExampleObject(
                    value = """
                    {
                        "id": 1,
                        "estudianteId": 1,
                        "estudianteNombre": "Juan Carlos Pérez García",
                        "actividades": "Apoyo en laboratorio de sistemas",
                        "fechaCreacion": "2024-01-15T08:00:00",
                        "fechaInicio": "2024-02-01",
                        "fechaFin": "2024-06-30",
                        "tipoContraprestacionId": 1,
                        "tipoContraprestacionNombre": "Monitoria Académica",
                        "porcentajeContraprestacion": "25",
                        "aprobada": true,
                        "semestre": "2024-I"
                    }
                    """
                )
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Contraprestación no encontrada",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponse.class))
        )
    })
    @GetMapping("/{idContraprestacion}")
    public ResponseEntity<ContraprestacionResponse> listarContraprestacion(
        @Parameter(
            description = "ID único de la contraprestación a consultar",
            required = true,
            example = "1",
            schema = @Schema(type = "integer", minimum = "1")
        )
        @PathVariable Integer idContraprestacion) throws ContraprestacionException {
        
        ContraprestacionResponse contraprestacionResponse = contraprestacionService.listarContraprestacion(idContraprestacion);
        return new ResponseEntity<>(contraprestacionResponse, HttpStatus.OK);
    }

    @Operation(
        summary = "Listar todas las contraprestaciones",
        description = "Obtiene el catálogo completo de contraprestaciones registradas en el sistema " +
                     "con información de estudiantes, tipos y estados."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Lista completa de contraprestaciones obtenida exitosamente",
            content = @Content(
                mediaType = "application/json",
                array = @ArraySchema(schema = @Schema(implementation = ContraprestacionResponse.class)),
                examples = @ExampleObject(
                    value = """
                    [
                        {
                            "id": 1,
                            "estudianteId": 1,
                            "estudianteNombre": "Juan Carlos Pérez García",
                            "actividades": "Apoyo en laboratorio de sistemas",
                            "fechaCreacion": "2024-01-15T08:00:00",
                            "fechaInicio": "2024-02-01",
                            "fechaFin": "2024-06-30",
                            "tipoContraprestacionId": 1,
                            "tipoContraprestacionNombre": "Monitoria Académica",
                            "porcentajeContraprestacion": "25",
                            "aprobada": true,
                            "semestre": "2024-I"
                        }
                    ]
                    """
                )
            )
        )
    })
    @GetMapping
    public ResponseEntity<List<ContraprestacionResponse>> listarContraprestaciones() {
        List<ContraprestacionResponse> contraprestaciones = contraprestacionService.listarContraprestaciones();
        return new ResponseEntity<>(contraprestaciones, HttpStatus.OK);
    }

    @Operation(
        summary = "Consultar contraprestaciones por tipo",
        description = "Filtra contraprestaciones por tipo específico (Monitoria Académica, Apoyo Administrativo, " +
                     "Investigación, etc.) para análisis y reportes segmentados."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Lista de contraprestaciones por tipo obtenida exitosamente",
            content = @Content(
                mediaType = "application/json",
                array = @ArraySchema(schema = @Schema(implementation = ContraprestacionResponse.class))
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Tipo de contraprestación no encontrado",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponse.class))
        )
    })
    @GetMapping("/tipo/{tipoContraprestacionId}")
    public ResponseEntity<List<ContraprestacionResponse>> listarContraprestacionesPorTipoContraprestacion(
        @Parameter(
            description = "Identificador del tipo de contraprestación",
            required = true,
            example = "1",
            schema = @Schema(type = "integer", minimum = "1")
        )
        @PathVariable Integer tipoContraprestacionId) throws ContraprestacionException {
        
        List<ContraprestacionResponse> contraprestaciones = contraprestacionService.listarContraprestacionesPorTipoContraprestacion(tipoContraprestacionId);
        return new ResponseEntity<>(contraprestaciones, HttpStatus.OK);
    }

    @Operation(
        summary = "Consultar historial de contraprestaciones por estudiante",
        description = "Obtiene todas las contraprestaciones asociadas a un estudiante específico " +
                     "incluyendo historial completo por semestres."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Historial de contraprestaciones del estudiante obtenido exitosamente",
            content = @Content(
                mediaType = "application/json",
                array = @ArraySchema(schema = @Schema(implementation = ContraprestacionResponse.class))
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Estudiante no encontrado en el sistema",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponse.class))
        )
    })
    @GetMapping("/estudiante/{estudianteId}")
    public ResponseEntity<List<ContraprestacionResponse>> listarContraprestacionesPorEstudiante(
        @Parameter(
            description = "Identificador único del estudiante",
            required = true,
            example = "1",
            schema = @Schema(type = "integer", minimum = "1")
        )
        @PathVariable Integer estudianteId) throws EstudianteNotFoundException {
        
        List<ContraprestacionResponse> contraprestaciones = contraprestacionService.listarContraprestacionesPorEstudiante(estudianteId);
        return new ResponseEntity<>(contraprestaciones, HttpStatus.OK);
    }

    @Operation(
        summary = "Aprobar contraprestación con informe final",
        description = "Aprueba una contraprestación cargando el informe final. Acepta archivos PDF o DOCX. " +
                     "Se almacena en AWS S3 y habilita la generación del certificado oficial."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Contraprestación aprobada exitosamente",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = HttpResponse.class),
                examples = @ExampleObject(
                    value = """
                    {
                        "httpStatusCode": 200,
                        "httpStatus": "OK",
                        "reason": "OK",
                        "message": "Contraprestacion aprobada con exito"
                    }
                    """
                )
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Formato de archivo no válido o contraprestación ya aprobada",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = HttpResponse.class),
                examples = @ExampleObject(
                    value = """
                    {
                        "httpStatusCode": 400,
                        "httpStatus": "BAD_REQUEST",
                        "reason": "Bad Request",
                        "message": "Solo se permiten archivos PDF o DOCX"
                    }
                    """
                )
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Contraprestación no encontrada",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponse.class))
        )
    })
    @PostMapping("/aprobar/{idContraprestacion}")
    public ResponseEntity<HttpResponse> aprobarContraprestacion(
        @Parameter(
            description = "ID de la contraprestación a aprobar",
            required = true,
            example = "1",
            schema = @Schema(type = "integer", minimum = "1")
        )
        @PathVariable Integer idContraprestacion,
        
        @Parameter(
            description = "Archivo del informe final (PDF o DOCX, máximo 10MB)",
            required = true,
            schema = @Schema(
                type = "string", 
                format = "binary",
                description = "Informe final de contraprestación en formato PDF o DOCX"
            )
        )
        @RequestParam("informe") MultipartFile file) throws ContraprestacionException, IOException {
        
        contraprestacionService.aprobarContraprestacion(idContraprestacion, file);
        return new ResponseEntity<>(
                new HttpResponse(HttpStatus.OK.value(), HttpStatus.OK, HttpStatus.OK.getReasonPhrase(),
                        "Contraprestacion aprobada con exito"),
                HttpStatus.OK);
    }

    @Operation(
        summary = "Obtener datos para certificado",
        description = "Consulta información completa necesaria para generar certificado oficial incluyendo " +
                     "datos del estudiante, programa académico, fechas y actividades realizadas."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Información para certificado obtenida exitosamente",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = CertificadoResponse.class),
                examples = @ExampleObject(
                    value = """
                    {
                        "id": 1,
                        "nombreCompleto": "Juan Carlos Pérez García",
                        "cedula": "1234567890",
                        "programa": "Maestría en TIC aplicadas a la Educación",
                        "cohorteNombre": "Cohorte 2024-I",
                        "fechaCreacion": "2024-01-15T08:00:00",
                        "semestre": "2024-I",
                        "actividades": "Apoyo en laboratorio de sistemas",
                        "fechaInicio": "2024-02-01",
                        "fechaFin": "2024-06-30",
                        "aprobada": true,
                        "codigoEstudiante": "20241001"
                    }
                    """
                )
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Contraprestación no encontrada",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponse.class))
        )
    })
    @GetMapping("/certificado/{idContraprestacion}")
    public ResponseEntity<CertificadoResponse> listarInformacionCertificado(
        @Parameter(
            description = "ID de la contraprestación para consultar datos del certificado",
            required = true,
            example = "1",
            schema = @Schema(type = "integer", minimum = "1")
        )
        @PathVariable Integer idContraprestacion) throws ContraprestacionException {
        
        CertificadoResponse certificadoResponse = contraprestacionService.listarInformacionCertificado(idContraprestacion);
        return new ResponseEntity<>(certificadoResponse, HttpStatus.OK);
    }

    @Operation(
        summary = "Generar certificado oficial PDF",
        description = "Genera certificado oficial en formato PDF para contraprestación aprobada. " +
                     "Si existe en AWS S3 lo descarga, si no lo genera, almacena y retorna."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Certificado PDF generado y descargado exitosamente",
            content = @Content(
                mediaType = "application/pdf",
                schema = @Schema(
                    type = "string", 
                    format = "binary",
                    description = "Archivo PDF del certificado oficial"
                )
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Contraprestación no aprobada o sin requisitos",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = HttpResponse.class),
                examples = @ExampleObject(
                    value = """
                    {
                        "httpStatusCode": 400,
                        "httpStatus": "BAD_REQUEST",
                        "reason": "Bad Request",
                        "message": "La contraprestación no ha sido aprobada"
                    }
                    """
                )
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Contraprestación no encontrada",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponse.class))
        )
    })
    @PostMapping("/generar/certificado/{contraprestacionId}")
    public ResponseEntity<byte[]> generarCertificado(
        @Parameter(
            description = "ID de la contraprestación aprobada para generar certificado",
            required = true,
            example = "1",
            schema = @Schema(type = "integer", minimum = "1")
        )
        @PathVariable Integer contraprestacionId) throws ContraprestacionException, IOException {

        byte[] pdfBytes = contraprestacionService.generarCertificado(contraprestacionId);
        CertificadoResponse certificado = contraprestacionService.listarInformacionCertificado(contraprestacionId);
        String nombreArchivo = "certificado_contraprestacion_" + certificado.getCodigoEstudiante() + ".pdf";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + nombreArchivo)
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfBytes);
    }

    @Operation(
        summary = "Consultar tipos de contraprestación disponibles",
        description = "Obtiene catálogo completo de tipos de contraprestación con porcentajes y descripciones " +
                     "para uso en formularios y validaciones."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Catálogo de tipos de contraprestación obtenido exitosamente",
            content = @Content(
                mediaType = "application/json",
                array = @ArraySchema(schema = @Schema(implementation = TipoContraprestacion.class)),
                examples = @ExampleObject(
                    value = """
                    [
                        {
                            "id": 1,
                            "nombre": "Monitoria Académica",
                            "porcentaje": 25,
                            "descripcion": "Apoyo a estudiantes en áreas académicas específicas"
                        },
                        {
                            "id": 2,
                            "nombre": "Apoyo Administrativo",
                            "porcentaje": 20,
                            "descripcion": "Soporte en procesos administrativos universitarios"
                        },
                        {
                            "id": 3,
                            "nombre": "Investigación",
                            "porcentaje": 30,
                            "descripcion": "Participación en proyectos de investigación"
                        }
                    ]
                    """
                )
            )
        )
    })
    @GetMapping("/tipos")
    public ResponseEntity<List<TipoContraprestacion>> listarTiposContraprestacion() {
        List<TipoContraprestacion> tiposContraprestacion = tipoContraprestacionRepository.findAll();
        return new ResponseEntity<>(tiposContraprestacion, HttpStatus.OK);
    }
}
