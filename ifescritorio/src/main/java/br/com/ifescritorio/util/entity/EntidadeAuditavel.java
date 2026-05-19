package br.com.ifescritorio.util.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnore;

import br.com.ifescritorio.model.usuario.Usuario;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Version;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class EntidadeAuditavel extends EntidadeNegocio {

    @Version
    @JsonIgnore
    private Long versao;

    @CreatedDate
    @JsonIgnore
    private LocalDateTime dataCriacao;

    @LastModifiedDate
    @JsonIgnore
    private LocalDateTime dataUltimaModificacao;

    @CreatedBy
    @ManyToOne
    @JoinColumn
    @JsonIgnore
    private Usuario criadoPor;

    @LastModifiedBy
    @ManyToOne
    @JoinColumn
    @JsonIgnore
    private Usuario ultimaModificacaoPor;
}