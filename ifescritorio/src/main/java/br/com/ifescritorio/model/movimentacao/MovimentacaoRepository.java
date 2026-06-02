package br.com.ifescritorio.model.movimentacao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MovimentacaoRepository
    extends JpaRepository<Movimentacao, Long> {

    List<Movimentacao> findByMaterialIdOrderByDataMovimentacaoDesc(Long materialId);
}