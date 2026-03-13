package com.sistemas_mangager_be.edu_virtual_ufps.security;

import com.sistemas_mangager_be.edu_virtual_ufps.repositories.AdminRepository;
import com.sistemas_mangager_be.edu_virtual_ufps.repositories.SesionActivaRepository;
import com.sistemas_mangager_be.edu_virtual_ufps.repositories.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration // Esto indica que es una clase de seguridad al momento de arrancar la
// aplicacion
@EnableWebSecurity // Se activa la seguridad web de nuestra aplicacion y ademas sera una clase que
// contendra toda la configuracion refente a la seguridad
@EnableMethodSecurity // Habilitamos la seguridad basada en anotaciones como @PreAuthorize
@RequiredArgsConstructor
public class SecurityConfig {

    @Value("${application.security.cors.allowed.origins}")
    private String allowedOrigins;

    @Value("${application.security.cors.allowed.methods}")
    private String allowedMethods;

    @Value("${application.security.cors.allowed.headers}")
    private String allowedHeaders;

    @Value("${application.security.cors.exposed.headers}")
    private String exposedHeaders;

    @Value("${application.security.cors.allowed.credentials}")
    private String allowedCredentials;

    @Value("${application.security.cors.max.age}")
    private long maxAge;

    private final JwtAutheticationEntryPoint jwtAuthenticationEntryPoint;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final CustomOAuth2FailureHandler customOAuth2FailureHandler;
    private final AdminRepository adminRepository;
    private final UsuarioRepository usuarioRepository;

    private final SesionActivaRepository sesionActivaRepository;

    @Value("${app.frontend.success-url}")
    private String frontUrl;

    @Bean
    public SessionManager sessionManager() {
        return new SessionManager(sesionActivaRepository);
    }

    @Bean
    public JwtTokenGenerator jwtTokenGenerator() {
        return new JwtTokenGenerator(sessionManager(), usuarioRepository);
    }

    @Bean
    public CustomUserDetailsService customUserDetailsService() {
        return new CustomUserDetailsService(adminRepository);
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(
                customUserDetailsService(),
                jwtTokenGenerator(),
                sessionManager(),
                usuarioRepository);
    }

    // Este bean va a encargarse de verificar la información de los usuarios que se
    // logearan en el sistema
    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    // Con este bean nos encargaremos de encriptar todas nuestras passwords
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Este bean establece una cadena de filtros de seguridad del sistema y define
    // permisos basados en roles
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint))
                .sessionManagement(sessionManagement -> sessionManagement
        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/api/auth/**", "/oauth2/**", "/**").permitAll()
                        .anyRequest().authenticated())
                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(customOAuth2UserService))
                        .successHandler((request, response, authentication) -> {
                            String token = jwtTokenGenerator()
                                    .generarTokenGlobal(authentication);
                            response.sendRedirect(frontUrl + "?token=" + token);
                        })
                        .failureHandler(customOAuth2FailureHandler))
                .httpBasic(Customizer.withDefaults());

        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /*
     * Este bean es responsable de configurar las opciones de seguridad de CORS
     * para nuestra aplicacion
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();

        corsConfiguration.setAllowedOrigins(Arrays.asList(allowedOrigins.split(",")));
        corsConfiguration.setAllowedMethods(Arrays.asList(allowedMethods.split(",")));
        corsConfiguration.setAllowedHeaders(Arrays.asList(allowedHeaders.split(",")));
        corsConfiguration.setExposedHeaders(Arrays.asList(exposedHeaders.split(",")));
        corsConfiguration.setAllowCredentials(Boolean.parseBoolean(allowedCredentials));
        corsConfiguration.setMaxAge(maxAge);

        UrlBasedCorsConfigurationSource corsConfigurationSource = new UrlBasedCorsConfigurationSource();
        corsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);
        return corsConfigurationSource;
    }

}