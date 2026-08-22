package br.com.ifescritorio.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.com.ifescritorio.model.usuario.Usuario;
import br.com.ifescritorio.model.usuario.UsuarioService;
import br.com.ifescritorio.seguranca.JwtService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin
@Tag(
    name = "API Autenticação",
    description = "API responsável pela autenticação e geração de token JWT"
)
public class AuthenticationController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private JwtService jwtService;

    @Operation(
        summary = "Realizar login",
        description = "Autentica um usuário utilizando email e senha e retorna um token JWT para acesso às demais APIs."
    )
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(
        @RequestBody @Valid AuthenticationRequest request
    ) {

        Usuario usuario = usuarioService.authenticate(
            request.getEmail(),
            request.getSenha()
        );

        String token = jwtService.generateToken(usuario);

        return ResponseEntity.ok(
            AuthenticationResponse.builder()
                .token(token)
                .nome(usuario.getNome())
                .role("ROLE_" + usuario.getTipo().name())
                .id(usuario.getId())
                .email(usuario.getEmail())
                .build()
        );
    }
}