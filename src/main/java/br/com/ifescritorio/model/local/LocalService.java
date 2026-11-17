package br.com.ifescritorio.model.local;

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
public class LocalService {

    @Autowired
    private LocalRepository repository;

    @Autowired
    private AuditoriaService auditoriaService;

    @Transactional
    public Local save(Local local) {

        local.setHabilitado(Boolean.TRUE);

        Local salvo = repository.save(local);

        auditoriaService.registrar(
                TipoAcaoAuditoria.CRIACAO,
                "LOCAL",
                salvo.getId(),
                "Cadastro de local",
                null,
                dadosLocal(salvo));

        return salvo;
    }

    @Transactional
    public Local update(
        Long id,
        Local novosDados
    ) {

        Local local = repository.findById(id)
            .orElseThrow(() ->
                new EntidadeNaoEncontradaException(
                    "Local",
                    id
                )
            );

        Map<String, Object> dadosAnteriores = dadosLocal(local);

        local.setNome(
            novosDados.getNome()
        );

        Local atualizado = repository.save(local);

        auditoriaService.registrar(
                TipoAcaoAuditoria.ATUALIZACAO,
                "LOCAL",
                atualizado.getId(),
                "Atualização de local",
                dadosAnteriores,
                dadosLocal(atualizado));

        return atualizado;
    }


    public List<Local> listarTodos() {

        return repository.findAll();
    }


    private Map<String, Object> dadosLocal(Local local) {
        return AuditoriaDados.mapa(
                "nome", local.getNome(),
                "habilitado", local.getHabilitado());
    }

    public Local obterPorID(Long id) {

        return repository.findById(id)
            .orElseThrow(() ->
                new EntidadeNaoEncontradaException(
                    "Local",
                    id
                )
            );
    }

    @Transactional
    public void delete(Long id) {

        Local local = repository.findById(id)
            .orElseThrow(() ->
                new EntidadeNaoEncontradaException(
                    "Local",
                    id
                )
            );

        Map<String, Object> dadosAnteriores = dadosLocal(local);

        local.setHabilitado(Boolean.FALSE);

        Local desabilitado = repository.save(local);

        auditoriaService.registrar(
                TipoAcaoAuditoria.DESATIVACAO,
                "LOCAL",
                desabilitado.getId(),
                "Desativação de local",
                dadosAnteriores,
                dadosLocal(desabilitado));
    }
}