package br.com.ifescritorio.model.patrimonio;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.ifescritorio.util.QrCodeUtil;
import br.com.ifescritorio.util.exception.EntidadeNaoEncontradaException;
import br.com.ifescritorio.util.exception.RegraNegocioException;

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

    @Transactional
    public Patrimonio salvar(
            Patrimonio patrimonio) {

        if (patrimonio.getCodigoPatrimonio() == null
                || patrimonio.getCodigoPatrimonio().isBlank()) {

            throw new RegraNegocioException(
                    "Código patrimonial é obrigatório"
            );
        }

        String codigo =
                patrimonio
                        .getCodigoPatrimonio()
                        .trim();

        repository
                .findByCodigoPatrimonio(codigo)
                .ifPresent(existente -> {

                    if (patrimonio.getId() == null
                            || !existente
                                    .getId()
                                    .equals(patrimonio.getId())) {

                        throw new RegraNegocioException(
                                "Código patrimonial já cadastrado"
                        );
                    }
                });

        patrimonio.setCodigoPatrimonio(codigo);

        /*
         * O QR Code utiliza o próprio código
         * patrimonial como identificador.
         */
        patrimonio.setQrCode(codigo);

        /*
         * Gera:
         *
         * uploads/qrcodes/PAT-000001.png
         */
        QrCodeUtil.gerarQRCode(
                codigo,
                codigo
        );

        return repository.save(
                patrimonio
        );
    }

    /**
     * Regenera o arquivo físico do QR Code
     * de um patrimônio existente.
     */
    @Transactional
    public Patrimonio regenerarQrCode(
            Long id) {

        Patrimonio patrimonio =
                obterPorId(id);

        String codigo =
                patrimonio.getCodigoPatrimonio();

        if (codigo == null
                || codigo.isBlank()) {

            throw new RegraNegocioException(
                    "Patrimônio não possui código patrimonial."
            );
        }

        /*
         * Garante que o banco continue
         * armazenando apenas o identificador.
         */
        patrimonio.setQrCode(codigo);

        /*
         * Caso exista um arquivo antigo,
         * ele é removido antes da regeneração.
         */
        QrCodeUtil.excluirQRCode(codigo);

        /*
         * Gera novamente o QR Code.
         */
        QrCodeUtil.gerarQRCode(
                codigo,
                codigo
        );

        return repository.save(
                patrimonio
        );
    }

    public void deletar(
            Long id) {

        Patrimonio patrimonio =
                obterPorId(id);

        patrimonio.setHabilitado(
                Boolean.FALSE
        );

        repository.save(
                patrimonio
        );
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

        Patrimonio patrimonio =
                repository
                        .findByQrCode(qrCode)
                        .orElseThrow(() ->
                                new EntidadeNaoEncontradaException(
                                        "Patrimonio",
                                        qrCode));

        if (Boolean.FALSE.equals(
                patrimonio.getHabilitado())) {

            throw new RegraNegocioException(
                    "O patrimônio informado está inativo."
            );
        }

        return patrimonio;
    }
}