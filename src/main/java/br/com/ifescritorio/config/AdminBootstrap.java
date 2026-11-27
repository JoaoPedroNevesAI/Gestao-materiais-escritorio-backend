package br.com.ifescritorio.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import br.com.ifescritorio.model.usuario.TipoUsuario;
import br.com.ifescritorio.model.usuario.Usuario;
import br.com.ifescritorio.model.usuario.UsuarioRepository;
import br.com.ifescritorio.model.usuario.UsuarioService;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class AdminBootstrap implements ApplicationRunner {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioService usuarioService;

    @Value("${BOOTSTRAP_ADMIN_ENABLED:false}")
    private boolean habilitado;

    @Value("${BOOTSTRAP_ADMIN_NOME:}")
    private String nome;

    @Value("${BOOTSTRAP_ADMIN_EMAIL:}")
    private String email;

    @Value("${BOOTSTRAP_ADMIN_SENHA:}")
    private String senha;

    public AdminBootstrap(
            UsuarioRepository usuarioRepository,
            UsuarioService usuarioService) {

        this.usuarioRepository = usuarioRepository;
        this.usuarioService = usuarioService;
    }

    @Override
    public void run(ApplicationArguments args) {

        if (!habilitado) {
            return;
        }

        if (!usuarioRepository.findByTipo(TipoUsuario.ADM).isEmpty()) {
            log.info("Já existe um administrador cadastrado. Bootstrap ignorado.");
            return;
        }

        if (
            nome == null || nome.isBlank() ||
            email == null || email.isBlank() ||
            senha == null || senha.isBlank()
        ) {
            throw new IllegalStateException(
                "Configure nome, email e senha do administrador inicial."
            );
        }

        Usuario administrador = Usuario.builder()
            .nome(nome.trim())
            .email(email.trim())
            .senha(senha)
            .tipo(TipoUsuario.ADM)
            .build();

        usuarioService.save(administrador);

        log.info("Administrador inicial criado com sucesso.");
    }
}