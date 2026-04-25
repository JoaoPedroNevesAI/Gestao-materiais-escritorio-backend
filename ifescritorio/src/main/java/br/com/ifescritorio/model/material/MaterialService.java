package br.com.ifescritorio.model.material;

import jakarta.transaction.Transactional;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.ifescritorio.model.categoria.Categoria;
import br.com.ifescritorio.model.categoria.CategoriaRepository;
import br.com.ifescritorio.util.exception.EntidadeNaoEncontradaException;
import br.com.ifescritorio.util.exception.RegraNegocioException;

@Service
public class MaterialService {

    @Autowired
    private MaterialRepository repository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Transactional
    public Material save(Material material) {

        if (material.getQuantidade() == null || material.getQuantidade() < 0) {
            throw new RegraNegocioException("Quantidade não pode ser negativa");
        }

        if (material.getQuantidade() == 0) {
            throw new RegraNegocioException("Quantidade deve ser maior que zero");
        }
        
        if (material.getCategoria() == null || material.getCategoria().getId() == null) {
            throw new RegraNegocioException("Categoria é obrigatória");
        }

        Categoria categoria = categoriaRepository.findById(material.getCategoria().getId())
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Categoria", material.getCategoria().getId()));

        material.setCategoria(categoria);

        material.setHabilitado(Boolean.TRUE);
        return repository.save(material);
    }

    public List<Material> listarTodos() {
        return repository.findAll();
    }

    public Material obterPorID(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Material", id));
    }

    public void delete(Long id) {
        Material material = obterPorID(id);
        repository.delete(material);
    }
}