package br.com.ifescritorio.api.material;

import java.math.BigDecimal;

import br.com.ifescritorio.model.material.Material;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MaterialRequest {

    @NotBlank(message = "Nome é obrigatório")
    private String nome;

    private String descricao;

    @NotNull(message = "Quantidade é obrigatória")
    @Min(value = 0, message = "Quantidade não pode ser negativa")
    private Integer quantidade;

    private String categoria;

    private String local;

    private BigDecimal valor;

    public Material build() {
        return Material.builder()
            .nome(nome)
            .descricao(descricao)
            .quantidade(quantidade)
            .categoria(categoria)
            .local(local)
            .valor(valor)
            .build();
    }
}