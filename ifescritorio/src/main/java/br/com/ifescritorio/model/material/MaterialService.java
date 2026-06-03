package br.com.ifescritorio.model.material;

import jakarta.transaction.Transactional;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.ifescritorio.model.categoria.Categoria;
import br.com.ifescritorio.model.categoria.CategoriaRepository;
import br.com.ifescritorio.model.local.Local;
import br.com.ifescritorio.model.local.LocalRepository;
import br.com.ifescritorio.util.exception.EntidadeNaoEncontradaException;

@Service
public class MaterialService {

    @Autowired
    private MaterialRepository repository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private LocalRepository localRepository;

    @Transactional
    public Material save(Material material) {

        Categoria categoria =
            categoriaRepository.findById(
                material.getCategoria().getId()
            ).orElseThrow(() ->
                new EntidadeNaoEncontradaException(
                    "Categoria",
                    material.getCategoria().getId()
                )
            );

        Local local =
            localRepository.findById(
                material.getLocal().getId()
            ).orElseThrow(() ->
                new EntidadeNaoEncontradaException(
                    "Local",
                    material.getLocal().getId()
                )
            );

        material.setCategoria(categoria);
        material.setLocal(local);
        material.setHabilitado(Boolean.TRUE);

        return repository.save(material);
    }

    @Transactional
    public Material update(
        Long id,
        Material novosDados
    ) {

        Material material =
            repository.findById(id)
                .orElseThrow(() ->
                    new EntidadeNaoEncontradaException(
                        "Material",
                        id
                    )
                );

        material.setNome(
            novosDados.getNome()
        );

        material.setDescricao(
            novosDados.getDescricao()
        );

        material.setQuantidade(
            novosDados.getQuantidade()
        );

        material.setValor(
            novosDados.getValor()
        );

        material.setImagemUrl(
            novosDados.getImagemUrl()
        );

        if (novosDados.getCategoria() != null) {

            Categoria categoria =
                categoriaRepository.findById(
                    novosDados.getCategoria().getId()
                ).orElseThrow(() ->
                    new EntidadeNaoEncontradaException(
                        "Categoria",
                        novosDados.getCategoria().getId()
                    )
                );

            material.setCategoria(categoria);
        }

        if (novosDados.getLocal() != null) {

            Local local =
                localRepository.findById(
                    novosDados.getLocal().getId()
                ).orElseThrow(() ->
                    new EntidadeNaoEncontradaException(
                        "Local",
                        novosDados.getLocal().getId()
                    )
                );

            material.setLocal(local);
        }

        return repository.save(material);
    }

    public List<Material> listarTodos() {

        return repository.findAll();
    }

    public Material obterPorID(Long id) {

        return repository.findById(id)
            .orElseThrow(() ->
                new EntidadeNaoEncontradaException(
                    "Material",
                    id
                )
            );
    }

    @Transactional
    public void delete(Long id) {

        Material material =
            repository.findById(id)
                .orElseThrow(() ->
                    new EntidadeNaoEncontradaException(
                        "Material",
                        id
                    )
                );

        material.setHabilitado(Boolean.FALSE);

        repository.save(material);
    }
    
    public List<Material> filtrar(
    	    String nome,
    	    Long idCategoria,
    	    Long idLocal
    	) {

    	    List<Material> listaMateriais =
    	        repository.findAll();

    	    if (
    	        (nome != null && !nome.isBlank()) &&
    	        idCategoria == null &&
    	        idLocal == null
    	    ) {

    	        listaMateriais =
    	            repository
    	                .findByNomeContainingIgnoreCaseOrderByNomeAsc(
    	                    nome
    	                );
    	    }

    	    else if (
    	        (nome == null || nome.isBlank()) &&
    	        idCategoria != null &&
    	        idLocal == null
    	    ) {

    	        listaMateriais =
    	            repository.consultarPorCategoria(
    	                idCategoria
    	            );
    	    }

    	    else if (
    	        (nome == null || nome.isBlank()) &&
    	        idCategoria == null &&
    	        idLocal != null
    	    ) {

    	        listaMateriais =
    	            repository.consultarPorLocal(
    	                idLocal
    	            );
    	    }

    	    else if (
    	        (nome != null && !nome.isBlank()) &&
    	        idCategoria != null &&
    	        idLocal == null
    	    ) {

    	        listaMateriais =
    	            repository.consultarPorNomeECategoria(
    	                nome,
    	                idCategoria
    	            );
    	    }

    	    return listaMateriais;
    	}
}