package br.com.ifescritorio.api.usuario;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import br.com.ifescritorio.model.usuario.TipoUsuario;
import br.com.ifescritorio.model.usuario.Usuario;
import br.com.ifescritorio.model.usuario.UsuarioService;
import br.com.ifescritorio.util.exception.RegraNegocioException;

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
        description = "Permite o cadastro público de colaboradores e o cadastro de usuários ADM por um administrador autenticado."
    )
    @PostMapping
    public ResponseEntity<Usuario> save(
            @RequestBody @Valid UsuarioRequest request,
            @AuthenticationPrincipal Usuario usuarioAutenticado) {

        if (request.getSenha() == null || request.getSenha().isBlank()) {
            throw new RegraNegocioException("Senha é obrigatória");
        }

        if (
            request.getTipo() == TipoUsuario.ADM &&
            (
                usuarioAutenticado == null ||
                usuarioAutenticado.getTipo() != TipoUsuario.ADM
            )
        ) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Usuario usuario =
                usuarioService.save(
                        request.build());

        return ResponseEntity
                .status(HttpStatus.CREATED)
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
        description = "Administradores podem consultar qualquer usuário. Colaboradores podem consultar somente o próprio cadastro."
    )
    @GetMapping("/{id}")
    public ResponseEntity<Usuario> obterPorID(
            @PathVariable Long id,
            @AuthenticationPrincipal Usuario usuarioAutenticado) {

        if (usuarioAutenticado == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        boolean administrador =
                usuarioAutenticado.getTipo() == TipoUsuario.ADM;

        if (!administrador && !id.equals(usuarioAutenticado.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        return ResponseEntity.ok(usuarioService.obterPorID(id));
    }

    @Operation(
        summary = "Atualizar usuário",
        description = "Administradores podem atualizar usuários. Colaboradores podem atualizar somente os próprios dados, sem alterar o tipo de usuário."
    )
    @PutMapping("/{id}")
    public ResponseEntity<Usuario> update(
            @PathVariable Long id,
            @RequestBody @Valid UsuarioRequest request,
            @AuthenticationPrincipal Usuario usuarioAutenticado) {

        if (usuarioAutenticado == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        boolean administrador =
                usuarioAutenticado.getTipo() == TipoUsuario.ADM;

        if (
            !administrador &&
            (
                !id.equals(usuarioAutenticado.getId()) ||
                (
                    request.getTipo() != null &&
                    request.getTipo() != usuarioAutenticado.getTipo()
                )
            )
        ) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

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