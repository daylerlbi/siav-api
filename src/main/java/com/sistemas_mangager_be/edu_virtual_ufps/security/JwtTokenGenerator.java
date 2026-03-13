package com.sistemas_mangager_be.edu_virtual_ufps.security;

import com.sistemas_mangager_be.edu_virtual_ufps.entities.Usuario;
import com.sistemas_mangager_be.edu_virtual_ufps.repositories.UsuarioRepository;
import com.sistemas_mangager_be.edu_virtual_ufps.security.CustomUserDetailsService.CustomUserDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Optional;

@Component
public class JwtTokenGenerator {

    @Value("${application.security.jwt.secret-key}")
    private String secretKey;

    @Value("${application.security.jwt.expiration-token}")
    private long jwtExpirationToken;

    @Value("${application.security.jwt.expiration-refresh-token}")
    private long jwtExpirationRefreshToken;

    @Value("${application.security.jwt.expiration-password-reset}")
    private long jwtExpirationPasswordReset;

    private final SessionManager sessionManager;
    private final UsuarioRepository usuarioRepository;

    public JwtTokenGenerator(SessionManager sessionManager,
                             UsuarioRepository usuarioRepository) {
        this.sessionManager = sessionManager;
        this.usuarioRepository = usuarioRepository;
    }

    // Método para obtener la clave secreta segura
    private SecretKey getSigningKey() {
        // Aseguramos que la clave tenga al menos 256 bits (32 bytes)
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);

        // Si la clave es menor a 32 bytes, la expandimos
        if (keyBytes.length < 32) {
            byte[] expandedKey = new byte[32];
            System.arraycopy(keyBytes, 0, expandedKey, 0, keyBytes.length);
            // Rellenamos el resto con un patrón predecible pero único
            for (int i = keyBytes.length; i < 32; i++) {
                expandedKey[i] = (byte) (keyBytes[i % keyBytes.length] ^ i);
            }
            keyBytes = expandedKey;
        }

        return Keys.hmacShaKeyFor(keyBytes);
    }

    // Metodo para generar el token por medio de la autenticación
    public String generarToken(Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        String correo = userDetails.getUsername();
        String rol = authentication.getAuthorities().stream().findFirst().get().getAuthority();
        Date fechaActual = new Date();
        Date expiracionToken = new Date(fechaActual.getTime() + jwtExpirationToken);

        // Aqui generamos el token con la información adicional
        String token = Jwts.builder()
                .subject(correo)
                .claim("role", rol)
                .claim("primerNombre", userDetails.getPrimerNombre())
                .claim("primerApellido", userDetails.getPrimerApellido())
                .issuedAt(new Date())
                .expiration(expiracionToken)
                .signWith(getSigningKey())
                .compact();

        // Registrar sesión activa
        sessionManager.registerUserSession(correo, token);

        return token;
    }

    public String generarTokenGlobal(Authentication authentication) {
        Date fechaActual = new Date();
        Date expiracionToken = new Date(fechaActual.getTime() + jwtExpirationToken);

        String email;
        String nombre = "";
        String apellido = "";
        String rol = "";
        String foto = "";

        Object principal = authentication.getPrincipal();

        if (principal instanceof CustomUserDetailsService.CustomUserDetails customUser) {
            email = customUser.getUsername();
            nombre = customUser.getPrimerNombre();
            apellido = customUser.getPrimerApellido();
            rol = authentication.getAuthorities().stream().findFirst().get().getAuthority();
        } else if (principal instanceof OAuth2User oauthUser) {
            email = oauthUser.getAttribute("email");
            nombre = oauthUser.getAttribute("given_name");
            apellido = oauthUser.getAttribute("family_name");
            foto = oauthUser.getAttribute("picture");

            Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(email);
            if (usuarioOpt.isPresent()) {
                rol = usuarioOpt.get().getRolId().getNombre();
            } else {
                rol = "";
            }
        } else {
            throw new RuntimeException("Tipo de usuario no soportado: " + principal.getClass().getName());
        }

        String token = Jwts.builder()
                .subject(email)
                .claim("role", rol)
                .claim("nombre", nombre)
                .claim("apellido", apellido)
                .claim("foto", foto)
                .issuedAt(new Date())
                .expiration(expiracionToken)
                .signWith(getSigningKey())
                .compact();

        // Registrar sesión activa
        sessionManager.registerUserSession(email, token);

        return token;
    }

    // Método para generar un Refresh Token
    public String generarRefreshToken(Authentication authentication) {
        String correo = authentication.getName();
        Date tiempoActual = new Date();
        Date expiracionRefreshToken = new Date(
                tiempoActual.getTime() + jwtExpirationRefreshToken);

        return Jwts.builder()
                .subject(correo)
                .issuedAt(tiempoActual)
                .expiration(expiracionRefreshToken)
                .signWith(getSigningKey())
                .compact();
    }

    // Metodo para extraer un email a partir de un token
    public String obtenerCorreoDeJWT(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return claims.getSubject();
    }

    // Metodo para validar el token
    public Boolean validarToken(String token) {
        try {
            Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            throw new AuthenticationCredentialsNotFoundException("JWT ha expirado o es incorrecto");
        }
    }

    // Validar si el Refresh Token ha expirado
    public Boolean validarRefreshToken(String token) {
        try {
            Claims claims = Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(token).getPayload();
            return !claims.getExpiration().before(new Date());
        } catch (Exception e) {
            return false; // Refresh Token inválido o expirado
        }
    }

    public Boolean validarTokenRecuperacion(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            // Verificamos si el token ha expirado
            return !claims.getExpiration().before(new Date());
        } catch (Exception e) {
            return false; // Token inválido o expirado
        }
    }

    // Método para generar un token único de recuperación de password
    public String generarTokenRecuperacion(String email) {
        Date tiempoActual = new Date();
        Date expiracionToken = new Date(tiempoActual.getTime() + jwtExpirationPasswordReset);

        // Generamos el token basado en el correo del usuario
        return Jwts.builder()
                .subject(email)
                .issuedAt(new Date())
                .expiration(expiracionToken)
                .signWith(getSigningKey())
                .compact();
    }
}