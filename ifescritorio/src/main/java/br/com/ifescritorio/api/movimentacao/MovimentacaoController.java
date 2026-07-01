package br.com.ifescritorio.api.movimentacao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import br.com.ifescritorio.model.movimentacao.Movimentacao;
import br.com.ifescritorio.model.movimentacao.MovimentacaoService;
import br.com.ifescritorio.model.usuario.Usuario;
import br.com.ifescritorio.model.usuario.UsuarioRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/movimentacao")
@CrossOrigin
@Tag(
    name = "API Movimentação",
    description = "API responsável pelas transferências e histórico de movimentações dos patrimônios"
)
public class MovimentacaoController {

    @Autowired
    private MovimentacaoService service;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Operation(
        summary = "Transferir patrimônio",
        description = "Realiza a transferência de um patrimônio para outro local e registra a movimentação."
    )
    @PostMapping("/transferir")
    public Movimentacao transferir(
            @RequestBody MovimentacaoRequest request) {

        String email =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getName();

        Usuario usuario =
                usuarioRepository
                        .findByEmail(email)
                        .orElse(null);

        return service.transferir(
                request.getPatrimonioId(),
                request.getLocalDestinoId(),
                request.getObservacao(),
                usuario);
    }

    @Operation(
        summary = "Histórico de movimentações",
        description = "Lista todas as movimentações registradas para um patrimônio específico."
    )
    @GetMapping("/patrimonio/{id}")
    public List<Movimentacao> listarPorPatrimonio(
            @PathVariable Long id) {

        return service.listarPorPatrimonio(id);
    }

    @Operation(
        summary = "Listar todas as movimentações",
        description = "Lista todas as movimentações de patrimônio registradas no sistema."
    )
    @GetMapping
    public List<Movimentacao> listarTodas() {
        return service.listarTodas();
    }
}