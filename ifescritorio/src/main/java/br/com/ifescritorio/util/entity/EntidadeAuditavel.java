package br.com.ifescritorio.util.entity;

import java.time.LocalDate;

import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Version;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
public abstract class EntidadeAuditavel extends EntidadeNegocio {
    
    @Version
    private Long versao;

    private LocalDate dataCriacao;

    private LocalDate dataUltimaModificacao;

    private Long criadoPor;

    private Long ultimaModificacaoPor;

    @PrePersist
    public void prePersist() {
        this.dataCriacao = LocalDate.now();
        this.dataUltimaModificacao = LocalDate.now();
        this.setHabilitado(true);
    }

    @PreUpdate
    public void preUpdate() {
        this.dataUltimaModificacao = LocalDate.now();
    }
}