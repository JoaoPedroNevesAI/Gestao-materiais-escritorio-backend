package br.com.ifescritorio.config;

import java.util.Arrays;

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
            AuthenticationProvider authenticationProvider) {

        this.authenticationProvider = authenticationProvider;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {

        http

            .cors(cors ->
                cors.configurationSource(
                    corsConfigurationSource()
                )
            )

            .csrf(csrf -> csrf.disable())

            .authorizeHttpRequests(auth -> auth

                // OPTIONS
                .requestMatchers(
                        HttpMethod.OPTIONS,
                        "/**")
                .permitAll()

                // ERROR
                .requestMatchers("/error")
                .permitAll()

                // AUTH
                .requestMatchers("/api/auth/**")
                .permitAll()

                // CADASTRO USUÁRIO
                .requestMatchers(
                        HttpMethod.POST,
                        "/api/usuario")
                .permitAll()

                // SWAGGER
                .requestMatchers(
                        "/swagger-ui/**",
                        "/v3/api-docs/**")
                .permitAll()

                // IMAGENS
                .requestMatchers(
                        "/imagens/**")
                .permitAll()

                // ==========================
                // MATERIAL
                // ==========================

                .requestMatchers(
                        HttpMethod.GET,
                        "/api/material/**")
                .hasAnyRole("ADM", "CLIENTE")

                .requestMatchers(
                        HttpMethod.POST,
                        "/api/material/**")
                .hasRole("ADM")

                .requestMatchers(
                        HttpMethod.PUT,
                        "/api/material/**")
                .hasRole("ADM")

                .requestMatchers(
                        HttpMethod.DELETE,
                        "/api/material/**")
                .hasRole("ADM")

                // ==========================
                // PATRIMÔNIO
                // ==========================

                .requestMatchers(
                        HttpMethod.GET,
                        "/api/patrimonio/**")
                .hasAnyRole("ADM", "CLIENTE")

                .requestMatchers(
                        HttpMethod.POST,
                        "/api/patrimonio/**")
                .hasRole("ADM")

                .requestMatchers(
                        HttpMethod.PUT,
                        "/api/patrimonio/**")
                .hasRole("ADM")

                .requestMatchers(
                        HttpMethod.DELETE,
                        "/api/patrimonio/**")
                .hasRole("ADM")

                // ==========================
                // CATEGORIA
                // ==========================

                .requestMatchers(
                        HttpMethod.GET,
                        "/api/categoria/**")
                .hasAnyRole("ADM", "CLIENTE")

                .requestMatchers(
                        HttpMethod.POST,
                        "/api/categoria/**")
                .hasRole("ADM")

                .requestMatchers(
                        HttpMethod.PUT,
                        "/api/categoria/**")
                .hasRole("ADM")

                .requestMatchers(
                        HttpMethod.DELETE,
                        "/api/categoria/**")
                .hasRole("ADM")

                // ==========================
                // MOVIMENTAÇÃO
                // ==========================

                .requestMatchers(
                        HttpMethod.GET,
                        "/api/movimentacao/**")
                .hasAnyRole("ADM", "CLIENTE")

                .requestMatchers(
                        HttpMethod.POST,
                        "/api/movimentacao/**")
                .hasRole("ADM")

                // ==========================
                // SOLICITAÇÃO MOVIMENTAÇÃO
                // ==========================

                .requestMatchers(
                        HttpMethod.POST,
                        "/api/solicitacao-movimentacao/solicitar")
                .hasAnyRole("ADM", "CLIENTE")

                .requestMatchers(
                        HttpMethod.GET,
                        "/api/solicitacao-movimentacao/**")
                .hasRole("ADM")

                .requestMatchers(
                        HttpMethod.PUT,
                        "/api/solicitacao-movimentacao/**")
                .hasRole("ADM")

                // ==========================
                // MANUTENÇÃO
                // ==========================

                .requestMatchers(
                        HttpMethod.POST,
                        "/api/manutencao/solicitar")
                .hasAnyRole("ADM", "CLIENTE")

                .requestMatchers(
                        HttpMethod.GET,
                        "/api/manutencao/**")
                .hasAnyRole("ADM", "CLIENTE")

                .requestMatchers(
                        HttpMethod.PUT,
                        "/api/manutencao/**")
                .hasRole("ADM")

                // RESTANTE
                .anyRequest()
                .authenticated()
            )

            .sessionManagement(session ->
                session.sessionCreationPolicy(
                    SessionCreationPolicy.STATELESS
                )
            )

            .authenticationProvider(
                authenticationProvider
            )

            .addFilterBefore(
                jwtAuthenticationFilter,
                UsernamePasswordAuthenticationFilter.class
            );

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration configuration =
                new CorsConfiguration();

        configuration.setAllowedOriginPatterns(
                Arrays.asList(
                        "http://localhost:3000",
                        "http://localhost:5173",
                        "http://localhost:8081",
                        "https://*.exp.direct",
                        "https://*.ngrok-free.app",
                        "https://*.ngrok-free.dev"
                )
        );

        configuration.setAllowedMethods(
                Arrays.asList(
                        "GET",
                        "POST",
                        "PUT",
                        "DELETE",
                        "OPTIONS"
                )
        );

        configuration.setAllowedHeaders(
                Arrays.asList("*")
        );

        configuration.setExposedHeaders(
                Arrays.asList(
                        "Authorization"
                )
        );

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