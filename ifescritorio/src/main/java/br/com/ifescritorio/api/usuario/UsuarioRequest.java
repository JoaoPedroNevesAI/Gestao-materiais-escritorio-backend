package br.com.ifescritorio.api.usuario;

import br.com.ifescritorio.model.usuario.TipoUsuario;
import br.com.ifescritorio.model.usuario.Usuario;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioRequest {

    @NotBlank(message = "Nome é obrigatório")
    private String nome;

    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email inválido")
    private String email;

    @NotBlank(message = "Senha é obrigatória")
    private String senha;

    private TipoUsuario tipo;

    private String telefone;

    private String cpf;

    public Usuario build() {
        return Usuario.builder()
            .nome(nome)
            .email(email)
            .senha(senha)
            .tipo(tipo)
            .telefone(telefone)
            .cpf(cpf)
            .build();
    }
}