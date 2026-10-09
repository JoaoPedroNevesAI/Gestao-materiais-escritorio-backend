package br.com.ifescritorio.model.notificacao;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

        return repository.save(notificacao);
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
    }
}