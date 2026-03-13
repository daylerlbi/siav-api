package com.sistemas_mangager_be.edu_virtual_ufps.controllers;

import com.sistemas_mangager_be.edu_virtual_ufps.exceptions.EstudianteNotFoundException;
import com.sistemas_mangager_be.edu_virtual_ufps.exceptions.GrupoNotFoundException;
import com.sistemas_mangager_be.edu_virtual_ufps.exceptions.MateriaNotFoundException;
import com.sistemas_mangager_be.edu_virtual_ufps.exceptions.MatriculaException;
import com.sistemas_mangager_be.edu_virtual_ufps.services.interfaces.IMatriculaService;
import com.sistemas_mangager_be.edu_virtual_ufps.shared.DTOs.MateriaDTO;
import com.sistemas_mangager_be.edu_virtual_ufps.shared.DTOs.MatriculaDTO;
import com.sistemas_mangager_be.edu_virtual_ufps.shared.responses.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/matriculas")
@RequiredArgsConstructor
@Tag(name = "Gestión de Matrículas Académicas", description = "API para la administración integral de matrículas de SIAV UFPS. "
                +
                "Gestiona el proceso completo de matrícula, validaciones académicas, integración con Moodle, " +
                "seguimiento de estados, notificaciones por correo y consulta del progreso académico estudiantil.")
public class MatriculaController {

        private final IMatriculaService matriculaService;

