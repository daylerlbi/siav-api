package com.sistemas_mangager_be.edu_virtual_ufps.controllers;

import com.sistemas_mangager_be.edu_virtual_ufps.exceptions.GrupoNotFoundException;
import com.sistemas_mangager_be.edu_virtual_ufps.exceptions.NotasException;
import com.sistemas_mangager_be.edu_virtual_ufps.exceptions.SemestreException;
import com.sistemas_mangager_be.edu_virtual_ufps.services.moodle.MoodleService;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/semestres")
@RequiredArgsConstructor
@Tag(name = "Gestión de Cierre de Semestre", description = "API para la administración del cierre de semestres académicos de SIAV UFPS. "
                +
                "Gestiona el proceso completo de finalización de semestre incluyendo cierre de notas, " +
                "creación de históricos en Moodle y actualización de estados académicos.")
public class SemestreController {

        private final MoodleService moodleService;

        @Operation(summary = "Terminar semestre académico completo", description = "Ejecuta el proceso integral de finalización de semestre para un programa académico específico. "
                        +
                        "Incluye validación de fechas, cierre automático de notas de todos los grupos, " +
                        "creación de estructura histórica en Moodle, duplicación de cursos para el siguiente semestre "
                        +
                        "y actualización del semestre activo del programa. Este proceso es irreversible y " +
                        "solo puede ejecutarse una vez por semestre en cada programa.", security = @SecurityRequirement(name = "Usuario-Header"))
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Semestre terminado exitosamente con todos los procesos completados", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponse.class), examples = @ExampleObject(value = """
                                        {
                                            "httpStatusCode": 200,
                                            "httpStatus": "OK",
                                            "reason": "OK",
                                            "message": "Semestre 2024-I terminado exitosamente para el programa ID: 1"
                                        }
                                        """))),
                        @ApiResponse(responseCode = "400", description = "Error en validaciones o proceso de terminación", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponse.class), examples = @ExampleObject(value = """
                                        {
                                            "httpStatusCode": 400,
                                            "httpStatus": "BAD_REQUEST",
                                            "reason": "Error en el proceso de terminación de semestre",
                                            "message": "El semestre 2024-I solo puede terminarse después del 1° de Julio de 2024"
                                        }
                                        """))),
                        @ApiResponse(responseCode = "404", description = "Programa no encontrado o sin grupos para el semestre", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponse.class), examples = @ExampleObject(value = """
                                        {
                                            "httpStatusCode": 404,
                                            "httpStatus": "NOT_FOUND",
                                            "reason": "Grupo no encontrado",
                                            "message": "No se encontraron grupos para el semestre 2024-I en el programa Maestría en TIC"
                                        }
                                        """))),
                        @ApiResponse(responseCode = "409", description = "Semestre ya terminado previamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponse.class), examples = @ExampleObject(value = """
                                        {
                                            "httpStatusCode": 400,
                                            "httpStatus": "BAD_REQUEST",
                                            "reason": "Error en el proceso de terminación de semestre",
                                            "message": "Este semestre ya ha sido terminado para este programa. No se puede ejecutar el proceso nuevamente."
                                        }
                                        """))),
                        @ApiResponse(responseCode = "500", description = "Error interno durante el proceso de terminación", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponse.class), examples = @ExampleObject(value = """
                                        {
                                            "httpStatusCode": 500,
                                            "httpStatus": "INTERNAL_SERVER_ERROR",
                                            "reason": "Error interno del servidor",
                                            "message": "Ocurrió un error inesperado al terminar el semestre: Error de conexión con Moodle"
                                        }
                                        """)))
        })
        @PostMapping("/terminar/{programaId}/{semestre}")
        public ResponseEntity<HttpResponse> terminarSemestre(
                        @Parameter(description = "Identificador único del programa académico", required = true, example = "1", schema = @Schema(type = "integer", minimum = "1")) @PathVariable Integer programaId,

                        @Parameter(description = "Semestre académico a terminar en formato YYYY-I o YYYY-II. " +
                                        "Para semestre I: solo puede terminarse después del 1° de Julio del año correspondiente. "
                                        +
                                        "Para semestre II: solo puede terminarse después del 1° de Enero del año siguiente.", required = true, example = "2024-I", schema = @Schema(type = "string", pattern = "^\\d{4}-(I|II)$", description = "Formato: YYYY-I o YYYY-II (ej: 2024-I, 2024-II)")) @PathVariable String semestre,

                        @Parameter(description = "Usuario responsable de ejecutar el cierre de semestre. " +
                                        "Se registra en los logs de auditoría y cierre de notas.", required = true, example = "admin.academico", schema = @Schema(type = "string", minLength = 3, maxLength = 50)) @RequestHeader(value = "X-Usuario") String usuario) {

                try {
                        moodleService.terminarSemestre(programaId, semestre, usuario);

                        return new ResponseEntity<>(
                                        new HttpResponse(
                                                        HttpStatus.OK.value(),
                                                        HttpStatus.OK,
                                                        HttpStatus.OK.getReasonPhrase(),
                                                        "Semestre " + semestre
                                                                        + " terminado exitosamente para el programa ID: "
                                                                        + programaId),
                                        HttpStatus.OK);

                } catch (SemestreException e) {
                        return new ResponseEntity<>(
                                        new HttpResponse(
                                                        HttpStatus.BAD_REQUEST.value(),
                                                        HttpStatus.BAD_REQUEST,
                                                        "Error en el proceso de terminación de semestre",
                                                        e.getMessage()),
                                        HttpStatus.BAD_REQUEST);

                } catch (GrupoNotFoundException e) {
                        return new ResponseEntity<>(
                                        new HttpResponse(
                                                        HttpStatus.NOT_FOUND.value(),
                                                        HttpStatus.NOT_FOUND,
                                                        "Grupo no encontrado",
                                                        e.getMessage()),
                                        HttpStatus.NOT_FOUND);

                } catch (NotasException e) {
                        return new ResponseEntity<>(
                                        new HttpResponse(
                                                        HttpStatus.BAD_REQUEST.value(),
                                                        HttpStatus.BAD_REQUEST,
                                                        "Error al cerrar notas",
                                                        e.getMessage()),
                                        HttpStatus.BAD_REQUEST);

                } catch (Exception e) {
                        return new ResponseEntity<>(
                                        new HttpResponse(
                                                        HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                                        HttpStatus.INTERNAL_SERVER_ERROR,
                                                        "Error interno del servidor",
                                                        "Ocurrió un error inesperado al terminar el semestre: "
                                                                        + e.getMessage()),
                                        HttpStatus.INTERNAL_SERVER_ERROR);
                }
        }
}