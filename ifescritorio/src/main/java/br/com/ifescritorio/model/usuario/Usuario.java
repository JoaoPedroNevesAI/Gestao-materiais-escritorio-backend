package br.com.ifescritorio.model.usuario;

import java.time.LocalDateTime;

import br.com.ifescritorio.util.entity.EntidadeAuditavel;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "usuario")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Usuario extends EntidadeAuditavel {

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(nullable = false, unique = true, length = 150)
    private String email;

    @Column(nullable = false)
    private String senha;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoUsuario tipo;

    @Column(length = 20)
    private String telefone;

    @Column(unique = true, length = 11)
    private String cpf;

    private LocalDateTime ultimoLogin;
}