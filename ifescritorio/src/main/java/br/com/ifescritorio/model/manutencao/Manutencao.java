package br.com.ifescritorio.model.manutencao;

import java.time.LocalDateTime;

import br.com.ifescritorio.model.patrimonio.Patrimonio;
import br.com.ifescritorio.model.usuario.Usuario;
import br.com.ifescritorio.util.entity.EntidadeAuditavel;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "manutencao")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Manutencao extends EntidadeAuditavel {

	@ManyToOne
	@JoinColumn(name = "patrimonio_id", nullable = false)
	private Patrimonio patrimonio;

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