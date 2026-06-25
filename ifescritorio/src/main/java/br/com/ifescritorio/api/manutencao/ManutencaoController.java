package br.com.ifescritorio.api.manutencao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import br.com.ifescritorio.model.manutencao.Manutencao;
import br.com.ifescritorio.model.manutencao.ManutencaoService;
import br.com.ifescritorio.model.usuario.Usuario;
import br.com.ifescritorio.model.usuario.UsuarioRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/manutencao")
@CrossOrigin
@Tag(
    name = "API Manutenção",
    description = "API responsável pelas solicitações e aprovações de manutenção dos patrimônios"
)
public class ManutencaoController {

    @Autowired
    private ManutencaoService service;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Operation(
        summary = "Solicitar manutenção",
        description = "Permite que um usuário solicite manutenção para um patrimônio."
    )
    @PostMapping("/solicitar")
    public Manutencao solicitar(
            @RequestBody ManutencaoRequest request) {

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
                request.getDescricao(),
                usuario);
    }

    @Operation(
        summary = "Aprovar manutenção",
        description = "Aprova uma solicitação de manutenção pendente."
    )
    @PutMapping("/{id}/aprovar")
    public Manutencao aprovar(
            @PathVariable Long id,
            @RequestBody AprovarManutencaoRequest request) {

        return service.aprovar(
                id,
                request.getObservacaoAdmin());
    }

    @Operation(
        summary = "Reprovar manutenção",
        description = "Reprova uma solicitação de manutenção pendente."
    )
    @PutMapping("/{id}/reprovar")
    public Manutencao reprovar(
            @PathVariable Long id,
            @RequestBody AprovarManutencaoRequest request) {

        return service.reprovar(
                id,
                request.getObservacaoAdmin());
    }

    @Operation(
        summary = "Listar manutenções pendentes",
        description = "Retorna todas as solicitações de manutenção com status PENDENTE."
    )
    @GetMapping("/pendentes")
    public List<Manutencao> listarPendentes() {

        return service.listarPendentes();
    }

    @Operation(
        summary = "Histórico de manutenção do patrimônio",
        description = "Lista todas as manutenções registradas para um patrimônio específico."
    )
    @GetMapping("/patrimonio/{id}")
    public List<Manutencao> listarPorPatrimonio(
            @PathVariable Long id) {

        return service.listarPorPatrimonio(id);
    }
}