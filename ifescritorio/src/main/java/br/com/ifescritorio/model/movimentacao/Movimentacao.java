package br.com.ifescritorio.model.movimentacao;

import java.time.LocalDateTime;

import br.com.ifescritorio.model.local.Local;
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
@Table(name = "movimentacao")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Movimentacao extends EntidadeAuditavel {

    @ManyToOne
    @JoinColumn(name = "patrimonio_id", nullable = false)
    @org.hibernate.annotations.NotFound(
        action = org.hibernate.annotations.NotFoundAction.IGNORE
    )
    private Patrimonio patrimonio;

    @ManyToOne
    @JoinColumn(name = "local_origem_id")
    @org.hibernate.annotations.NotFound(
        action = org.hibernate.annotations.NotFoundAction.IGNORE
    )
    private Local localOrigem;

    @ManyToOne
    @JoinColumn(name = "local_destino_id", nullable = false)
    @org.hibernate.annotations.NotFound(
        action = org.hibernate.annotations.NotFoundAction.IGNORE
    )
    private Local localDestino;

    /*
     * Usuário que solicitou a movimentação.
     */
    @ManyToOne
    @JoinColumn(name = "solicitante_id")
    private Usuario solicitante;

    /*
     * Usuário administrativo responsável
     * pela aprovação/execução.
     *
     * Mantemos o nome da coluna antiga "usuario_id"
     * para preservar compatibilidade com o banco.
     */
    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario aprovador;

    @Column(length = 500)
    private String observacao;

    @Column(length = 500)
    private String observacaoAdmin;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusMovimentacao status;

    private LocalDateTime dataSolicitacao;

    private LocalDateTime dataResposta;

    /*
     * Só é preenchida quando a movimentação
     * efetivamente é aprovada/executada.
     */
    private LocalDateTime dataMovimentacao;
}