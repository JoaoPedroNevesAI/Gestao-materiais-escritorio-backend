package br.com.ifescritorio.model.movimentacao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.ifescritorio.model.material.Material;
import br.com.ifescritorio.model.material.MaterialRepository;
import jakarta.transaction.Transactional;

@Service
public class MovimentacaoService {

    @Autowired
    private MovimentacaoRepository repository;

    @Autowired
    private MaterialRepository materialRepository;

    @Transactional
    public Movimentacao save(Movimentacao movimentacao) {

        Material material = materialRepository.findById(
            movimentacao.getMaterial().getId()
        ).get();

        // Atualiza estoque
        if (movimentacao.getTipo() == TipoMovimentacao.ENTRADA) {
            material.setQuantidade(material.getQuantidade() + movimentacao.getQuantidade());
        } else {
            material.setQuantidade(material.getQuantidade() - movimentacao.getQuantidade());
        }

        materialRepository.save(material);

        movimentacao.setHabilitado(true);
        return repository.save(movimentacao);
    }

    public List<Movimentacao> listarTodos() {
        return repository.findAll();
    }
}