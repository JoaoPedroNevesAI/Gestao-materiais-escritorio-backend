package br.com.ifescritorio.model.manutencao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ManutencaoRepository
        extends JpaRepository<Manutencao, Long> {

    List<Manutencao> findByMaterialIdOrderByDataSolicitacaoDesc(
            Long materialId);

    List<Manutencao> findByStatus(
            StatusManutencao status);
}