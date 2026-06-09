package br.com.ifescritorio.model.manutencao;

import java.time.LocalDateTime;

import br.com.ifescritorio.model.material.Material;
import br.com.ifescritorio.model.usuario.Usuario;
import br.com.ifescritorio.util.entity.EntidadeAuditavel;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "manutencao")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Manutencao extends EntidadeAuditavel {

    @ManyToOne
    @JoinColumn(name = "material_id", nullable = false)
    private Material material;

    @ManyToOne
    @JoinColumn(name = "usuario_solicitante_id")
    private Usuario solicitante;

    @Column(nullable = false, length = 500)
    private String descricao;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusManutencao status;

    @Column(length = 500)
    private String observacaoAdmin;

    @Column(nullable = false)
    private LocalDateTime dataSolicitacao;
}