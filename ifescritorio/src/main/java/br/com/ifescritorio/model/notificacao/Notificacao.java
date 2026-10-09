package br.com.ifescritorio.model.notificacao;

import java.time.LocalDateTime;

import br.com.ifescritorio.model.usuario.Usuario;
import br.com.ifescritorio.util.entity.EntidadeAuditavel;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "notificacao")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Notificacao extends EntidadeAuditavel {

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(nullable = false, length = 150)
    private String titulo;

    @Column(nullable = false, length = 500)
    private String mensagem;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoNotificacao tipo;

    @Column(nullable = false)
    private Boolean lida;

    @Column(nullable = false)
    private LocalDateTime dataCriacao;

    /**
     * ID do registro relacionado à notificação.
     *
     * Exemplo:
     * movimentação -> ID da solicitação
     * manutenção -> ID da manutenção
     */
    private Long referenciaId;
}