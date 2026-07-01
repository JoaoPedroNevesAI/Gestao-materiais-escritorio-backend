package br.com.ifescritorio.model.patrimonio;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.ifescritorio.util.exception.EntidadeNaoEncontradaException;

@Service
public class PatrimonioService {

    @Autowired
    private PatrimonioRepository repository;

    public Patrimonio obterPorId(Long id) {

        return repository.findById(id)
                .orElseThrow(() ->
                        new EntidadeNaoEncontradaException(
                                "Patrimonio",
                                id));
    }

    public List<Patrimonio> listarTodos() {

        return repository.findAll();
    }

    public Patrimonio salvar(Patrimonio patrimonio) {

        return repository.save(patrimonio);
    }

    public void deletar(Long id) {

        Patrimonio patrimonio =
                obterPorId(id);

        patrimonio.setHabilitado(false);

        repository.save(patrimonio);
    }
    
    public Patrimonio obterPorCodigo(
            String codigo) {

        return repository
                .findByCodigoPatrimonio(codigo)
                .orElseThrow(() ->
                        new EntidadeNaoEncontradaException(
                                "Patrimonio",
                                codigo));
    }

    public Patrimonio obterPorQrCode(
            String qrCode) {

        return repository
                .findByQrCode(qrCode)
                .orElseThrow(() ->
                        new EntidadeNaoEncontradaException(
                                "Patrimonio",
                                qrCode));
    }
}