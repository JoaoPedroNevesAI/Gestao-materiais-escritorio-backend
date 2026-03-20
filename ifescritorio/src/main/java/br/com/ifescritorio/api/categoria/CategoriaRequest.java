package br.com.ifescritorio.api.categoria;

import br.com.ifescritorio.model.categoria.Categoria;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoriaRequest {

    private String nome;

    private String descricao;

    public Categoria build() {
        return Categoria.builder()
            .nome(nome)
            .descricao(descricao)
            .build();
    }
}