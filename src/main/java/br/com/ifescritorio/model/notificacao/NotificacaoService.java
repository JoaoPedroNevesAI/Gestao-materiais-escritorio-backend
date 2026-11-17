package br.com.ifescritorio.model.notificacao;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.ifescritorio.model.auditoria.AuditoriaDados;
import br.com.ifescritorio.model.auditoria.AuditoriaService;
import br.com.ifescritorio.model.auditoria.TipoAcaoAuditoria;
import br.com.ifescritorio.model.usuario.TipoUsuario;
import br.com.ifescritorio.model.usuario.Usuario;
import br.com.ifescritorio.model.usuario.UsuarioRepository;
import br.com.ifescritorio.util.exception.EntidadeNaoEncontradaException;

@Service
public class NotificacaoService {

    @Autowired
    private NotificacaoRepository repository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private AuditoriaService auditoriaService;

    @Transactional
    public Notificacao criar(
            Usuario usuario,
            String titulo,
            String mensagem,
            TipoNotificacao tipo,
            Long referenciaId) {

        Notificacao notificacao =
                Notificacao.builder()
                        .usuario(usuario)
                        .titulo(titulo)
                        .mensagem(mensagem)
                        .tipo(tipo)
                        .lida(Boolean.FALSE)
                        .dataCriacao(LocalDateTime.now())
                        .referenciaId(referenciaId)
                        .build();

        Notificacao salva = repository.save(notificacao);

        auditoriaService.registrar(
                TipoAcaoAuditoria.CRIACAO,
                "NOTIFICACAO",
                salva.getId(),
                "Notificação criada",
                null,
                AuditoriaDados.mapa(
                        "usuarioDestinoId", usuario.getId(),
                        "tipo", tipo.name(),
                        "titulo", titulo,
                        "referenciaId", referenciaId));

        return salva;
    }

    /**
     * Envia uma notificação para todos os administradores.
     */
    @Transactional
    public void notificarAdministradores(
            String titulo,
            String mensagem,
            TipoNotificacao tipo,
            Long referenciaId) {

        List<Usuario> administradores =
                usuarioRepository.findByTipo(
                        TipoUsuario.ADM);

        for (Usuario administrador : administradores) {

            criar(
                    administrador,
                    titulo,
                    mensagem,
                    tipo,
                    referenciaId
            );
        }
    }

    public List<Notificacao> listarPorUsuario(
            Long usuarioId) {

        return repository
                .findByUsuarioIdOrderByDataCriacaoDesc(
                        usuarioId);
    }

    public List<Notificacao> listarNaoLidas(
            Long usuarioId) {

        return repository
                .findByUsuarioIdAndLidaFalseOrderByDataCriacaoDesc(
                        usuarioId);
    }

    public long contarNaoLidas(
            Long usuarioId) {

        return repository
                .countByUsuarioIdAndLidaFalse(
                        usuarioId);
    }

    @Transactional
    public void marcarComoLida(
            Long notificacaoId,
            Long usuarioId) {

        Notificacao notificacao =
                repository.findById(notificacaoId)
                        .orElseThrow(() ->
                                new EntidadeNaoEncontradaException(
                                        "Notificacao",
                                        notificacaoId));

        /*
         * Impede que um usuário marque como lida
         * uma notificação pertencente a outro usuário.
         */
        if (!notificacao.getUsuario()
                .getId()
                .equals(usuarioId)) {

            throw new IllegalArgumentException(
                    "A notificação não pertence ao usuário."
            );
        }

        notificacao.setLida(Boolean.TRUE);

        repository.save(notificacao);

        auditoriaService.registrar(
                TipoAcaoAuditoria.ATUALIZACAO,
                "NOTIFICACAO",
                notificacao.getId(),
                "Notificação marcada como lida",
                AuditoriaDados.mapa("lida", Boolean.FALSE),
                AuditoriaDados.mapa("lida", Boolean.TRUE));
    }

    @Transactional
    public void marcarTodasComoLidas(
            Long usuarioId) {

        List<Notificacao> notificacoes =
                repository
                        .findByUsuarioIdAndLidaFalseOrderByDataCriacaoDesc(
                                usuarioId);

        for (Notificacao notificacao : notificacoes) {

            notificacao.setLida(Boolean.TRUE);
        }

        repository.saveAll(notificacoes);

        if (!notificacoes.isEmpty()) {
            auditoriaService.registrar(
                    TipoAcaoAuditoria.ATUALIZACAO,
                    "NOTIFICACAO",
                    null,
                    "Todas as notificações pendentes foram marcadas como lidas",
                    AuditoriaDados.mapa("quantidade", notificacoes.size()),
                    AuditoriaDados.mapa("lidas", Boolean.TRUE));
        }
    }

    @Transactional
    public void excluir(
            Long notificacaoId,
            Long usuarioId) {

        Notificacao notificacao =
                repository.findById(notificacaoId)
                        .orElseThrow(() ->
                                new EntidadeNaoEncontradaException(
                                        "Notificacao",
                                        notificacaoId));

        if (!notificacao.getUsuario()
                .getId()
                .equals(usuarioId)) {

            throw new IllegalArgumentException(
                    "A notificação não pertence ao usuário."
            );
        }

        repository.delete(notificacao);

        auditoriaService.registrar(
                TipoAcaoAuditoria.EXCLUSAO,
                "NOTIFICACAO",
                notificacao.getId(),
                "Notificação excluída",
                AuditoriaDados.mapa(
                        "usuarioDestinoId", notificacao.getUsuario().getId(),
                        "tipo", notificacao.getTipo().name(),
                        "titulo", notificacao.getTitulo()),
                null);
    }
}