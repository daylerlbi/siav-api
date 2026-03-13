package com.sistemas_mangager_be.edu_virtual_ufps.controllers.divisist;

import com.sistemas_mangager_be.edu_virtual_ufps.exceptions.PensumNotFoundException;
import com.sistemas_mangager_be.edu_virtual_ufps.services.interfaces.divisist.IDivisistService;
import com.sistemas_mangager_be.edu_virtual_ufps.shared.DTOs.divisist.*;
import com.sistemas_mangager_be.edu_virtual_ufps.shared.responses.divisist.NotasDivisistResponse;
import com.sistemas_mangager_be.edu_virtual_ufps.shared.responses.HttpResponse;
import io.swagger.v3.oas.annotations.Operation;
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
@RequestMapping("/api/divisist")
@RequiredArgsConstructor
@Tag(name = "Gestión de DIVISIST", description = "API para la sincronización e integración con el sistema DIVISIST de UFPS. "
        + "Gestiona la creación y actualización de datos académicos incluyendo estudiantes, profesores, materias, grupos y notas.")
public class DivisistController {

    private final IDivisistService divisistService;

    @PostMapping("/estudiante")
    public ResponseEntity<EstudianteDivisistDTO> crearOActualizarEstudiante(
            @RequestBody EstudianteDivisistDTO estudianteDivisistDTO) {
        EstudianteDivisistDTO estudiante = divisistService.crearoActualizarEstudiante(estudianteDivisistDTO);
        return new ResponseEntity<>(estudiante, HttpStatus.OK);
    }

