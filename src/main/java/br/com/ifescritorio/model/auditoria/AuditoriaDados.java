package br.com.ifescritorio.model.auditoria;

import java.util.LinkedHashMap;
import java.util.Map;

public final class AuditoriaDados {

    private AuditoriaDados() {
    }

    public static Map<String, Object> mapa(Object... campos) {

        if (campos == null || campos.length % 2 != 0) {
            throw new IllegalArgumentException(
                    "Os dados de auditoria devem ser informados em pares de chave e valor.");
        }

        Map<String, Object> dados = new LinkedHashMap<>();

        for (int i = 0; i < campos.length; i += 2) {

            if (!(campos[i] instanceof String chave)) {
                throw new IllegalArgumentException(
                        "A chave dos dados de auditoria deve ser uma String.");
            }

            dados.put(chave, campos[i + 1]);
        }

        return dados;
    }
}
