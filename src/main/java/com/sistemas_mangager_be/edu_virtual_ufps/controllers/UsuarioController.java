package com.sistemas_mangager_be.edu_virtual_ufps.controllers;

import com.sistemas_mangager_be.edu_virtual_ufps.exceptions.RoleNotFoundException;
import com.sistemas_mangager_be.edu_virtual_ufps.exceptions.UserExistException;
import com.sistemas_mangager_be.edu_virtual_ufps.exceptions.UserNotFoundException;
import com.sistemas_mangager_be.edu_virtual_ufps.services.interfaces.IUsuarioService;
import com.sistemas_mangager_be.edu_virtual_ufps.shared.DTOs.UsuarioDTO;
import com.sistemas_mangager_be.edu_virtual_ufps.shared.requests.DocenteRequest;
import com.sistemas_mangager_be.edu_virtual_ufps.shared.requests.LoginGoogleRequest;
import com.sistemas_mangager_be.edu_virtual_ufps.shared.requests.MoodleRequest;
import com.sistemas_mangager_be.edu_virtual_ufps.shared.responses.HttpResponse;
import com.sistemas_mangager_be.edu_virtual_ufps.shared.responses.UsuarioResponse;
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
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
@Tag(name = "Gestión de Usuarios del Sistema", description = "API para la administración integral de usuarios de SIAV UFPS. "
        +
        "Gestiona el registro automático vía Google OAuth2, creación manual de docentes, " +
        "validación de datos únicos (email, código, cédula, GoogleID), asignación de roles " +
        "y sincronización con plataforma Moodle.")
public class UsuarioController {

    private final IUsuarioService iUsuarioService;

