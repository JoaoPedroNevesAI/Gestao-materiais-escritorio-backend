package br.com.ifescritorio.model.movimentacao;

import java.time.LocalDateTime;

import br.com.ifescritorio.model.local.Local;
import br.com.ifescritorio.model.material.Material;
import br.com.ifescritorio.model.usuario.Usuario;
import br.com.ifescritorio.util.entity.EntidadeAuditavel;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "movimentacao")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Movimentacao extends EntidadeAuditavel {

    @ManyToOne
    @JoinColumn(name = "material_id", nullable = false)
    private Material material;

    @ManyToOne
    @JoinColumn(name = "local_origem_id", nullable = false)
    private Local localOrigem;

    @ManyToOne
    @JoinColumn(name = "local_destino_id", nullable = false)
    private Local localDestino;

    @Column(length = 500)
    private String observacao;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @Column(nullable = false)
    private LocalDateTime dataMovimentacao;
}