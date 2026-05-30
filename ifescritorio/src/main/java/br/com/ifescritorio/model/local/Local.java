package br.com.ifescritorio.model.local;

import org.hibernate.annotations.SQLRestriction;

import br.com.ifescritorio.util.entity.EntidadeAuditavel;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "local")
@SQLRestriction("habilitado = true")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Local extends EntidadeAuditavel {

    @Column(nullable = false, length = 100)
    private String nome;
}