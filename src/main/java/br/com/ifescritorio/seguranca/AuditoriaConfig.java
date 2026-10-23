package br.com.ifescritorio.seguranca;

import java.util.Optional;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;

import br.com.ifescritorio.model.usuario.Usuario;

@Configuration
public class AuditoriaConfig {

    @Bean
    AuditorAware<Usuario> auditorAware() {

        return new AuditoriaAwareImpl();
    }
}