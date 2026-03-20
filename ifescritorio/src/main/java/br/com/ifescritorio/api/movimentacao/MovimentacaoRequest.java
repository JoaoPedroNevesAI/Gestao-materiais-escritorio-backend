package br.com.ifescritorio.api.movimentacao;

import java.time.LocalDate;

import br.com.ifescritorio.model.material.Material;
import br.com.ifescritorio.model.movimentacao.Movimentacao;
import br.com.ifescritorio.model.movimentacao.TipoMovimentacao;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MovimentacaoRequest {

    private Long materialId;
    private TipoMovimentacao tipo;
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