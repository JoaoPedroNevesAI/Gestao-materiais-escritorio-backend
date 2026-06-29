package br.com.ifescritorio.model.movimentacao;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.ifescritorio.model.local.Local;
import br.com.ifescritorio.model.local.LocalRepository;
import br.com.ifescritorio.model.patrimonio.Patrimonio;
import br.com.ifescritorio.model.patrimonio.PatrimonioRepository;
import br.com.ifescritorio.model.usuario.Usuario;
import br.com.ifescritorio.util.exception.EntidadeNaoEncontradaException;

@Service
public class SolicitacaoMovimentacaoService {

    @Autowired
    private SolicitacaoMovimentacaoRepository repository;

    @Autowired
    private PatrimonioRepository patrimonioRepository;

    @Autowired
    private LocalRepository localRepository;

    @Autowired
    private MovimentacaoService movimentacaoService;

    public SolicitacaoMovimentacao solicitar(
            Long patrimonioId,
            Long localDestinoId,
            String observacao,
            Usuario solicitante) {

        Patrimonio patrimonio =
                patrimonioRepository.findById(patrimonioId)
                        .orElseThrow(() ->
                                new EntidadeNaoEncontradaException(
                                        "Patrimonio",
                                        patrimonioId));

        Local destino =
                localRepository.findById(localDestinoId)
                        .orElseThrow(() ->
                                new EntidadeNaoEncontradaException(
                                        "Local",
                                        localDestinoId));

        SolicitacaoMovimentacao solicitacao =
                SolicitacaoMovimentacao.builder()
                        .patrimonio(patrimonio)
                        .localOrigem(patrimonio.getLocal())
                        .localDestino(destino)
                        .solicitante(solicitante)
                        .observacao(observacao)
                        .status(StatusMovimentacao.PENDENTE)
                        .dataSolicitacao(LocalDateTime.now())
                        .build();

        return repository.save(solicitacao);
    }

    public SolicitacaoMovimentacao aprovar(
            Long id,
            String observacaoAdmin,
            Usuario admin) {

        SolicitacaoMovimentacao solicitacao =
                repository.findById(id)
                        .orElseThrow(() ->
                                new EntidadeNaoEncontradaException(
                                        "Solicitação",
                                        id));

        movimentacaoService.transferir(
                solicitacao.getPatrimonio().getId(),
                solicitacao.getLocalDestino().getId(),
                solicitacao.getObservacao(),
                admin);

        solicitacao.setStatus(StatusMovimentacao.APROVADA);
        solicitacao.setObservacaoAdmin(observacaoAdmin);
        solicitacao.setDataResposta(LocalDateTime.now());

        return repository.save(solicitacao);
    }

    public SolicitacaoMovimentacao reprovar(
            Long id,
            String observacaoAdmin) {

        SolicitacaoMovimentacao solicitacao =
                repository.findById(id)
                        .orElseThrow(() ->
                                new EntidadeNaoEncontradaException(
                                        "Solicitação",
                                        id));

        solicitacao.setStatus(StatusMovimentacao.REPROVADA);
        solicitacao.setObservacaoAdmin(observacaoAdmin);
        solicitacao.setDataResposta(LocalDateTime.now());

        return repository.save(solicitacao);
    }

    public List<SolicitacaoMovimentacao> listarPendentes() {

        return repository.findByStatus(
                StatusMovimentacao.PENDENTE);
    }
}