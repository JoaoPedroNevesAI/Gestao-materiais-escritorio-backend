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
        AuthenticationProvider authenticationProvider
    ) {
        this.authenticationProvider = authenticationProvider;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth

                .requestMatchers(HttpMethod.OPTIONS, "/**")
                .permitAll()

                // AUTENTICACAO
                .requestMatchers("/api/auth/**")
                .permitAll()

                // CADASTRO PUBLICO DE COLABORADORES.
                // O cadastro de ADM e validado no UsuarioController.
                .requestMatchers(HttpMethod.POST, "/api/usuario")
                .permitAll()

                // CONSULTA E ATUALIZACAO DE USUARIOS AUTENTICADOS.
                // A regra de atualizacao do proprio perfil e validada no controller.
                .requestMatchers(HttpMethod.PUT, "/api/usuario/**")
                .hasAnyRole("ADM", "COLABORADOR")

                // EXCLUSAO DE USUARIOS
                .requestMatchers(HttpMethod.DELETE, "/api/usuario/**")
                .hasRole("ADM")

                // SWAGGER
                .requestMatchers(
                        "/swagger-ui/**",
                        "/swagger-ui.html",
                        "/v3/api-docs/**",
                        "/api-docs/**",
                        "/api-docs")
                .permitAll()

                // IMAGENS E QRCODES
                .requestMatchers(
                        "/imagens/**",
                        "/qrcodes/**")
                .permitAll()

                // MATERIAL
                .requestMatchers(HttpMethod.POST, "/api/material/filtrar")
                .hasAnyRole("ADM", "COLABORADOR")

                .requestMatchers(HttpMethod.GET, "/api/material/**")
                .hasAnyRole("ADM", "COLABORADOR")

                .requestMatchers(HttpMethod.POST, "/api/material/**")
                .hasRole("ADM")

                .requestMatchers(HttpMethod.PUT, "/api/material/**")
                .hasRole("ADM")

                // PATRIMONIO
                .requestMatchers(
                        HttpMethod.GET,
                        "/api/patrimonio",
                        "/api/patrimonio/**")
                .hasAnyRole("ADM", "COLABORADOR")

                .requestMatchers(
                        HttpMethod.POST,
                        "/api/patrimonio/*/qrcode/regenerar")
                .hasRole("ADM")

                .requestMatchers(HttpMethod.POST, "/api/patrimonio/**")
                .hasRole("ADM")

                .requestMatchers(HttpMethod.PUT, "/api/patrimonio/**")
                .hasRole("ADM")

                .requestMatchers(HttpMethod.DELETE, "/api/patrimonio/**")
                .hasRole("ADM")

                // CATEGORIA
                .requestMatchers(HttpMethod.GET, "/api/categoria/**")
                .hasAnyRole("ADM", "COLABORADOR")

                .requestMatchers(HttpMethod.POST, "/api/categoria/**")
                .hasRole("ADM")

                .requestMatchers(HttpMethod.PUT, "/api/categoria/**")
                .hasRole("ADM")

                .requestMatchers(HttpMethod.DELETE, "/api/categoria/**")
                .hasRole("ADM")

                // MOVIMENTACAO
                .requestMatchers(
                        HttpMethod.GET,
                        "/api/movimentacao",
                        "/api/movimentacao/**")
                .hasAnyRole("ADM", "COLABORADOR")

                // A rota implementada pelo controller cria uma solicitacao.
                .requestMatchers(HttpMethod.POST, "/api/movimentacao")
                .hasAnyRole("ADM", "COLABORADOR")

                // Outras operacoes POST de movimentacao ficam restritas ao ADM.
                .requestMatchers(HttpMethod.POST, "/api/movimentacao/**")
                .hasRole("ADM")

                // Somente ADM pode aprovar ou reprovar solicitacoes.
                .requestMatchers(
                        HttpMethod.PUT,
                        "/api/movimentacao/*/aprovar",
                        "/api/movimentacao/*/reprovar")
                .hasRole("ADM")

                .requestMatchers(HttpMethod.PUT, "/api/movimentacao/**")
                .hasRole("ADM")

                // MANUTENCAO
                .requestMatchers(HttpMethod.POST, "/api/manutencao/solicitar")
                .hasAnyRole("ADM", "COLABORADOR")

                .requestMatchers(HttpMethod.GET, "/api/manutencao/**")
                .hasAnyRole("ADM", "COLABORADOR")

                .requestMatchers(HttpMethod.PUT, "/api/manutencao/**")
                .hasRole("ADM")

                // NOTIFICACAO
                .requestMatchers(
                        HttpMethod.GET,
                        "/api/notificacao",
                        "/api/notificacao/**")
                .hasAnyRole("ADM", "COLABORADOR")

                .requestMatchers(HttpMethod.PUT, "/api/notificacao/**")
                .hasAnyRole("ADM", "COLABORADOR")

                .requestMatchers(HttpMethod.DELETE, "/api/notificacao/**")
                .hasAnyRole("ADM", "COLABORADOR")

                .anyRequest()
                .authenticated()
            )
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .authenticationProvider(authenticationProvider)
            .addFilterBefore(
                jwtAuthenticationFilter,
                UsernamePasswordAuthenticationFilter.class
            );

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(Arrays.asList(
            "http://localhost:3000",
            "http://localhost:5173",
            "http://localhost:8080",
            "http://localhost:8081",
            "http://localhost:19006"
        ));

        configuration.setAllowedMethods(Arrays.asList(
            "GET", "POST", "PUT", "DELETE", "OPTIONS"
        ));

        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}