package br.com.ifescritorio.model.manutencao;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.ifescritorio.model.notificacao.NotificacaoService;
import br.com.ifescritorio.model.notificacao.TipoNotificacao;
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

    @Autowired
    private NotificacaoService notificacaoService;

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

        Manutencao salva =
                repository.save(manutencao);

        notificacaoService.notificarAdministradores(
                "Nova manutenção pendente",
                "Foi solicitada uma manutenção para o patrimônio "
                        + patrimonio.getCodigoPatrimonio()
                        + ".",
                TipoNotificacao.MANUTENCAO,
                salva.getId()
        );

        return salva;
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

        Manutencao salva =
                repository.save(manutencao);

        if (salva.getSolicitante() != null) {

            notificacaoService.criar(
                    salva.getSolicitante(),
                    "Manutenção aprovada",
                    "A manutenção do patrimônio "
                            + salva.getPatrimonio()
                                   .getCodigoPatrimonio()
                            + " foi aprovada.",
                    TipoNotificacao.MANUTENCAO,
                    salva.getId()
            );
        }

        return salva;
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

        Manutencao salva =
                repository.save(manutencao);

        if (salva.getSolicitante() != null) {

            notificacaoService.criar(
                    salva.getSolicitante(),
                    "Manutenção reprovada",
                    "A manutenção do patrimônio "
                            + salva.getPatrimonio()
                                   .getCodigoPatrimonio()
                            + " foi reprovada.",
                    TipoNotificacao.MANUTENCAO,
                    salva.getId()
            );
        }

        return salva;
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

        Manutencao salva =
                repository.save(manutencao);

        if (salva.getSolicitante() != null) {

            notificacaoService.criar(
                    salva.getSolicitante(),
                    "Manutenção concluída",
                    "A manutenção do patrimônio "
                            + salva.getPatrimonio()
                                   .getCodigoPatrimonio()
                            + " foi concluída.",
                    TipoNotificacao.MANUTENCAO,
                    salva.getId()
            );
        }

        return salva;
    }

    /**
     * Lista todas as manutenções pendentes.
     */
    public List<Manutencao> listarPendentes() {

        return repository.findByStatus(
                StatusManutencao.PENDENTE);
    }

    /**
     * Lista o histórico de manutenções de um patrimônio.
     */
    public List<Manutencao> listarPorPatrimonio(
            Long patrimonioId) {

        return repository
                .findByPatrimonioIdOrderByDataSolicitacaoDesc(
                        patrimonioId);
    }
}