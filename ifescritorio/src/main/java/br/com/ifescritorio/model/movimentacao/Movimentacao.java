package br.com.ifescritorio.model.movimentacao;

import java.time.LocalDateTime;

import br.com.ifescritorio.model.local.Local;
import br.com.ifescritorio.model.patrimonio.Patrimonio;
import br.com.ifescritorio.model.usuario.Usuario;
import br.com.ifescritorio.util.entity.EntidadeAuditavel;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
    @org.hibernate.annotations.NotFound(action = org.hibernate.annotations.NotFoundAction.IGNORE)
    private Patrimonio patrimonio;

    @ManyToOne
    @JoinColumn(name = "local_origem_id", nullable = false)
    @org.hibernate.annotations.NotFound(action = org.hibernate.annotations.NotFoundAction.IGNORE)
    private Local localOrigem;

    @ManyToOne
    @JoinColumn(name = "local_destino_id", nullable = false)
    @org.hibernate.annotations.NotFound(action = org.hibernate.annotations.NotFoundAction.IGNORE)
    private Local localDestino;

    @Column(length = 500)
    private String observacao;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @Column(nullable = false)
    private LocalDateTime dataMovimentacao;
}