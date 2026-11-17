package br.com.ifescritorio.model.auditoria;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegistroAuditoriaRepository
        extends JpaRepository<RegistroAuditoria, Long> {
}
