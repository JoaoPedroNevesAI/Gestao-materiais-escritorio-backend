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
public class MovimentacaoService {

    @Autowired
    private MovimentacaoRepository repository;

    @Autowired
    private PatrimonioRepository patrimonioRepository;

    @Autowired
    private LocalRepository localRepository;

    public Movimentacao transferir(
            Long patrimonioId,
            Long localDestinoId,
            String observacao,
            Usuario usuario) {

        Patrimonio patrimonio =
                patrimonioRepository.findById(patrimonioId)
                        .orElseThrow(() ->
                                new EntidadeNaoEncontradaException(
                                        "Patrimonio",
                                        patrimonioId));

        Local origem = patrimonio.getLocal();

        Local destino =
                localRepository.findById(localDestinoId)
                        .orElseThrow(() ->
                                new EntidadeNaoEncontradaException(
                                        "Local",
                                        localDestinoId));

        if (origem.getId().equals(destino.getId())) {
            throw new IllegalArgumentException(
                    "O patrimônio já está neste local.");
        }

        material.setLocal(destino);
        materialRepository.save(material);

        Movimentacao movimentacao =
                Movimentacao.builder()
                        .patrimonio(patrimonio)
                        .localOrigem(origem)
                        .localDestino(destino)
                        .observacao(observacao)
                        .usuario(usuario)
                        .dataMovimentacao(LocalDateTime.now())
                        .build();

        return repository.save(movimentacao);
    }

    public List<Movimentacao> listarTodas() {
        return repository.findAllByOrderByDataMovimentacaoDesc();
    }

    public List<Movimentacao> listarPorMaterial(
            Long materialId) {

        return repository
                .findByPatrimonioIdOrderByDataMovimentacaoDesc(
                        patrimonioId);
    }
}