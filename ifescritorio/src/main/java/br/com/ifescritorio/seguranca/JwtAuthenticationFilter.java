package br.com.ifescritorio.seguranca;

import java.io.IOException;

import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import br.com.ifescritorio.model.usuario.Usuario;
import br.com.ifescritorio.model.usuario.UsuarioRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter
    extends OncePerRequestFilter {

    private final JwtService jwtService;

    private final UsuarioRepository usuarioRepository;

    public JwtAuthenticationFilter(
        JwtService jwtService,
        UsuarioRepository usuarioRepository
    ) {

        this.jwtService = jwtService;

        this.usuarioRepository = usuarioRepository;
    }

    @Override
    protected void doFilterInternal(
        @NonNull HttpServletRequest request,
        @NonNull HttpServletResponse response,
        @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        final String authHeader =
            request.getHeader("Authorization");

        final String jwt;

        final String email;

        // =========================
        // SEM TOKEN
        // =========================
        if (
            authHeader == null ||
            !authHeader.startsWith("Bearer ")
        ) {

            filterChain.doFilter(
                request,
                response
            );

            return;
        }

        // =========================
        // EXTRAI TOKEN
        // =========================
        jwt = authHeader.substring(7);

        email = jwtService.extractUsername(jwt);
        

        // =========================
        // NÃO AUTENTICADO AINDA
        // =========================
        if (
            email != null &&
            SecurityContextHolder
                .getContext()
                .getAuthentication() == null
        ) {

            Usuario usuario =
                usuarioRepository
                    .findByEmail(email)
                    .orElse(null);

            if (
                usuario != null &&
                jwtService.isTokenValid(jwt, usuario)
            ) {

                // =========================
                // ROLE CORRETA
                // =========================
                String role =
                    "ROLE_" +
                    usuario.getTipo().name();
        SimpleGrantedAuthority authority =
            new SimpleGrantedAuthority(role);

                UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(
                        usuario,
                        null,
                        java.util.List.of(authority)
                    );

                authToken.setDetails(
                    new WebAuthenticationDetailsSource()
                        .buildDetails(request)
                );

                SecurityContextHolder
                    .getContext()
                    .setAuthentication(authToken);
            }
        }

        filterChain.doFilter(
            request,
            response
        );
    }
}