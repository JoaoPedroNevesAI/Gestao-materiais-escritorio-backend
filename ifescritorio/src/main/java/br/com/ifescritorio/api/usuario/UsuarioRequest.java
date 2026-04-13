package br.com.ifescritorio.api.usuario;

import br.com.ifescritorio.model.usuario.Usuario;
import br.com.ifescritorio.model.usuario.TipoUsuario;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    @Size(min = 6, message = "Senha deve ter pelo menos 6 caracteres")
    private String senha;

    private TipoUsuario tipo;

    public Usuario build() {
        return Usuario.builder()
            .nome(nome)
            .email(email)
            .senha(senha)
            .tipo(tipo)
            .build();
    }
}