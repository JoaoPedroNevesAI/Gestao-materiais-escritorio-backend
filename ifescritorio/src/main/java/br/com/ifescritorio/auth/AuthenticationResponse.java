package br.com.ifescritorio.auth;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthenticationResponse {

    private String token;
    private String nome;
    private String role;
}