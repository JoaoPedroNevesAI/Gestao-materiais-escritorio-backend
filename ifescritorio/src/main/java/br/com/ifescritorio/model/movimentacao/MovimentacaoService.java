package br.com.ifescritorio.model.movimentacao;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.ifescritorio.model.material.Material;
import br.com.ifescritorio.model.usuario.Usuario;

@Service
public class MovimentacaoService {

    @Autowired
    private MovimentacaoRepository repository;

    public Movimentacao registrar(
        Material material,
        TipoMovimentacao tipo,
        String descricao,
        Usuario usuario
    ) {

        Movimentacao movimentacao =
            Movimentacao.builder()
                .material(material)
                .tipo(tipo)
                .descricao(descricao)
                .usuario(usuario)
                .dataMovimentacao(LocalDateTime.now())
                .build();

        return repository.save(movimentacao);
    }

    public List<Movimentacao> listarPorMaterial(Long materialId) {

        return repository
            .findByMaterialIdOrderByDataMovimentacaoDesc(materialId);
    }
}