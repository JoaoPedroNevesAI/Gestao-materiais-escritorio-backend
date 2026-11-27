package br.com.ifescritorio.api.auditoria;

import java.time.LocalDateTime;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.ifescritorio.model.auditoria.RegistroAuditoria;
import br.com.ifescritorio.model.auditoria.TipoAcaoAuditoria;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AuditoriaResponse {

    private Long id;
    private Long usuarioId;
    private String usuarioNome;
    private String usuarioTipo;
    private TipoAcaoAuditoria acao;
    private String entidade;
    private Long registroId;
    private String descricao;
    private JsonNode dadosAnteriores;
    private JsonNode dadosNovos;
    private LocalDateTime dataHora;

    public static AuditoriaResponse from(
            RegistroAuditoria registro,
            ObjectMapper objectMapper) {

        return AuditoriaResponse.builder()
                .id(registro.getId())
                .usuarioId(registro.getUsuarioId())
                .usuarioNome(registro.getUsuarioNome())
                .usuarioTipo(registro.getUsuarioTipo())
                .acao(registro.getAcao())
                .entidade(registro.getEntidade())
                .registroId(registro.getRegistroId())
                .descricao(registro.getDescricao())
                .dadosAnteriores(
                        converterJson(registro.getDadosAnteriores(), objectMapper))
                .dadosNovos(
                        converterJson(registro.getDadosNovos(), objectMapper))
                .dataHora(registro.getDataHora())
                .build();
    }

    private static JsonNode converterJson(
            String valor,
            ObjectMapper objectMapper) {

        if (valor == null || valor.isBlank()) {
            return null;
        }

        try {
            return objectMapper.readTree(valor);
        } catch (Exception e) {
            throw new IllegalStateException(
                    "Não foi possível ler os detalhes do registro de auditoria.",
                    e);
        }
    }
}
