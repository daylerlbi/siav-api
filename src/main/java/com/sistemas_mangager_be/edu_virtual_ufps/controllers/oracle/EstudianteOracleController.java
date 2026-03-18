package com.sistemas_mangager_be.edu_virtual_ufps.controllers.oracle;

import com.sistemas_mangager_be.edu_virtual_ufps.oracle.entities.EstudianteOracle;
import com.sistemas_mangager_be.edu_virtual_ufps.oracle.repositories.EstudianteOracleRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Profile;
import java.util.List;


@RestController
@RequestMapping("/api/oracle/estudiantes")
@Tag(name = "Gestión de Estudiantes Oracle", description = "API para la consulta de información de estudiantes desde la base de datos Oracle de UFPS. "
        + "Proporciona acceso a datos académicos y personales de estudiantes para integración con sistemas externos y análisis institucional.")
public class EstudianteOracleController {

    private final EstudianteOracleRepository estudianteOracleRepository;

    public EstudianteOracleController(EstudianteOracleRepository estudianteOracleRepository) {
        this.estudianteOracleRepository = estudianteOracleRepository;
    }

    @Operation(summary = "Listar todos los estudiantes", description = "Obtiene el catálogo completo de estudiantes registrados en la base de datos Oracle "
            + "incluyendo información personal, académica, carrera y estado de matrícula.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de estudiantes obtenida exitosamente", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = EstudianteOracle.class)), examples = @ExampleObject(value = """
                    [
                        {
                            "codigo": "0230273",
                            "nomCarrera": "CONTADURIA PUBLICA",
                            "primerNombre": "WILSON",
                            "segundoNombre": null,
                            "primerApellido": "BAENA",
                            "segundoApellido": "MARTIN",
                            "documento": "9596",
                            "tipoDocumento": "CC",
                            "fechaNacimiento": "1955-01-01",
                            "descTipoCar": "PREGRADO PRESENCIAL",
                            "email": "wilsonbm@ufps.edu.co",
                            "tmatriculado": "ACTIVADO PARA LIQUIDACION"
                        },
                        {
                            "codigo": "1390552",
                            "nomCarrera": "MAESTRIA EN PRACTICA PEDAGOGICA",
                            "primerNombre": "MAGDALENA",
                            "segundoNombre": null,
                            "primerApellido": "MENDOZA",
                            "segundoApellido": "ALBA",
                            "documento": "633565480",
                            "tipoDocumento": "CC",
                            "fechaNacimiento": "1970-04-23",
                            "descTipoCar": "POSTGRADOS - MAESTRIA",
                            "email": null,
                            "tmatriculado": "ACTIVADO PARA LIQUIDACION"
                        }
                    ]
                    """)))
    })
    @GetMapping
    public List<EstudianteOracle> obtenerTodos() {
        return estudianteOracleRepository.findAll();
    }

    @Operation(summary = "Listar estudiantes de Maestría en TIC", description = "Obtiene todos los estudiantes matriculados en el programa de Maestría en Tecnologías de la Información y la Comunicación (TIC) Aplicadas a la Educación.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de estudiantes de posgrado obtenida exitosamente", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = EstudianteOracle.class)), examples = @ExampleObject(value = """
                    [
                        {
                            "codigo": "2470348",
                            "nomCarrera": "MAESTRIA EN TECNOLOGIAS DE LA INFORMACION Y LA COMUNICACION (TIC) APLICADAS A LA EDUCACION",
                            "primerNombre": "TATIANA",
                            "segundoNombre": null,
                            "primerApellido": "MARAVELIAS",
                            "segundoApellido": "CORTES",
                            "documento": "52708295",
                            "tipoDocumento": "CC",
                            "fechaNacimiento": "1980-06-04",
                            "descTipoCar": "POSTGRADOS - MAESTRIA",
                            "email": "tatianamaco@ufps.edu.co",
                            "tmatriculado": "MATRICULA AUTOMATICA (1P)"
                        },
                        {
                            "codigo": "2470498",
                            "nomCarrera": "MAESTRIA EN TECNOLOGIAS DE LA INFORMACION Y LA COMUNICACION (TIC) APLICADAS A LA EDUCACION",
                            "primerNombre": "FERNANDO",
                            "segundoNombre": "ALONSO",
                            "primerApellido": "PEÑA",
                            "segundoApellido": "CEPEDA",
                            "documento": "5478215",
                            "tipoDocumento": "CC",
                            "fechaNacimiento": "1978-07-20",
                            "descTipoCar": "POSTGRADOS - MAESTRIA",
                            "email": null,
                            "tmatriculado": "MATRICULA AUTOMATICA (1P)"
                        }
                    ]
                    """)))
    })
    @GetMapping("/posgrado")
    public List<EstudianteOracle> obtenerEstudiantesMaestria() {
        return estudianteOracleRepository.findByNomCarrera(
                "MAESTRIA EN TECNOLOGIAS DE LA INFORMACION Y LA COMUNICACION (TIC) APLICADAS A LA EDUCACION");

    }

    @Operation(summary = "Buscar estudiante por código exacto", description = "Busca un estudiante específico en la base de datos Oracle utilizando su código exacto. "
            + "Retorna información completa del estudiante si existe, o un array vacío si no se encuentra.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Búsqueda realizada exitosamente", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = EstudianteOracle.class)), examples = {
                    @ExampleObject(name = "Estudiante encontrado", value = """
                            [
                                {
                                    "codigo": "2470348",
                                    "nomCarrera": "MAESTRIA EN TECNOLOGIAS DE LA INFORMACION Y LA COMUNICACION (TIC) APLICADAS A LA EDUCACION",
                                    "primerNombre": "TATIANA",
                                    "segundoNombre": null,
                                    "primerApellido": "MARAVELIAS",
                                    "segundoApellido": "CORTES",
                                    "documento": "52708295",
                                    "tipoDocumento": "CC",
                                    "fechaNacimiento": "1980-06-04",
                                    "descTipoCar": "POSTGRADOS - MAESTRIA",
                                    "email": "tatianamaco@ufps.edu.co",
                                    "tmatriculado": "MATRICULA AUTOMATICA (1P)"
                                }
                            ]
                            """),
                    @ExampleObject(name = "Estudiante no encontrado", value = "[]")
            }))
    })
    @GetMapping("/buscar/codigo")
    public List<EstudianteOracle> buscarPorCodigo(
            @Parameter(description = "Código exacto del estudiante a buscar", required = true, example = "2470348", schema = @Schema(type = "string")) @RequestParam String codigo) {
        return estudianteOracleRepository.findByCodigo(codigo);
    }

    @Operation(summary = "Buscar estudiantes por código que inicia con", description = "Busca todos los estudiantes cuyos códigos comienzan con la cadena especificada. "
            + "Útil para búsquedas por cohorte o patrones de código.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Búsqueda realizada exitosamente", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = EstudianteOracle.class)), examples = {
                    @ExampleObject(name = "Estudiantes encontrados", value = """
                            [
                                {
                                    "codigo": "2470474",
                                    "nomCarrera": "MAESTRIA EN TECNOLOGIAS DE LA INFORMACION Y LA COMUNICACION (TIC) APLICADAS A LA EDUCACION",
                                    "primerNombre": "YAMILE",
                                    "segundoNombre": null,
                                    "primerApellido": "TORRADO",
                                    "segundoApellido": "RINCON",
                                    "documento": "37324917",
                                    "tipoDocumento": "CC",
                                    "fechaNacimiento": "1972-08-12",
                                    "descTipoCar": "POSTGRADOS - MAESTRIA",
                                    "email": null,
                                    "tmatriculado": "MATRICULA AUTOMATICA (1P)"
                                },
                                {
                                    "codigo": "2470516",
                                    "nomCarrera": "MAESTRIA EN TECNOLOGIAS DE LA INFORMACION Y LA COMUNICACION (TIC) APLICADAS A LA EDUCACION",
                                    "primerNombre": "RICARDO",
                                    "segundoNombre": "JOSE",
                                    "primerApellido": "ATENCIA",
                                    "segundoApellido": "PEREZ",
                                    "documento": "78698153",
                                    "tipoDocumento": "CC",
                                    "fechaNacimiento": "1969-01-23",
                                    "descTipoCar": "POSTGRADOS - MAESTRIA",
                                    "email": null,
                                    "tmatriculado": "MATRICULA AUTOMATICA (1P)"
                                }
                            ]
                            """),
                    @ExampleObject(name = "Sin resultados", value = "[]")
            }))
    })
    @GetMapping("/buscar-por-codigo")
    public List<EstudianteOracle> buscarPorCodigoStarting(
            @Parameter(description = "Patrón inicial del código de estudiante para filtrar", required = true, example = "247", schema = @Schema(type = "string")) @RequestParam String codigo) {
        return estudianteOracleRepository.findByCodigoStartingWith(codigo);
    }

    @Operation(summary = "Listar estudiantes de Tecnología en Analítica de Datos", description = "Obtiene todos los estudiantes matriculados en el programa de Tecnología en Analítica de Datos "
            + "incluyendo información académica y estado de matrícula.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de estudiantes de analítica obtenida exitosamente", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = EstudianteOracle.class)), examples = @ExampleObject(value = """
                    [
                        {
                            "codigo": "2150001",
                            "nomCarrera": "TECNOLOGIA ANALITICA DE DATOS",
                            "primerNombre": "EDINSON",
                            "segundoNombre": "ANDRES",
                            "primerApellido": "AFANADOR",
                            "segundoApellido": "VALERO",
                            "documento": "1093791203",
                            "tipoDocumento": "CC",
                            "fechaNacimiento": "1997-07-10",
                            "descTipoCar": "VIRTUAL",
                            "email": "edinsonandresav@ufps.edu.co",
                            "tmatriculado": "MATRICULA INCLUSIONES-CANCELACIONES"
                        },
                        {
                            "codigo": "2150003",
                            "nomCarrera": "TECNOLOGIA ANALITICA DE DATOS",
                            "primerNombre": "STEFANY",
                            "segundoNombre": "JULIANA",
                            "primerApellido": "GOMEZ",
                            "segundoApellido": "TORRES",
                            "documento": "1092361325",
                            "tipoDocumento": "CC",
                            "fechaNacimiento": "1996-09-12",
                            "descTipoCar": "VIRTUAL",
                            "email": "stefanyjulianagt@ufps.edu.co",
                            "tmatriculado": "LIQUIDADO"
                        }
                    ]
                    """)))
    })
    @GetMapping("/analitica")
    public List<EstudianteOracle> obtenerEstudiantesSistemas() {
        return estudianteOracleRepository.findByNomCarrera("TECNOLOGIA ANALITICA DE DATOS");
    }

}