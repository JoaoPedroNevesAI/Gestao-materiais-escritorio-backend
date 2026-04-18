package br.com.ifescritorio.model.material;

import java.math.BigDecimal;

import br.com.ifescritorio.model.categoria.Categoria;
import br.com.ifescritorio.util.entity.EntidadeAuditavel;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "material")
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

    @Column(length = 100)
    private String local;

    @Column(precision = 10, scale = 2)
    private BigDecimal valor;
}