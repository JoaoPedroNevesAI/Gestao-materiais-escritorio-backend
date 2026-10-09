package br.com.ifescritorio.api.movimentacao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import br.com.ifescritorio.model.movimentacao.Movimentacao;
import br.com.ifescritorio.model.movimentacao.MovimentacaoService;
import br.com.ifescritorio.model.usuario.Usuario;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/movimentacao")
@CrossOrigin
@Tag(
    name = "API Movimentação",
    description = "API responsável pelas solicitações, aprovações e histórico de movimentações dos patrimônios"
)
public class MovimentacaoController {

    @Autowired
    private MovimentacaoService service;

    @Operation(
        summary = "Solicitar movimentação",
        description = "Cria uma solicitação de transferência de um patrimônio para outro local."
    )
    @PostMapping
    public Movimentacao solicitar(
            @RequestBody MovimentacaoRequest request,
            @AuthenticationPrincipal Usuario usuario) {

        return service.solicitar(
                request.getPatrimonioId(),
                request.getLocalDestinoId(),
                request.getObservacao(),
                usuario
        );
    }

    @Operation(
        summary = "Listar todas as movimentações",
        description = "Lista todas as movimentações e solicitações registradas no sistema."
    )
    @GetMapping
    public List<Movimentacao> listarTodas() {

        return service.listarTodas();
    }

    @Operation(
        summary = "Listar movimentações pendentes",
        description = "Retorna as movimentações que aguardam aprovação administrativa."
    )
    @GetMapping("/pendentes")
    public List<Movimentacao> listarPendentes() {

        return service.listarPendentes();
    }

    @Operation(
        summary = "Listar histórico por patrimônio",
        description = "Retorna o histórico de movimentações de um patrimônio."
    )
    @GetMapping("/patrimonio/{id}")
    public List<Movimentacao> listarPorPatrimonio(
            @PathVariable Long id) {

        return service.listarPorPatrimonio(id);
    }

    @Operation(
        summary = "Buscar movimentação por ID",
        description = "Retorna uma movimentação específica."
    )
    @GetMapping("/{id}")
    public Movimentacao obterPorId(
            @PathVariable Long id) {

        return service.obterPorId(id);
    }

    @Operation(
        summary = "Aprovar movimentação",
        description = "Aprova a solicitação e realiza a transferência do patrimônio."
    )
    @PutMapping("/{id}/aprovar")
    public Movimentacao aprovar(
            @PathVariable Long id,
            @RequestBody AprovarMovimentacaoRequest request,
            @AuthenticationPrincipal Usuario admin) {

        return service.aprovar(
                id,
                request.getObservacaoAdmin(),
                admin
        );
    }

    @Operation(
        summary = "Reprovar movimentação",
        description = "Reprova uma solicitação de movimentação."
    )
    @PutMapping("/{id}/reprovar")
    public Movimentacao reprovar(
            @PathVariable Long id,
            @RequestBody AprovarMovimentacaoRequest request,
            @AuthenticationPrincipal Usuario admin) {

        return service.reprovar(
                id,
                request.getObservacaoAdmin(),
                admin
        );
    }
}