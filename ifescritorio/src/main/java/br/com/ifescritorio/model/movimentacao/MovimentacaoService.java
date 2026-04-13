package br.com.ifescritorio.model.movimentacao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.ifescritorio.model.material.Material;
import br.com.ifescritorio.model.material.MaterialRepository;
import br.com.ifescritorio.util.exception.EntidadeNaoEncontradaException;
import br.com.ifescritorio.util.exception.RegraNegocioException;
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
        ).orElseThrow(() -> new EntidadeNaoEncontradaException("Material", movimentacao.getMaterial().getId()));

        Integer quantidade = movimentacao.getQuantidade();

        if (quantidade == null || quantidade <= 0) {
            throw new RegraNegocioException("Quantidade deve ser maior que zero");
        }

        if (movimentacao.getTipo() == TipoMovimentacao.ENTRADA) {
            material.setQuantidade(material.getQuantidade() + quantidade);
        }

        if (movimentacao.getTipo() == TipoMovimentacao.SAIDA) {

            if (material.getQuantidade() < quantidade) {
                throw new RegraNegocioException("Estoque insuficiente");
            }

            material.setQuantidade(material.getQuantidade() - quantidade);
        }

        materialRepository.save(material);

        movimentacao.setHabilitado(true);
        return repository.save(movimentacao);
    }

    public List<Movimentacao> listarTodos() {
        return repository.findAll();
    }
}