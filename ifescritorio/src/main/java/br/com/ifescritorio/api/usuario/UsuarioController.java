package br.com.ifescritorio.api.usuario;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.com.ifescritorio.model.usuario.Usuario;
import br.com.ifescritorio.model.usuario.UsuarioService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/usuario")
@CrossOrigin
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping
    public ResponseEntity<Usuario> save(@RequestBody @Valid UsuarioRequest request) {
        Usuario usuario = usuarioService.save(request.build());
        return ResponseEntity.status(201).body(usuario);
    }

    @GetMapping
    public List<Usuario> listarTodos() {
        return usuarioService.listarTodos();
    }

    @GetMapping("/{id}")
    public Usuario obterPorID(@PathVariable Long id) {
        return usuarioService.obterPorID(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Usuario> update(
            @PathVariable Long id,
            @RequestBody @Valid UsuarioRequest request) {

        Usuario atualizado = usuarioService.update(id, request.build());

        return ResponseEntity.ok(atualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        usuarioService.delete(id);
        return ResponseEntity.noContent().build();
    }
}