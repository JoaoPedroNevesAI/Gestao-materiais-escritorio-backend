package br.com.ifescritorio.model.manutencao;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.ifescritorio.model.patrimonio.Patrimonio;
import br.com.ifescritorio.model.patrimonio.PatrimonioRepository;
import br.com.ifescritorio.model.usuario.Usuario;
import br.com.ifescritorio.util.exception.EntidadeNaoEncontradaException;

@Service
public class ManutencaoService {

    @Autowired
    private ManutencaoRepository repository;

    @Autowired
    private PatrimonioRepository patrimonioRepository;

    public Manutencao solicitar(
            Long patrimonioId,
            String descricao,
            Usuario usuario) {

        Patrimonio patrimonio =
                patrimonioRepository.findById(patrimonioId)
                        .orElseThrow(() ->
                                new EntidadeNaoEncontradaException(
                                        "Patrimonio",
                                        patrimonioId));

        Manutencao manutencao =
                Manutencao.builder()
                        .patrimonio(patrimonio)
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

    public Manutencao concluir(
            Long id,
            String observacaoAdmin) {

        Manutencao manutencao =
                repository.findById(id)
                        .orElseThrow(() ->
                                new EntidadeNaoEncontradaException(
                                        "Manutencao",
                                        id));

        manutencao.setStatus(
                StatusManutencao.CONCLUIDA);

        manutencao.setObservacaoAdmin(
                observacaoAdmin);

        return repository.save(manutencao);
    }

    public List<Manutencao> listarPendentes() {

        return repository.findByStatus(
                StatusManutencao.PENDENTE);
    }

    public List<Manutencao> listarPorPatrimonio(
            Long patrimonioId) {

        return repository
                .findByPatrimonioIdOrderByDataSolicitacaoDesc(
                        patrimonioId);
    }
}