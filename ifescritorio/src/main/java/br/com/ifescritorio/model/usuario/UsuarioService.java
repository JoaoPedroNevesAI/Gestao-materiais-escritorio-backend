package br.com.ifescritorio.model.usuario;

import jakarta.transaction.Transactional;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.ifescritorio.util.exception.EntidadeNaoEncontradaException;
import br.com.ifescritorio.util.exception.RegraNegocioException;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository repository;

    @Transactional
    public Usuario save(Usuario usuario) {

        if (repository.findByEmail(usuario.getEmail()).isPresent()) {
            throw new RegraNegocioException("Email já cadastrado");
        }

        if (usuario.getTipo() == null) {
            usuario.setTipo(TipoUsuario.CLIENTE);
        }

        return repository.save(usuario);
    }

    public List<Usuario> listarTodos() {
        return repository.findAll();
    }

    public Usuario obterPorID(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Usuário", id));
    }

    public void delete(Long id) {
        Usuario usuario = obterPorID(id);
        repository.delete(usuario);
    }
}