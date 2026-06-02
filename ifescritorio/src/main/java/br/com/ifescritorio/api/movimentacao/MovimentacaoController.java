package br.com.ifescritorio.api.movimentacao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import br.com.ifescritorio.model.movimentacao.Movimentacao;
import br.com.ifescritorio.model.movimentacao.MovimentacaoService;

@RestController
@RequestMapping("/api/movimentacao")
@CrossOrigin
public class MovimentacaoController {

    @Autowired
    private MovimentacaoService service;

    @GetMapping("/material/{id}")
    public List<Movimentacao> listarPorMaterial(
        @PathVariable Long id
    ) {

        return service.listarPorMaterial(id);
    }
}