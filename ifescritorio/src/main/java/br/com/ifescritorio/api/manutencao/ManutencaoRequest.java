package br.com.ifescritorio.api.manutencao;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ManutencaoRequest {

	@NotNull
	private Long patrimonioId;

    @NotBlank
    private String descricao;
}