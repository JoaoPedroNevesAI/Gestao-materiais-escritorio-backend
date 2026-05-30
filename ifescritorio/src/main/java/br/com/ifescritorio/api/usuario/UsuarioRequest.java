package br.com.ifescritorio.api.usuario;

import br.com.ifescritorio.model.usuario.TipoUsuario;
import br.com.ifescritorio.model.usuario.Usuario;
import jakarta.validation.constraints.NotBlank;

public class UsuarioRequest {
    
    @NotBlank
    private String nome;
    
    @NotBlank
    private String email;
    
    @NotBlank
    private String senha;
    
    // Esse cara precisa receber o "ADM" ou "CLIENTE" do Front-end
    private String tipo; 

    // Getters e Setters (importante para o Spring mapear o JSON)
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }
    
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public Usuario build() {
        Usuario usuario = new Usuario();
        usuario.setNome(this.nome);
        usuario.setEmail(this.email);
        usuario.setSenha(this.senha);
        
        // AQUI ESTÁ O SEGREDO: Transforma o texto do Front no Enum do Java
        if (this.tipo != null && !this.tipo.isBlank()) {
            usuario.setTipo(TipoUsuario.valueOf(this.tipo.toUpperCase()));
        }
        
        return usuario;
    }
}