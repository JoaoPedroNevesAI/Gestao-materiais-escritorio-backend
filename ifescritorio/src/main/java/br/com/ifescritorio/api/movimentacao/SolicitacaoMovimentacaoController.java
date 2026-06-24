package br.com.ifescritorio.api.movimentacao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import br.com.ifescritorio.model.movimentacao.SolicitacaoMovimentacao;
import br.com.ifescritorio.model.movimentacao.SolicitacaoMovimentacaoService;
import br.com.ifescritorio.model.usuario.Usuario;
import br.com.ifescritorio.model.usuario.UsuarioRepository;

@RestController
@RequestMapping("/api/solicitacao-movimentacao")
@CrossOrigin
public class SolicitacaoMovimentacaoController {

    @Autowired
    private SolicitacaoMovimentacaoService service;

    @Autowired
    private UsuarioRepository usuarioRepository;

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

    @GetMapping("/pendentes")
    public List<SolicitacaoMovimentacao> pendentes() {

        return service.listarPendentes();
    }

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

    @PutMapping("/{id}/reprovar")
    public SolicitacaoMovimentacao reprovar(
            @PathVariable Long id,
            @RequestBody AprovarMovimentacaoRequest request) {

        return service.reprovar(
                id,
                request.getObservacaoAdmin());
    }
}