    @Operation(summary = "Autenticar o registrar usuario con Google OAuth2", description = "Procesa el login con Google creando automáticamente usuarios nuevos o actualizando existentes. "
            +
            "Para usuarios nuevos asigna rol 'Estudiante' por defecto. Para usuarios existentes conserva " +
            "el rol asignado (especialmente docentes) y actualiza información de perfil (nombre, foto). " +
            "Valida unicidad de GoogleID para prevenir conflictos de autenticación.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario autenticado o registrado exitosamente con Google", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponse.class), examples = @ExampleObject(value = """
                    {
                        "httpStatusCode": 200,
                        "httpStatus": "OK",
                        "reason": "OK",
                        "message": "Usuario registrado con exito"
                    }
                    """))),
            @ApiResponse(responseCode = "400", description = "GoogleID ya está en uso por otro usuario", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponse.class), examples = @ExampleObject(value = """
                    {
                        "httpStatusCode": 400,
                        "httpStatus": "BAD_REQUEST",
                        "reason": "Bad Request",
                        "message": "El ID de Google 1234567890 ya esta registrado en el sistema"
                    }
                    """)))
    })
    @PostMapping("/google/login")
    public ResponseEntity<HttpResponse> loginGoogle(
            @Parameter(description = "Datos del usuario obtenidos de Google OAuth2 para autenticación/registro", required = true, schema = @Schema(implementation = LoginGoogleRequest.class), example = """
                    {
                        "email": "juan.perez@ufps.edu.co",
                        "googleId": "1234567890",
                        "nombre": "Juan Carlos Pérez García",
                        "fotoUrl": "https://lh3.googleusercontent.com/photo.jpg"
                    }
                    """) @RequestBody LoginGoogleRequest loginGoogleRequest)
            throws UserExistException {

        iUsuarioService.registraroActualizarUsuarioGoogle(loginGoogleRequest);
        return new ResponseEntity<>(
                new HttpResponse(HttpStatus.OK.value(), HttpStatus.OK, HttpStatus.OK.getReasonPhrase(),
                        " Usuario registrado con exito"),
                HttpStatus.OK);
    }

    @Operation(summary = "Crear nuevo docente en el sistema", description = "Registra un docente con rol específico y validaciones completas de unicidad. "
            +
            "Valida que email, código, cédula y GoogleID sean únicos en el sistema. " +
            "Asigna automáticamente rol 'Docente' (ID: 2) y genera nombre completo concatenando " +
            "todos los nombres y apellidos. Permite campos opcionales como código y cédula.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Docente creado exitosamente con rol asignado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsuarioDTO.class), examples = @ExampleObject(value = """
                    {
                        "id": 15,
                        "primerNombre": "Carlos",
                        "segundoNombre": "Eduardo",
                        "primerApellido": "López",
                        "segundoApellido": "Martínez",
                        "nombreCompleto": "Carlos Eduardo López Martínez",
                        "email": "carlos.lopez@ufps.edu.co",
                        "telefono": "3001234567",
                        "cedula": "1098765432",
                        "codigo": "DOC001",
                        "rolId": 2,
                        "googleId": "9876543210",
                        "moodleId": null
                    }
                    """))),
            @ApiResponse(responseCode = "400", description = "Datos duplicados - email, código, cédula o GoogleID ya existen", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponse.class), examples = @ExampleObject(value = """
                    {
                        "httpStatusCode": 400,
                        "httpStatus": "BAD_REQUEST",
                        "reason": "Bad Request",
                        "message": "El correo electrónico carlos.lopez@ufps.edu.co ya esta registrado en el sistema"
                    }
                    """))),
            @ApiResponse(responseCode = "404", description = "Rol de docente no encontrado en el sistema", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponse.class), examples = @ExampleObject(value = """
                    {
                        "httpStatusCode": 404,
                        "httpStatus": "NOT_FOUND",
                        "reason": "Not Found",
                        "message": "el rol docente no fue encontrado"
                    }
                    """)))
    })
    @PostMapping("/profesores/crear")
    public ResponseEntity<UsuarioDTO> crearProfesor(
            @Parameter(description = "Datos completos del docente a registrar con validaciones de unicidad", required = true, schema = @Schema(implementation = DocenteRequest.class), example = """
                    {
                        "primerNombre": "Carlos",
                        "segundoNombre": "Eduardo",
                        "primerApellido": "López",
                        "segundoApellido": "Martínez",
                        "email": "carlos.lopez@ufps.edu.co",
                        "telefono": "3001234567",
                        "cedula": "1098765432",
                        "codigo": "DOC001",
                        "googleId": "9876543210"
                    }
                    """) @RequestBody DocenteRequest docenteRequest)
            throws UserExistException, RoleNotFoundException {

        UsuarioDTO usuario = iUsuarioService.crearProfesor(docenteRequest);
        return new ResponseEntity<>(usuario, HttpStatus.OK);
    }

    @Operation(summary = "Vincular docente con usuario de Moodle", description = "Establece la conexión entre un docente del sistema y su usuario correspondiente en Moodle. "
            +
            "Valida que el usuario tenga rol de docente (ID: 2) y que el MoodleID no esté en uso. " +
            "Esta vinculación es necesaria para la sincronización de cursos y calificaciones.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vinculación con Moodle realizada exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponse.class), examples = @ExampleObject(value = """
                    {
                        "httpStatusCode": 200,
                        "httpStatus": "OK",
                        "reason": "OK",
                        "message": "Vinculacion con moodle realizada con exito"
                    }
                    """))),
            @ApiResponse(responseCode = "400", description = "MoodleID ya está en uso por otro usuario", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponse.class), examples = @ExampleObject(value = """
                    {
                        "httpStatusCode": 400,
                        "httpStatus": "BAD_REQUEST",
                        "reason": "Bad Request",
                        "message": "El ID de Moodle TEACHER_123 ya esta registrado en el sistema"
                    }
                    """))),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado o no tiene rol de docente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponse.class), examples = @ExampleObject(value = """
                    {
                        "httpStatusCode": 404,
                        "httpStatus": "NOT_FOUND",
                        "reason": "Not Found",
                        "message": "El usuario no tiene rol de profesor"
                    }
                    """)))
    })
    @PostMapping("/profesores/moodle")
    public ResponseEntity<HttpResponse> vincularEstudianteMoodle(
            @Parameter(description = "Información para vincular docente con usuario de Moodle", required = true, schema = @Schema(implementation = MoodleRequest.class), example = """
                    {
                        "backendId": 15,
                        "moodleId": "TEACHER_123"
                    }
                    """) @RequestBody MoodleRequest moodleRequest)
            throws UserNotFoundException, UserExistException {

        iUsuarioService.vincularMoodleId(moodleRequest);
        return new ResponseEntity<>(
                new HttpResponse(HttpStatus.OK.value(), HttpStatus.OK, HttpStatus.OK.getReasonPhrase(),
                        " Vinculacion con moodle realizada con exito"),
                HttpStatus.OK);
    }

    @Operation(summary = "Actualizar información de docente", description = "Modifica los datos de un docente existente con validaciones de unicidad solo en campos modificados. "
            +
            "Valida que el usuario tenga rol de docente antes de permitir la actualización. " +
            "Regenera automáticamente el nombre completo al cambiar nombres o apellidos. " +
            "Mantiene la asignación de rol docente tras la actualización.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Docente actualizado exitosamente con validaciones completadas", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponse.class), examples = @ExampleObject(value = """
                    {
                        "httpStatusCode": 200,
                        "httpStatus": "OK",
                        "reason": "OK",
                        "message": "Docente actualizado con exito"
                    }
                    """))),
            @ApiResponse(responseCode = "400", description = "Datos duplicados en campos modificados", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponse.class), examples = @ExampleObject(value = """
                    {
                        "httpStatusCode": 400,
                        "httpStatus": "BAD_REQUEST",
                        "reason": "Bad Request",
                        "message": "El código DOC002 ya esta registrado en el sistema"
                    }
                    """))),
            @ApiResponse(responseCode = "404", description = "Docente no encontrado o usuario sin rol de docente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponse.class), examples = @ExampleObject(value = """
                    {
                        "httpStatusCode": 404,
                        "httpStatus": "NOT_FOUND",
                        "reason": "Not Found",
                        "message": "el profesor con id 15 no fue encontrado"
                    }
                    """)))
    })
    @PutMapping("/profesores/{id}")
    public ResponseEntity<HttpResponse> actualizarProfesor(
            @Parameter(description = "Nuevos datos del docente con validaciones inteligentes de unicidad", required = true, schema = @Schema(implementation = DocenteRequest.class), example = """
                    {
                        "primerNombre": "Carlos Eduardo",
                        "segundoNombre": "José",
                        "primerApellido": "López",
                        "segundoApellido": "Martínez",
                        "email": "carlos.lopez.nuevo@ufps.edu.co",
                        "telefono": "3009876543",
                        "cedula": "1098765432",
                        "codigo": "DOC001",
                        "googleId": "9876543210"
                    }
                    """) @RequestBody DocenteRequest docenteRequest,

            @Parameter(description = "ID único del docente a actualizar", required = true, example = "15", schema = @Schema(type = "integer", minimum = "1")) @PathVariable Integer id)
            throws UserExistException, RoleNotFoundException, UserNotFoundException {

        iUsuarioService.actualizarProfesor(docenteRequest, id);
        return new ResponseEntity<>(
                new HttpResponse(HttpStatus.OK.value(), HttpStatus.OK, HttpStatus.OK.getReasonPhrase(),
                        " Docente actualizado con exito"),
                HttpStatus.OK);
    }

    @Operation(summary = "Consultar usuarios por rol específico", description = "Filtra todos los usuarios que tienen un rol determinado en el sistema. "
            +
            "Roles disponibles: 1=Estudiante, 2=Docente, 3=Administrador. " +
            "Incluye información completa del usuario y datos de integración con Moodle.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de usuarios por rol obtenida exitosamente", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = UsuarioResponse.class)), examples = @ExampleObject(value = """
                    [
                        {
                            "id": 15,
                            "primerNombre": "Carlos",
                            "segundoNombre": "Eduardo",
                            "primerApellido": "López",
                            "segundoApellido": "Martínez",
                            "nombreCompleto": "Carlos Eduardo López Martínez",
                            "email": "carlos.lopez@ufps.edu.co",
                            "telefono": "3001234567",
                            "cedula": "1098765432",
                            "codigo": "DOC001",
                            "rol": "Docente",
                            "googleId": "9876543210",
                            "moodleId": "TEACHER_123",
                            "fotoUrl": "https://lh3.googleusercontent.com/photo.jpg"
                        }
                    ]
                    """))),
            @ApiResponse(responseCode = "404", description = "Rol no encontrado en el sistema", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponse.class), examples = @ExampleObject(value = """
                    {
                        "httpStatusCode": 404,
                        "httpStatus": "NOT_FOUND",
                        "reason": "Not Found",
                        "message": "el rol con id 5 no fue encontrado"
                    }
                    """)))
    })
    @GetMapping("/rol/{rolId}")
    public ResponseEntity<List<UsuarioResponse>> listarUsuariosPorRol(
            @Parameter(description = "ID del rol para filtrar usuarios (1=Estudiante, 2=Docente, 3=Administrador)", required = true, example = "2", schema = @Schema(type = "integer", minimum = "1", maximum = "3")) @PathVariable Integer rolId)
            throws RoleNotFoundException {

        List<UsuarioResponse> usuarioResponse = iUsuarioService.listarUsuariosPorRol(rolId);
        return new ResponseEntity<>(usuarioResponse, HttpStatus.OK);
    }

    @Operation(summary = "Consultar usuario específico", description = "Obtiene información completa de un usuario incluyendo datos personales, "
            +
            "rol asignado, información de contacto y datos de integración con sistemas externos " +
            "(Google OAuth2 y Moodle).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Información del usuario obtenida exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsuarioResponse.class), examples = @ExampleObject(value = """
                    {
                        "id": 15,
                        "primerNombre": "Carlos",
                        "segundoNombre": "Eduardo",
                        "primerApellido": "López",
                        "segundoApellido": "Martínez",
                        "nombreCompleto": "Carlos Eduardo López Martínez",
                        "email": "carlos.lopez@ufps.edu.co",
                        "telefono": "3001234567",
                        "cedula": "1098765432",
                        "codigo": "DOC001",
                        "rol": "Docente",
                        "googleId": "9876543210",
                        "moodleId": "TEACHER_123",
                        "fotoUrl": "https://lh3.googleusercontent.com/photo.jpg"
                    }
                    """))),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponse.class), examples = @ExampleObject(value = """
                    {
                        "httpStatusCode": 404,
                        "httpStatus": "NOT_FOUND",
                        "reason": "Not Found",
                        "message": "el usuario con id 15 no fue encontrado"
                    }
                    """)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponse> listarUsuario(
            @Parameter(description = "ID único del usuario a consultar", required = true, example = "15", schema = @Schema(type = "integer", minimum = "1")) @PathVariable Integer id)
            throws UserNotFoundException {

        UsuarioResponse usuarioResponse = iUsuarioService.listarUsuario(id);
        return new ResponseEntity<>(usuarioResponse, HttpStatus.OK);
    }

    @Operation(summary = "Listar todos los usuarios del sistema", description = "Obtiene el catálogo completo de usuarios registrados incluyendo estudiantes, "
            +
            "docentes y administradores. Incluye información de roles, datos de contacto " +
            "y estado de integración con sistemas externos (Google OAuth2 y Moodle).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista completa de usuarios obtenida exitosamente", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = UsuarioResponse.class))))
    })
    @GetMapping
    public ResponseEntity<List<UsuarioResponse>> listarUsuarios() {
        List<UsuarioResponse> usuarioResponse = iUsuarioService.listarUsuarios();
        return new ResponseEntity<>(usuarioResponse, HttpStatus.OK);
    }
}
