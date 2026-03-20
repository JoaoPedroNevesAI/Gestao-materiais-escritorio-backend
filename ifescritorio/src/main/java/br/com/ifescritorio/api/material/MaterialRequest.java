package br.com.ifescritorio.api.material;

import br.com.ifescritorio.model.material.Material;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MaterialRequest {

    private String nome;

    private String descricao;

    private Integer quantidade;

    public Material build() {

        return Material.builder()
            .nome(nome)
            .descricao(descricao)
            .quantidade(quantidade)
            .build();
    }

}