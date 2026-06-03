package br.com.ifescritorio.api.movimentacao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import br.com.ifescritorio.model.movimentacao.Movimentacao;
import br.com.ifescritorio.model.movimentacao.MovimentacaoService;
import br.com.ifescritorio.model.usuario.Usuario;
import br.com.ifescritorio.model.usuario.UsuarioRepository;

@RestController
@RequestMapping("/api/movimentacao")
@CrossOrigin
public class MovimentacaoController {

    @Autowired
    private MovimentacaoService service;

    @Autowired
    private UsuarioRepository usuarioRepository;

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
                request.getMaterialId(),
                request.getLocalDestinoId(),
                request.getObservacao(),
                usuario);
    }

    @GetMapping("/material/{id}")
    public List<Movimentacao> listarPorMaterial(
            @PathVariable Long id) {

        return service.listarPorMaterial(id);
    }
}