package br.com.ifescritorio.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import br.com.ifescritorio.model.usuario.UsuarioRepository;

@Configuration
public class ApplicationConfiguration {

    private final UsuarioRepository usuarioRepository;

    public ApplicationConfiguration(UsuarioRepository usuarioRepository) {

        this.usuarioRepository = usuarioRepository;
    }

    @Bean
    UserDetailsService userDetailsService() {

        return username -> usuarioRepository
            .findByEmail(username)
            .orElseThrow(() ->
                new UsernameNotFoundException("Usuário não encontrado"));
    }

    @Bean
    PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();
    }

    @Bean
    AuthenticationProvider authenticationProvider() {

        DaoAuthenticationProvider authProvider =
            new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

    @Bean
    AuthenticationManager authenticationManager(
        AuthenticationConfiguration config
    ) throws Exception {

        return config.getAuthenticationManager();
    }
}