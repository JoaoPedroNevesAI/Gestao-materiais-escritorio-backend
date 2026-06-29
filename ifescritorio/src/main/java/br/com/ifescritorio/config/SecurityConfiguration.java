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
                            "/v3/api-docs/**",
                            "/api-docs/**",
                            "/api-docs")
                .permitAll()

                // ================================================================
                // CORREÇÃO: Mudado de '/imagens/**' para '/uploads/**' para liberar o acesso público
                // ================================================================
                .requestMatchers(
                        "/uploads/**")
                .permitAll()

                // ==========================
                // MATERIAL
                // ==========================

                .requestMatchers(
                        HttpMethod.GET,
                        "/api/material/**")
                .hasAnyAuthority("ROLE_ADM", "ROLE_COLABORADOR", "ADM", "COLABORADOR")

                .requestMatchers(
                        HttpMethod.POST,
                        "/api/material/**")
                .hasAnyAuthority("ROLE_ADM", "ADM")

                .requestMatchers(
                        HttpMethod.PUT,
                        "/api/material/**")
                .hasAnyAuthority("ROLE_ADM", "ADM")

                .requestMatchers(
                        HttpMethod.DELETE,
                        "/api/material/**")
                .hasAnyAuthority("ROLE_ADM", "ADM")

                // ==========================
                // PATRIMÔNIO
                // ==========================

                .requestMatchers(
                        HttpMethod.GET,
                        "/api/patrimonio/**")
                .hasAnyAuthority("ROLE_ADM", "ROLE_COLABORADOR", "ADM", "COLABORADOR")

                .requestMatchers(
                        HttpMethod.POST,
                        "/api/patrimonio/**")
                .hasAnyAuthority("ROLE_ADM", "ADM")

                .requestMatchers(
                        HttpMethod.PUT,
                        "/api/patrimonio/**")
                .hasAnyAuthority("ROLE_ADM", "ADM")

                .requestMatchers(
                        HttpMethod.DELETE,
                        "/api/patrimonio/**")
                .hasAnyAuthority("ROLE_ADM", "ADM")

                // ==========================
                // CATEGORIA
                // ==========================

                .requestMatchers(
                        HttpMethod.GET,
                        "/api/categoria/**")
                .hasAnyAuthority("ROLE_ADM", "ROLE_COLABORADOR", "ADM", "COLABORADOR")

                .requestMatchers(
                        HttpMethod.POST,
                        "/api/categoria/**")
                .hasAnyAuthority("ROLE_ADM", "ADM")

                .requestMatchers(
                        HttpMethod.PUT,
                        "/api/categoria/**")
                .hasAnyAuthority("ROLE_ADM", "ADM")

                .requestMatchers(
                        HttpMethod.DELETE,
                        "/api/categoria/**")
                .hasAnyAuthority("ROLE_ADM", "ADM")

                // ==========================
                // MOVIMENTAÇÃO
                // ==========================

                .requestMatchers(
                        HttpMethod.GET,
                        "/api/movimentacao/**")
                .hasAnyAuthority("ROLE_ADM", "ROLE_COLABORADOR", "ADM", "COLABORADOR")

                .requestMatchers(
                        HttpMethod.POST,
                        "/api/movimentacao/**")
                .hasAnyAuthority("ROLE_ADM", "ADM")

                // ==========================
                // SOLICITAÇÃO MOVIMENTAÇÃO
                // ==========================

                .requestMatchers(
                        HttpMethod.POST,
                        "/api/solicitacao-movimentacao/solicitar")
                .hasAnyAuthority("ROLE_ADM", "ROLE_COLABORADOR", "ADM", "COLABORADOR")

                .requestMatchers(
                        HttpMethod.GET,
                        "/api/solicitacao-movimentacao/**")
                .hasAnyAuthority("ROLE_ADM", "ADM")

                .requestMatchers(
                        HttpMethod.PUT,
                        "/api/solicitacao-movimentacao/**")
                .hasAnyAuthority("ROLE_ADM", "ADM")

                // ==========================
                // MANUTENÇÃO
                // ==========================

                .requestMatchers(
                        HttpMethod.POST,
                        "/api/manutencao/solicitar")
                .hasAnyAuthority("ROLE_ADM", "ROLE_COLABORADOR", "ADM", "COLABORADOR")

                .requestMatchers(
                        HttpMethod.GET,
                        "/api/manutencao/**")
                .hasAnyAuthority("ROLE_ADM", "ROLE_COLABORADOR", "ADM", "COLABORADOR")

                .requestMatchers(
                        HttpMethod.PUT,
                        "/api/manutencao/**")
                .hasAnyAuthority("ROLE_ADM", "ADM")

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