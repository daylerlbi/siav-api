package com.sistemas_mangager_be.edu_virtual_ufps.controllers;

import com.sistemas_mangager_be.edu_virtual_ufps.exceptions.*;
import com.sistemas_mangager_be.edu_virtual_ufps.services.interfaces.IEstudianteService;
import com.sistemas_mangager_be.edu_virtual_ufps.services.interfaces.IGrupoService;
import com.sistemas_mangager_be.edu_virtual_ufps.shared.DTOs.EstudianteDTO;
import com.sistemas_mangager_be.edu_virtual_ufps.shared.requests.MoodleRequest;
import com.sistemas_mangager_be.edu_virtual_ufps.shared.responses.EstudianteGrupoResponse;
import com.sistemas_mangager_be.edu_virtual_ufps.shared.responses.EstudianteResponse;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/estudiantes")
@RequiredArgsConstructor
@Tag(name = "Gestión de Estudiantes", description = "API para la administración integral de estudiantes de SIAV UFPS. "
        +
        "Gestiona registro, actualización, consultas y vinculación con plataforma Moodle.")
public class EstudianteController {

    private final IEstudianteService estudianteService;
    private final IGrupoService iGrupoService;

    @Operation(summary = "Crear nuevo estudiante", description = "Registra un nuevo estudiante en el sistema validando unicidad de código, email, cédula y MoodleID. "
            +
            "Crea automáticamente el usuario asociado y establece la relación con pensum, cohorte y estado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Estudiante creado exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = EstudianteDTO.class), examples = @ExampleObject(value = """
                    {
                        "id": 1,
                        "usuarioId": 15,
                        "codigo": "20241001",
                        "nombre": "Juan Carlos",
                        "nombre2": "Andrés",
                        "apellido": "Pérez",
                        "apellido2": "García",
                        "email": "juan.perez@ufps.edu.co",
                        "telefono": "3001234567",
                        "cedula": "1098765432",
                        "pensumId": 1,
                        "cohorteId": 1,
                        "estadoEstudianteId": 1,
                        "moodleId": "12345",
                        "esPosgrado": true
                    }
                    """))),
            @ApiResponse(responseCode = "400", description = "Datos duplicados o validaciones fallidas", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponse.class), examples = @ExampleObject(value = """
                    {
                        "httpStatusCode": 400,
                        "httpStatus": "BAD_REQUEST",
                        "reason": "Bad Request",
                        "message": "El código de estudiante 20241001 ya existe en el sistema"
                    }
                    """))),
            @ApiResponse(responseCode = "404", description = "Pensum, cohorte o estado de estudiante no encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponse.class)))
    })
    @PostMapping("/crear")
    public ResponseEntity<EstudianteDTO> crearEstudiante(
            @Parameter(description = "Información completa del estudiante incluyendo datos personales y académicos", required = true, schema = @Schema(implementation = EstudianteDTO.class), example = """
                    {
                        "codigo": "20241001",
                        "nombre": "Juan Carlos",
                        "nombre2": "Andrés",
                        "apellido": "Pérez",
                        "apellido2": "García",
                        "email": "juan.perez@ufps.edu.co",
                        "telefono": "3001234567",
                        "cedula": "1098765432",
                        "pensumId": 1,
                        "cohorteId": 1,
                        "moodleId": "12345"
                    }
                    """) @RequestBody EstudianteDTO estudianteDTO)
            throws PensumNotFoundException, CohorteNotFoundException,
            EstadoEstudianteNotFoundException, RoleNotFoundException, UserExistException {

        EstudianteDTO estudiante = estudianteService.crearEstudiante(estudianteDTO);
        return new ResponseEntity<>(estudiante, HttpStatus.OK);
    }

    @Operation(summary = "Vincular estudiante con Moodle", description = "Establece la vinculación entre un estudiante existente y su ID en la plataforma Moodle. "
            +
            "Valida que el MoodleID no esté ya en uso por otro estudiante.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vinculación con Moodle realizada exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponse.class), examples = @ExampleObject(value = """
                    {
                        "httpStatusCode": 200,
                        "httpStatus": "OK",
                        "reason": "OK",
                        "message": "Vinculacion con moodle realizada con exito"
                    }
                    """))),
            @ApiResponse(responseCode = "400", description = "MoodleID ya está en uso por otro estudiante", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponse.class), examples = @ExampleObject(value = """
                    {
                        "httpStatusCode": 400,
                        "httpStatus": "BAD_REQUEST",
                        "reason": "Bad Request",
                        "message": "El ID de Moodle 12345 ya existe en el sistema"
                    }
                    """))),
            @ApiResponse(responseCode = "404", description = "Estudiante no encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponse.class)))
    })
    @PostMapping("/moodle")
    public ResponseEntity<HttpResponse> vincularEstudianteMoodle(
            @Parameter(description = "Información para vincular estudiante con plataforma Moodle", required = true, schema = @Schema(implementation = MoodleRequest.class), example = """
                    {
                        "backendId": 1,
                        "moodleId": "12345"
                    }
                    """) @RequestBody MoodleRequest moodleRequest)
            throws EstudianteNotFoundException, UserExistException {

        estudianteService.vincularMoodleId(moodleRequest);
        return new ResponseEntity<>(
                new HttpResponse(HttpStatus.OK.value(), HttpStatus.OK, HttpStatus.OK.getReasonPhrase(),
                        "Vinculacion con moodle realizada con exito"),
                HttpStatus.OK);
    }

    @Operation(summary = "Actualizar datos de estudiante", description = "Modifica la información de un estudiante existente validando unicidad de campos únicos "
            +
            "solo cuando estos cambien. Actualiza datos personales y académicos.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Estudiante actualizado exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponse.class), examples = @ExampleObject(value = """
                    {
                        "httpStatusCode": 200,
                        "httpStatus": "OK",
                        "reason": "OK",
                        "message": "Estudiante actualizado con exito"
                    }
                    """))),
            @ApiResponse(responseCode = "400", description = "Datos duplicados o validaciones fallidas", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponse.class))),
            @ApiResponse(responseCode = "404", description = "Estudiante, pensum o cohorte no encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponse.class)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<HttpResponse> actualizarEstudiante(
            @Parameter(description = "ID único del estudiante a actualizar", required = true, example = "1", schema = @Schema(type = "integer", minimum = "1")) @PathVariable Integer id,

            @Parameter(description = "Datos actualizados del estudiante", required = true, schema = @Schema(implementation = EstudianteDTO.class), example = """
                    {
                        "codigo": "20241001",
                        "nombre": "Juan Carlos",
                        "nombre2": "Andrés",
                        "apellido": "Pérez",
                        "apellido2": "García",
                        "email": "juan.perez.actualizado@ufps.edu.co",
                        "telefono": "3009876543",
                        "cedula": "1098765432",
                        "pensumId": 1,
                        "cohorteId": 2,
                        "moodleId": "54321"
                    }
                    """) @RequestBody EstudianteDTO estudianteDTO)
            throws UserNotFoundException, PensumNotFoundException,
            CohorteNotFoundException, EstadoEstudianteNotFoundException, EstudianteNotFoundException,
            EmailExistException, UserExistException {

        estudianteService.actualizarEstudiante(id, estudianteDTO);
        return new ResponseEntity<>(
                new HttpResponse(HttpStatus.OK.value(), HttpStatus.OK, HttpStatus.OK.getReasonPhrase(),
                        "Estudiante actualizado con exito"),
                HttpStatus.OK);
    }

    @Operation(summary = "Consultar estudiante específico", description = "Obtiene información completa de un estudiante incluyendo datos personales, académicos, "
            +
            "pensum, cohorte, programa y estado actual.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Información del estudiante obtenida exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = EstudianteResponse.class), examples = @ExampleObject(value = """
                    {
                        "id": 1,
                        "usuarioId": 15,
                        "codigo": "20241001",
                        "nombre": "Juan Carlos",
                        "nombre2": "Andrés",
                        "apellido": "Pérez",
                        "apellido2": "García",
                        "email": "juan.perez@ufps.edu.co",
                        "telefono": "3001234567",
                        "cedula": "1098765432",
                        "pensumId": 1,
                        "pensumNombre": "Pensum 2024 Maestría TIC",
                        "cohorteId": 1,
                        "cohorteNombre": "Cohorte 2024-I Maestría TIC",
                        "programaId": 1,
                        "programaNombre": "Maestría en TIC aplicadas a la Educación",
                        "estadoEstudianteId": 1,
                        "estadoEstudianteNombre": "Activo",
                        "moodleId": "12345",
                        "esPosgrado": true
                    }
                    """))),
            @ApiResponse(responseCode = "404", description = "Estudiante no encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponse.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<EstudianteResponse> listarEstudiante(
            @Parameter(description = "ID único del estudiante a consultar", required = true, example = "1", schema = @Schema(type = "integer", minimum = "1")) @PathVariable Integer id)
            throws EstudianteNotFoundException {

        EstudianteResponse estudianteResponse = estudianteService.listarEstudiante(id);
        return new ResponseEntity<>(estudianteResponse, HttpStatus.OK);
    }

    @Operation(summary = "Listar todos los estudiantes", description = "Obtiene el catálogo completo de estudiantes registrados en el sistema con información "
            +
            "básica de cada uno incluyendo datos académicos y de contacto.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de estudiantes obtenida exitosamente", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = EstudianteResponse.class))))
    })
    @GetMapping
    public ResponseEntity<List<EstudianteResponse>> listarEstudiantes() {
        List<EstudianteResponse> estudianteResponse = estudianteService.listarEstudiantes();
        return new ResponseEntity<>(estudianteResponse, HttpStatus.OK);
    }

    @Operation(summary = "Consultar estudiantes por pensum", description = "Filtra estudiantes que están asociados a un pensum específico. "
            +
            "Útil para análisis curriculares y gestión por plan de estudios.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de estudiantes por pensum obtenida exitosamente", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = EstudianteResponse.class)))),
            @ApiResponse(responseCode = "404", description = "Pensum no encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponse.class)))
    })
    @GetMapping("/listar/pensum/{pensumId}")
    public ResponseEntity<List<EstudianteResponse>> listarEstudiantesPorPensum(
            @Parameter(description = "ID del pensum para filtrar estudiantes", required = true, example = "1", schema = @Schema(type = "integer", minimum = "1")) @PathVariable Integer pensumId)
            throws PensumNotFoundException {

        List<EstudianteResponse> estudianteResponse = estudianteService.listarEstudiantesPorPensum(pensumId);
        return new ResponseEntity<>(estudianteResponse, HttpStatus.OK);
    }

    @Operation(summary = "Consultar estudiantes por cohorte", description = "Obtiene todos los estudiantes pertenecientes a una cohorte específica. "
            +
            "Esencial para gestión académica por grupos de ingreso.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de estudiantes por cohorte obtenida exitosamente", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = EstudianteResponse.class)))),
            @ApiResponse(responseCode = "404", description = "Cohorte no encontrada", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponse.class)))
    })
    @GetMapping("/listar/cohorte/{cohorteId}")
    public ResponseEntity<List<EstudianteResponse>> listarEstudiantesPorCohorte(
            @Parameter(description = "ID de la cohorte para filtrar estudiantes", required = true, example = "1", schema = @Schema(type = "integer", minimum = "1")) @PathVariable Integer cohorteId)
            throws CohorteNotFoundException {

        List<EstudianteResponse> estudianteResponse = estudianteService.listarEstudiantesPorCohorte(cohorteId);
        return new ResponseEntity<>(estudianteResponse, HttpStatus.OK);
    }

    @Operation(summary = "Consultar estudiantes por programa académico", description = "Filtra estudiantes matriculados en un programa específico (Maestría TIC, Analítica de Datos, etc.). "
            +
            "Útil para reportes institucionales y análisis por programa.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de estudiantes por programa obtenida exitosamente", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = EstudianteResponse.class)))),
            @ApiResponse(responseCode = "404", description = "Programa no encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponse.class)))
    })
    @GetMapping("/listar/programa/{programaId}")
    public ResponseEntity<List<EstudianteResponse>> listarEstudiantesPorPrograma(
            @Parameter(description = "ID del programa académico para filtrar estudiantes", required = true, example = "1", schema = @Schema(type = "integer", minimum = "1")) @PathVariable Integer programaId)
            throws ProgramaNotFoundException {

        List<EstudianteResponse> estudianteResponse = estudianteService.listarEstudiantesPorPrograma(programaId);
        return new ResponseEntity<>(estudianteResponse, HttpStatus.OK);
    }

    @Operation(summary = "Consultar estudiantes por estado académico", description = "Filtra estudiantes según su estado académico (Activo, Graduado, Retirado, etc.). "
            +
            "Solo incluye estudiantes de posgrado para el análisis.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de estudiantes por estado obtenida exitosamente", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = EstudianteResponse.class)))),
            @ApiResponse(responseCode = "404", description = "Estado de estudiante no encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponse.class)))
    })
    @GetMapping("/listar/estado/{estadoEstudianteId}")
    public ResponseEntity<List<EstudianteResponse>> listarEstudiantesPorEstado(
            @Parameter(description = "ID del estado académico para filtrar estudiantes", required = true, example = "1", schema = @Schema(type = "integer", minimum = "1")) @PathVariable Integer estadoEstudianteId)
            throws EstadoEstudianteNotFoundException {

        List<EstudianteResponse> estudianteResponse = estudianteService.listarEstudiantesPorEstado(estadoEstudianteId);
        return new ResponseEntity<>(estudianteResponse, HttpStatus.OK);
    }

    @Operation(summary = "Consultar estudiantes por grupo-cohorte", description = "Obtiene información de estudiantes en un grupo-cohorte específico incluyendo "
            +
            "datos del grupo y estadísticas. Útil para gestión de grupos académicos.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Información del grupo-cohorte con estudiantes obtenida exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = EstudianteGrupoResponse.class), examples = @ExampleObject(value = """
                    {
                        "grupoCohorteId": 1,
                        "grupoCohorteNombre": "Grupo A 2024-I",
                        "semestre": "2024-I",
                        "totalEstudiantes": 25,
                        "estudiantes": [
                            {
                                "id": 1,
                                "codigo": "20241001",
                                "nombreCompleto": "Juan Carlos Pérez García",
                                "email": "juan.perez@ufps.edu.co",
                                "programaNombre": "Maestría en TIC aplicadas a la Educación"
                            }
                        ]
                    }
                    """)))
    })
    @GetMapping("/grupo-cohorte/{grupoCohorteId}")
    public ResponseEntity<EstudianteGrupoResponse> listarEstudiantesPorGrupoCohorte(
            @Parameter(description = "ID del grupo-cohorte para consultar estudiantes", required = true, example = "1", schema = @Schema(type = "integer", format = "int64", minimum = "1")) @PathVariable Long grupoCohorteId) {

        EstudianteGrupoResponse estudianteGrupoResponse = iGrupoService
                .listarEstudiantesPorGrupoCohorte(grupoCohorteId);
        return new ResponseEntity<>(estudianteGrupoResponse, HttpStatus.OK);
    }

    @Operation(summary = "Consultar estudiantes matriculados actualmente en grupo-cohorte", description = "Obtiene estudiantes con matrícula activa (estado 'En curso') en un grupo-cohorte específico "
            +
            "para el semestre actual. Usado para listas de clase y gestión académica activa.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de estudiantes matriculados obtenida exitosamente", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = EstudianteResponse.class)))),
            @ApiResponse(responseCode = "404", description = "Grupo-cohorte no encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponse.class)))
    })
    @GetMapping("/matriculados/grupo-cohorte/{grupoCohorteId}")
    public ResponseEntity<List<EstudianteResponse>> listarEstudiantesPorGrupoCohorteConMatriculaEnCurso(
            @Parameter(description = "ID del grupo-cohorte para consultar estudiantes con matrícula activa", required = true, example = "1", schema = @Schema(type = "integer", format = "int64", minimum = "1")) @PathVariable Long grupoCohorteId)
            throws CohorteNotFoundException {

        List<EstudianteResponse> estudianteResponse = estudianteService
                .listarEstudiantesPorGrupoCohorteConMatriculaEnCurso(grupoCohorteId);
        return new ResponseEntity<>(estudianteResponse, HttpStatus.OK);
    }
}
