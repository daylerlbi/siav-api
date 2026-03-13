package com.sistemas_mangager_be.edu_virtual_ufps.controllers;

import com.sistemas_mangager_be.edu_virtual_ufps.exceptions.*;
import com.sistemas_mangager_be.edu_virtual_ufps.services.interfaces.IGrupoService;
import com.sistemas_mangager_be.edu_virtual_ufps.shared.DTOs.GrupoDTO;
import com.sistemas_mangager_be.edu_virtual_ufps.shared.requests.GrupoRequest;
import com.sistemas_mangager_be.edu_virtual_ufps.shared.responses.*;
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

import java.util.List;

@RequestMapping("/api/grupos")
@RestController
@RequiredArgsConstructor
@Tag(name = "Gestión de Grupos Académicos", description = "API para la administración integral de grupos académicos de SIAV UFPS. "
        +
        "Gestiona la creación automática de grupos por materia, vinculación con cohortes y docentes, " +
        "integración con Moodle y administración de estudiantes por grupo.")
public class GrupoController {

    private final IGrupoService iGrupoService;

    @Operation(summary = "Crear nuevo grupo académico", description = "Crea un grupo para una materia específica con nomenclatura automática. "
            +
            "Genera automáticamente el nombre 'Materia - Grupo X' y código 'CODX' donde X es la letra consecutiva (A, B, C...). "
            +
            "La letra se asigna según el número de grupos existentes para esa materia.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Grupo creado exitosamente con nomenclatura automática", content = @Content(mediaType = "application/json", schema = @Schema(implementation = GrupoDTO.class), examples = @ExampleObject(value = """
                    {
                        "id": 1,
                        "nombre": "Metodología de la Investigación - Grupo A",
                        "codigo": "MINA",
                        "activo": true,
                        "materiaId": 5
                    }
                    """))),
            @ApiResponse(responseCode = "404", description = "Materia no encontrada", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponse.class)))
    })
    @PostMapping("/crear")
    public ResponseEntity<GrupoDTO> crearGrupo(
            @Parameter(description = "Datos del grupo incluyendo la materia asociada", required = true, schema = @Schema(implementation = GrupoDTO.class), example = """
                    {
                        "materiaId": 5
                    }
                    """) @RequestBody GrupoDTO grupoDTO)
            throws MateriaNotFoundException, CohorteNotFoundException,
            UserNotFoundException, RoleNotFoundException {

        GrupoDTO grupoCreado = iGrupoService.crearGrupo(grupoDTO);
        return new ResponseEntity<>(grupoCreado, HttpStatus.OK);
    }

    @Operation(summary = "Actualizar grupo académico", description = "Modifica los datos de un grupo existente. Si se cambia la materia, "
            +
            "regenera automáticamente el nombre y código del grupo según la nueva materia.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Grupo actualizado exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponse.class), examples = @ExampleObject(value = """
                    {
                        "httpStatusCode": 200,
                        "httpStatus": "OK",
                        "reason": "OK",
                        "message": "Grupo actualizado con exito"
                    }
                    """))),
            @ApiResponse(responseCode = "404", description = "Grupo o materia no encontrados", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponse.class)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<HttpResponse> actualizarGrupo(
            @Parameter(description = "ID único del grupo a actualizar", required = true, example = "1", schema = @Schema(type = "integer", minimum = "1")) @PathVariable Integer id,

            @Parameter(description = "Nuevos datos del grupo", required = true, schema = @Schema(implementation = GrupoDTO.class), example = """
                    {
                        "materiaId": 7
                    }
                    """) @RequestBody GrupoDTO grupoDTO)
            throws MateriaNotFoundException, CohorteNotFoundException,
            UserNotFoundException, RoleNotFoundException, GrupoNotFoundException {

        iGrupoService.actualizarGrupo(grupoDTO, id);
        return new ResponseEntity<>(
                new HttpResponse(HttpStatus.OK.value(), HttpStatus.OK, HttpStatus.OK.getReasonPhrase(),
                        "Grupo actualizado con exito"),
                HttpStatus.OK);
    }

    @Operation(summary = "Consultar grupo específico", description = "Obtiene información detallada de un grupo incluyendo datos de la materia asociada.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Información del grupo obtenida exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = GrupoResponse.class), examples = @ExampleObject(value = """
                    {
                        "id": 1,
                        "nombre": "Metodología de la Investigación - Grupo A",
                        "codigo": "MINA",
                        "activo": true,
                        "materiaId": 5,
                        "materiaNombre": "Metodología de la Investigación"
                    }
                    """))),
            @ApiResponse(responseCode = "404", description = "Grupo no encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponse.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<GrupoResponse> listarGrupo(
            @Parameter(description = "ID único del grupo a consultar", required = true, example = "1", schema = @Schema(type = "integer", minimum = "1")) @PathVariable Integer id)
            throws GrupoNotFoundException {

        GrupoResponse grupoResponse = iGrupoService.listarGrupo(id);
        return new ResponseEntity<>(grupoResponse, HttpStatus.OK);
    }

    @Operation(summary = "Listar todos los grupos", description = "Obtiene el catálogo completo de grupos académicos registrados en el sistema "
            +
            "con información básica de cada grupo y su materia asociada.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de grupos obtenida exitosamente", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = GrupoResponse.class))))
    })
    @GetMapping
    public ResponseEntity<List<GrupoResponse>> listarGrupos() {
        List<GrupoResponse> grupoResponse = iGrupoService.listarGrupos();
        return new ResponseEntity<>(grupoResponse, HttpStatus.OK);
    }

    @Operation(summary = "Consultar grupos vinculados por materia", description = "Obtiene todos los grupos-cohorte-docente asociados a una materia específica. "
            +
            "Incluye información completa de vinculaciones activas con cohortes y docentes asignados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de grupos vinculados por materia obtenida exitosamente", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = GrupoCohorteDocenteResponse.class)))),
            @ApiResponse(responseCode = "404", description = "Materia no encontrada", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponse.class)))
    })
    @GetMapping("/materia/{materiaId}")
    public ResponseEntity<List<GrupoCohorteDocenteResponse>> listarGruposPorMateria(
            @Parameter(description = "ID de la materia para filtrar grupos", required = true, example = "5", schema = @Schema(type = "integer", minimum = "1")) @PathVariable Integer materiaId)
            throws MateriaNotFoundException {

        List<GrupoCohorteDocenteResponse> grupoResponse = iGrupoService.listarGruposCohortePorMateria(materiaId);
        return new ResponseEntity<>(grupoResponse, HttpStatus.OK);
    }

    @Operation(summary = "Activar grupo académico", description = "Cambia el estado de un grupo a activo, habilitándolo para operaciones académicas "
            +
            "como vinculaciones con cohortes y matrículas de estudiantes.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Grupo activado exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponse.class), examples = @ExampleObject(value = """
                    {
                        "httpStatusCode": 200,
                        "httpStatus": "OK",
                        "reason": "OK",
                        "message": "Grupo activado con exito"
                    }
                    """))),
            @ApiResponse(responseCode = "404", description = "Grupo no encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponse.class)))
    })
    @PostMapping("/{id}/activar")
    public ResponseEntity<HttpResponse> activarGrupo(
            @Parameter(description = "ID del grupo a activar", required = true, example = "1", schema = @Schema(type = "integer", minimum = "1")) @PathVariable Integer id)
            throws GrupoNotFoundException {

        iGrupoService.activarGrupo(id);
        return new ResponseEntity<>(
                new HttpResponse(HttpStatus.OK.value(), HttpStatus.OK, HttpStatus.OK.getReasonPhrase(),
                        "Grupo activado con exito"),
                HttpStatus.OK);
    }

    @Operation(summary = "Desactivar grupo académico", description = "Cambia el estado de un grupo a inactivo, impidiendo nuevas operaciones académicas "
            +
            "pero manteniendo el historial de matrículas y calificaciones existentes.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Grupo desactivado exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponse.class), examples = @ExampleObject(value = """
                    {
                        "httpStatusCode": 200,
                        "httpStatus": "OK",
                        "reason": "OK",
                        "message": "Grupo desactivado con exito"
                    }
                    """))),
            @ApiResponse(responseCode = "404", description = "Grupo no encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponse.class)))
    })
    @PostMapping("/{id}/desactivar")
    public ResponseEntity<HttpResponse> desactivarGrupo(
            @Parameter(description = "ID del grupo a desactivar", required = true, example = "1", schema = @Schema(type = "integer", minimum = "1")) @PathVariable Integer id)
            throws GrupoNotFoundException {

        iGrupoService.desactivarGrupo(id);
        return new ResponseEntity<>(
                new HttpResponse(HttpStatus.OK.value(), HttpStatus.OK, HttpStatus.OK.getReasonPhrase(),
                        "Grupo desactivado con exito"),
                HttpStatus.OK);
    }

    @Operation(summary = "Vincular grupo con cohorte y docente", description = "Crea una vinculación entre un grupo académico, una cohorte de estudiantes y un docente. "
            +
            "Esta vinculación permite la gestión de clases y matrícula de estudiantes. " +
            "Valida que el usuario tenga rol de docente (rol ID = 2).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vinculación creada exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = GrupoCohorteDocenteResponse.class), examples = @ExampleObject(value = """
                    {
                        "id": 1,
                        "grupoCohorteId": 1,
                        "grupoId": 1,
                        "grupoNombre": "Metodología de la Investigación - Grupo A",
                        "codigoGrupo": "MINA",
                        "cohorteGrupoId": 1,
                        "cohorteGrupoNombre": "Grupo A 2024-I",
                        "cohorteId": 1,
                        "cohorteNombre": "Cohorte 2024-I Maestría TIC",
                        "docenteId": 15,
                        "docenteNombre": "Dr. Carlos Eduardo López",
                        "materia": "Metodología de la Investigación",
                        "codigoMateria": "MIN",
                        "semestreMateria": 1,
                        "materiaid": 5,
                        "programaId": 1,
                        "fechaCreacion": "2024-01-15T08:00:00",
                        "moodleId": null
                    }
                    """))),
            @ApiResponse(responseCode = "400", description = "Usuario no es docente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponse.class), examples = @ExampleObject(value = """
                    {
                        "httpStatusCode": 400,
                        "httpStatus": "BAD_REQUEST",
                        "reason": "Bad Request",
                        "message": "el usuario con el id 15 no es un docente"
                    }
                    """))),
            @ApiResponse(responseCode = "404", description = "Grupo, cohorte o usuario no encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponse.class)))
    })
    @PostMapping("/vincular")
    public ResponseEntity<GrupoCohorteDocenteResponse> vincularCohorteDocente(
            @Parameter(description = "Datos para la vinculación grupo-cohorte-docente", required = true, schema = @Schema(implementation = GrupoRequest.class), example = """
                    {
                        "grupoId": 1,
                        "cohorteGrupoId": 1,
                        "docenteId": 15
                    }
                    """) @RequestBody GrupoRequest grupoRequest)
            throws CohorteNotFoundException, GrupoNotFoundException,
            UserNotFoundException {

        GrupoCohorteDocenteResponse grupoCohorteDocenteResponse = iGrupoService.vincularCohorteDocente(grupoRequest);
        return new ResponseEntity<>(grupoCohorteDocenteResponse, HttpStatus.OK);
    }

    @Operation(summary = "Vincular grupo con curso de Moodle", description = "Establece la conexión entre un grupo-cohorte del sistema y su curso correspondiente en Moodle. "
            +
            "Valida que el MoodleID no esté ya en uso por otro grupo.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vinculación con Moodle realizada exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponse.class), examples = @ExampleObject(value = """
                    {
                        "httpStatusCode": 200,
                        "httpStatus": "OK",
                        "reason": "OK",
                        "message": "Vinculacion con moodle realizada con exito"
                    }
                    """))),
            @ApiResponse(responseCode = "400", description = "MoodleID ya está en uso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponse.class), examples = @ExampleObject(value = """
                    {
                        "httpStatusCode": 400,
                        "httpStatus": "BAD_REQUEST",
                        "reason": "Bad Request",
                        "message": "el id moodle 123 ya esta en uso"
                    }
                    """))),
            @ApiResponse(responseCode = "404", description = "Grupo-cohorte no encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponse.class)))
    })
    @PostMapping("/moodle/{grupoId}")
    public ResponseEntity<HttpResponse> vincularGrupoMoodle(
            @Parameter(description = "ID del grupo-cohorte a vincular con Moodle", required = true, example = "1", schema = @Schema(type = "integer", format = "int64", minimum = "1")) @PathVariable Long grupoId,

            @Parameter(description = "ID del curso en Moodle", required = true, example = "123", schema = @Schema(type = "string")) @RequestParam String moodleId)
            throws GrupoNotFoundException, GrupoExistException {

        iGrupoService.vincularGrupoMoodle(grupoId, moodleId);
        return new ResponseEntity<>(
                new HttpResponse(HttpStatus.OK.value(), HttpStatus.OK, HttpStatus.OK.getReasonPhrase(),
                        "Vinculacion con moodle realizada con exito"),
                HttpStatus.OK);
    }

    @Operation(summary = "Actualizar vinculación grupo-cohorte-docente", description = "Modifica una vinculación existente permitiendo cambiar grupo, cohorte-grupo o docente asignado. "
            +
            "Valida roles de docente y actualiza fecha de modificación.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vinculación actualizada exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponse.class), examples = @ExampleObject(value = """
                    {
                        "httpStatusCode": 200,
                        "httpStatus": "OK",
                        "reason": "OK",
                        "message": "Grupo vinculado con exito"
                    }
                    """))),
            @ApiResponse(responseCode = "400", description = "Usuario no es docente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponse.class))),
            @ApiResponse(responseCode = "404", description = "Vinculación, grupo, cohorte o docente no encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponse.class)))
    })
    @PutMapping("/vincular/{id}")
    public ResponseEntity<HttpResponse> actualizarVinculacionCohorteDocente(
            @Parameter(description = "ID único de la vinculación a actualizar", required = true, example = "1", schema = @Schema(type = "integer", format = "int64", minimum = "1")) @PathVariable Long id,

            @Parameter(description = "Nuevos datos para la vinculación", required = true, schema = @Schema(implementation = GrupoRequest.class), example = """
                    {
                        "grupoId": 2,
                        "cohorteGrupoId": 1,
                        "docenteId": 18
                    }
                    """) @RequestBody GrupoRequest grupoRequest)
            throws CohorteNotFoundException, GrupoNotFoundException,
            UserNotFoundException, VinculacionNotFoundException {

        iGrupoService.actualizarVinculacionCohorteDocente(id, grupoRequest);
        return new ResponseEntity<>(
                new HttpResponse(HttpStatus.OK.value(), HttpStatus.OK, HttpStatus.OK.getReasonPhrase(),
                        "Grupo vinculado con exito"),
                HttpStatus.OK);
    }

    @Operation(summary = "Consultar vinculación específica", description = "Obtiene información detallada de una vinculación grupo-cohorte-docente incluyendo "
            +
            "datos completos del grupo, materia, cohorte, docente y conexión con Moodle.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Información de vinculación obtenida exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = GrupoCohorteDocenteResponse.class))),
            @ApiResponse(responseCode = "404", description = "Vinculación no encontrada", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponse.class)))
    })
    @GetMapping("/vinculado/{id}")
    public ResponseEntity<GrupoCohorteDocenteResponse> listarGrupoCohorteDocente(
            @Parameter(description = "ID único de la vinculación a consultar", required = true, example = "1", schema = @Schema(type = "integer", format = "int64", minimum = "1")) @PathVariable Long id)
            throws VinculacionNotFoundException {

        GrupoCohorteDocenteResponse grupoCohorteDocenteResponse = iGrupoService.listarGrupoCohorteDocente(id);
        return new ResponseEntity<>(grupoCohorteDocenteResponse, HttpStatus.OK);
    }

    @Operation(summary = "Listar todas las vinculaciones", description = "Obtiene el catálogo completo de vinculaciones grupo-cohorte-docente activas en el sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de vinculaciones obtenida exitosamente", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = GrupoCohorteDocenteResponse.class))))
    })
    @GetMapping("/vinculados")
    public ResponseEntity<List<GrupoCohorteDocenteResponse>> listarGrupoCohorteDocentes() {
        List<GrupoCohorteDocenteResponse> grupoCohorteDocenteResponse = iGrupoService.listarGrupoCohorteDocentes();
        return new ResponseEntity<>(grupoCohorteDocenteResponse, HttpStatus.OK);
    }

    @Operation(summary = "Consultar vinculaciones por cohorte", description = "Filtra todas las vinculaciones asociadas a una cohorte específica. "
            +
            "Útil para gestión académica por cohortes de estudiantes.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de vinculaciones por cohorte obtenida exitosamente", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = GrupoCohorteDocenteResponse.class)))),
            @ApiResponse(responseCode = "404", description = "Cohorte no encontrada", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponse.class)))
    })
    @GetMapping("/cohorte/{cohorteId}")
    public ResponseEntity<List<GrupoCohorteDocenteResponse>> listarGruposPorCohorte(
            @Parameter(description = "ID de la cohorte para filtrar vinculaciones", required = true, example = "1", schema = @Schema(type = "integer", minimum = "1")) @PathVariable Integer cohorteId)
            throws CohorteNotFoundException {

        List<GrupoCohorteDocenteResponse> grupoCohorteDocenteResponse = iGrupoService.listarGruposPorCohorte(cohorteId);
        return new ResponseEntity<>(grupoCohorteDocenteResponse, HttpStatus.OK);
    }

    @Operation(summary = "Consultar vinculaciones por programa académico", description = "Filtra todas las vinculaciones de grupos asociados a un programa académico específico "
            +
            "(Maestría TIC, Analítica de Datos, etc.).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de vinculaciones por programa obtenida exitosamente", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = GrupoCohorteDocenteResponse.class)))),
            @ApiResponse(responseCode = "404", description = "Programa no encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponse.class)))
    })
    @GetMapping("/programa/{programaId}")
    public ResponseEntity<List<GrupoCohorteDocenteResponse>> listarGruposPorPrograma(
            @Parameter(description = "ID del programa académico para filtrar vinculaciones", required = true, example = "1", schema = @Schema(type = "integer", minimum = "1")) @PathVariable Integer programaId)
            throws ProgramaNotFoundException {

        List<GrupoCohorteDocenteResponse> grupoCohorteDocenteResponse = iGrupoService
                .listarGruposPorPrograma(programaId);
        return new ResponseEntity<>(grupoCohorteDocenteResponse, HttpStatus.OK);
    }

    @Operation(summary = "Consultar vinculaciones por grupo específico", description = "Obtiene todas las vinculaciones asociadas a un grupo académico específico. "
            +
            "Un grupo puede tener múltiples vinculaciones con diferentes cohortes.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de vinculaciones por grupo obtenida exitosamente", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = GrupoCohorteDocenteResponse.class)))),
            @ApiResponse(responseCode = "404", description = "Grupo no encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponse.class)))
    })
    @GetMapping("/grupo/{grupoId}")
    public ResponseEntity<List<GrupoCohorteDocenteResponse>> listarGruposPorGrupo(
            @Parameter(description = "ID del grupo para consultar sus vinculaciones", required = true, example = "1", schema = @Schema(type = "integer", minimum = "1")) @PathVariable Integer grupoId)
            throws GrupoNotFoundException {

        List<GrupoCohorteDocenteResponse> grupoCohorteDocenteResponse = iGrupoService.listarGruposPorGrupo(grupoId);
        return new ResponseEntity<>(grupoCohorteDocenteResponse, HttpStatus.OK);
    }

    @Operation(summary = "Consultar grupos asignados a un docente", description = "Obtiene todas las vinculaciones donde un docente específico está asignado. "
            +
            "Útil para consultar la carga académica de un profesor.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de grupos asignados al docente obtenida exitosamente", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = GrupoCohorteDocenteResponse.class)))),
            @ApiResponse(responseCode = "404", description = "Docente no encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponse.class)))
    })
    @GetMapping("/docente/{docenteId}")
    public ResponseEntity<List<GrupoCohorteDocenteResponse>> listarGruposPorDocente(
            @Parameter(description = "ID del docente para consultar sus grupos asignados", required = true, example = "15", schema = @Schema(type = "integer", minimum = "1")) @PathVariable Integer docenteId)
            throws UserNotFoundException {

        List<GrupoCohorteDocenteResponse> grupoCohorteDocenteResponse = iGrupoService.listarGruposPorDocente(docenteId);
        return new ResponseEntity<>(grupoCohorteDocenteResponse, HttpStatus.OK);
    }

    @Operation(summary = "Consultar grupos por cohorte-grupo", description = "Obtiene información básica de grupos asociados a un cohorte-grupo específico. "
            +
            "Retorna información simplificada para listados y selecciones.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de grupos por cohorte-grupo obtenida exitosamente", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = GrupoCohorteResponse.class)))),
            @ApiResponse(responseCode = "404", description = "Cohorte-grupo no encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponse.class)))
    })
    @GetMapping("/cohortegrupo/{cohorteGrupoId}")
    public ResponseEntity<List<GrupoCohorteResponse>> listarGruposPorCohorteGrupo(
            @Parameter(description = "ID del cohorte-grupo para filtrar grupos", required = true, example = "1", schema = @Schema(type = "integer", minimum = "1")) @PathVariable Integer cohorteGrupoId)
            throws CohorteNotFoundException {

        List<GrupoCohorteResponse> grupoCohorteResponse = iGrupoService.listarGruposPorCohorteGrupo(cohorteGrupoId);
        return new ResponseEntity<>(grupoCohorteResponse, HttpStatus.OK);
    }

    @Operation(summary = "Consultar estudiantes matriculados en grupo-cohorte", description = "Obtiene información completa de estudiantes matriculados en un grupo-cohorte específico "
            +
            "incluyendo datos del grupo, docente y lista detallada de estudiantes con sus datos de Moodle.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Información de estudiantes del grupo-cohorte obtenida exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = EstudianteGrupoResponse.class), examples = @ExampleObject(value = """
                    {
                        "id": 1,
                        "grupoNombre": "Metodología de la Investigación - Grupo A",
                        "grupoCodigo": "MINA",
                        "grupoCohorte": "Grupo A 2024-I",
                        "grupoCohorteId": 1,
                        "docenteNombre": "Dr. Carlos Eduardo López",
                        "estudiantes": [
                            {
                                "id": 1,
                                "nombre": "Juan Carlos",
                                "nombre2": "Andrés",
                                "apellido": "Pérez",
                                "apellido2": "García",
                                "codigo": "20241001",
                                "email": "juan.perez@ufps.edu.co",
                                "moodleId": "12345"
                            }
                        ]
                    }
                    """))),
            @ApiResponse(responseCode = "404", description = "Grupo-cohorte no encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponse.class)))
    })
    @GetMapping("/estudiantes/grupocohorte/{grupoCohorteId}")
    public ResponseEntity<EstudianteGrupoResponse> listarEstudiantesPorGrupoCohorte(
            @Parameter(description = "ID del grupo-cohorte para consultar estudiantes matriculados", required = true, example = "1", schema = @Schema(type = "integer", format = "int64", minimum = "1")) @PathVariable Long grupoCohorteId) {

        EstudianteGrupoResponse estudianteGrupoResponse = iGrupoService
                .listarEstudiantesPorGrupoCohorte(grupoCohorteId);
        return new ResponseEntity<>(estudianteGrupoResponse, HttpStatus.OK);
    }
}
