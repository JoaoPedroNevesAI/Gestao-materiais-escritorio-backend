package br.com.ifescritorio.model.usuario;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.ifescritorio.model.auditoria.AuditoriaDados;
import br.com.ifescritorio.model.auditoria.AuditoriaService;
import br.com.ifescritorio.model.auditoria.TipoAcaoAuditoria;
import br.com.ifescritorio.util.exception.EntidadeNaoEncontradaException;
import br.com.ifescritorio.util.exception.RegraNegocioException;
import jakarta.transaction.Transactional;

@Service
public class UsuarioService implements UserDetailsService {

    @Autowired
    private UsuarioRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private AuditoriaService auditoriaService;

    @Transactional
    public Usuario save(Usuario usuario) {

        if (repository.findByEmail(usuario.getEmail()).isPresent()) {
            throw new RegraNegocioException("Email já cadastrado");
        }

        if (
            usuario.getCpf() != null &&
            repository.findByCpf(usuario.getCpf()).isPresent()
        ) {
            throw new RegraNegocioException("CPF já cadastrado");
        }

        if (usuario.getTipo() == null) {
            usuario.setTipo(TipoUsuario.COLABORADOR);
        }

        usuario.setSenha(
            passwordEncoder.encode(usuario.getSenha())
        );

        usuario.setHabilitado(Boolean.TRUE);

        Usuario salvo = repository.save(usuario);

        auditoriaService.registrar(
                TipoAcaoAuditoria.CRIACAO,
                "USUARIO",
                salvo.getId(),
                "Cadastro de usuário",
                null,
                AuditoriaDados.mapa(
                        "nome", salvo.getNome(),
                        "tipo", salvo.getTipo().name(),
                        "habilitado", salvo.getHabilitado()));

        return salvo;
    }

    @Transactional
    public Usuario update(Long id, Usuario novosDados) {

        Usuario usuario = repository.findById(id)
            .orElseThrow(() ->
                new EntidadeNaoEncontradaException("Usuário", id)
            );

        if (
            !usuario.getEmail().equals(novosDados.getEmail()) &&
            repository.findByEmail(novosDados.getEmail()).isPresent()
        ) {
            throw new RegraNegocioException("Email já cadastrado");
        }

        if (
            novosDados.getCpf() != null &&
            !novosDados.getCpf().equals(usuario.getCpf()) &&
            repository.findByCpf(novosDados.getCpf()).isPresent()
        ) {
            throw new RegraNegocioException("CPF já cadastrado");
        }

        java.util.Map<String, Object> dadosAnteriores =
                AuditoriaDados.mapa(
                        "nome", usuario.getNome(),
                        "tipo", usuario.getTipo().name(),
                        "habilitado", usuario.getHabilitado());

        usuario.setNome(novosDados.getNome());
        usuario.setEmail(novosDados.getEmail());
        usuario.setCpf(novosDados.getCpf());
        usuario.setTelefone(novosDados.getTelefone());

        if (
            novosDados.getSenha() != null &&
            !novosDados.getSenha().isBlank()
        ) {

            usuario.setSenha(
                passwordEncoder.encode(novosDados.getSenha())
            );
        }

        if (novosDados.getTipo() != null) {
            usuario.setTipo(novosDados.getTipo());
        }

        Usuario atualizado = repository.save(usuario);

        auditoriaService.registrar(
                TipoAcaoAuditoria.ATUALIZACAO,
                "USUARIO",
                atualizado.getId(),
                "Atualização de cadastro de usuário",
                dadosAnteriores,
                AuditoriaDados.mapa(
                        "nome", atualizado.getNome(),
                        "tipo", atualizado.getTipo().name(),
                        "habilitado", atualizado.getHabilitado()));

        return atualizado;
    }

    public Usuario authenticate(String email, String senha) {

        try {
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, senha)
            );
        } catch (AuthenticationException e) {
            auditoriaService.registrar(
                    TipoAcaoAuditoria.LOGIN_FALHA,
                    "AUTENTICACAO",
                    null,
                    "Tentativa de login malsucedida",
                    null,
                    null);
            throw e;
        }

        Usuario usuario = repository.findByEmail(email)
            .orElseThrow(() ->
                new UsernameNotFoundException("Usuário não encontrado")
            );

        usuario.setUltimoLogin(LocalDateTime.now());

        Usuario autenticado = repository.save(usuario);

        auditoriaService.registrarComUsuario(
                TipoAcaoAuditoria.LOGIN_SUCESSO,
                "AUTENTICACAO",
                autenticado.getId(),
                "Login realizado com sucesso",
                null,
                null,
                autenticado);

        return autenticado;
    }

    @Override
    public UserDetails loadUserByUsername(String email)
        throws UsernameNotFoundException {

        return repository.findByEmail(email)
            .orElseThrow(() ->
                new UsernameNotFoundException("Usuário não encontrado")
            );
    }

    public List<Usuario> listarTodos() {

        return repository.findAll();
    }

    public Usuario obterPorID(Long id) {

        return repository.findById(id)
            .orElseThrow(() ->
                new EntidadeNaoEncontradaException("Usuário", id)
            );
    }

    @Transactional
    public void delete(Long id) {

        Usuario usuario = obterPorID(id);

        java.util.Map<String, Object> dadosAnteriores =
                AuditoriaDados.mapa(
                        "nome", usuario.getNome(),
                        "tipo", usuario.getTipo().name(),
                        "habilitado", usuario.getHabilitado());

        repository.delete(usuario);

        auditoriaService.registrar(
                TipoAcaoAuditoria.EXCLUSAO,
                "USUARIO",
                id,
                "Exclusão de usuário",
                dadosAnteriores,
                null);
    }
}