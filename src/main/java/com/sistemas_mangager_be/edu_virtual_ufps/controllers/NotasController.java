package com.sistemas_mangager_be.edu_virtual_ufps.controllers;

import com.sistemas_mangager_be.edu_virtual_ufps.exceptions.GrupoNotFoundException;
import com.sistemas_mangager_be.edu_virtual_ufps.exceptions.MatriculaException;
import com.sistemas_mangager_be.edu_virtual_ufps.exceptions.NotasException;
import com.sistemas_mangager_be.edu_virtual_ufps.services.interfaces.INotaService;
import com.sistemas_mangager_be.edu_virtual_ufps.shared.requests.NotasPosgradoRequest;
import com.sistemas_mangager_be.edu_virtual_ufps.shared.responses.HttpResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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

@RestController
@RequestMapping("/api/notas")
@RequiredArgsConstructor
@Tag(name = "Gestión de Notas Académicas", description = "API para la administración integral de calificaciones de SIAV UFPS. "
                +
                "Gestiona el registro, actualización y cierre de notas para programas de posgrado, " +
                "validación de rangos académicos, cambios automáticos de estados de matrícula " +
                "y trazabilidad completa del proceso de calificación.")
public class NotasController {

        private final INotaService notaService;

        @Operation(summary = "Registrar o actualizar nota de posgrado", description = "Guarda o actualiza la calificación de un estudiante de posgrado en una matrícula específica. "
                        +
                        "Valida que la matrícula esté activa y permita edición de notas (notaAbierta=true). " +
                        "Aplica validación de rango de notas (0.0 a 5.0) según estándares académicos de posgrado. " +
                        "Actualiza automáticamente la fecha de registro y genera historial de cambios para trazabilidad.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Nota registrada o actualizada exitosamente con validaciones completadas", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponse.class), examples = @ExampleObject(value = """
                                        {
                                            "httpStatusCode": 200,
                                            "httpStatus": "OK",
                                            "reason": "OK",
                                            "message": "Nota registrada con exito"
                                        }
                                        """))),
                        @ApiResponse(responseCode = "400", description = "Nota fuera del rango válido (0.0-5.0) o formato incorrecto", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponse.class), examples = @ExampleObject(value = """
                                        {
                                            "httpStatusCode": 400,
                                            "httpStatus": "BAD_REQUEST",
                                            "reason": "Bad Request",
                                            "message": "la nota no es valido"
                                        }
                                        """))),
                        @ApiResponse(responseCode = "403", description = "Periodo de notas cerrado - no se permiten modificaciones", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponse.class), examples = @ExampleObject(value = """
                                        {
                                            "httpStatusCode": 403,
                                            "httpStatus": "FORBIDDEN",
                                            "reason": "Forbidden",
                                            "message": "no esta permitido no se puede guardar la nota"
                                        }
                                        """))),
                        @ApiResponse(responseCode = "404", description = "Matrícula no encontrada o inactiva", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponse.class), examples = @ExampleObject(value = """
                                        {
                                            "httpStatusCode": 404,
                                            "httpStatus": "NOT_FOUND",
                                            "reason": "Not Found",
                                            "message": "la matricula con id 1 no fue encontrada"
                                        }
                                        """)))
        })
        @PostMapping("/registrar")
        public ResponseEntity<HttpResponse> registrarNotaPosgrado(
                        @Parameter(description = "Datos de la nota incluyendo matrícula, calificación y responsable del registro", required = true, schema = @Schema(implementation = NotasPosgradoRequest.class), example = """
                                        {
                                            "matriculaId": 1,
                                            "nota": 4.5,
                                            "realizadoPor": "administrativo"
                                        }
                                        """) @RequestBody NotasPosgradoRequest request)
                        throws NotasException, MatriculaException {

                notaService.guardaroActualizarNotaPosgrado(request);
                return new ResponseEntity<>(
                                new HttpResponse(HttpStatus.OK.value(), HttpStatus.OK, HttpStatus.OK.getReasonPhrase(),
                                                " Nota registrada con exito"),
                                HttpStatus.OK);
        }

        @Operation(summary = "Cerrar periodo de notas por grupo-cohorte", description = "Ejecuta el proceso de cierre de calificaciones para todas las matrículas activas de un grupo-cohorte específico. "
                        +
                        "Establece notaAbierta=false para prevenir modificaciones posteriores y realiza cambios automáticos de estado: "
                        +
                        "• Notas ≥ 3.0: Estado 'Aprobada' " +
                        "• Notas < 3.0: Estado 'Reprobada' " +
                        "• Sin nota: Asigna 0.0 y estado 'Reprobada' " +
                        "Genera historial de cierre para posible reapertura posterior y registra trazabilidad completa.", security = @SecurityRequirement(name = "Usuario-Header"))
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Notas cerradas exitosamente con estados actualizados automáticamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponse.class), examples = @ExampleObject(value = """
                                        {
                                            "httpStatusCode": 200,
                                            "httpStatus": "OK",
                                            "reason": "OK",
                                            "message": "Notas cerradas con exito"
                                        }
                                        """))),
                        @ApiResponse(responseCode = "400", description = "Notas ya cerradas previamente o sin matrículas para procesar", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponse.class), examples = @ExampleObject(value = """
                                        {
                                            "httpStatusCode": 400,
                                            "httpStatus": "BAD_REQUEST",
                                            "reason": "Bad Request",
                                            "message": "Las notas del grupo ya están cerradas"
                                        }
                                        """))),
                        @ApiResponse(responseCode = "404", description = "Grupo-cohorte no encontrado o sin estudiantes matriculados", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponse.class), examples = @ExampleObject(value = """
                                        {
                                            "httpStatusCode": 404,
                                            "httpStatus": "NOT_FOUND",
                                            "reason": "Not Found",
                                            "message": "el grupo cohorte con id 1 no fue encontrado"
                                        }
                                        """))),
                        @ApiResponse(responseCode = "500", description = "Error interno durante el proceso de cierre masivo", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponse.class), examples = @ExampleObject(value = """
                                        {
                                            "httpStatusCode": 500,
                                            "httpStatus": "INTERNAL_SERVER_ERROR",
                                            "reason": "Internal Server Error",
                                            "message": "Error procesando matrícula durante el cierre"
                                        }
                                        """)))
        })
        @PostMapping("/cerrar/grupo/{idGrupo}")
        public ResponseEntity<HttpResponse> cerrarNotasGrupoPosgrado(
                        @Parameter(description = "ID único del grupo-cohorte para cerrar periodo de notas", required = true, example = "1", schema = @Schema(type = "integer", format = "int64", minimum = "1")) @PathVariable Long idGrupo,

                        @Parameter(description = "Usuario responsable del cierre de notas para registro de auditoría", required = true, example = "docente.apellido", schema = @Schema(type = "string", minLength = 3, maxLength = 50)) @RequestHeader(value = "X-Usuario") String usuario)
                        throws GrupoNotFoundException, NotasException {

                notaService.cerrarNotasGrupoPosgrado(idGrupo, usuario);
                return new ResponseEntity<>(
                                new HttpResponse(HttpStatus.OK.value(), HttpStatus.OK, HttpStatus.OK.getReasonPhrase(),
                                                "Notas cerradas con exito"),
                                HttpStatus.OK);
        }

        @Operation(summary = "Reabrir periodo de notas por grupo-cohorte", description = "Restaura la capacidad de edición de notas para un grupo-cohorte previamente cerrado. "
                        +
                        "Recupera todas las matrículas del historial de cierre y ejecuta las siguientes acciones: " +
                        "• Establece notaAbierta=true para permitir modificaciones " +
                        "• Restaura estado de matrículas a 'En curso' " +
                        "• Elimina el historial de cierre tras completar la reapertura " +
                        "• Registra cambios de estado con trazabilidad del usuario responsable " +
                        "Proceso útil para correcciones posteriores al cierre oficial.", security = @SecurityRequirement(name = "Usuario-Header"))
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Notas reabiertas exitosamente con estados restaurados a 'En curso'", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponse.class), examples = @ExampleObject(value = """
                                        {
                                            "httpStatusCode": 200,
                                            "httpStatus": "OK",
                                            "reason": "OK",
                                            "message": "Notas abiertas con exito"
                                        }
                                        """))),
                        @ApiResponse(responseCode = "400", description = "No existe historial de cierre - notas ya están abiertas", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponse.class), examples = @ExampleObject(value = """
                                        {
                                            "httpStatusCode": 400,
                                            "httpStatus": "BAD_REQUEST",
                                            "reason": "Bad Request",
                                            "message": "el grupo 1 no tiene historial de cierre de notas"
                                        }
                                        """))),
                        @ApiResponse(responseCode = "404", description = "Grupo-cohorte no encontrado o sin historial de cierre previo", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponse.class), examples = @ExampleObject(value = """
                                        {
                                            "httpStatusCode": 404,
                                            "httpStatus": "NOT_FOUND",
                                            "reason": "Not Found",
                                            "message": "el grupo cohorte con id 1 no fue encontrado"
                                        }
                                        """))),
                        @ApiResponse(responseCode = "500", description = "Error interno durante el proceso de reapertura", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponse.class), examples = @ExampleObject(value = """
                                        {
                                            "httpStatusCode": 500,
                                            "httpStatus": "INTERNAL_SERVER_ERROR",
                                            "reason": "Internal Server Error",
                                            "message": "Error restaurando matrícula durante la reapertura"
                                        }
                                        """)))
        })
        @PostMapping("/abrir/grupo/{idGrupo}")
        public ResponseEntity<HttpResponse> abrirNotasGrupoPosgrado(
                        @Parameter(description = "ID único del grupo-cohorte para reabrir periodo de notas", required = true, example = "1", schema = @Schema(type = "integer", format = "int64", minimum = "1")) @PathVariable Long idGrupo,

                        @Parameter(description = "Usuario responsable de la reapertura de notas para registro de auditoría", required = true, example = "admin.academico", schema = @Schema(type = "string", minLength = 3, maxLength = 50)) @RequestHeader(value = "X-Usuario") String usuario)
                        throws GrupoNotFoundException, NotasException {

                notaService.abrirNotasGrupoPosgrado(idGrupo, usuario);
                return new ResponseEntity<>(
                                new HttpResponse(HttpStatus.OK.value(), HttpStatus.OK, HttpStatus.OK.getReasonPhrase(),
                                                "Notas abiertas con exito"),
                                HttpStatus.OK);
        }
}
