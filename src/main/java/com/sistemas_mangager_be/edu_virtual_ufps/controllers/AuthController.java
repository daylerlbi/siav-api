package com.sistemas_mangager_be.edu_virtual_ufps.controllers;

import com.sistemas_mangager_be.edu_virtual_ufps.exceptions.*;
import com.sistemas_mangager_be.edu_virtual_ufps.security.JwtTokenGenerator;
import com.sistemas_mangager_be.edu_virtual_ufps.services.interfaces.IAdminService;
import com.sistemas_mangager_be.edu_virtual_ufps.services.moodle.MoodleService;
import com.sistemas_mangager_be.edu_virtual_ufps.shared.DTOs.AdminDTO;
import com.sistemas_mangager_be.edu_virtual_ufps.shared.requests.LoginRequest;
import com.sistemas_mangager_be.edu_virtual_ufps.shared.requests.NuevaPasswordRequest;
import com.sistemas_mangager_be.edu_virtual_ufps.shared.responses.AuthResponse;
import com.sistemas_mangager_be.edu_virtual_ufps.shared.responses.HttpResponse;
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
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Autenticación y Gestión de Administradores", description = "API para la gestión completa del sistema de autenticación y administradores de SIAV UFPS. "
                +
                "Gestiona el login con JWT, registro y administración de usuarios administradores, " +
                "recuperación de contraseñas mediante tokens temporales, activación/desactivación de cuentas " +
                "y protección de super-administradores del sistema.")
public class AuthController {

        private final AuthenticationManager authenticationManager;
        private final JwtTokenGenerator jwtTokenGenerator;
        private final IAdminService adminService;
        private final MoodleService moodleService;

