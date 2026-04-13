package br.com.ifescritorio.api.movimentacao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.ifescritorio.model.movimentacao.Movimentacao;
import br.com.ifescritorio.model.movimentacao.MovimentacaoService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/movimentacao")
@CrossOrigin
public class MovimentacaoController {

    @Autowired
    private MovimentacaoService service;

    @PostMapping
    public Movimentacao save(@RequestBody @Valid MovimentacaoRequest request) {
        return service.save(request.build());
    }

    @GetMapping
    public List<Movimentacao> listarTodos() {
        return service.listarTodos();
    }
}