        @Operation(summary = "Registrar nueva matrícula académica", description = "Crea una matrícula de estudiante en un grupo-cohorte específico con validaciones completas. "
                        +
                        "Verifica que el estudiante no tenga matrícula activa/aprobada en la misma materia, " +
                        "registra automáticamente en Moodle (si ambos tienen ID) y establece estado 'En curso'. " +
                        "Genera automáticamente el cambio de estado con trazabilidad de usuario responsable.", security = @SecurityRequirement(name = "Usuario-Header"))
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Matrícula registrada exitosamente con integración Moodle completada", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponse.class), examples = @ExampleObject(value = """
                                        {
                                            "httpStatusCode": 200,
                                            "httpStatus": "OK",
                                            "reason": "OK",
                                            "message": "Matricula registrada con exito"
                                        }
                                        """))),
                        @ApiResponse(responseCode = "400", description = "Matrícula duplicada o grupo sin materia asignada", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponse.class), examples = @ExampleObject(value = """
                                        {
                                            "httpStatusCode": 400,
                                            "httpStatus": "BAD_REQUEST",
                                            "reason": "Bad Request",
                                            "message": "El estudiante ya tiene esta materia Metodología de la Investigación (Estado: En curso)"
                                        }
                                        """))),
                        @ApiResponse(responseCode = "404", description = "Estudiante, grupo-cohorte o estado de matrícula no encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponse.class)))
        })
        @PostMapping("/crear")
        public ResponseEntity<HttpResponse> crearMatricula(
                        @Parameter(description = "Datos de la matrícula incluyendo estudiante y grupo-cohorte", required = true, schema = @Schema(implementation = MatriculaDTO.class), example = """
                                        {
                                            "estudianteId": 1,
                                            "grupoCohorteId": 1,
                                            "nuevaMatricula": true,
                                            "fechaMatriculacion": "2024-01-15T08:00:00"
                                        }
                                        """) @RequestBody MatriculaDTO matriculaDTO,

                        @Parameter(description = "Usuario responsable del registro de matrícula para trazabilidad", required = true, example = "admin.academico", schema = @Schema(type = "string")) @RequestHeader(value = "X-Usuario") String usuario)
                        throws MatriculaException, EstudianteNotFoundException, GrupoNotFoundException {

                matriculaService.crearMatricula(matriculaDTO, usuario);
                return new ResponseEntity<>(
                                new HttpResponse(HttpStatus.OK.value(), HttpStatus.OK, HttpStatus.OK.getReasonPhrase(),
                                                " Matricula registrada con exito"),
                                HttpStatus.OK);
        }

        @Operation(summary = "Anular matrícula académica", description = "Cancela una matrícula activa cambiando su estado a 'Anulada'. "
                        +
                        "Valida que la matrícula esté en estado 'En curso', desmatricula automáticamente " +
                        "en Moodle (si tiene integración) y registra el cambio de estado con trazabilidad.", security = @SecurityRequirement(name = "Usuario-Header"))
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Matrícula anulada exitosamente con desmatrícula en Moodle", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponse.class), examples = @ExampleObject(value = """
                                        {
                                            "httpStatusCode": 200,
                                            "httpStatus": "OK",
                                            "reason": "OK",
                                            "message": "Matricula anulada con exito"
                                        }
                                        """))),
                        @ApiResponse(responseCode = "400", description = "Matrícula no está en estado 'En curso'", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponse.class), examples = @ExampleObject(value = """
                                        {
                                            "httpStatusCode": 400,
                                            "httpStatus": "BAD_REQUEST",
                                            "reason": "Bad Request",
                                            "message": "La matrícula no se encuentra en estado 'En curso'"
                                        }
                                        """))),
                        @ApiResponse(responseCode = "404", description = "Matrícula no encontrada", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponse.class)))
        })
        @DeleteMapping("/{idMatricula}")
        public ResponseEntity<HttpResponse> anularMatricula(
                        @Parameter(description = "ID único de la matrícula a anular", required = true, example = "1", schema = @Schema(type = "integer", format = "int64", minimum = "1")) @PathVariable Long idMatricula,

                        @Parameter(description = "Usuario responsable de la anulación para trazabilidad", required = true, example = "admin.academico", schema = @Schema(type = "string")) @RequestHeader(value = "X-Usuario") String usuario)
                        throws MatriculaException {

                matriculaService.anularMatricula(idMatricula, usuario);
                return new ResponseEntity<>(
                                new HttpResponse(HttpStatus.OK.value(), HttpStatus.OK, HttpStatus.OK.getReasonPhrase(),
                                                " Matricula anulada con exito"),
                                HttpStatus.OK);
        }

        @Operation(summary = "Consultar matrículas activas por estudiante", description = "Obtiene todas las matrículas en estado 'En curso' de un estudiante para el semestre actual. "
                        +
                        "Incluye información completa de materias, grupos, estados, notas y datos de correo.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Lista de matrículas activas obtenida exitosamente", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = MatriculaResponse.class)), examples = @ExampleObject(value = """
                                        [
                                            {
                                                "id": 1,
                                                "estadoMatriculaId": 2,
                                                "estadoMatriculaNombre": "En curso",
                                                "estudianteId": 1,
                                                "estudianteNombre": "Juan Carlos",
                                                "grupoId": 1,
                                                "grupoNombre": "Metodología de la Investigación - Grupo A",
                                                "nombreMateria": "Metodología de la Investigación",
                                                "codigoMateria": "MIN",
                                                "semestreMateria": "1",
                                                "creditos": 3,
                                                "fechaMatriculacion": "2024-01-15T08:00:00",
                                                "nota": null,
                                                "fechaNota": null,
                                                "correoEnviado": false,
                                                "fechaCorreoEnviado": null
                                            }
                                        ]
                                        """))),
                        @ApiResponse(responseCode = "404", description = "Estudiante no encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponse.class)))
        })
        @GetMapping("/estudiante/{estudianteId}")
        public ResponseEntity<List<MatriculaResponse>> listarMatriculasEnCursoPorEstudiante(
                        @Parameter(description = "ID del estudiante para consultar sus matrículas activas", required = true, example = "1", schema = @Schema(type = "integer", minimum = "1")) @PathVariable Integer estudianteId)
                        throws EstudianteNotFoundException {

                List<MatriculaResponse> matriculas = matriculaService
                                .listarMatriculasEnCursoPorEstudiante(estudianteId);
                return new ResponseEntity<>(matriculas, HttpStatus.OK);
        }

        @Operation(summary = "Consultar materias disponibles para matrícula", description = "Obtiene todas las materias del pensum del estudiante que no tienen matrícula activa o aprobada. "
                        +
                        "Filtra automáticamente materias ya cursadas exitosamente o en proceso. " +
                        "Ordenadas por semestre y código ascendente.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Lista de materias disponibles para matrícula obtenida exitosamente", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = MateriaDTO.class)))),
                        @ApiResponse(responseCode = "404", description = "Estudiante no encontrado o sin pensum asignado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponse.class)))
        })
        @GetMapping("/materias/nomatriculadas/{estudianteId}")
        public ResponseEntity<List<MateriaDTO>> listarMateriasNoMatriculadasPorEstudiante(
                        @Parameter(description = "ID del estudiante para consultar materias disponibles", required = true, example = "1", schema = @Schema(type = "integer", minimum = "1")) @PathVariable Integer estudianteId)
                        throws EstudianteNotFoundException {

                List<MateriaDTO> materias = matriculaService.listarMateriasNoMatriculadasPorEstudiante(estudianteId);
                return new ResponseEntity<>(materias, HttpStatus.OK);
        }

        @Operation(summary = "Consultar progreso académico completo del estudiante", description = "Obtiene el progreso curricular del estudiante organizado por semestres del pensum. "
                        +
                        "Muestra estado de cada materia (No matriculada, En curso, Aprobada, Reprobada) " +
                        "con colores distintivos y semestre de aprobación. Información ideal para dashboards académicos.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Progreso académico del estudiante obtenido exitosamente", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = PensumResponse.class)), examples = @ExampleObject(value = """
                                        [
                                            {
                                                "pensumNombre": "Pensum 2024 Maestría TIC",
                                                "semestrePensum": "1",
                                                "materias": [
                                                    {
                                                        "codigo": "MIN",
                                                        "nombre": "Metodología de la Investigación",
                                                        "creditos": 3,
                                                        "semestreAprobado": "2024-I",
                                                        "estadoId": 1,
                                                        "estadoNombre": "Aprobado",
                                                        "colorCard": "#17C96466"
                                                    },
                                                    {
                                                        "codigo": "FTIC",
                                                        "nombre": "Fundamentos de TIC",
                                                        "creditos": 4,
                                                        "semestreAprobado": null,
                                                        "estadoId": 2,
                                                        "estadoNombre": "En curso",
                                                        "colorCard": "#F5A52466"
                                                    }
                                                ]
                                            }
                                        ]
                                        """))),
                        @ApiResponse(responseCode = "404", description = "Estudiante no encontrado o sin pensum asignado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponse.class)))
        })
        @GetMapping("/pensum/estudiante/{estudianteId}")
        public ResponseEntity<List<PensumResponse>> listarPensumPorEstudiante(
                        @Parameter(description = "ID del estudiante para consultar su progreso académico", required = true, example = "1", schema = @Schema(type = "integer", minimum = "1")) @PathVariable Integer estudianteId)
                        throws EstudianteNotFoundException {

                List<PensumResponse> pensums = matriculaService.listarPensumPorEstudiante(estudianteId);
                return new ResponseEntity<>(pensums, HttpStatus.OK);
        }

        @Operation(summary = "Enviar notificación de matrícula por correo electrónico", description = "Genera y envía correo de confirmación de matrícula al estudiante con listado completo "
                        +
                        "de materias matriculadas. Actualiza estado de matrículas a 'Correo enviado' y " +
                        "registra fecha/hora del envío. Solo procesa matrículas en estado 'En curso'.", security = @SecurityRequirement(name = "Usuario-Header"))
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Correo de matrícula enviado exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponse.class), examples = @ExampleObject(value = """
                                        {
                                            "httpStatusCode": 200,
                                            "httpStatus": "OK",
                                            "reason": "OK",
                                            "message": "Correo enviado con exito"
                                        }
                                        """))),
                        @ApiResponse(responseCode = "400", description = "Estudiante sin matrículas en curso para notificar", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponse.class), examples = @ExampleObject(value = """
                                        {
                                            "httpStatusCode": 400,
                                            "httpStatus": "BAD_REQUEST",
                                            "reason": "Bad Request",
                                            "message": "El estudiante no tiene matrículas en curso"
                                        }
                                        """))),
                        @ApiResponse(responseCode = "404", description = "Estudiante no encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponse.class)))
        })
        @PostMapping("/correo/estudiante/{estudianteId}")
        public ResponseEntity<HttpResponse> enviarCorreo(
                        @Parameter(description = "ID del estudiante para enviar notificación de matrícula", required = true, example = "1", schema = @Schema(type = "integer", minimum = "1")) @PathVariable Integer estudianteId,

                        @Parameter(description = "Usuario responsable del envío de correo (opcional, por defecto 'sistema')", required = false, example = "admin.academico", schema = @Schema(type = "string")) @RequestHeader(value = "X-Usuario", defaultValue = "sistema") String usuario)
                        throws EstudianteNotFoundException, MatriculaException {

                matriculaService.enviarCorreo(estudianteId, usuario);
                return new ResponseEntity<>(
                                new HttpResponse(HttpStatus.OK.value(), HttpStatus.OK, HttpStatus.OK.getReasonPhrase(),
                                                " Correo enviado con exito"),
                                HttpStatus.OK);
        }

        @Operation(summary = "Verificar estado de envío de correo de matrícula", description = "Consulta si el estudiante tiene al menos una matrícula en curso con correo de confirmación enviado. "
                        +
                        "Útil para validar notificaciones y evitar envíos duplicados.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Estado de correo consultado exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(type = "boolean"), examples = @ExampleObject(value = "true"))),
                        @ApiResponse(responseCode = "404", description = "Estudiante no encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponse.class)))
        })
        @GetMapping("/correo-enviado/{estudianteId}")
        public ResponseEntity<Boolean> verificarCorreoEnviado(
                        @Parameter(description = "ID del estudiante para verificar estado de correo", required = true, example = "1", schema = @Schema(type = "integer", minimum = "1")) @PathVariable Integer estudianteId)
                        throws EstudianteNotFoundException {

                boolean correoEnviado = matriculaService.verificarCorreoEnviado(estudianteId);
                return new ResponseEntity<>(correoEnviado, HttpStatus.OK);
        }

        @Operation(summary = "Consultar grupos disponibles por materia", description = "Obtiene todos los grupos-cohorte-docente disponibles para una materia específica. "
                        +
                        "Incluye información completa de grupos, cohortes, docentes asignados y detalles " +
                        "de la materia. Útil para el proceso de selección de grupo durante matrícula.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Lista de grupos por materia obtenida exitosamente", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = GrupoCohorteDocenteResponse.class)))),
                        @ApiResponse(responseCode = "404", description = "Materia no encontrada", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponse.class), examples = @ExampleObject(value = """
                                        {
                                            "httpStatusCode": 404,
                                            "httpStatus": "NOT_FOUND",
                                            "reason": "Not Found",
                                            "message": "la materia con el id MIN no fue encontrada"
                                        }
                                        """)))
        })
        @GetMapping("/grupos/materia/{materiaId}")
        public ResponseEntity<List<GrupoCohorteDocenteResponse>> listarGruposPorMateria(
                        @Parameter(description = "Código de la materia para consultar grupos disponibles", required = true, example = "MIN", schema = @Schema(type = "string")) @PathVariable String materiaId)
                        throws MateriaNotFoundException {

                List<GrupoCohorteDocenteResponse> grupoCohorteDocenteResponses = matriculaService
                                .listarGrupoCohorteDocentePorMateria(materiaId);
                return new ResponseEntity<>(grupoCohorteDocenteResponses, HttpStatus.OK);
        }

        @Operation(summary = "Consultar historial de cambios de estado de matrícula", description = "Obtiene el historial completo de cambios de estado de matrículas de un estudiante "
                        +
                        "para el semestre actual. Incluye trazabilidad de usuarios responsables, fechas " +
                        "y detalles de materias/grupos. Información crucial para auditorías académicas.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Historial de cambios de estado obtenido exitosamente", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = CambioEstadoMatriculaResponse.class)), examples = @ExampleObject(value = """
                                        [
                                            {
                                                "id": 1,
                                                "estadoMatriculaId": 2,
                                                "estadoMatriculaNombre": "En curso",
                                                "materiaId": 1,
                                                "materiaNombre": "Metodología de la Investigación",
                                                "materiaCodigo": "MIN",
                                                "grupoId": 1,
                                                "grupoNombre": "Metodología de la Investigación - Grupo A",
                                                "grupoCodigo": "MINA",
                                                "fechaCambioEstadoMatricula": "2024-01-15T08:00:00",
                                                "usuarioCambioEstadoMatricula": "admin.academico"
                                            }
                                        ]
                                        """))),
                        @ApiResponse(responseCode = "400", description = "No se encontraron cambios de estado para el estudiante", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponse.class), examples = @ExampleObject(value = """
                                        {
                                            "httpStatusCode": 400,
                                            "httpStatus": "BAD_REQUEST",
                                            "reason": "Bad Request",
                                            "message": "No se encontraron cambios de estado para el estudiante"
                                        }
                                        """))),
                        @ApiResponse(responseCode = "404", description = "Estudiante no encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponse.class)))
        })
        @GetMapping("/cambio/estudiante/{estudianteId}")
        public ResponseEntity<List<CambioEstadoMatriculaResponse>> listarCambioEstadoMatriculaPorEstudiante(
                        @Parameter(description = "ID del estudiante para consultar historial de cambios de estado", required = true, example = "1", schema = @Schema(type = "integer", minimum = "1")) @PathVariable Integer estudianteId)
                        throws EstudianteNotFoundException, MatriculaException {

                List<CambioEstadoMatriculaResponse> cambios = matriculaService
                                .listarCambiosdeEstadoMatriculaPorEstudiante(estudianteId);
                return new ResponseEntity<>(cambios, HttpStatus.OK);
        }
}
