package com.sistemas_mangager_be.edu_virtual_ufps.controllers.oracle;

import com.sistemas_mangager_be.edu_virtual_ufps.oracle.entities.ProfesorOracle;
import com.sistemas_mangager_be.edu_virtual_ufps.oracle.repositories.ProfesorOracleRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@Profile("prod")
@RestController
@RequestMapping("/api/oracle/profesores")
@Tag(name = "Gestión de Profesores Oracle", description = "API para la consulta de información de profesores desde la base de datos Oracle de UFPS. "
        + "Proporciona acceso a datos académicos y personales de profesores para integración con sistemas externos.")
public class ProfesorOracleController {

    private final ProfesorOracleRepository profesorOracleRepository;

    public ProfesorOracleController(ProfesorOracleRepository profesorOracleRepository) {
        this.profesorOracleRepository = profesorOracleRepository;
    }

    @Operation(summary = "Listar todos los profesores", description = "Obtiene el catálogo completo de profesores registrados en la base de datos Oracle "
            + "incluyendo información personal, documento de identidad y datos de contacto.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de profesores obtenida exitosamente", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = ProfesorOracle.class)), examples = @ExampleObject(value = """
                                    [
                    {
                        "codProfesor": "03161",
                        "documento": "91177953",
                        "tipoDocumento": "CC",
                        "fechaNacimiento": "1969-10-01",
                        "nombre1": "JHON",
                        "nombre2": "JAIRO",
                        "apellido1": "RAMIREZ",
                        "apellido2": "MATEUS",
                        "emaili": "jhonjairorm@ufps.edu.co"
                    },
                    {
                        "codProfesor": "03278",
                        "documento": "88207433",
                        "tipoDocumento": "CC",
                        "fechaNacimiento": "1974-03-22",
                        "nombre1": "WILLIAM",
                        "nombre2": "RODRIGO",
                        "apellido1": "AVENDAÑO",
                        "apellido2": "CASTRO",
                        "emaili": "williamavendano@ufps.edu.co"
                    }
                        ]
                                    """)))
    })
    @GetMapping()
    public List<ProfesorOracle> obtenerTodos() {
        return profesorOracleRepository.findAll();
    }

    @Operation(summary = "Buscar profesor por código", description = "Busca un profesor específico en la base de datos Oracle utilizando su código único. "
            + "Retorna información completa del profesor si existe, o un array vacío si no se encuentra.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Búsqueda realizada exitosamente", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = ProfesorOracle.class)), examples = {
                    @ExampleObject(name = "Profesor encontrado", value = """
                            [
                                {
                                    "codProfesor": "03278",
                                    "documento": "88207433",
                                    "tipoDocumento": "CC",
                                    "fechaNacimiento": "1974-03-22",
                                    "nombre1": "WILLIAM",
                                    "nombre2": "RODRIGO",
                                    "apellido1": "AVENDAÑO",
                                    "apellido2": "CASTRO",
                                    "emaili": "williamavendano@ufps.edu.co"
                                }
                            ]
                            """),
                    @ExampleObject(name = "Profesor no encontrado", value = "[]")
            }))
    })
    @GetMapping("/buscar/codigo")
    public List<ProfesorOracle> buscarPorCodigo(
            @Parameter(description = "Código único del profesor a buscar", required = true, example = "03278", schema = @Schema(type = "string")) @RequestParam String codProfesor) {
        return profesorOracleRepository.findByCodProfesor(codProfesor);
    }
}
