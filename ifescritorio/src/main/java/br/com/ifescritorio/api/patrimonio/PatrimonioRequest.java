package br.com.ifescritorio.api.patrimonio;

import br.com.ifescritorio.model.local.Local;
import br.com.ifescritorio.model.material.Material;
import br.com.ifescritorio.model.patrimonio.Patrimonio;
import br.com.ifescritorio.model.patrimonio.StatusPatrimonio;
import lombok.Data;

@Data
public class PatrimonioRequest {

    private String codigoPatrimonio;

    private Long materialId;

    private Long localId;

    private String observacao;

    public Patrimonio build() {

        Material material = new Material();
        material.setId(materialId);

        Local local = new Local();
        local.setId(localId);

        return Patrimonio.builder()
                .codigoPatrimonio(codigoPatrimonio)
                .material(material)
                .local(local)
                .status(StatusPatrimonio.DISPONIVEL)
                .qrCode(codigoPatrimonio)
                .observacao(observacao)
                .build();
    }
}