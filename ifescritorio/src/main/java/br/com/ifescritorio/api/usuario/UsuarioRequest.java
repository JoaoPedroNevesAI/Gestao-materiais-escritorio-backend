package br.com.ifescritorio.api.usuario;

import br.com.ifescritorio.model.usuario.Usuario;
import br.com.ifescritorio.model.usuario.TipoUsuario;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioRequest {

    private String nome;

    private String email;

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