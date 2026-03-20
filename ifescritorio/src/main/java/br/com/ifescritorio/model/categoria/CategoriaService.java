package br.com.ifescritorio.model.categoria;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        return repository.findById(id).get();
    }

    public void delete(Long id) {
	    repository.deleteById(id);
	}
}