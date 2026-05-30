package br.com.ifescritorio.api.movimentacao;

import java.time.LocalDate;

import br.com.ifescritorio.model.material.Material;
import br.com.ifescritorio.model.movimentacao.Movimentacao;
import br.com.ifescritorio.model.movimentacao.TipoMovimentacao;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MovimentacaoRequest {

    @NotNull(message = "Material é obrigatório")
    private Long materialId;

    @NotNull(message = "Tipo de movimentação é obrigatório")
    private TipoMovimentacao tipo;

    @NotNull(message = "Quantidade é obrigatória")
    @Min(value = 1, message = "Quantidade deve ser maior que 0")
    private Integer quantidade;

    public Movimentacao build() {
        Material material = new Material();
        material.setId(materialId);

        return Movimentacao.builder()
            .material(material)
            .tipo(tipo)
            .quantidade(quantidade)
            .data(LocalDate.now())
            .build();
    }
}