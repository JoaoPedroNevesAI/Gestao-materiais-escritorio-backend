package br.com.ifescritorio.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.http.HttpMethod;

import org.springframework.security.authentication.AuthenticationProvider;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.web.SecurityFilterChain;

import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import org.springframework.web.cors.CorsConfiguration;

import org.springframework.web.cors.CorsConfigurationSource;

import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import br.com.ifescritorio.seguranca.JwtAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    private final AuthenticationProvider authenticationProvider;

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfiguration(
        JwtAuthenticationFilter jwtAuthenticationFilter,
        AuthenticationProvider authenticationProvider
    ) {

        this.authenticationProvider = authenticationProvider;

        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
        HttpSecurity http
    ) throws Exception {

        http

            // =========================
            // CORS
            // =========================
            .cors(cors ->
                cors.configurationSource(
                    corsConfigurationSource()
                )
            )

            // =========================
            // CSRF
            // =========================
            .csrf(csrf -> csrf.disable())

            // =========================
            // AUTORIZAÇÃO
            // =========================
            .authorizeHttpRequests(auth -> auth

                // LIBERA OPTIONS
                .requestMatchers(
                    HttpMethod.OPTIONS,
                    "/**"
                ).permitAll()

                // =========================
                // AUTH
                // =========================
                .requestMatchers(
                    "/api/auth/**"
                ).permitAll()

                // =========================
                // CADASTRO USUÁRIO
                // =========================
                .requestMatchers(
                    HttpMethod.POST,
                    "/api/usuario"
                ).permitAll()

                // =========================
                // SWAGGER
                // =========================
                .requestMatchers(
                    "/swagger-ui/**",
                    "/v3/api-docs/**"
                ).permitAll()

                // =========================
                // MATERIAL
                // =========================

                // LISTAR
                .requestMatchers(
                    HttpMethod.GET,
                    "/api/material/**"
                )
                .hasAnyRole("ADM", "CLIENTE")

                // CRIAR
                .requestMatchers(
                    HttpMethod.POST,
                    "/api/material/**"
                )
                .hasRole("ADM")

                // EDITAR
                .requestMatchers(
                    HttpMethod.PUT,
                    "/api/material/**"
                )
                .hasRole("ADM")

                // DELETAR
                .requestMatchers(
                    HttpMethod.DELETE,
                    "/api/material/**"
                )
                .hasRole("ADM")

                // =========================
                // CATEGORIA
                // =========================

                // LISTAR
                .requestMatchers(
                    HttpMethod.GET,
                    "/api/categoria/**"
                )
                .hasAnyRole("ADM", "CLIENTE")

                // CRIAR
                .requestMatchers(
                    HttpMethod.POST,
                    "/api/categoria/**"
                )
                .hasRole("ADM")

                // EDITAR
                .requestMatchers(
                    HttpMethod.PUT,
                    "/api/categoria/**"
                )
                .hasRole("ADM")

                // DELETAR
                .requestMatchers(
                    HttpMethod.DELETE,
                    "/api/categoria/**"
                )
                .hasRole("ADM")

                // =========================
                // RESTANTE
                // =========================
                .anyRequest()
                .authenticated()
            )

            // =========================
            // SESSION
            // =========================
            .sessionManagement(session ->
                session.sessionCreationPolicy(
                    SessionCreationPolicy.STATELESS
                )
            )

            // =========================
            // PROVIDER
            // =========================
            .authenticationProvider(
                authenticationProvider
            )

            // =========================
            // JWT FILTER
            // =========================
            .addFilterBefore(
                jwtAuthenticationFilter,
                UsernamePasswordAuthenticationFilter.class
            );

        return http.build();
    }

    // =========================
    // CORS CONFIG
    // =========================
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration configuration =
            new CorsConfiguration();

        // FRONTENDS
        configuration.setAllowedOrigins(
            Arrays.asList(
                "http://localhost:3000",
                "http://localhost:5173"
            )
        );

        // MÉTODOS
        configuration.setAllowedMethods(
            Arrays.asList(
                "GET",
                "POST",
                "PUT",
                "DELETE",
                "OPTIONS"
            )
        );

        // HEADERS
        configuration.setAllowedHeaders(
            Arrays.asList(
                "*"
            )
        );

        // CREDENCIAIS
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source =
            new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration(
            "/**",
            configuration
        );

        return source;
    }
}