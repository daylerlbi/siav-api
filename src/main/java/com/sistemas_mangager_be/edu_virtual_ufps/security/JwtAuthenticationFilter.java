package com.sistemas_mangager_be.edu_virtual_ufps.security;

import com.sistemas_mangager_be.edu_virtual_ufps.entities.Usuario;
import com.sistemas_mangager_be.edu_virtual_ufps.repositories.UsuarioRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final CustomUserDetailsService customUserDetailsService;
    private final JwtTokenGenerator jwtTokenGenerator;
    private final SessionManager sessionManager;
    private final UsuarioRepository usuarioRepository;

    private String obtenerTokenDeSolicitud(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String token = obtenerTokenDeSolicitud(request);

        if (StringUtils.hasText(token) && jwtTokenGenerator.validarToken(token)) {
            String correo = jwtTokenGenerator.obtenerCorreoDeJWT(token);

            try {
                if (!sessionManager.isTokenValid(correo, token)) {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.setContentType("application/json");
                    response.setCharacterEncoding("UTF-8");
                    String jsonResponse = "{\"message\": \"Sesión iniciada en otro dispositivo, se cerrará la sesión\"}";
                    response.getWriter().write(jsonResponse);
                    return;
                }

                UserDetails userDetails;

                try {
                    userDetails = customUserDetailsService.loadUserByUsername(correo);
                } catch (UsernameNotFoundException e) {
                    Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(correo);

                    if (usuarioOpt.isEmpty()) {
                        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                        response.setContentType("application/json");
                        response.setCharacterEncoding("UTF-8");
                        response.getWriter().write("{\"message\": \"Usuario no registrado en el sistema.\"}");
                        return;
                    }
                    Usuario usuario = usuarioOpt.get();
                    String rol = usuario.getRolId().getNombre();

                    Collection<GrantedAuthority> authorities = List
                            .of(new SimpleGrantedAuthority("ROLE_" + rol.toUpperCase()));

                    userDetails = new User(
                            usuario.getEmail(),
                            "",
                            authorities);
                }

                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            } catch (UsernameNotFoundException e) {
                // Si la cuenta no está activa, enviamos respuesta de error
                if (e.getMessage().contains("inactiva")) {
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    response.setContentType("application/json");
                    response.setCharacterEncoding("UTF-8");
                    String jsonResponse = "{\"message\": \"" + e.getMessage() + "\"}";
                    response.getWriter().write(jsonResponse);
                    return;
                }
            }
        }

        filterChain.doFilter(request, response);
    }
}