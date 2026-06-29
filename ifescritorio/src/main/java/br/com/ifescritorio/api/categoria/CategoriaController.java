package br.com.ifescritorio.api.categoria;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.com.ifescritorio.model.categoria.Categoria;
import br.com.ifescritorio.model.categoria.CategoriaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/categoria")
@CrossOrigin
@Tag(
    name = "API Categoria",
    description = "API responsável pelo gerenciamento das categorias de materiais"
)
public class CategoriaController {

    @Autowired
    private CategoriaService categoriaService;

    @Operation(
        summary = "Cadastrar categoria",
        description = "Realiza o cadastro de uma nova categoria no sistema."
    )
    @PostMapping
    public Categoria save(
            @RequestBody @Valid CategoriaRequest request) {

        return categoriaService.save(request.build());
    }

    @Operation(
        summary = "Listar categorias",
        description = "Retorna todas as categorias cadastradas."
    )
    @GetMapping
    public List<Categoria> listarTodos() {

        return categoriaService.listarTodos();
    }

    @Operation(
        summary = "Buscar categoria por ID",
        description = "Retorna uma categoria específica através do seu identificador."
    )
    @GetMapping("/{id}")
    public Categoria obterPorID(
            @PathVariable Long id) {

        return categoriaService.obterPorID(id);
    }

    @Operation(
        summary = "Atualizar categoria",
        description = "Atualiza os dados de uma categoria existente."
    )
    @PutMapping("/{id}")
    public ResponseEntity<Categoria> update(
            @PathVariable Long id,
            @RequestBody @Valid CategoriaRequest request) {

        Categoria atualizada =
                categoriaService.update(
                        id,
                        request.build());

        return ResponseEntity.ok(atualizada);
    }

    @Operation(
        summary = "Excluir categoria",
        description = "Realiza a exclusão lógica de uma categoria."
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id) {

        categoriaService.delete(id);

        return ResponseEntity.noContent().build();
    }
}