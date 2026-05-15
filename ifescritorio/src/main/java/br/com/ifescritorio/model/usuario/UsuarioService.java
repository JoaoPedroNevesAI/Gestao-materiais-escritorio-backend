package br.com.ifescritorio.model.usuario;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.ifescritorio.util.exception.EntidadeNaoEncontradaException;
import br.com.ifescritorio.util.exception.RegraNegocioException;
import jakarta.transaction.Transactional;

@Service
public class UsuarioService implements UserDetailsService {

    @Autowired
    private UsuarioRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Transactional
    public Usuario save(Usuario usuario) {

        if (repository.findByEmail(usuario.getEmail()).isPresent()) {
            throw new RegraNegocioException("Email já cadastrado");
        }

        if (
            usuario.getCpf() != null &&
            repository.findByCpf(usuario.getCpf()).isPresent()
        ) {
            throw new RegraNegocioException("CPF já cadastrado");
        }

        if (usuario.getTipo() == null) {
            usuario.setTipo(TipoUsuario.CLIENTE);
        }

        usuario.setSenha(
            passwordEncoder.encode(usuario.getSenha())
        );

        usuario.setHabilitado(Boolean.TRUE);

        return repository.save(usuario);
    }

    @Transactional
    public Usuario update(Long id, Usuario novosDados) {

        Usuario usuario = repository.findById(id)
            .orElseThrow(() ->
                new EntidadeNaoEncontradaException("Usuário", id)
            );

        if (
            !usuario.getEmail().equals(novosDados.getEmail()) &&
            repository.findByEmail(novosDados.getEmail()).isPresent()
        ) {
            throw new RegraNegocioException("Email já cadastrado");
        }

        if (
            novosDados.getCpf() != null &&
            !novosDados.getCpf().equals(usuario.getCpf()) &&
            repository.findByCpf(novosDados.getCpf()).isPresent()
        ) {
            throw new RegraNegocioException("CPF já cadastrado");
        }

        usuario.setNome(novosDados.getNome());
        usuario.setEmail(novosDados.getEmail());
        usuario.setCpf(novosDados.getCpf());
        usuario.setTelefone(novosDados.getTelefone());

        if (
            novosDados.getSenha() != null &&
            !novosDados.getSenha().isBlank()
        ) {

            usuario.setSenha(
                passwordEncoder.encode(novosDados.getSenha())
            );
        }

        if (novosDados.getTipo() != null) {
            usuario.setTipo(novosDados.getTipo());
        }

        return repository.save(usuario);
    }

    public Usuario authenticate(String email, String senha) {

        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(email, senha)
        );

        Usuario usuario = repository.findByEmail(email)
            .orElseThrow(() ->
                new UsernameNotFoundException("Usuário não encontrado")
            );

        usuario.setUltimoLogin(LocalDateTime.now());

        return repository.save(usuario);
    }

    @Override
    public UserDetails loadUserByUsername(String email)
        throws UsernameNotFoundException {

        return repository.findByEmail(email)
            .orElseThrow(() ->
                new UsernameNotFoundException("Usuário não encontrado")
            );
    }

    public List<Usuario> listarTodos() {

        return repository.findAll();
    }

    public Usuario obterPorID(Long id) {

        return repository.findById(id)
            .orElseThrow(() ->
                new EntidadeNaoEncontradaException("Usuário", id)
            );
    }

    public void delete(Long id) {

        Usuario usuario = obterPorID(id);

        repository.delete(usuario);
    }
}