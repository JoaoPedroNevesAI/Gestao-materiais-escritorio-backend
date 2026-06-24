package br.com.ifescritorio.api.manutencao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import br.com.ifescritorio.model.manutencao.Manutencao;
import br.com.ifescritorio.model.manutencao.ManutencaoService;
import br.com.ifescritorio.model.usuario.Usuario;
import br.com.ifescritorio.model.usuario.UsuarioRepository;

@RestController
@RequestMapping("/api/manutencao")
@CrossOrigin
public class ManutencaoController {

    @Autowired
    private ManutencaoService service;

    @Autowired
    private UsuarioRepository usuarioRepository;

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

    @PutMapping("/{id}/aprovar")
    public Manutencao aprovar(
            @PathVariable Long id,
            @RequestBody AprovarManutencaoRequest request) {

        return service.aprovar(
                id,
                request.getObservacaoAdmin());
    }

    @PutMapping("/{id}/reprovar")
    public Manutencao reprovar(
            @PathVariable Long id,
            @RequestBody AprovarManutencaoRequest request) {

        return service.reprovar(
                id,
                request.getObservacaoAdmin());
    }

    @GetMapping("/pendentes")
    public List<Manutencao> listarPendentes() {

        return service.listarPendentes();
    }

    @GetMapping("/patrimonio/{id}")
    public List<Manutencao> listarPorPatrimonio(
            @PathVariable Long id) {

        return service.listarPorPatrimonio(id);
    }
}