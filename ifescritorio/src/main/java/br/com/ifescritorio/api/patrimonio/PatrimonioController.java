package br.com.ifescritorio.api.patrimonio;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.com.ifescritorio.model.patrimonio.Patrimonio;
import br.com.ifescritorio.model.patrimonio.PatrimonioService;

@RestController
@RequestMapping("/api/patrimonio")
@CrossOrigin
public class PatrimonioController {

    @Autowired
    private PatrimonioService service;

    @GetMapping
    public List<Patrimonio> listarTodos() {

        return service.listarTodos();
    }

    @GetMapping("/{id}")
    public Patrimonio obterPorId(
            @PathVariable Long id) {

        return service.obterPorId(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id) {

        service.deletar(id);

        return ResponseEntity.noContent().build();
    }
}