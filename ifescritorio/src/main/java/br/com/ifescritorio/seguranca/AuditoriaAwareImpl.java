package br.com.ifescritorio.seguranca;

import java.util.Optional;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import br.com.ifescritorio.model.usuario.Usuario;

public class AuditoriaAwareImpl implements AuditorAware<Usuario> {

    @Override
    public Optional<Usuario> getCurrentAuditor() {

        Authentication authentication =
            SecurityContextHolder.getContext().getAuthentication();

        if (
            authentication == null ||
            !authentication.isAuthenticated() ||
            authentication.getPrincipal().equals("anonymousUser")
        ) {
            return Optional.empty();
        }

        return Optional.of(
            (Usuario) authentication.getPrincipal()
        );
    }
}