        @Operation(summary = "Autenticar administrador en el sistema", description = "Valida las credenciales del administrador y genera tokens de acceso JWT para sesiones autenticadas. "
                        +
                        "Utiliza AuthenticationManager para validar email/contraseña encriptada, verifica que la cuenta "
                        +
                        "esté activa y genera tanto AccessToken (corta duración) como RefreshToken (larga duración) " +
                        "para manejo seguro de sesiones. Establece el contexto de seguridad para requests posteriores.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Autenticación exitosa con tokens JWT generados", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AuthResponse.class), examples = @ExampleObject(value = """
                                        {
                                            "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
                                            "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
                                        }
                                        """))),
                        @ApiResponse(responseCode = "401", description = "Credenciales inválidas o cuenta desactivada", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponse.class), examples = @ExampleObject(value = """
                                        {
                                            "httpStatusCode": 401,
                                            "httpStatus": "UNAUTHORIZED",
                                            "reason": "Unauthorized",
                                            "message": "Email o contraseña incorrectos"
                                        }
                                        """))),
                        @ApiResponse(responseCode = "403", description = "Cuenta de administrador desactivada", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponse.class), examples = @ExampleObject(value = """
                                        {
                                            "httpStatusCode": 403,
                                            "httpStatus": "FORBIDDEN",
                                            "reason": "Forbidden",
                                            "message": "La cuenta del administrador está desactivada"
                                        }
                                        """)))
        })
        @PostMapping("/login")
        public ResponseEntity<AuthResponse> login(
                        @Parameter(description = "Credenciales del administrador para autenticación JWT", required = true, schema = @Schema(implementation = LoginRequest.class), example = """
                                        {
                                            "email": "admin@ufps.edu.co",
                                            "password": "admin123"
                                        }
                                        """) @RequestBody LoginRequest loginRequest) {

                Authentication authentication = authenticationManager.authenticate(
                                new UsernamePasswordAuthenticationToken(
                                                loginRequest.getEmail(),
                                                loginRequest.getPassword()));

                SecurityContextHolder.getContext().setAuthentication(authentication);

                String accessToken = jwtTokenGenerator.generarToken(authentication);
                String refreshToken = jwtTokenGenerator.generarRefreshToken(authentication);

                return new ResponseEntity<>(new AuthResponse(accessToken, refreshToken), HttpStatus.OK);
        }

        @Operation(summary = "Registrar nuevo administrador", description = "Crea una nueva cuenta de administrador en el sistema con validaciones de unicidad. "
                        +
                        "Valida que el email no esté en uso, encripta la contraseña con BCrypt, " +
                        "establece el estado activo por defecto y almacena el administrador en base de datos. " +
                        "El nuevo administrador podrá autenticarse inmediatamente tras el registro.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Administrador registrado exitosamente con contraseña encriptada", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponse.class), examples = @ExampleObject(value = """
                                        {
                                            "httpStatusCode": 200,
                                            "httpStatus": "OK",
                                            "reason": "OK",
                                            "message": "Administrador registrado con exito"
                                        }
                                        """))),
                        @ApiResponse(responseCode = "400", description = "Email ya registrado en el sistema", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponse.class), examples = @ExampleObject(value = """
                                        {
                                            "httpStatusCode": 400,
                                            "httpStatus": "BAD_REQUEST",
                                            "reason": "Bad Request",
                                            "message": "El correo ya esta en uso"
                                        }
                                        """)))
        })
        @PostMapping("/register")
        public ResponseEntity<HttpResponse> registrarUsuario(
                        @Parameter(description = "Datos del nuevo administrador con validación de email único", required = true, schema = @Schema(implementation = AdminDTO.class), example = """
                                        {
                                            "nombre": "Juan Carlos Pérez",
                                            "email": "juan.perez@ufps.edu.co",
                                            "password": "admin123",
                                            "esSuperAdmin": false
                                        }
                                        """) @RequestBody AdminDTO adminDTO) {

                adminService.registrarAdmin(adminDTO);
                return new ResponseEntity<>(
                                new HttpResponse(HttpStatus.OK.value(), HttpStatus.OK, HttpStatus.OK.getReasonPhrase(),
                                                " Administrador registrado con exito"),
                                HttpStatus.OK);
        }

        @Operation(summary = "Listar todos los administradores", description = "Obtiene el catálogo completo de administradores del sistema incluyendo información "
                        +
                        "de estado (activo/inactivo), privilegios de super-administrador y datos de contacto. " +
                        "Útil para gestión de usuarios administrativos y control de accesos al sistema.", security = @SecurityRequirement(name = "JWT"))
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Lista de administradores obtenida exitosamente", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = AdminDTO.class)), examples = @ExampleObject(value = """
                                        [
                                            {
                                                "id": 1,
                                                "nombre": "Super Administrador",
                                                "email": "superadmin@ufps.edu.co",
                                                "activo": true,
                                                "esSuperAdmin": true
                                            },
                                            {
                                                "id": 2,
                                                "nombre": "Juan Carlos Pérez",
                                                "email": "juan.perez@ufps.edu.co",
                                                "activo": true,
                                                "esSuperAdmin": false
                                            }
                                        ]
                                        """))),
                        @ApiResponse(responseCode = "401", description = "Token JWT inválido o expirado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponse.class)))
        })
        @GetMapping("/admins")
        public ResponseEntity<List<AdminDTO>> listarAdmins() {
                List<AdminDTO> adminDTO = adminService.listarAdmins();
                return new ResponseEntity<>(adminDTO, HttpStatus.OK);
        }

        @Operation(summary = "Actualizar información de administrador", description = "Modifica los datos de un administrador existente con validaciones de unicidad. "
                        +
                        "Valida que el nuevo email no esté en uso por otro administrador (si se modifica), " +
                        "encripta la nueva contraseña si se proporciona y mantiene el estado activo/inactivo. " +
                        "Preserva el ID y estado de activación durante la actualización.", security = @SecurityRequirement(name = "JWT"))
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Administrador actualizado exitosamente con validaciones completadas", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponse.class), examples = @ExampleObject(value = """
                                        {
                                            "httpStatusCode": 200,
                                            "httpStatus": "OK",
                                            "reason": "OK",
                                            "message": "Administrador actualizado con exito"
                                        }
                                        """))),
                        @ApiResponse(responseCode = "400", description = "Email ya está en uso por otro administrador", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponse.class), examples = @ExampleObject(value = """
                                        {
                                            "httpStatusCode": 400,
                                            "httpStatus": "BAD_REQUEST",
                                            "reason": "Bad Request",
                                            "message": "El correo ya esta en uso"
                                        }
                                        """))),
                        @ApiResponse(responseCode = "404", description = "Administrador no encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponse.class), examples = @ExampleObject(value = """
                                        {
                                            "httpStatusCode": 404,
                                            "httpStatus": "NOT_FOUND",
                                            "reason": "Not Found",
                                            "message": "El administrador no fue encontrado"
                                        }
                                        """)))
        })
        @PutMapping("/admins/{id}")
        public ResponseEntity<HttpResponse> actualizarAdmin(
                        @Parameter(description = "ID único del administrador a actualizar", required = true, example = "2", schema = @Schema(type = "integer", minimum = "1")) @PathVariable Integer id,

                        @Parameter(description = "Nuevos datos del administrador con validaciones de unicidad", required = true, schema = @Schema(implementation = AdminDTO.class), example = """
                                        {
                                            "nombre": "Juan Carlos Pérez Actualizado",
                                            "email": "juan.perez.nuevo@ufps.edu.co",
                                            "password": "nuevaPassword123",
                                            "esSuperAdmin": false
                                        }
                                        """) @RequestBody AdminDTO adminDTO)
                        throws UserNotFoundException {

                adminService.actualizarAdmin(id, adminDTO);
                return new ResponseEntity<>(
                                new HttpResponse(HttpStatus.OK.value(), HttpStatus.OK, HttpStatus.OK.getReasonPhrase(),
                                                " Administrador actualizado con exito"),
                                HttpStatus.OK);
        }

        @Operation(summary = "Desactivar cuenta de administrador", description = "Cambia el estado de un administrador a inactivo para bloquear su acceso al sistema. "
                        +
                        "Valida que no sea un super-administrador (protección del sistema) antes de permitir " +
                        "la desactivación. Los administradores desactivados no pueden autenticarse hasta " +
                        "ser reactivados por otro administrador activo.", security = @SecurityRequirement(name = "JWT"))
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Administrador desactivado exitosamente con acceso bloqueado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponse.class), examples = @ExampleObject(value = """
                                        {
                                            "httpStatusCode": 200,
                                            "httpStatus": "OK",
                                            "reason": "OK",
                                            "message": "Administrador desactivado con exito"
                                        }
                                        """))),
                        @ApiResponse(responseCode = "403", description = "No se permite desactivar super-administradores", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponse.class), examples = @ExampleObject(value = """
                                        {
                                            "httpStatusCode": 403,
                                            "httpStatus": "FORBIDDEN",
                                            "reason": "Forbidden",
                                            "message": "No esta permitido desactivar un superadmin"
                                        }
                                        """))),
                        @ApiResponse(responseCode = "404", description = "Administrador no encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponse.class)))
        })
        @PostMapping("/admins/{id}/desactivar")
        public ResponseEntity<HttpResponse> desactivarAdmin(
                        @Parameter(description = "ID único del administrador a desactivar (excepto super-admin)", required = true, example = "2", schema = @Schema(type = "integer", minimum = "1")) @PathVariable Integer id)
                        throws UserNotFoundException, ChangeNotAllowedException {

                adminService.desactivarAdmin(id);
                return new ResponseEntity<>(
                                new HttpResponse(HttpStatus.OK.value(), HttpStatus.OK, HttpStatus.OK.getReasonPhrase(),
                                                " Administrador desactivado con exito"),
                                HttpStatus.OK);
        }

        @Operation(summary = "Activar cuenta de administrador", description = "Reactiva una cuenta de administrador desactivada para restaurar su acceso al sistema. "
                        +
                        "Valida que no sea un super-administrador (ya están siempre activos) antes de proceder. " +
                        "Los administradores activados pueden autenticarse inmediatamente tras la reactivación.", security = @SecurityRequirement(name = "JWT"))
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Administrador activado exitosamente con acceso restaurado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponse.class), examples = @ExampleObject(value = """
                                        {
                                            "httpStatusCode": 200,
                                            "httpStatus": "OK",
                                            "reason": "OK",
                                            "message": "Administrador activado con exito"
                                        }
                                        """))),
                        @ApiResponse(responseCode = "403", description = "No se permite activar super-administradores (siempre activos)", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponse.class), examples = @ExampleObject(value = """
                                        {
                                            "httpStatusCode": 403,
                                            "httpStatus": "FORBIDDEN",
                                            "reason": "Forbidden",
                                            "message": "No esta permitido activar un superadmin"
                                        }
                                        """))),
                        @ApiResponse(responseCode = "404", description = "Administrador no encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponse.class)))
        })
        @PostMapping("/admins/{id}/activar")
        public ResponseEntity<HttpResponse> activarAdmin(
                        @Parameter(description = "ID único del administrador a activar (excepto super-admin)", required = true, example = "2", schema = @Schema(type = "integer", minimum = "1")) @PathVariable Integer id)
                        throws UserNotFoundException, ChangeNotAllowedException {

                adminService.activarAdmin(id);
                return new ResponseEntity<>(
                                new HttpResponse(HttpStatus.OK.value(), HttpStatus.OK, HttpStatus.OK.getReasonPhrase(),
                                                " Administrador activado con exito"),
                                HttpStatus.OK);
        }

        @Operation(summary = "Enviar token de recuperación de contraseña", description = "Genera un token JWT temporal de recuperación y envía email con enlace seguro para "
                        +
                        "restablecer contraseña. Valida que el email corresponda a un administrador registrado, " +
                        "crea token con expiración limitada y envía correo con URL de restablecimiento. " +
                        "El token tiene duración limitada por seguridad.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Token de recuperación enviado exitosamente por correo electrónico", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponse.class), examples = @ExampleObject(value = """
                                        {
                                            "httpStatusCode": 200,
                                            "httpStatus": "OK",
                                            "reason": "OK",
                                            "message": "Se le ha enviado un correo con instrucciones para restablecer su contraseña"
                                        }
                                        """))),
                        @ApiResponse(responseCode = "404", description = "Email no encontrado en administradores registrados", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponse.class), examples = @ExampleObject(value = """
                                        {
                                            "httpStatusCode": 404,
                                            "httpStatus": "NOT_FOUND",
                                            "reason": "Not Found",
                                            "message": "El administrador no fue encontrado"
                                        }
                                        """)))
        })
        @PostMapping("/recuperar-password/{correo}")
        public ResponseEntity<HttpResponse> recuperarPassword(
                        @Parameter(description = "Email del administrador para envío de token de recuperación", required = true, example = "admin@ufps.edu.co", schema = @Schema(type = "string", format = "email")) @PathVariable String correo)
                        throws UserNotFoundException {

                adminService.enviarTokenRecuperacion(correo);

                return new ResponseEntity<>(
                                new HttpResponse(HttpStatus.OK.value(), HttpStatus.OK, HttpStatus.OK.getReasonPhrase(),
                                                "Se le ha enviado un correo con instrucciones para restablecer su contraseña"),
                                HttpStatus.OK);
        }

        @Operation(summary = "Restablecer contraseña con token de recuperación", description = "Cambia la contraseña de un administrador usando token de recuperación temporal válido. "
                        +
                        "Valida el token JWT, verifica que no haya expirado, obtiene el email del token, " +
                        "confirma que las nuevas contraseñas coincidan y actualiza la contraseña encriptada. " +
                        "El token se invalida automáticamente tras el uso exitoso.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Contraseña restablecida exitosamente con token válido", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponse.class), examples = @ExampleObject(value = """
                                        {
                                            "httpStatusCode": 200,
                                            "httpStatus": "OK",
                                            "reason": "OK",
                                            "message": "Contraseña actualizada exitosamente"
                                        }
                                        """))),
                        @ApiResponse(responseCode = "400", description = "Las nuevas contraseñas no coinciden", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponse.class), examples = @ExampleObject(value = """
                                        {
                                            "httpStatusCode": 400,
                                            "httpStatus": "BAD_REQUEST",
                                            "reason": "Bad Request",
                                            "message": "LAS NUEVAS CONTRASEÑAS no son iguales"
                                        }
                                        """))),
                        @ApiResponse(responseCode = "401", description = "Token de recuperación inválido o expirado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponse.class), examples = @ExampleObject(value = """
                                        {
                                            "httpStatusCode": 401,
                                            "httpStatus": "UNAUTHORIZED",
                                            "reason": "Unauthorized",
                                            "message": "EL TOKEN no es valido"
                                        }
                                        """))),
                        @ApiResponse(responseCode = "404", description = "Email del token no encontrado en el sistema", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponse.class), examples = @ExampleObject(value = """
                                        {
                                            "httpStatusCode": 404,
                                            "httpStatus": "NOT_FOUND",
                                            "reason": "Not Found",
                                            "message": "EL CORREO no fue encontrado"
                                        }
                                        """)))
        })
        @PostMapping("/cambiar-password")
        public ResponseEntity<HttpResponse> resetPassword(
                        @Parameter(description = "Datos para restablecimiento de contraseña con token de recuperación", required = true, schema = @Schema(implementation = NuevaPasswordRequest.class), example = """
                                        {
                                            "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
                                            "nuevaPassword": "nuevaPassword123",
                                            "nuevaPassword2": "nuevaPassword123"
                                        }
                                        """) @Valid @RequestBody NuevaPasswordRequest nuevaPasswordRequest)
                        throws EmailNotFoundException, TokenNotValidException, PasswordNotEqualsException {

                adminService.restablecerpassword(nuevaPasswordRequest.getToken(),
                                nuevaPasswordRequest.getNuevaPassword(), nuevaPasswordRequest.getNuevaPassword2());

                return new ResponseEntity<>(
                                new HttpResponse(HttpStatus.OK.value(), HttpStatus.OK, HttpStatus.OK.getReasonPhrase(),
                                                " Contraseña actualizada exitosamente"),
                                HttpStatus.OK);
        }
}