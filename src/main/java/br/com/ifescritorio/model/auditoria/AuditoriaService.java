package br.com.ifescritorio.model.auditoria;

import java.time.LocalDateTime;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.ifescritorio.model.usuario.Usuario;

@Service
public class AuditoriaService {

    private final RegistroAuditoriaRepository repository;
    private final ObjectMapper objectMapper;

    public AuditoriaService(
            RegistroAuditoriaRepository repository,
            ObjectMapper objectMapper) {

        this.repository = repository;
        this.objectMapper = objectMapper;
    }

    @Transactional
    public RegistroAuditoria registrar(
            TipoAcaoAuditoria acao,
            String entidade,
            Long registroId,
            String descricao,
            Object dadosAnteriores,
            Object dadosNovos) {

        Usuario usuario = obterUsuarioAutenticado();

        RegistroAuditoria registro = RegistroAuditoria.builder()
                .usuarioId(usuario != null ? usuario.getId() : null)
                .usuarioNome(usuario != null ? usuario.getNome() : null)
                .usuarioTipo(
                        usuario != null && usuario.getTipo() != null
                                ? usuario.getTipo().name()
                                : null)
                .acao(acao)
                .entidade(entidade)
                .registroId(registroId)
                .descricao(descricao)
                .dadosAnteriores(serializar(dadosAnteriores))
                .dadosNovos(serializar(dadosNovos))
                .dataHora(LocalDateTime.now())
                .build();

        return repository.save(registro);
    }

    private Usuario obterUsuarioAutenticado() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (
                authentication == null ||
                !authentication.isAuthenticated() ||
                !(authentication.getPrincipal() instanceof Usuario usuario)
        ) {
            return null;
        }

        return usuario;
    }

    private String serializar(Object dados) {

        if (dados == null) {
            return null;
        }

        try {
            return objectMapper.writeValueAsString(dados);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException(
                    "Não foi possível serializar os detalhes da auditoria.",
                    e);
        }
    }
}
