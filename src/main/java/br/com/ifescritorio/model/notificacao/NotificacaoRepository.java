package br.com.ifescritorio.model.notificacao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificacaoRepository
        extends JpaRepository<Notificacao, Long> {

    List<Notificacao>
            findByUsuarioIdOrderByDataCriacaoDesc(
                    Long usuarioId);

    List<Notificacao>
            findByUsuarioIdAndLidaFalseOrderByDataCriacaoDesc(
                    Long usuarioId);

    long countByUsuarioIdAndLidaFalse(
            Long usuarioId);
}