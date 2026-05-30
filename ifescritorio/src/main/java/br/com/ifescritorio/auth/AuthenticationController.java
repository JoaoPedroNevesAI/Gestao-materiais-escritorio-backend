package br.com.ifescritorio.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.com.ifescritorio.model.usuario.Usuario;
import br.com.ifescritorio.model.usuario.UsuarioService;
import br.com.ifescritorio.seguranca.JwtService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin
public class AuthenticationController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private JwtService jwtService;

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
                .build()
        );
    }
}