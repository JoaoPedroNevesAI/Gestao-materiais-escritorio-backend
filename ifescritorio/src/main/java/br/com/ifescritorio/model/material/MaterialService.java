package br.com.ifescritorio.model.material;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import br.com.ifescritorio.model.categoria.Categoria;
import br.com.ifescritorio.model.categoria.CategoriaRepository;
import br.com.ifescritorio.model.local.Local;
import br.com.ifescritorio.model.local.LocalRepository;
import br.com.ifescritorio.model.patrimonio.Patrimonio;
import br.com.ifescritorio.model.patrimonio.PatrimonioRepository;
import br.com.ifescritorio.model.patrimonio.StatusPatrimonio;
import br.com.ifescritorio.util.QrCodeUtil;
import br.com.ifescritorio.util.Util;
import br.com.ifescritorio.util.exception.EntidadeNaoEncontradaException;
import jakarta.transaction.Transactional;

@Service
public class MaterialService {

    @Autowired
    private MaterialRepository repository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private LocalRepository localRepository;

    @Autowired
    private PatrimonioRepository patrimonioRepository;

    @Transactional
    public Material save(Material material) {

        Categoria categoria =
                categoriaRepository.findById(
                        material.getCategoria().getId())
                        .orElseThrow(() ->
                                new EntidadeNaoEncontradaException(
                                        "Categoria",
                                        material.getCategoria().getId()));

        Local local =
                localRepository.findById(
                        material.getLocal().getId())
                        .orElseThrow(() ->
                                new EntidadeNaoEncontradaException(
                                        "Local",
                                        material.getLocal().getId()));

        material.setCategoria(categoria);
        material.setLocal(local);
        material.setHabilitado(Boolean.TRUE);

        Material materialSalvo =
                repository.save(material);

        gerarPatrimonios(materialSalvo);

        return materialSalvo;
    }

    @Transactional
    public Material update(
            Long id,
            Material novosDados) {

        Material material =
                repository.findById(id)
                        .orElseThrow(() ->
                                new EntidadeNaoEncontradaException(
                                        "Material",
                                        id));

        material.setNome(
                novosDados.getNome());

        material.setDescricao(
                novosDados.getDescricao());

        material.setQuantidade(
                novosDados.getQuantidade());

        material.setValor(
                novosDados.getValor());

        material.setImagem(
                novosDados.getImagem());

        if (novosDados.getCategoria() != null) {

            Categoria categoria =
                    categoriaRepository.findById(
                            novosDados.getCategoria().getId())
                            .orElseThrow(() ->
                                    new EntidadeNaoEncontradaException(
                                            "Categoria",
                                            novosDados.getCategoria().getId()));

            material.setCategoria(categoria);
        }

        if (novosDados.getLocal() != null) {

            Local local =
                    localRepository.findById(
                            novosDados.getLocal().getId())
                            .orElseThrow(() ->
                                    new EntidadeNaoEncontradaException(
                                            "Local",
                                            novosDados.getLocal().getId()));

            material.setLocal(local);
        }

        return repository.save(material);
    }

    @Transactional
    public Material salvarImagem(
            Long id,
            MultipartFile imagem) {

        Material material =
                repository.findById(id)
                        .orElseThrow(() ->
                                new EntidadeNaoEncontradaException(
                                        "Material",
                                        id));

        String nomeImagem =
                Util.fazerUploadImagem(imagem);

        if (nomeImagem != null) {
            material.setImagem(nomeImagem);
        }

        return repository.save(material);
    }

    public List<Material> listarTodos() {

        return repository.findAll();
    }

    public Material obterPorID(Long id) {

        return repository.findById(id)
                .orElseThrow(() ->
                        new EntidadeNaoEncontradaException(
                                "Material",
                                id));
    }

    @Transactional
    public void delete(Long id) {

        Material material =
                repository.findById(id)
                        .orElseThrow(() ->
                                new EntidadeNaoEncontradaException(
                                        "Material",
                                        id));

        material.setHabilitado(Boolean.FALSE);

        repository.save(material);
    }

    public List<Material> filtrar(
            String nome,
            Long idCategoria,
            Long idLocal) {

        List<Material> listaMateriais =
                repository.findAll();

        if (
                (nome != null && !nome.isBlank()) &&
                idCategoria == null &&
                idLocal == null) {

            listaMateriais =
                    repository.findByNomeContainingIgnoreCaseOrderByNomeAsc(
                            nome);
        }

        else if (
                (nome == null || nome.isBlank()) &&
                idCategoria != null &&
                idLocal == null) {

            listaMateriais =
                    repository.consultarPorCategoria(
                            idCategoria);
        }

        else if (
                (nome == null || nome.isBlank()) &&
                idCategoria == null &&
                idLocal != null) {

            listaMateriais =
                    repository.consultarPorLocal(
                            idLocal);
        }

        else if (
                (nome != null && !nome.isBlank()) &&
                idCategoria != null &&
                idLocal == null) {

            listaMateriais =
                    repository.consultarPorNomeECategoria(
                            nome,
                            idCategoria);
        }

        return listaMateriais;
    }

    private void gerarPatrimonios(
            Material material) {

        for (int i = 0; i < material.getQuantidade(); i++) {

            Patrimonio patrimonio =
                    Patrimonio.builder()
                            .material(material)
                            .local(material.getLocal())
                            .status(StatusPatrimonio.DISPONIVEL)
                            .observacao("Gerado automaticamente")
                            .build();

            patrimonio =
                    patrimonioRepository.save(
                            patrimonio);

            String codigo =
                    String.format(
                            "PAT-%06d",
                            patrimonio.getId());

            patrimonio.setCodigoPatrimonio(
                    codigo);

            String qrCodeImagem =
                    QrCodeUtil.gerarQRCode(
                            codigo,
                            codigo);

            patrimonio.setQrCode(
                    qrCodeImagem);

            patrimonioRepository.save(
                    patrimonio);

            // Imprime no console para facilitar a apresentação
            QrCodeUtil.imprimirNoConsole(codigo);
        }
    }
}