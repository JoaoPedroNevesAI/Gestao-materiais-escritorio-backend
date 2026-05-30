package br.com.ifescritorio.seguranca;

import java.io.IOException;

import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    public JwtAuthenticationFilter(
        JwtService jwtService,
        UserDetailsService userDetailsService
    ) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(
        @NonNull HttpServletRequest request,
        @NonNull HttpServletResponse response,
        @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        System.out.println("====================================");
        System.out.println("REQUISIÇÃO: " + request.getMethod() + " " + request.getRequestURI());

        final String authHeader = request.getHeader("Authorization");
        System.out.println("AUTH HEADER: " + authHeader);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            System.out.println("SEM TOKEN JWT OU HEADER INVÁLIDO");
            System.out.println("====================================");
            filterChain.doFilter(request, response);
            return;
        }

        final String jwt = authHeader.substring(7);
        final String userEmail = jwtService.extractUsername(jwt);
        System.out.println("EMAIL EXTRAÍDO DO TOKEN: " + userEmail);

        Authentication authentication =
            SecurityContextHolder.getContext().getAuthentication();

        if (userEmail != null && authentication == null) {

            UserDetails userDetails =
                this.userDetailsService.loadUserByUsername(userEmail);

            // AQUI ESTÁ O TRUNFO DO TESTE: Vamos ver o que o banco devolve de permissão
            System.out.println("USUÁRIO CARREGADO DO BANCO: " + userDetails.getUsername());
            System.out.println("ROLES ENCONTRADAS NO USERDETAILS: " + userDetails.getAuthorities());

            if (jwtService.isTokenValid(jwt, userDetails)) {
                System.out.println("STATUS DO TOKEN: VÁLIDO");

                UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                    );

                authToken.setDetails(
                    new WebAuthenticationDetailsSource()
                        .buildDetails(request)
                    );

                SecurityContextHolder
                    .getContext()
                    .setAuthentication(authToken);
                
                System.out.println("RESULTADO: USUÁRIO AUTENTICADO NO SECURITY CONTEXT");
            } else {
                System.out.println("STATUS DO TOKEN: INVÁLIDO");
            }
        }

        System.out.println("====================================");
        filterChain.doFilter(request, response);
    }
}