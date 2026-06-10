package br.com.ifescritorio.model.movimentacao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SolicitacaoMovimentacaoRepository
        extends JpaRepository<SolicitacaoMovimentacao, Long> {

    List<SolicitacaoMovimentacao>
            findByStatus(StatusMovimentacao status);
}