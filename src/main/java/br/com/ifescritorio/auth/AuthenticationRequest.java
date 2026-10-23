package br.com.ifescritorio.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AuthenticationRequest {

    @NotBlank
    private String email;

    @NotBlank
    private String senha;
}