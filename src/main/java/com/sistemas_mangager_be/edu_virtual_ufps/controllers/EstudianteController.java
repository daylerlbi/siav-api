package com.sistemas_mangager_be.edu_virtual_ufps.controllers;

import com.sistemas_mangager_be.edu_virtual_ufps.entities.Materia;
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
            @RequestBody EstudianteDTO estudianteDTO)
            throws PensumNotFoundException, CohorteNotFoundException,
            EstadoEstudianteNotFoundException, RoleNotFoundException, UserExistException {
        EstudianteDTO estudiante = estudianteService.crearEstudiante(estudianteDTO);
        return new ResponseEntity<>(estudiante, HttpStatus.OK);
    }

    @PostMapping("/moodle")
    public ResponseEntity<HttpResponse> vincularEstudianteMoodle(
            @RequestBody MoodleRequest moodleRequest)
            throws EstudianteNotFoundException, UserExistException {
        estudianteService.vincularMoodleId(moodleRequest);
        return new ResponseEntity<>(
                new HttpResponse(HttpStatus.OK.value(), HttpStatus.OK, HttpStatus.OK.getReasonPhrase(),
                        "Vinculacion con moodle realizada con exito"),
                HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<HttpResponse> actualizarEstudiante(
            @PathVariable Integer id,
            @RequestBody EstudianteDTO estudianteDTO)
            throws UserNotFoundException, PensumNotFoundException,
            CohorteNotFoundException, EstadoEstudianteNotFoundException, EstudianteNotFoundException,
            EmailExistException, UserExistException {
        estudianteService.actualizarEstudiante(id, estudianteDTO);
        return new ResponseEntity<>(
                new HttpResponse(HttpStatus.OK.value(), HttpStatus.OK, HttpStatus.OK.getReasonPhrase(),
                        "Estudiante actualizado con exito"),
                HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EstudianteResponse> listarEstudiante(
            @PathVariable Integer id)
            throws EstudianteNotFoundException {
        EstudianteResponse estudianteResponse = estudianteService.listarEstudiante(id);
        return new ResponseEntity<>(estudianteResponse, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<EstudianteResponse>> listarEstudiantes() {
        List<EstudianteResponse> estudianteResponse = estudianteService.listarEstudiantes();
        return new ResponseEntity<>(estudianteResponse, HttpStatus.OK);
    }

    @GetMapping("/listar/pensum/{pensumId}")
    public ResponseEntity<List<EstudianteResponse>> listarEstudiantesPorPensum(
            @PathVariable Integer pensumId)
            throws PensumNotFoundException {
        List<EstudianteResponse> estudianteResponse = estudianteService.listarEstudiantesPorPensum(pensumId);
        return new ResponseEntity<>(estudianteResponse, HttpStatus.OK);
    }

    @GetMapping("/listar/cohorte/{cohorteId}")
    public ResponseEntity<List<EstudianteResponse>> listarEstudiantesPorCohorte(
            @PathVariable Integer cohorteId)
            throws CohorteNotFoundException {
        List<EstudianteResponse> estudianteResponse = estudianteService.listarEstudiantesPorCohorte(cohorteId);
        return new ResponseEntity<>(estudianteResponse, HttpStatus.OK);
    }

    @GetMapping("/listar/programa/{programaId}")
    public ResponseEntity<List<EstudianteResponse>> listarEstudiantesPorPrograma(
            @PathVariable Integer programaId)
            throws ProgramaNotFoundException {
        List<EstudianteResponse> estudianteResponse = estudianteService.listarEstudiantesPorPrograma(programaId);
        return new ResponseEntity<>(estudianteResponse, HttpStatus.OK);
    }

    @GetMapping("/listar/estado/{estadoEstudianteId}")
    public ResponseEntity<List<EstudianteResponse>> listarEstudiantesPorEstado(
            @PathVariable Integer estadoEstudianteId)
            throws EstadoEstudianteNotFoundException {
        List<EstudianteResponse> estudianteResponse = estudianteService.listarEstudiantesPorEstado(estadoEstudianteId);
        return new ResponseEntity<>(estudianteResponse, HttpStatus.OK);
    }

    @GetMapping("/grupo-cohorte/{grupoCohorteId}")
    public ResponseEntity<EstudianteGrupoResponse> listarEstudiantesPorGrupoCohorte(
            @PathVariable Long grupoCohorteId) {
        EstudianteGrupoResponse estudianteGrupoResponse = iGrupoService
                .listarEstudiantesPorGrupoCohorte(grupoCohorteId);
        return new ResponseEntity<>(estudianteGrupoResponse, HttpStatus.OK);
    }

    @GetMapping("/matriculados/grupo-cohorte/{grupoCohorteId}")
    public ResponseEntity<List<EstudianteResponse>> listarEstudiantesPorGrupoCohorteConMatriculaEnCurso(
            @PathVariable Long grupoCohorteId)
            throws CohorteNotFoundException {
        List<EstudianteResponse> estudianteResponse = estudianteService
                .listarEstudiantesPorGrupoCohorteConMatriculaEnCurso(grupoCohorteId);
        return new ResponseEntity<>(estudianteResponse, HttpStatus.OK);
    }

    @GetMapping("/email/{email:.+}")
    public ResponseEntity<EstudianteResponse> listarEstudiantePorEmail(
            @PathVariable String email) throws EstudianteNotFoundException {
        EstudianteResponse estudianteResponse = estudianteService.listarEstudiantePorEmail(email);
        return new ResponseEntity<>(estudianteResponse, HttpStatus.OK);
    }

    @GetMapping("/{id}/materias")
    public ResponseEntity<List<Materia>> listarMateriasPorEstudiante(
            @PathVariable Integer id) throws EstudianteNotFoundException {
        List<Materia> materias = estudianteService.listarMateriasPorEstudiante(id);
        return new ResponseEntity<>(materias, HttpStatus.OK);
    }
}




