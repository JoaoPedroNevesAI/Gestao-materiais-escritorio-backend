package br.com.ifescritorio.model.material;

import jakarta.transaction.Transactional;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import br.com.ifescritorio.model.categoria.Categoria;
import br.com.ifescritorio.model.categoria.CategoriaRepository;
import br.com.ifescritorio.model.local.Local;
import br.com.ifescritorio.model.local.LocalRepository;
import br.com.ifescritorio.model.movimentacao.MovimentacaoService;
import br.com.ifescritorio.model.movimentacao.TipoMovimentacao;
import br.com.ifescritorio.model.usuario.Usuario;
import br.com.ifescritorio.model.usuario.UsuarioRepository;
import br.com.ifescritorio.util.exception.EntidadeNaoEncontradaException;

@Service
public class MaterialService {

    @Autowired
    private MaterialRepository repository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private LocalRepository localRepository;

    @Autowired
    private MovimentacaoService movimentacaoService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    private Usuario obterUsuarioLogado() {

        String email =
            SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        return usuarioRepository
            .findByEmail(email)
            .orElse(null);
    }

    @Transactional
    public Material save(Material material) {

        Categoria categoria =
            categoriaRepository.findById(
                material.getCategoria().getId()
            ).orElseThrow(() ->
                new EntidadeNaoEncontradaException(
                    "Categoria",
                    material.getCategoria().getId()
                )
            );

        Local local =
            localRepository.findById(
                material.getLocal().getId()
            ).orElseThrow(() ->
                new EntidadeNaoEncontradaException(
                    "Local",
                    material.getLocal().getId()
                )
            );

        material.setCategoria(categoria);
        material.setLocal(local);
        material.setHabilitado(Boolean.TRUE);

        Material materialSalvo =
            repository.save(material);

        movimentacaoService.registrar(
            materialSalvo,
            TipoMovimentacao.CADASTRO,
            "Cadastro do material: " + materialSalvo.getNome(),
            obterUsuarioLogado()
        );

        return materialSalvo;
    }

    @Transactional
    public Material update(
        Long id,
        Material novosDados
    ) {

        Material material =
            repository.findById(id)
                .orElseThrow(() ->
                    new EntidadeNaoEncontradaException(
                        "Material",
                        id
                    )
                );

        material.setNome(
            novosDados.getNome()
        );

        material.setDescricao(
            novosDados.getDescricao()
        );

        material.setQuantidade(
            novosDados.getQuantidade()
        );

        material.setValor(
            novosDados.getValor()
        );

        material.setImagemUrl(
            novosDados.getImagemUrl()
        );

        if (novosDados.getCategoria() != null) {

            Categoria categoria =
                categoriaRepository.findById(
                    novosDados.getCategoria().getId()
                ).orElseThrow(() ->
                    new EntidadeNaoEncontradaException(
                        "Categoria",
                        novosDados.getCategoria().getId()
                    )
                );

            material.setCategoria(categoria);
        }

        if (novosDados.getLocal() != null) {

            Local local =
                localRepository.findById(
                    novosDados.getLocal().getId()
                ).orElseThrow(() ->
                    new EntidadeNaoEncontradaException(
                        "Local",
                        novosDados.getLocal().getId()
                    )
                );

            material.setLocal(local);
        }

        Material materialAtualizado =
            repository.save(material);

        movimentacaoService.registrar(
            materialAtualizado,
            TipoMovimentacao.EDICAO,
            "Edição do material: " + materialAtualizado.getNome(),
            obterUsuarioLogado()
        );

        return materialAtualizado;
    }

    public List<Material> listarTodos() {

        return repository.findAll();
    }

    public Material obterPorID(Long id) {

        return repository.findById(id)
            .orElseThrow(() ->
                new EntidadeNaoEncontradaException(
                    "Material",
                    id
                )
            );
    }

    @Transactional
    public void delete(Long id) {

        Material material =
            repository.findById(id)
                .orElseThrow(() ->
                    new EntidadeNaoEncontradaException(
                        "Material",
                        id
                    )
                );

        material.setHabilitado(Boolean.FALSE);

        repository.save(material);

        movimentacaoService.registrar(
            material,
            TipoMovimentacao.BAIXA,
            "Baixa do material: " + material.getNome(),
            obterUsuarioLogado()
        );
    }
}