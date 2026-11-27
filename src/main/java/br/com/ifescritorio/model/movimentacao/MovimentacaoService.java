package br.com.ifescritorio.model.movimentacao;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.ifescritorio.model.auditoria.AuditoriaDados;
import br.com.ifescritorio.model.auditoria.AuditoriaService;
import br.com.ifescritorio.model.auditoria.TipoAcaoAuditoria;
import br.com.ifescritorio.model.local.Local;
import br.com.ifescritorio.model.local.LocalRepository;
import br.com.ifescritorio.model.notificacao.NotificacaoService;
import br.com.ifescritorio.model.notificacao.TipoNotificacao;
import br.com.ifescritorio.model.patrimonio.Patrimonio;
import br.com.ifescritorio.model.patrimonio.PatrimonioRepository;
import br.com.ifescritorio.model.usuario.Usuario;
import br.com.ifescritorio.util.exception.EntidadeNaoEncontradaException;
import br.com.ifescritorio.util.exception.RegraNegocioException;

@Service
public class MovimentacaoService {

    @Autowired
    private MovimentacaoRepository repository;

    @Autowired
    private PatrimonioRepository patrimonioRepository;

    @Autowired
    private LocalRepository localRepository;

    @Autowired
    private NotificacaoService notificacaoService;

    @Autowired
    private AuditoriaService auditoriaService;

    /**
     * Cria uma solicitação de movimentação.
     */
    @Transactional
    public Movimentacao solicitar(
            Long patrimonioId,
            Long localDestinoId,
            String observacao,
            Usuario solicitante) {

        if (solicitante == null) {
            throw new RegraNegocioException(
                    "Usuário solicitante não identificado.");
        }

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

        Local origem = patrimonio.getLocal();

        if (origem != null
                && origem.getId().equals(destino.getId())) {

            throw new RegraNegocioException(
                    "O patrimônio já está neste local.");
        }

        Movimentacao movimentacao =
                Movimentacao.builder()
                        .patrimonio(patrimonio)
                        .localOrigem(origem)
                        .localDestino(destino)
                        .solicitante(solicitante)
                        .observacao(observacao)
                        .status(StatusMovimentacao.PENDENTE)
                        .dataSolicitacao(LocalDateTime.now())
                        .build();

        Movimentacao salva =
                repository.save(movimentacao);

        auditoriaService.registrar(
                TipoAcaoAuditoria.SOLICITACAO,
                "MOVIMENTACAO",
                salva.getId(),
                "Solicitação de movimentação criada para o patrimônio "
                        + patrimonio.getCodigoPatrimonio(),
                null,
                dadosMovimentacao(salva));

        /*
         * Notifica todos os administradores sobre
         * a nova solicitação pendente.
         */
        notificacaoService.notificarAdministradores(
                "Nova movimentação pendente",
                "Foi solicitada uma movimentação para o patrimônio "
                        + patrimonio.getCodigoPatrimonio()
                        + ".",
                TipoNotificacao.MOVIMENTACAO,
                salva.getId()
        );

        return salva;
    }

    /**
     * Aprova e executa a movimentação.
     */
    @Transactional
    public Movimentacao aprovar(
            Long id,
            String observacaoAdmin,
            Usuario admin) {

        Movimentacao movimentacao =
                obterPorId(id);

        validarSolicitacaoPendente(
                movimentacao);

        if (admin == null) {
            throw new RegraNegocioException(
                    "Administrador não identificado.");
        }

        Patrimonio patrimonio =
                movimentacao.getPatrimonio();

        if (patrimonio == null) {
            throw new RegraNegocioException(
                    "A movimentação não possui patrimônio associado.");
        }

        Map<String, Object> dadosAnteriores =
                dadosMovimentacao(movimentacao);

        Map<String, Object> localAnterior =
                AuditoriaDados.mapa(
                        "localId", patrimonio.getLocal() != null
                                ? patrimonio.getLocal().getId() : null,
                        "localNome", patrimonio.getLocal() != null
                                ? patrimonio.getLocal().getNome() : null);

        Local destino =
                movimentacao.getLocalDestino();

        if (destino == null) {
            throw new RegraNegocioException(
                    "A movimentação não possui local de destino.");
        }

        /*
         * A alteração do local acontece somente
         * após a aprovação.
         */
        patrimonio.setLocal(destino);

        patrimonioRepository.save(patrimonio);

        movimentacao.setStatus(
                StatusMovimentacao.APROVADA);

        movimentacao.setObservacaoAdmin(
                observacaoAdmin);

        movimentacao.setAprovador(
                admin);

        movimentacao.setDataResposta(
                LocalDateTime.now());

        movimentacao.setDataMovimentacao(
                LocalDateTime.now());

        Movimentacao salva =
                repository.save(movimentacao);

        auditoriaService.registrar(
                TipoAcaoAuditoria.APROVACAO,
                "MOVIMENTACAO",
                salva.getId(),
                "Solicitação de movimentação aprovada",
                dadosAnteriores,
                dadosMovimentacao(salva));

        auditoriaService.registrar(
                TipoAcaoAuditoria.ATUALIZACAO,
                "PATRIMONIO",
                patrimonio.getId(),
                "Local do patrimônio alterado após aprovação da movimentação",
                localAnterior,
                AuditoriaDados.mapa(
                        "localId", patrimonio.getLocal().getId(),
                        "localNome", patrimonio.getLocal().getNome()));

        /*
         * Notifica o usuário que fez a solicitação.
         */
        if (salva.getSolicitante() != null) {

            notificacaoService.criar(
                    salva.getSolicitante(),
                    "Movimentação aprovada",
                    "Sua solicitação de movimentação do patrimônio "
                            + salva.getPatrimonio()
                                   .getCodigoPatrimonio()
                            + " foi aprovada.",
                    TipoNotificacao.MOVIMENTACAO,
                    salva.getId()
            );
        }

        return salva;
    }