    @Operation(summary = "Crear o actualizar profesor", description = "Crea un nuevo profesor o actualiza uno existente en el sistema DIVISIST. "
            + "La operación determina automáticamente si debe crear o actualizar basándose en el código del profesor.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Profesor creado o actualizado exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProfesorDivisistDTO.class), examples = @ExampleObject(value = """
                    {
                        "codigo": "03278",
                        "nomCarrera": "INGENIERIA DE SISTEMAS",
                        "primerNombre": "William",
                        "segundoNombre": "Rodrigo",
                        "primerApellido": "Avendaño",
                        "segundoApellido": "Castro",
                        "documento": "88207433",
                        "tipoDocumento": "CC",
                        "fechaNacimiento": "1974-03-22",
                        "descTipoCar": "PRESENCIAL",
                        "email": "williamavendano@ufps.edu.co",
                        "moodleId": "12345",
                        "activo": true
                    }
                    """))),
            @ApiResponse(responseCode = "500", description = "Error cuando no se proporciona el request body requerido", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponse.class), examples = @ExampleObject(value = """
                    {
                        "httpStatus": "INTERNAL_SERVER_ERROR",
                        "reason": "Internal Server Error",
                        "message": "REQUIRED REQUEST BODY IS MISSING: PUBLIC ORG.SPRINGFRAMEWORK.HTTP.RESPONSEENTITY<COM.SISTEMAS_MANGAGER_BE.EDU_VIRTUAL_UFPS.SHARED.DTOS.DIVISIST.PROFESORDIVISISTDTO> COM.SISTEMAS_MANGAGER_BE.EDU_VIRTUAL_UFPS.CONTROLLERS.DIVISIST.DIVISISTCONTROLLER.CREAROACTUALIZARPROFESOR(COM.SISTEMAS_MANGAGER_BE.EDU_VIRTUAL_UFPS.SHARED.DTOS.DIVISIST.PROFESORDIVISISTDTO)",
                        "httpStatusCode": 500
                    }
                    """)))
    })
    @PostMapping("/profesor")
    public ResponseEntity<ProfesorDivisistDTO> crearOActualizarProfesor(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Información completa del profesor para crear o actualizar en DIVISIST", required = true, content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProfesorDivisistDTO.class), examples = @ExampleObject(name = "Ejemplo de profesor", value = """
                                        {
                        "codigo": "20211001",
                        "nomCarrera": "INGENIERIA DE SISTEMAS",
                        "primerNombre": "Juan",
                        "segundoNombre": "Carlos",
                        "primerApellido": "Pérez",
                        "segundoApellido": "González",
                        "documento": "1098765432",
                        "tipoDocumento": "CC",
                        "fechaNacimiento": "2000-05-15",
                        "tMatriculado": "PRIMER_INGRESO",
                        "descTipoCar": "PRESENCIAL",
                        "email": "juan.perez@ufps.edu.co",
                        "moodleId": "12345",
                        "activo": true
                    }
                                        """))) @RequestBody ProfesorDivisistDTO profesorDivisistDTO) {
        ProfesorDivisistDTO profesor = divisistService.crearOActualizarProfesor(profesorDivisistDTO);
        return new ResponseEntity<>(profesor, HttpStatus.OK);
    }

    @PostMapping("/materia")
    public ResponseEntity<MateriaDivisistDTO> crearOActualizarMateria(
            @RequestBody MateriaDivisistDTO materiaDivisistDTO) throws PensumNotFoundException {
        MateriaDivisistDTO materia = divisistService.crearOActualizarMateria(materiaDivisistDTO);
        return new ResponseEntity<>(materia, HttpStatus.OK);
    }

    @PostMapping("/grupo")
    public ResponseEntity<GrupoDivisistDTO> crearOActualizarGrupo(@RequestBody GrupoDivisistDTO grupoDivisistDTO) {
        GrupoDivisistDTO grupo = divisistService.crearOActualizarGrupo(grupoDivisistDTO);
        return new ResponseEntity<>(grupo, HttpStatus.OK);
    }

    @PostMapping("/materia-matriculada")
    public ResponseEntity<MateriaMatriculadaDivisistDTO> crearOActualizarMateriaMatriculada(
            @RequestBody MateriaMatriculadaDivisistDTO materiaMatriculadaDivisistDTO) {
        MateriaMatriculadaDivisistDTO materiaMatriculada = divisistService
                .crearOActualizarMateriaMatriculada(materiaMatriculadaDivisistDTO);
        return new ResponseEntity<>(materiaMatriculada, HttpStatus.OK);
    }

    @PostMapping("/nota")
    public ResponseEntity<NotaDivisistDTO> crearOActualizarNota(@RequestBody NotaDivisistDTO notaDivisistDTO) {
        NotaDivisistDTO nota = divisistService.crearOActualizarNota(notaDivisistDTO);
        return new ResponseEntity<>(nota, HttpStatus.OK);
    }

    @GetMapping("/notas/{codAlumno}")
    public ResponseEntity<List<NotasDivisistResponse>> listarNotasPorEstudianteSemestreActual(
            @PathVariable String codAlumno) {
        List<NotasDivisistResponse> notas = divisistService.listarNotasPorEstudianteSemestreActual(codAlumno);
        return new ResponseEntity<>(notas, HttpStatus.OK);
    }

    @PatchMapping("/nota")
    public ResponseEntity<NotaDivisistDTO> actualizarNotaParcial(@RequestBody ActualizarNotaDivisistRequest request) {
        NotaDivisistDTO nota = divisistService.actualizarNotaParcial(request);
        return new ResponseEntity<>(nota, HttpStatus.OK);
    }

    @GetMapping("/materia/{codCarrera}/{codMateria}")
    public ResponseEntity<MateriaDivisistDTO> buscarMateriaPorCarreraYCodigo(
            @PathVariable String codCarrera,
            @PathVariable String codMateria) {
        MateriaDivisistDTO materia = divisistService.buscarMateriaPorCarreraYCodigo(codCarrera, codMateria);

        if (materia == null) {
            // Retornar un objeto vacío en lugar de 404
            materia = new MateriaDivisistDTO();
        }

        return new ResponseEntity<>(materia, HttpStatus.OK);
    }

    @GetMapping("/grupo/{codCarrera}/{codMateria}/{grupo}")
    public ResponseEntity<GrupoDivisistDTO> buscarGrupoPorCarreraMateriaYGrupo(
            @PathVariable String codCarrera,
            @PathVariable String codMateria,
            @PathVariable String grupo) {
        GrupoDivisistDTO grupoDTO = divisistService.buscarGrupoPorCarreraMateriaYGrupo(codCarrera, codMateria, grupo);

        if (grupoDTO == null) {
            // Retornar un objeto vacío en lugar de 404
            grupoDTO = new GrupoDivisistDTO();
        }

        return new ResponseEntity<>(grupoDTO, HttpStatus.OK);
    }

    @GetMapping("/profesor/{codProfesor}")
    public ResponseEntity<ProfesorDivisistDTO> buscarProfesorPorCodigo(@PathVariable String codProfesor) {
        ProfesorDivisistDTO profesorDTO = divisistService.buscarProfesorPorCodigo(codProfesor);

        if (profesorDTO == null) {
            // Retornar un objeto vacío en lugar de 404
            profesorDTO = new ProfesorDivisistDTO();
        }

        return new ResponseEntity<>(profesorDTO, HttpStatus.OK);
    }

    @GetMapping("/estudiante/{codigo}")
    public ResponseEntity<EstudianteDivisistDTO> buscarEstudiantePorCodigo(@PathVariable String codigo) {
        EstudianteDivisistDTO estudianteDTO = divisistService.buscarEstudiantePorCodigo(codigo);

        if (estudianteDTO == null) {
            // Retornar un objeto vacío en lugar de 404
            estudianteDTO = new EstudianteDivisistDTO();
        }

        return new ResponseEntity<>(estudianteDTO, HttpStatus.OK);
    }

    @GetMapping("/nota/{codAlumno}/{codCarrera}/{codMateria}/{grupo}")
    public ResponseEntity<NotaDivisistDTO> buscarNotaPorAlumnoCarreraMateriaGrupo(
            @PathVariable String codAlumno,
            @PathVariable String codCarrera,
            @PathVariable String codMateria,
            @PathVariable String grupo) {
        NotaDivisistDTO notaDTO = divisistService.buscarNotaPorAlumnoCarreraMateriaGrupo(codAlumno, codCarrera,
                codMateria, grupo);

        if (notaDTO == null) {
            // Retornar un objeto vacío en lugar de 404
            notaDTO = new NotaDivisistDTO();
        }

        return new ResponseEntity<>(notaDTO, HttpStatus.OK);
    }

    @GetMapping("/notas-grupo/{codCarrera}/{codMateria}/{grupo}")
    public ResponseEntity<List<NotasDivisistResponse>> listarNotasPorGrupoSemestreActual(
            @PathVariable String codCarrera,
            @PathVariable String codMateria,
            @PathVariable String grupo) {
        List<NotasDivisistResponse> notas = divisistService.listarNotasPorGrupoSemestreActual(codCarrera, codMateria,
                grupo);
        return new ResponseEntity<>(notas, HttpStatus.OK);
    }

    @GetMapping("/notas-semestre-actual")
    public ResponseEntity<List<NotasDivisistResponse>> listarNotasPorSemestreActual() {
        List<NotasDivisistResponse> notas = divisistService.listarNotasPorSemestreActual();
        return new ResponseEntity<>(notas, HttpStatus.OK);
    }
}
