package br.com.ifescritorio.model.material;

import java.math.BigDecimal;
import java.util.List;

import org.hibernate.annotations.SQLRestriction;

import com.fasterxml.jackson.annotation.JsonIgnore;

import br.com.ifescritorio.model.categoria.Categoria;
import br.com.ifescritorio.model.local.Local;
import br.com.ifescritorio.model.patrimonio.Patrimonio;
import br.com.ifescritorio.util.entity.EntidadeAuditavel;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "material")
@SQLRestriction("habilitado = true")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Material extends EntidadeAuditavel {

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(length = 255)
    private String descricao;

    @Column(nullable = false)
    private Integer quantidade;

    @ManyToOne
    @JoinColumn(name = "categoria_id", nullable = false)
    private Categoria categoria;

    @ManyToOne
    @JoinColumn(name = "local_id")
    private Local local;

    @Column(precision = 10, scale = 2)
    private BigDecimal valor;

    @Column(name = "imagem")
    private String imagem;

    @OneToMany(mappedBy = "material")
    private List<Patrimonio> patrimonios;
}