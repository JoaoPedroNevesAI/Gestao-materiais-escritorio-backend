package br.com.ifescritorio.api.local;

import br.com.ifescritorio.model.local.Local;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LocalRequest {

    @NotBlank(message = "Nome é obrigatório")
    private String nome;

    public Local build() {

        return Local.builder()
            .nome(nome)
            .build();
    }
}