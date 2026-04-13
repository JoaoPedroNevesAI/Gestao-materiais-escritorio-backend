package br.com.ifescritorio.model.categoria;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.ifescritorio.util.exception.EntidadeNaoEncontradaException;
import jakarta.transaction.Transactional;

@Service
public class CategoriaService {

    @Autowired
    private CategoriaRepository repository;

    @Transactional
    public Categoria save(Categoria categoria) {
        categoria.setHabilitado(Boolean.TRUE);
        return repository.save(categoria);
    }

    public List<Categoria> listarTodos() {
        return repository.findAll();
    }

    public Categoria obterPorID(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Categoria", id));
    }

    public void delete(Long id) {
        Categoria categoria = obterPorID(id);
        repository.delete(categoria);
    }
}