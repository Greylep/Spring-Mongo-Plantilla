package org.example;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity // Permite usar @PreAuthorize en los controladores si quieres
public class SecurityConfig {

    // 1. DEFINICIÓN DE USUARIOS EN MEMORIA (Para el examen es lo más rápido)
    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder encoder) {
        // Usuario básico: solo puede ver (GET permitido sin login de todas formas)
        UserDetails user = User.builder()
                .username("user")
                .password(encoder.encode("1234"))
                .roles("USER")
                .build();

        // Administrador: puede crear, editar y borrar
        UserDetails admin = User.builder()
                .username("admin")
                .password(encoder.encode("1234"))
                .roles("ADMIN", "USER") // ADMIN también tiene rol USER
                .build();

        return new InMemoryUserDetailsManager(user, admin);
    }

    // 2. FILTRO DE SEGURIDAD (Las reglas de acceso)
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // CSRF: Desactivado para simplificar en el examen.
                // GUÍA: En producción con vistas HTML se debería activar.
                // Con CSRF activo, todos los forms POST necesitan un token oculto:
                // <input type="hidden" th:name="${_csrf.parameterName}" th:value="${_csrf.token}"/>
                .csrf(csrf -> csrf.disable())

                // REGLAS DE AUTORIZACIÓN (se evalúan en orden, de arriba a abajo)
                .authorizeHttpRequests(auth -> auth
                        // Recursos estáticos: siempre públicos
                        .requestMatchers("/css/**", "/js/**", "/images/**").permitAll()

                        // GET público: cualquiera puede ver la lista y los detalles
                        .requestMatchers(org.springframework.http.HttpMethod.GET, "/api/v1/recursos/**").permitAll()
                        .requestMatchers(org.springframework.http.HttpMethod.GET, "/web/recursos/**").permitAll()

                        // Operaciones de escritura: solo ADMIN
                        .requestMatchers(org.springframework.http.HttpMethod.DELETE, "/**").hasRole("ADMIN")
                        .requestMatchers(org.springframework.http.HttpMethod.POST, "/**").hasRole("ADMIN")
                        .requestMatchers(org.springframework.http.HttpMethod.PUT, "/**").hasRole("ADMIN")

                        // Todo lo demás requiere estar autenticado
                        .anyRequest().authenticated()
                )

                // FORMULARIO DE LOGIN
                // CORRECCION: Eliminado .loginPage("/login") porque no existe esa vista/ruta.
                // Sin loginPage(), Spring genera automáticamente una página de login en /login.
                // Si quieres una vista propia, crea un LoginController y una vista login.html.
                .formLogin(form -> form
                        .defaultSuccessUrl("/web/recursos", true) // Redirige aquí tras login exitoso
                        .permitAll() // La página de login es pública (lógicamente)
                )

                // LOGOUT
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/web/recursos") // Tras cerrar sesión, vuelve al listado
                        .permitAll()
                )

                // MANEJO DE ACCESO DENEGADO (Error 403)
                .exceptionHandling(ex -> ex
                        .accessDeniedHandler(accessDeniedHandler())
                );

        return http.build();
    }

    // 3. CODIFICADOR DE CONTRASEÑAS
    // GUÍA: NUNCA guardes contraseñas en texto plano. BCrypt hace un hash seguro.
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 4. MANEJADOR DE ACCESO DENEGADO PERSONALIZADO (Error 403)
    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return (request, response, accessDeniedException) -> {
            // GUÍA: Dos opciones según si usas vistas o API pura:

            // Opción A - Para vistas HTML: redirige a una página de error
            response.sendRedirect("/error/403");

            // Opción B - Para API REST: devuelve JSON con status 403
            // response.setStatus(403);
            // response.setContentType("application/json");
            // response.getWriter().write("{\"error\": \"Acceso denegado: necesitas rol ADMIN\"}");
        };
    }
}