    /**
     * Reprova uma solicitação.
     */
    @Transactional
    public Movimentacao reprovar(
            Long id,
            String observacaoAdmin,
            Usuario admin) {

        Movimentacao movimentacao =
                obterPorId(id);

        validarSolicitacaoPendente(
                movimentacao);

        if (admin == null) {
            throw new RegraNegocioException(
                    "Administrador não identificado.");
        }

        Map<String, Object> dadosAnteriores =
                dadosMovimentacao(movimentacao);

        movimentacao.setStatus(
                StatusMovimentacao.REPROVADA);

        movimentacao.setObservacaoAdmin(
                observacaoAdmin);

        movimentacao.setAprovador(
                admin);

        movimentacao.setDataResposta(
                LocalDateTime.now());

        Movimentacao salva =
                repository.save(movimentacao);

        auditoriaService.registrar(
                TipoAcaoAuditoria.REPROVACAO,
                "MOVIMENTACAO",
                salva.getId(),
                "Solicitação de movimentação reprovada",
                dadosAnteriores,
                dadosMovimentacao(salva));

        /*
         * Notifica o usuário que fez a solicitação.
         */
        if (salva.getSolicitante() != null) {

            notificacaoService.criar(
                    salva.getSolicitante(),
                    "Movimentação reprovada",
                    "Sua solicitação de movimentação do patrimônio "
                            + salva.getPatrimonio()
                                   .getCodigoPatrimonio()
                            + " foi reprovada.",
                    TipoNotificacao.MOVIMENTACAO,
                    salva.getId()
            );
        }

        return salva;
    }

    /**
     * Busca uma movimentação pelo ID.
     */
    public Movimentacao obterPorId(Long id) {

        return repository.findById(id)
                .orElseThrow(() ->
                        new EntidadeNaoEncontradaException(
                                "Movimentacao",
                                id));
    }

    /**
     * Lista todas as movimentações.
     */
    public List<Movimentacao> listarTodas() {

        return repository
                .findAllByOrderByDataSolicitacaoDesc();
    }

    /**
     * Lista somente movimentações pendentes.
     */
    public List<Movimentacao> listarPendentes() {

        return repository
                .findByStatusOrderByDataSolicitacaoDesc(
                        StatusMovimentacao.PENDENTE);
    }

    /**
     * Lista o histórico de movimentações de um patrimônio.
     */
    public List<Movimentacao> listarPorPatrimonio(
            Long patrimonioId) {

        patrimonioRepository.findById(patrimonioId)
                .orElseThrow(() ->
                        new EntidadeNaoEncontradaException(
                                "Patrimonio",
                                patrimonioId));

        return repository
                .findByPatrimonioIdOrderByDataMovimentacaoDesc(
                        patrimonioId);
    }

    private Map<String, Object> dadosMovimentacao(
            Movimentacao movimentacao) {

        return AuditoriaDados.mapa(
                "patrimonioId", movimentacao.getPatrimonio() != null
                        ? movimentacao.getPatrimonio().getId() : null,
                "codigoPatrimonio", movimentacao.getPatrimonio() != null
                        ? movimentacao.getPatrimonio().getCodigoPatrimonio() : null,
                "localOrigemId", movimentacao.getLocalOrigem() != null
                        ? movimentacao.getLocalOrigem().getId() : null,
                "localOrigemNome", movimentacao.getLocalOrigem() != null
                        ? movimentacao.getLocalOrigem().getNome() : null,
                "localDestinoId", movimentacao.getLocalDestino() != null
                        ? movimentacao.getLocalDestino().getId() : null,
                "localDestinoNome", movimentacao.getLocalDestino() != null
                        ? movimentacao.getLocalDestino().getNome() : null,
                "status", movimentacao.getStatus() != null
                        ? movimentacao.getStatus().name() : null,
                "solicitanteId", movimentacao.getSolicitante() != null
                        ? movimentacao.getSolicitante().getId() : null,
                "aprovadorId", movimentacao.getAprovador() != null
                        ? movimentacao.getAprovador().getId() : null);
    }

    private void validarSolicitacaoPendente(
            Movimentacao movimentacao) {

        if (movimentacao.getStatus()
                != StatusMovimentacao.PENDENTE) {

            throw new RegraNegocioException(
                    "A movimentação não está pendente.");
        }
    }
}