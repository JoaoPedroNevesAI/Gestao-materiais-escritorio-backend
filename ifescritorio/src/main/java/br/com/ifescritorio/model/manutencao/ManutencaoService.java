package br.com.ifescritorio.model.manutencao;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.ifescritorio.model.material.Material;
import br.com.ifescritorio.model.material.MaterialRepository;
import br.com.ifescritorio.model.usuario.Usuario;
import br.com.ifescritorio.util.exception.EntidadeNaoEncontradaException;

@Service
public class ManutencaoService {

    @Autowired
    private ManutencaoRepository repository;

    @Autowired
    private MaterialRepository materialRepository;

    public Manutencao solicitar(
            Long materialId,
            String descricao,
            Usuario usuario) {

        Material material =
                materialRepository.findById(materialId)
                        .orElseThrow(() ->
                                new EntidadeNaoEncontradaException(
                                        "Material",
                                        materialId));

        Manutencao manutencao =
                Manutencao.builder()
                        .material(material)
                        .solicitante(usuario)
                        .descricao(descricao)
                        .status(StatusManutencao.PENDENTE)
                        .dataSolicitacao(LocalDateTime.now())
                        .build();

        return repository.save(manutencao);
    }

    public Manutencao aprovar(
            Long id,
            String observacaoAdmin) {

        Manutencao manutencao =
                repository.findById(id)
                        .orElseThrow(() ->
                                new EntidadeNaoEncontradaException(
                                        "Manutencao",
                                        id));

        manutencao.setStatus(
                StatusManutencao.APROVADA);

        manutencao.setObservacaoAdmin(
                observacaoAdmin);

        return repository.save(manutencao);
    }

    public Manutencao reprovar(
            Long id,
            String observacaoAdmin) {

        Manutencao manutencao =
                repository.findById(id)
                        .orElseThrow(() ->
                                new EntidadeNaoEncontradaException(
                                        "Manutencao",
                                        id));

        manutencao.setStatus(
                StatusManutencao.REPROVADA);

        manutencao.setObservacaoAdmin(
                observacaoAdmin);

        return repository.save(manutencao);
    }

    public List<Manutencao> listarPendentes() {

        return repository.findByStatus(
                StatusManutencao.PENDENTE);
    }

    public List<Manutencao> listarPorMaterial(
            Long materialId) {

        return repository
                .findByMaterialIdOrderByDataSolicitacaoDesc(
                        materialId);
    }
}