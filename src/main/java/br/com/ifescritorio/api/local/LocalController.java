package br.com.ifescritorio.api.local;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.com.ifescritorio.model.local.Local;
import br.com.ifescritorio.model.local.LocalService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/local")
@CrossOrigin
@Tag(
    name = "API Local",
    description = "API responsável pelo gerenciamento dos locais onde os patrimônios são armazenados"
)
public class LocalController {

    @Autowired
    private LocalService localService;

    @Operation(
        summary = "Cadastrar local",
        description = "Cadastra um novo local no sistema."
    )
    @PostMapping
    public ResponseEntity<Local> save(
        @RequestBody
        @Valid
        LocalRequest request
    ) {

        Local local =
            localService.save(
                request.build()
            );

        return new ResponseEntity<>(
            local,
            HttpStatus.CREATED
        );
    }

    @Operation(
        summary = "Listar locais",
        description = "Retorna todos os locais cadastrados."
    )
    @GetMapping
    public List<Local> listarTodos() {

        return localService.listarTodos();
    }

    @Operation(
        summary = "Buscar local por ID",
        description = "Retorna os dados de um local específico."
    )
    @GetMapping("/{id}")
    public Local obterPorID(
        @PathVariable Long id
    ) {

        return localService.obterPorID(id);
    }

    @Operation(
        summary = "Atualizar local",
        description = "Atualiza os dados de um local existente."
    )
    @PutMapping("/{id}")
    public ResponseEntity<Local> update(
        @PathVariable Long id,
        @RequestBody
        @Valid
        LocalRequest request
    ) {

        localService.update(
            id,
            request.build()
        );

        return ResponseEntity.ok().build();
    }

    @Operation(
        summary = "Excluir local",
        description = "Realiza a exclusão lógica de um local."
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
        @PathVariable Long id
    ) {

        localService.delete(id);

        return ResponseEntity.noContent().build();
    }
}