package br.com.ifescritorio.api.movimentacao;

import br.com.ifescritorio.model.movimentacao.TipoMovimentacao;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MovimentacaoRequest {

    @NotNull
    private Long materialId;

    @NotNull
    private TipoMovimentacao tipo;

    private String descricao;
}