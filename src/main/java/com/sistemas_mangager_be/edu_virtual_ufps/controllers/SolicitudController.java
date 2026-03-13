package com.sistemas_mangager_be.edu_virtual_ufps.controllers;

import com.sistemas_mangager_be.edu_virtual_ufps.exceptions.EstudianteNotFoundException;
import com.sistemas_mangager_be.edu_virtual_ufps.exceptions.SolicitudException;
import com.sistemas_mangager_be.edu_virtual_ufps.services.interfaces.ISolicitudService;
import com.sistemas_mangager_be.edu_virtual_ufps.shared.DTOs.SolicitudDTO;
import com.sistemas_mangager_be.edu_virtual_ufps.shared.responses.HttpResponse;
import com.sistemas_mangager_be.edu_virtual_ufps.shared.responses.SolicitudResponse;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/solicitudes")
@RequiredArgsConstructor
@Tag(name = "Gestión de Solicitudes Académicas", description = "API para la administración integral de solicitudes académicas de SIAV UFPS. "
                +
                "Gestiona cancelaciones de materias, aplazamientos de semestre y reintegros con sus respectivos " +
                "procesos de aprobación")
public class SolicitudController {

        private final ISolicitudService solicitudService;

        @Operation(summary = "Crear solicitud de cancelación de materia", description = "Registra una solicitud para cancelar una materia específica. Valida que el estudiante esté activo, "
                        +
                        "la matrícula pertenezca al estudiante y no exista otra solicitud pendiente para la misma materia.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Solicitud de cancelación creada exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponse.class), examples = @ExampleObject(value = """
                                        {
                                            "httpStatusCode": 200,
                                            "httpStatus": "OK",
                                            "reason": "OK",
                                            "message": "Cancelacion creada con exito"
                                        }
                                        """))),
                        @ApiResponse(responseCode = "400", description = "Solicitud duplicada o estudiante no activo", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponse.class), examples = @ExampleObject(value = """
                                        {
                                            "httpStatusCode": 400,
                                            "httpStatus": "BAD_REQUEST",
                                            "reason": "Bad Request",
                                            "message": "Ya existe una solicitud de cancelación pendiente para la materia Cálculo I"
                                        }
                                        """))),
                        @ApiResponse(responseCode = "404", description = "Estudiante o matrícula no encontrada", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponse.class)))
        })
        @PostMapping("/cancelacion/crear")
        public ResponseEntity<HttpResponse> crearSolicitudCancelacion(
                        @Parameter(description = "Datos de la solicitud de cancelación incluyendo estudiante y matrícula", required = true, schema = @Schema(implementation = SolicitudDTO.class), example = """
                                        {
                                            "estudianteId": 1,
                                            "matriculaId": 15,
                                            "descripcion": "Solicito la cancelación de la materia por motivos de salud que me impiden continuar"
                                        }
                                        """) @RequestBody SolicitudDTO solicitudDTO)
                        throws SolicitudException, EstudianteNotFoundException {

                solicitudService.crearSolicitud(solicitudDTO, 1);
                return new ResponseEntity<>(
                                new HttpResponse(HttpStatus.OK.value(), HttpStatus.OK, HttpStatus.OK.getReasonPhrase(),
                                                "Cancelacion creada con exito"),
                                HttpStatus.OK);
        }

        @Operation(summary = "Crear solicitud de aplazamiento de semestre", description = "Registra una solicitud para aplazar el semestre actual. Valida que no sea primer semestre, "
                        +
                        "el estudiante esté activo y no tenga solicitudes pendientes del mismo tipo.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Solicitud de aplazamiento creada exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponse.class), examples = @ExampleObject(value = """
                                        {
                                            "httpStatusCode": 200,
                                            "httpStatus": "OK",
                                            "reason": "OK",
                                            "message": "Solicitud de aplazamiento creada con exito"
                                        }
                                        """))),
                        @ApiResponse(responseCode = "400", description = "Primer semestre o solicitud duplicada", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponse.class), examples = @ExampleObject(value = """
                                        {
                                            "httpStatusCode": 400,
                                            "httpStatus": "BAD_REQUEST",
                                            "reason": "Bad Request",
                                            "message": "No se puede solicitar aplazamiento en el primer semestre"
                                        }
                                        """))),
                        @ApiResponse(responseCode = "404", description = "Estudiante no encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponse.class)))
        })
        @PostMapping("/aplazamiento/crear")
        public ResponseEntity<HttpResponse> crearSolicitudAplazamiento(
                        @Parameter(description = "Datos de la solicitud de aplazamiento de semestre", required = true, schema = @Schema(implementation = SolicitudDTO.class), example = """
                                        {
                                            "estudianteId": 1,
                                            "descripcion": "Solicito aplazamiento del semestre por motivos laborales que requieren dedicación completa"
                                        }
                                        """) @RequestBody SolicitudDTO solicitudDTO)
                        throws SolicitudException, EstudianteNotFoundException {

                solicitudService.crearSolicitud(solicitudDTO, 2);
                return new ResponseEntity<>(
                                new HttpResponse(HttpStatus.OK.value(), HttpStatus.OK, HttpStatus.OK.getReasonPhrase(),
                                                "Solicitud de aplazamiento creada con exito"),
                                HttpStatus.OK);
        }

        @Operation(summary = "Crear solicitud de reintegro", description = "Registra una solicitud de reintegro al programa académico. Valida que el estudiante esté inactivo "
                        +
                        "y tenga al menos un aplazamiento aprobado previo. Vincula automáticamente con el último aplazamiento.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Solicitud de reintegro creada exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponse.class), examples = @ExampleObject(value = """
                                        {
                                            "httpStatusCode": 200,
                                            "httpStatus": "OK",
                                            "reason": "OK",
                                            "message": "Solicitud de reintegro creada con exito"
                                        }
                                        """))),
                        @ApiResponse(responseCode = "400", description = "Estudiante activo o sin aplazamiento previo", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponse.class), examples = @ExampleObject(value = """
                                        {
                                            "httpStatusCode": 400,
                                            "httpStatus": "BAD_REQUEST",
                                            "reason": "Bad Request",
                                            "message": "Solo estudiantes inactivos pueden solicitar reintegro"
                                        }
                                        """))),
                        @ApiResponse(responseCode = "404", description = "Estudiante no encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponse.class)))
        })
        @PostMapping("/reintegro/crear")
        public ResponseEntity<HttpResponse> crearSolicitudReintegro(
                        @Parameter(description = "Datos de la solicitud de reintegro al programa", required = true, schema = @Schema(implementation = SolicitudDTO.class), example = """
                                        {
                                            "estudianteId": 1,
                                            "descripcion": "Solicito reintegro al programa académico tras superar las circunstancias que motivaron el aplazamiento"
                                        }
                                        """) @RequestBody SolicitudDTO solicitudDTO)
                        throws SolicitudException, EstudianteNotFoundException {

                solicitudService.crearSolicitud(solicitudDTO, 3);
                return new ResponseEntity<>(
                                new HttpResponse(HttpStatus.OK.value(), HttpStatus.OK, HttpStatus.OK.getReasonPhrase(),
                                                "Solicitud de reintegro creada con exito"),
                                HttpStatus.OK);
        }

        @Operation(summary = "Actualizar solicitud de cancelación", description = "Modifica datos de una solicitud de cancelación existente. Permite cambiar descripción, "
                        +
                        "estudiante y matrícula con las mismas validaciones de creación.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Solicitud de cancelación actualizada exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponse.class), examples = @ExampleObject(value = """
                                        {
                                            "httpStatusCode": 200,
                                            "httpStatus": "OK",
                                            "reason": "OK",
                                            "message": "Solicitud de cancelación actualizada con exito"
                                        }
                                        """))),
                        @ApiResponse(responseCode = "400", description = "Datos inválidos o cambio de tipo no permitido", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponse.class))),
                        @ApiResponse(responseCode = "404", description = "Solicitud no encontrada", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponse.class)))
        })
        @PutMapping("/cancelacion/actualizar/{id}")
        public ResponseEntity<HttpResponse> actualizarSolicitudCancelacion(
                        @Parameter(description = "ID único de la solicitud de cancelación", required = true, example = "1", schema = @Schema(type = "integer", format = "int64", minimum = "1")) @PathVariable Long id,

                        @Parameter(description = "Datos actualizados de la solicitud de cancelación", required = true, schema = @Schema(implementation = SolicitudDTO.class), example = """
                                        {
                                            "estudianteId": 1,
                                            "matriculaId": 15,
                                            "descripcion": "Descripción actualizada: Cancelación por circunstancias familiares imprevistas"
                                        }
                                        """) @RequestBody SolicitudDTO solicitudDTO)
                        throws SolicitudException, EstudianteNotFoundException {

                solicitudService.actualizarSolicitud(id, 1, solicitudDTO);
                return new ResponseEntity<>(
                                new HttpResponse(HttpStatus.OK.value(), HttpStatus.OK, HttpStatus.OK.getReasonPhrase(),
                                                "Solicitud de cancelación actualizada con exito"),
                                HttpStatus.OK);
        }

        @Operation(summary = "Actualizar solicitud de aplazamiento", description = "Modifica datos de una solicitud de aplazamiento existente. Permite cambiar descripción "
                        +
                        "y estudiante manteniendo las validaciones correspondientes.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Solicitud de aplazamiento actualizada exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponse.class), examples = @ExampleObject(value = """
                                        {
                                            "httpStatusCode": 200,
                                            "httpStatus": "OK",
                                            "reason": "OK",
                                            "message": "Solicitud de aplazamiento actualizada con exito"
                                        }
                                        """))),
                        @ApiResponse(responseCode = "400", description = "Datos inválidos o estudiante sin requisitos", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponse.class))),
                        @ApiResponse(responseCode = "404", description = "Solicitud no encontrada", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponse.class)))
        })
        @PutMapping("/aplazamiento/actualizar/{id}")
        public ResponseEntity<HttpResponse> actualizarSolicitudAplazamiento(
                        @Parameter(description = "ID único de la solicitud de aplazamiento", required = true, example = "1", schema = @Schema(type = "integer", format = "int64", minimum = "1")) @PathVariable Long id,

                        @Parameter(description = "Datos actualizados de la solicitud de aplazamiento", required = true, schema = @Schema(implementation = SolicitudDTO.class), example = """
                                        {
                                            "estudianteId": 1,
                                            "descripcion": "Descripción actualizada: Aplazamiento por oportunidad laboral en el exterior"
                                        }
                                        """) @RequestBody SolicitudDTO solicitudDTO)
                        throws SolicitudException, EstudianteNotFoundException {

                solicitudService.actualizarSolicitud(id, 2, solicitudDTO);
                return new ResponseEntity<>(
                                new HttpResponse(HttpStatus.OK.value(), HttpStatus.OK, HttpStatus.OK.getReasonPhrase(),
                                                "Solicitud de aplazamiento actualizada con exito"),
                                HttpStatus.OK);
        }

        @Operation(summary = "Actualizar solicitud de reintegro", description = "Modifica datos de una solicitud de reintegro existente. Permite cambiar descripción "
                        +
                        "y estudiante verificando que mantenga el vínculo con aplazamiento previo.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Solicitud de reintegro actualizada exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponse.class), examples = @ExampleObject(value = """
                                        {
                                            "httpStatusCode": 200,
                                            "httpStatus": "OK",
                                            "reason": "OK",
                                            "message": "Solicitud de reintegro actualizada con exito"
                                        }
                                        """))),
                        @ApiResponse(responseCode = "400", description = "Estudiante sin aplazamiento previo o datos inválidos", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponse.class))),
                        @ApiResponse(responseCode = "404", description = "Solicitud no encontrada", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponse.class)))
        })
        @PutMapping("/reintegro/actualizar/{id}")
        public ResponseEntity<HttpResponse> actualizarSolicitudReintegro(
                        @Parameter(description = "ID único de la solicitud de reintegro", required = true, example = "1", schema = @Schema(type = "integer", format = "int64", minimum = "1")) @PathVariable Long id,

                        @Parameter(description = "Datos actualizados de la solicitud de reintegro", required = true, schema = @Schema(implementation = SolicitudDTO.class), example = """
                                        {
                                            "estudianteId": 1,
                                            "descripcion": "Descripción actualizada: Reintegro tras finalizar compromiso laboral temporal"
                                        }
                                        """) @RequestBody SolicitudDTO solicitudDTO)
                        throws SolicitudException, EstudianteNotFoundException {

                solicitudService.actualizarSolicitud(id, 3, solicitudDTO);
                return new ResponseEntity<>(
                                new HttpResponse(HttpStatus.OK.value(), HttpStatus.OK, HttpStatus.OK.getReasonPhrase(),
                                                "Solicitud de reintegro actualizada con exito"),
                                HttpStatus.OK);
        }

        @Operation(summary = "Consultar solicitud específica", description = "Obtiene información completa de una solicitud incluyendo datos del estudiante, tipo, "
                        +
                        "materia (si aplica), fechas y estado de aprobación.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Información de solicitud obtenida exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SolicitudResponse.class), examples = @ExampleObject(value = """
                                        {
                                            "id": 1,
                                            "estudianteId": 1,
                                            "estudianteNombre": "Juan Carlos Pérez García",
                                            "estudianteCodigo": "20241001",
                                            "tipoSolicitudId": 1,
                                            "tipoSolicitudNombre": "Cancelación de Materia",
                                            "descripcion": "Solicito cancelación por motivos de salud",
                                            "semestre": "2024-I",
                                            "estaAprobado": false,
                                            "materiaNombre": "Cálculo Diferencial",
                                            "materiaCodigo": "MAT101",
                                            "grupoNombre": "Grupo A",
                                            "soporte": null
                                        }
                                        """))),
                        @ApiResponse(responseCode = "404", description = "Solicitud no encontrada", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponse.class)))
        })
        @GetMapping("/solicitud/{id}")
        public ResponseEntity<SolicitudResponse> listarSolicitud(
                        @Parameter(description = "ID único de la solicitud a consultar", required = true, example = "1", schema = @Schema(type = "integer", format = "int64", minimum = "1")) @PathVariable Long id)
                        throws SolicitudException {

                SolicitudResponse solicitudResponse = solicitudService.listarSolicitudPorId(id);
                return new ResponseEntity<>(solicitudResponse, HttpStatus.OK);
        }

        @Operation(summary = "Listar solicitudes de cancelación", description = "Obtiene todas las solicitudes de cancelación de materias registradas en el sistema "
                        +
                        "con información de estudiantes, materias y estados.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Lista de solicitudes de cancelación obtenida exitosamente", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = SolicitudResponse.class))))
        })
        @GetMapping("/cancelacion")
        public ResponseEntity<List<SolicitudResponse>> listarSolicitudesCancelaciones() throws SolicitudException {
                return new ResponseEntity<>(
                                solicitudService.listarSolicitudesPorTipo(1),
                                HttpStatus.OK);
        }

        @Operation(summary = "Listar solicitudes de aplazamiento", description = "Obtiene todas las solicitudes de aplazamiento de semestre registradas en el sistema "
                        +
                        "con información de estudiantes y estados.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Lista de solicitudes de aplazamiento obtenida exitosamente", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = SolicitudResponse.class))))
        })
        @GetMapping("/aplazamiento")
        public ResponseEntity<List<SolicitudResponse>> listarSolicitudesAplazamientos() throws SolicitudException {
                return new ResponseEntity<>(
                                solicitudService.listarSolicitudesPorTipo(2),
                                HttpStatus.OK);
        }

        @Operation(summary = "Listar solicitudes de reintegro", description = "Obtiene todas las solicitudes de reintegro registradas en el sistema incluyendo "
                        +
                        "vínculos con aplazamientos previos y estados.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Lista de solicitudes de reintegro obtenida exitosamente", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = SolicitudResponse.class))))
        })
        @GetMapping("/reintegro")
        public ResponseEntity<List<SolicitudResponse>> listarSolicitudesReintegros() throws SolicitudException {
                return new ResponseEntity<>(
                                solicitudService.listarSolicitudesPorTipo(3),
                                HttpStatus.OK);
        }

        @Operation(summary = "Aprobar solicitud de cancelación con informe", description = "Aprueba una solicitud de cancelación cargando documento de soporte. Cambia estado de matrícula "
                        +
                        "a 'Cancelada', suspende en Moodle y registra cambio de estado con usuario responsable.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Solicitud de cancelación aprobada exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponse.class), examples = @ExampleObject(value = """
                                        {
                                            "httpStatusCode": 200,
                                            "httpStatus": "OK",
                                            "reason": "OK",
                                            "message": "Solicitud de cancelación aprobada con exito"
                                        }
                                        """))),
                        @ApiResponse(responseCode = "400", description = "Solicitud ya aprobada o archivo inválido", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponse.class), examples = @ExampleObject(value = """
                                        {
                                            "httpStatusCode": 400,
                                            "httpStatus": "BAD_REQUEST",
                                            "reason": "Bad Request",
                                            "message": "La solicitud ya está aprobada"
                                        }
                                        """))),
                        @ApiResponse(responseCode = "404", description = "Solicitud no encontrada", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponse.class)))
        })
        @PostMapping("/cancelacion/aprobar/{id}")
        public ResponseEntity<HttpResponse> aprobarCancelacion(
                        @Parameter(description = "ID de la solicitud de cancelación a aprobar", required = true, example = "1", schema = @Schema(type = "integer", format = "int64", minimum = "1")) @PathVariable Long id,

                        @Parameter(description = "Documento de soporte para la aprobación (PDF o DOCX)", required = true, schema = @Schema(type = "string", format = "binary")) @RequestParam("informe") MultipartFile file,

                        @Parameter(description = "Usuario responsable de la aprobación", required = true, example = "admin.academico", schema = @Schema(type = "string")) @RequestHeader(value = "X-Usuario") String usuario)
                        throws SolicitudException, IOException {

                solicitudService.aprobarSolicitud(id, 1, file, usuario);
                return new ResponseEntity<>(
                                new HttpResponse(HttpStatus.OK.value(), HttpStatus.OK, HttpStatus.OK.getReasonPhrase(),
                                                "Solicitud de cancelación aprobada con exito"),
                                HttpStatus.OK);
        }

        @Operation(summary = "Aprobar solicitud de aplazamiento con informe", description = "Aprueba una solicitud de aplazamiento cargando documento de soporte. Cancela todas las matrículas "
                        +
                        "en curso del semestre, suspende en Moodle y cambia estado del estudiante a 'Inactivo'.", parameters = {
                                        @Parameter(name = "id", description = "ID de la solicitud de aplazamiento a aprobar", required = true, example = "1", schema = @Schema(type = "integer", format = "int64", minimum = "1")),
                                        @Parameter(name = "informe", description = "Documento de soporte para la aprobación (PDF o DOCX)", required = true, schema = @Schema(type = "string", format = "binary")),
                                        @Parameter(name = "X-Usuario", description = "Usuario responsable de la aprobación", required = true, example = "admin.academico", schema = @Schema(type = "string"))
                        })
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Solicitud de aplazamiento aprobada exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponse.class), examples = @ExampleObject(value = """
                                        {
                                            "httpStatusCode": 200,
                                            "httpStatus": "OK",
                                            "reason": "OK",
                                            "message": "Solicitud de aplazamiento aprobada con exito"
                                        }
                                        """))),
                        @ApiResponse(responseCode = "400", description = "Solicitud ya aprobada o falta documento", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponse.class), examples = @ExampleObject(value = """
                                        {
                                            "httpStatusCode": 400,
                                            "httpStatus": "BAD_REQUEST",
                                            "reason": "Bad Request",
                                            "message": "Se requiere documento de soporte para aprobar aplazamiento"
                                        }
                                        """))),
                        @ApiResponse(responseCode = "404", description = "Solicitud no encontrada", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponse.class)))
        })
        @PostMapping("/aplazamiento/aprobar/{id}")
        public ResponseEntity<HttpResponse> aprobarAplazamiento(
                        @Parameter(description = "ID de la solicitud de aplazamiento a aprobar", required = true, example = "1", schema = @Schema(type = "integer", format = "int64", minimum = "1")) @PathVariable Long id,

                        @Parameter(description = "Documento de soporte para la aprobación (PDF o DOCX)", required = true, schema = @Schema(type = "string", format = "binary")) @RequestParam("informe") MultipartFile file,

                        @Parameter(description = "Usuario responsable de la aprobación", required = true, example = "admin.academico", schema = @Schema(type = "string")) @RequestHeader(value = "X-Usuario") String usuario)
                        throws SolicitudException, IOException {

                solicitudService.aprobarSolicitud(id, 2, file, usuario);
                return new ResponseEntity<>(
                                new HttpResponse(HttpStatus.OK.value(), HttpStatus.OK, HttpStatus.OK.getReasonPhrase(),
                                                "Solicitud de aplazamiento aprobada con exito"),
                                HttpStatus.OK);
        }

        @Operation(summary = "Aprobar solicitud de reintegro con informe", description = "Aprueba una solicitud de reintegro cargando documento de soporte. "
                        +
                        "Cambia estado del estudiante de 'Inactivo' a 'En curso' habilitando nueva matrícula.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Solicitud de reintegro aprobada exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponse.class), examples = @ExampleObject(value = """
                                        {
                                            "httpStatusCode": 200,
                                            "httpStatus": "OK",
                                            "reason": "OK",
                                            "message": "Solicitud de reintegro aprobada con exito"
                                        }
                                        """))),
                        @ApiResponse(responseCode = "400", description = "Solicitud ya aprobada o falta documento", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponse.class), examples = @ExampleObject(value = """
                                        {
                                            "httpStatusCode": 400,
                                            "httpStatus": "BAD_REQUEST",
                                            "reason": "Bad Request",
                                            "message": "Se requiere documento de soporte para aprobar reintegro"
                                        }
                                        """))),
                        @ApiResponse(responseCode = "404", description = "Solicitud no encontrada", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponse.class)))
        })
        @PostMapping("/reintegro/aprobar/{id}")
        public ResponseEntity<HttpResponse> aprobarReintegro(
                        @Parameter(description = "ID de la solicitud de reintegro a aprobar", required = true, example = "1", schema = @Schema(type = "integer", format = "int64", minimum = "1")) @PathVariable Long id,

                        @Parameter(description = "Documento de soporte para la aprobación (PDF o DOCX)", required = true, schema = @Schema(type = "string", format = "binary")) @RequestParam("informe") MultipartFile file)
                        throws SolicitudException, IOException {

                solicitudService.aprobarSolicitud(id, 3, file, null);
                return new ResponseEntity<>(
                                new HttpResponse(HttpStatus.OK.value(), HttpStatus.OK, HttpStatus.OK.getReasonPhrase(),
                                                "Solicitud de reintegro aprobada con exito"),
                                HttpStatus.OK);
        }
}
