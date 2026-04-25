package br.com.ifescritorio.model.material;

import jakarta.transaction.Transactional;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.ifescritorio.model.categoria.Categoria;
import br.com.ifescritorio.model.categoria.CategoriaRepository;
import br.com.ifescritorio.util.exception.EntidadeNaoEncontradaException;

@Service
public class MaterialService {

    @Autowired
    private MaterialRepository repository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Transactional
    public Material save(Material material) {

        Categoria categoria = categoriaRepository.findById(
                material.getCategoria().getId()
        ).orElseThrow(() ->
                new EntidadeNaoEncontradaException("Categoria", material.getCategoria().getId())
        );

        material.setCategoria(categoria);
        material.setHabilitado(Boolean.TRUE);

        return repository.save(material);
    }

    @Transactional
    public Material update(Long id, Material novosDados) {

        Material material = repository.findById(id)
            .orElseThrow(() -> new EntidadeNaoEncontradaException("Material", id));

        material.setNome(novosDados.getNome());
        material.setDescricao(novosDados.getDescricao());
        material.setQuantidade(novosDados.getQuantidade());
        material.setLocal(novosDados.getLocal());
        material.setValor(novosDados.getValor());
        material.setImagemUrl(novosDados.getImagemUrl());

        if (novosDados.getCategoria() != null) {
            Categoria categoria = categoriaRepository.findById(
                    novosDados.getCategoria().getId()
            ).orElseThrow(() ->
                    new EntidadeNaoEncontradaException("Categoria", novosDados.getCategoria().getId())
            );
            material.setCategoria(categoria);
        }

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