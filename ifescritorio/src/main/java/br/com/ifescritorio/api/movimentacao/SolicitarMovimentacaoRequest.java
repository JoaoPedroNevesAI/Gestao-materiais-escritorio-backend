package br.com.ifescritorio.api.movimentacao;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SolicitarMovimentacaoRequest {

	@NotNull
	private Long patrimonioId;

    @NotNull
    private Long localDestinoId;

    private String observacao;
}