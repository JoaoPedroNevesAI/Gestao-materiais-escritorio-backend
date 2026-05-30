package br.com.ifescritorio.api.categoria;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.com.ifescritorio.model.categoria.Categoria;
import br.com.ifescritorio.model.categoria.CategoriaService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/categoria")
@CrossOrigin
public class CategoriaController {

    @Autowired
    private CategoriaService categoriaService;

    @PostMapping
    public Categoria save(@RequestBody @Valid CategoriaRequest request) {
        return categoriaService.save(request.build());
    }

    @GetMapping
    public List<Categoria> listarTodos() {
        return categoriaService.listarTodos();
    }

    @GetMapping("/{id}")
    public Categoria obterPorID(@PathVariable Long id) {
        return categoriaService.obterPorID(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Categoria> update(
            @PathVariable Long id,
            @RequestBody @Valid CategoriaRequest request) {

        Categoria atualizada = categoriaService.update(id, request.build());

        return ResponseEntity.ok(atualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        categoriaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}