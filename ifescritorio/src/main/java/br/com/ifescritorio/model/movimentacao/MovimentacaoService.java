package br.com.ifescritorio.model.movimentacao;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.ifescritorio.model.local.Local;
import br.com.ifescritorio.model.local.LocalRepository;
import br.com.ifescritorio.model.material.Material;
import br.com.ifescritorio.model.material.MaterialRepository;
import br.com.ifescritorio.model.usuario.Usuario;
import br.com.ifescritorio.util.exception.EntidadeNaoEncontradaException;

@Service
public class MovimentacaoService {

    @Autowired
    private MovimentacaoRepository repository;

    @Autowired
    private MaterialRepository materialRepository;

    @Autowired
    private LocalRepository localRepository;

    public Movimentacao transferir(
            Long materialId,
            Long localDestinoId,
            String observacao,
            Usuario usuario) {

        Material material =
                materialRepository.findById(materialId)
                        .orElseThrow(() ->
                                new EntidadeNaoEncontradaException(
                                        "Material",
                                        materialId));

        Local origem = material.getLocal();

        Local destino =
                localRepository.findById(localDestinoId)
                        .orElseThrow(() ->
                                new EntidadeNaoEncontradaException(
                                        "Local",
                                        localDestinoId));

        if (origem.getId().equals(destino.getId())) {
            throw new IllegalArgumentException(
                    "O material já está neste local.");
        }

        material.setLocal(destino);
        materialRepository.save(material);

        Movimentacao movimentacao =
                Movimentacao.builder()
                        .material(material)
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
                .findByMaterialIdOrderByDataMovimentacaoDesc(
                        materialId);
    }
}