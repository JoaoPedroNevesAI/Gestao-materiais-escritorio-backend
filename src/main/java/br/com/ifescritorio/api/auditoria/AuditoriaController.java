package br.com.ifescritorio.api.auditoria;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.ifescritorio.model.auditoria.AuditoriaService;
import br.com.ifescritorio.model.auditoria.RegistroAuditoria;
import br.com.ifescritorio.model.auditoria.TipoAcaoAuditoria;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/auditoria")
@Tag(
    name = "API Auditoria",
    description = "Consulta do histórico de operações realizadas no sistema"
)
public class AuditoriaController {

    private final AuditoriaService auditoriaService;
    private final ObjectMapper objectMapper;

    public AuditoriaController(
            AuditoriaService auditoriaService,
            ObjectMapper objectMapper) {

        this.auditoriaService = auditoriaService;
        this.objectMapper = objectMapper;
    }

    @Operation(
        summary = "Consultar histórico de auditoria",
        description = "Retorna o histórico paginado de operações, com filtros por usuário, ação, módulo, registro e período. A consulta é restrita a administradores."
    )
    @GetMapping
    public ResponseEntity<Page<AuditoriaResponse>> listar(
            @RequestParam(required = false) Long usuarioId,
            @RequestParam(required = false) TipoAcaoAuditoria acao,
            @RequestParam(required = false) String entidade,
            @RequestParam(required = false) Long registroId,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime dataInicio,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime dataFim,
            @PageableDefault(
                    size = 20,
                    sort = "dataHora",
                    direction = Sort.Direction.DESC)
            Pageable pageable) {

        if (
                dataInicio != null &&
                dataFim != null &&
                dataInicio.isAfter(dataFim)
        ) {
            return ResponseEntity.badRequest().build();
        }

        int tamanhoPagina = Math.max(1, Math.min(pageable.getPageSize(), 100));

        Pageable paginaSegura = PageRequest.of(
                pageable.getPageNumber(),
                tamanhoPagina,
                Sort.by(Sort.Direction.DESC, "dataHora"));

        Page<AuditoriaResponse> resposta =
                auditoriaService.listar(
                        usuarioId,
                        acao,
                        entidade,
                        registroId,
                        dataInicio,
                        dataFim,
                        paginaSegura)
                .map(registro ->
                        AuditoriaResponse.from(registro, objectMapper));

        return ResponseEntity.ok(resposta);
    }
}
