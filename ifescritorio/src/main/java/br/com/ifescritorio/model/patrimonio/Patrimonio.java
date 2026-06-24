package br.com.ifescritorio.model.patrimonio;

import br.com.ifescritorio.model.local.Local;
import br.com.ifescritorio.model.material.Material;
import br.com.ifescritorio.util.entity.EntidadeAuditavel;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "patrimonio")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Patrimonio extends EntidadeAuditavel {

    @Column(unique = true)
    private String codigoPatrimonio;

    @ManyToOne
    @JoinColumn(name = "material_id", nullable = false)
    private Material material;

    @ManyToOne
    @JoinColumn(name = "local_id")
    private Local local;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusPatrimonio status;

    @Column(length = 500)
    private String observacao;

    @Column
    private String qrCode;
}