package br.com.ifescritorio.model.material;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import br.com.ifescritorio.model.auditoria.AuditoriaDados;
import br.com.ifescritorio.model.auditoria.AuditoriaService;
import br.com.ifescritorio.model.auditoria.TipoAcaoAuditoria;
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

    @Autowired
    private AuditoriaService auditoriaService;

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

        auditoriaService.registrar(
                TipoAcaoAuditoria.CRIACAO,
                "MATERIAL",
                materialSalvo.getId(),
                "Cadastro de material e geração automática dos patrimônios",
                null,
                dadosMaterial(materialSalvo));

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

        Map<String, Object> dadosAnteriores = dadosMaterial(material);

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

        Material atualizado = repository.save(material);

        auditoriaService.registrar(
                TipoAcaoAuditoria.ATUALIZACAO,
                "MATERIAL",
                atualizado.getId(),
                "Atualização de material",
                dadosAnteriores,
                dadosMaterial(atualizado));

        return atualizado;
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

        String imagemAnterior = material.getImagem();

        String nomeImagem =
                Util.fazerUploadImagem(imagem);

        if (nomeImagem != null) {
            material.setImagem(nomeImagem);

            Material atualizado = repository.save(material);

            auditoriaService.registrar(
                    TipoAcaoAuditoria.UPLOAD,
                    "MATERIAL",
                    atualizado.getId(),
                    "Imagem de material enviada ou atualizada",
                    AuditoriaDados.mapa("imagem", imagemAnterior),
                    AuditoriaDados.mapa("imagem", nomeImagem));

            return atualizado;
        }

        return material;
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

        Map<String, Object> dadosAnteriores = dadosMaterial(material);

        material.setHabilitado(Boolean.FALSE);

        Material desabilitado = repository.save(material);

        auditoriaService.registrar(
                TipoAcaoAuditoria.DESATIVACAO,
                "MATERIAL",
                desabilitado.getId(),
                "Desativação de material",
                dadosAnteriores,
                dadosMaterial(desabilitado));
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

    private Map<String, Object> dadosMaterial(Material material) {

        return AuditoriaDados.mapa(
                "nome", material.getNome(),
                "descricao", material.getDescricao(),
                "quantidade", material.getQuantidade(),
                "valor", material.getValor(),
                "categoriaId", material.getCategoria() != null
                        ? material.getCategoria().getId() : null,
                "categoriaNome", material.getCategoria() != null
                        ? material.getCategoria().getNome() : null,
                "localId", material.getLocal() != null
                        ? material.getLocal().getId() : null,
                "localNome", material.getLocal() != null
                        ? material.getLocal().getNome() : null,
                "habilitado", material.getHabilitado());
    }

    /**
     * Gera automaticamente um patrimônio
     * para cada unidade do material.
     */
    private void gerarPatrimonios(Material material) {

        for (int i = 0; i < material.getQuantidade(); i++) {

            String codigo = gerarProximoCodigoPatrimonio();

            Patrimonio patrimonio =
                    Patrimonio.builder()
                            .codigoPatrimonio(codigo)
                            .qrCode(codigo)
                            .material(material)
                            .local(material.getLocal())
                            .status(StatusPatrimonio.DISPONIVEL)
                            .observacao("Gerado automaticamente")
                            .build();

            Patrimonio patrimonioSalvo = patrimonioRepository.save(patrimonio);

            auditoriaService.registrar(
                    TipoAcaoAuditoria.CRIACAO,
                    "PATRIMONIO",
                    patrimonioSalvo.getId(),
                    "Patrimônio gerado automaticamente a partir do cadastro do material",
                    null,
                    AuditoriaDados.mapa(
                            "codigoPatrimonio", patrimonioSalvo.getCodigoPatrimonio(),
                            "materialId", material.getId(),
                            "materialNome", material.getNome(),
                            "localId", material.getLocal() != null
                                    ? material.getLocal().getId() : null,
                            "status", patrimonioSalvo.getStatus().name()));

            QrCodeUtil.gerarQRCode(codigo, codigo);
        }
    }
    
    private String gerarProximoCodigoPatrimonio() {

        long numero = 1;

        while (patrimonioRepository.existsByCodigoPatrimonio(
                String.format("PAT-%06d", numero))) {
            numero++;
        }

        return String.format("PAT-%06d", numero);
    }
}