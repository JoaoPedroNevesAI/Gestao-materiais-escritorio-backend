package br.com.ifescritorio.model.categoria;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.ifescritorio.model.auditoria.AuditoriaDados;
import br.com.ifescritorio.model.auditoria.AuditoriaService;
import br.com.ifescritorio.model.auditoria.TipoAcaoAuditoria;
import br.com.ifescritorio.util.exception.EntidadeNaoEncontradaException;
import jakarta.transaction.Transactional;

@Service
public class CategoriaService {

    @Autowired
    private CategoriaRepository repository;

    @Autowired
    private AuditoriaService auditoriaService;

    @Transactional
    public Categoria save(Categoria categoria) {
        categoria.setHabilitado(Boolean.TRUE);
        Categoria salva = repository.save(categoria);

        auditoriaService.registrar(
                TipoAcaoAuditoria.CRIACAO,
                "CATEGORIA",
                salva.getId(),
                "Cadastro de categoria",
                null,
                dadosCategoria(salva));

        return salva;
    }

    @Transactional
    public Categoria update(Long id, Categoria novosDados) {

        Categoria categoria = repository.findById(id)
            .orElseThrow(() -> new EntidadeNaoEncontradaException("Categoria", id));

        Map<String, Object> dadosAnteriores = dadosCategoria(categoria);

        categoria.setNome(novosDados.getNome());
        categoria.setDescricao(novosDados.getDescricao());

        Categoria atualizada = repository.save(categoria);

        auditoriaService.registrar(
                TipoAcaoAuditoria.ATUALIZACAO,
                "CATEGORIA",
                atualizada.getId(),
                "Atualização de categoria",
                dadosAnteriores,
                dadosCategoria(atualizada));

        return atualizada;
    }

    public List<Categoria> listarTodos() {
        return repository.findAll();
    }

    public Categoria obterPorID(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Categoria", id));
    }

    @Transactional
    public void delete(Long id) {
        Categoria categoria = obterPorID(id);
        Map<String, Object> dadosAnteriores = dadosCategoria(categoria);

        repository.delete(categoria);

        auditoriaService.registrar(
                TipoAcaoAuditoria.EXCLUSAO,
                "CATEGORIA",
                id,
                "Exclusão de categoria",
                dadosAnteriores,
                null);
    }

    private Map<String, Object> dadosCategoria(Categoria categoria) {
        return AuditoriaDados.mapa(
                "nome", categoria.getNome(),
                "descricao", categoria.getDescricao(),
                "habilitado", categoria.getHabilitado());
    }
}