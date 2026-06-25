package br.com.ifescritorio.api.usuario;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.com.ifescritorio.model.usuario.Usuario;
import br.com.ifescritorio.model.usuario.UsuarioService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/usuario")
@CrossOrigin
@Tag(
    name = "API Usuário",
    description = "API responsável pelo gerenciamento de usuários do sistema"
)
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Operation(
        summary = "Cadastrar usuário",
        description = "Realiza o cadastro de um novo usuário no sistema."
    )
    @PostMapping
    public ResponseEntity<Usuario> save(
            @RequestBody @Valid UsuarioRequest request) {

        Usuario usuario =
                usuarioService.save(
                        request.build());

        return ResponseEntity
                .status(201)
                .body(usuario);
    }

    @Operation(
        summary = "Listar usuários",
        description = "Retorna todos os usuários cadastrados."
    )
    @GetMapping
    public List<Usuario> listarTodos() {

        return usuarioService.listarTodos();
    }

    @Operation(
        summary = "Buscar usuário por ID",
        description = "Retorna um usuário específico através do seu identificador."
    )
    @GetMapping("/{id}")
    public Usuario obterPorID(
            @PathVariable Long id) {

        return usuarioService.obterPorID(id);
    }

    @Operation(
        summary = "Atualizar usuário",
        description = "Atualiza os dados de um usuário existente."
    )
    @PutMapping("/{id}")
    public ResponseEntity<Usuario> update(
            @PathVariable Long id,
            @RequestBody @Valid UsuarioRequest request) {

        Usuario atualizado =
                usuarioService.update(
                        id,
                        request.build());

        return ResponseEntity.ok(atualizado);
    }

    @Operation(
        summary = "Excluir usuário",
        description = "Realiza a exclusão lógica de um usuário."
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id) {

        usuarioService.delete(id);

        return ResponseEntity.noContent().build();
    }
}