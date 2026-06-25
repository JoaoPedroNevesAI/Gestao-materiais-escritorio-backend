package br.com.ifescritorio.api.movimentacao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import br.com.ifescritorio.model.movimentacao.SolicitacaoMovimentacao;
import br.com.ifescritorio.model.movimentacao.SolicitacaoMovimentacaoService;
import br.com.ifescritorio.model.usuario.Usuario;
import br.com.ifescritorio.model.usuario.UsuarioRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/solicitacao-movimentacao")
@CrossOrigin
@Tag(
    name = "API Solicitação de Movimentação",
    description = "API responsável pelas solicitações de transferência de patrimônios entre locais"
)
public class SolicitacaoMovimentacaoController {

    @Autowired
    private SolicitacaoMovimentacaoService service;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Operation(
        summary = "Solicitar movimentação",
        description = "Cria uma solicitação de transferência de um patrimônio para outro local."
    )
    @PostMapping("/solicitar")
    public SolicitacaoMovimentacao solicitar(
            @RequestBody SolicitarMovimentacaoRequest request) {

        String email =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getName();

        Usuario usuario =
                usuarioRepository
                        .findByEmail(email)
                        .orElse(null);

        return service.solicitar(
                request.getPatrimonioId(),
                request.getLocalDestinoId(),
                request.getObservacao(),
                usuario);
    }

    @Operation(
        summary = "Listar solicitações pendentes",
        description = "Retorna todas as solicitações de movimentação que aguardam aprovação."
    )
    @GetMapping("/pendentes")
    public List<SolicitacaoMovimentacao> pendentes() {

        return service.listarPendentes();
    }

    @Operation(
        summary = "Aprovar solicitação",
        description = "Aprova uma solicitação de movimentação e realiza a transferência do patrimônio."
    )
    @PutMapping("/{id}/aprovar")
    public SolicitacaoMovimentacao aprovar(
            @PathVariable Long id,
            @RequestBody AprovarMovimentacaoRequest request) {

        String email =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getName();

        Usuario admin =
                usuarioRepository
                        .findByEmail(email)
                        .orElse(null);

        return service.aprovar(
                id,
                request.getObservacaoAdmin(),
                admin);
    }

    @Operation(
        summary = "Reprovar solicitação",
        description = "Reprova uma solicitação de movimentação informando o motivo."
    )
    @PutMapping("/{id}/reprovar")
    public SolicitacaoMovimentacao reprovar(
            @PathVariable Long id,
            @RequestBody AprovarMovimentacaoRequest request) {

        return service.reprovar(
                id,
                request.getObservacaoAdmin());
    }
}