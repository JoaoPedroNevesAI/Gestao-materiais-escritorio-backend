package br.com.ifescritorio.model.manutencao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ManutencaoRepository
        extends JpaRepository<Manutencao, Long> {

	List<Manutencao> findByPatrimonioIdOrderByDataSolicitacaoDesc(
	        Long patrimonioId);

    List<Manutencao> findByStatus(
            StatusManutencao status);
}