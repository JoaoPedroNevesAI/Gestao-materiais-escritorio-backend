package br.com.ifescritorio.model.local;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.ifescritorio.util.exception.EntidadeNaoEncontradaException;
import jakarta.transaction.Transactional;

@Service
public class LocalService {

    @Autowired
    private LocalRepository repository;

    @Transactional
    public Local save(Local local) {

        local.setHabilitado(Boolean.TRUE);

        return repository.save(local);
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

        local.setNome(
            novosDados.getNome()
        );

        return repository.save(local);
    }


    public List<Local> listarTodos() {

        return repository.findAll();
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

        local.setHabilitado(Boolean.FALSE);

        repository.save(local);
    }
}