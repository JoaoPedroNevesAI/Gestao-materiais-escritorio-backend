package br.com.ifescritorio.model.auditoria;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RegistroAuditoriaRepository
        extends JpaRepository<RegistroAuditoria, Long> {

    @Query("""
            SELECT registro
            FROM RegistroAuditoria registro
            WHERE (:usuarioId IS NULL OR registro.usuarioId = :usuarioId)
              AND (:acao IS NULL OR registro.acao = :acao)
              AND (:entidade IS NULL OR LOWER(registro.entidade) = LOWER(:entidade))
              AND (:registroId IS NULL OR registro.registroId = :registroId)
              AND (:dataInicio IS NULL OR registro.dataHora >= :dataInicio)
              AND (:dataFim IS NULL OR registro.dataHora <= :dataFim)
            """)
    Page<RegistroAuditoria> buscar(
            @Param("usuarioId") Long usuarioId,
            @Param("acao") TipoAcaoAuditoria acao,
            @Param("entidade") String entidade,
            @Param("registroId") Long registroId,
            @Param("dataInicio") LocalDateTime dataInicio,
            @Param("dataFim") LocalDateTime dataFim,
            Pageable pageable);
}
