package br.com.ifescritorio.ifescritorio;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.ifescritorio.model.auditoria.AuditoriaDados;
import br.com.ifescritorio.model.auditoria.AuditoriaService;
import br.com.ifescritorio.model.auditoria.RegistroAuditoria;
import br.com.ifescritorio.model.auditoria.RegistroAuditoriaRepository;
import br.com.ifescritorio.model.auditoria.TipoAcaoAuditoria;
import br.com.ifescritorio.model.usuario.TipoUsuario;
import br.com.ifescritorio.model.usuario.Usuario;

@ExtendWith(MockitoExtension.class)
class AuditoriaServiceTest {

    @Mock
    private RegistroAuditoriaRepository repository;

    private AuditoriaService auditoriaService;

    @BeforeEach
    void setUp() {
        auditoriaService = new AuditoriaService(
                repository,
                new ObjectMapper());

        when(repository.save(any(RegistroAuditoria.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
    }

    @AfterEach
    void limparContextoDeSeguranca() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void deveRegistrarUsuarioAutenticadoComoResponsavel() {

        Usuario usuario = new Usuario();
        usuario.setId(12L);
        usuario.setNome("Administrador de Teste");
        usuario.setTipo(TipoUsuario.ADM);

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(
                        usuario,
                        null,
                        List.of(new SimpleGrantedAuthority("ROLE_ADM"))));

        auditoriaService.registrar(
                TipoAcaoAuditoria.ATUALIZACAO,
                "PATRIMONIO",
                152L,
                "Atualização de patrimônio",
                AuditoriaDados.mapa("local", "Almoxarifado"),
                AuditoriaDados.mapa("local", "Sala de TI"));

        ArgumentCaptor<RegistroAuditoria> captor =
                ArgumentCaptor.forClass(RegistroAuditoria.class);

        org.mockito.Mockito.verify(repository).save(captor.capture());

        RegistroAuditoria registro = captor.getValue();

        assertEquals(12L, registro.getUsuarioId());
        assertEquals("Administrador de Teste", registro.getUsuarioNome());
        assertEquals("ADM", registro.getUsuarioTipo());
        assertEquals(TipoAcaoAuditoria.ATUALIZACAO, registro.getAcao());
        assertEquals("PATRIMONIO", registro.getEntidade());
        assertEquals(152L, registro.getRegistroId());
        assertEquals(
                "{\"local\":\"Almoxarifado\"}",
                registro.getDadosAnteriores());
        assertEquals(
                "{\"local\":\"Sala de TI\"}",
                registro.getDadosNovos());
    }

    @Test
    void deveIdentificarOperacaoSemUsuarioComoAnonima() {

        SecurityContextHolder.clearContext();

        auditoriaService.registrar(
                TipoAcaoAuditoria.CRIACAO,
                "USUARIO",
                20L,
                "Cadastro público de usuário",
                null,
                AuditoriaDados.mapa("tipo", "COLABORADOR"));

        ArgumentCaptor<RegistroAuditoria> captor =
                ArgumentCaptor.forClass(RegistroAuditoria.class);

        org.mockito.Mockito.verify(repository).save(captor.capture());

        RegistroAuditoria registro = captor.getValue();

        assertNull(registro.getUsuarioId());
        assertEquals("ANONIMO", registro.getUsuarioNome());
        assertEquals("ANONIMO", registro.getUsuarioTipo());
        assertEquals(TipoAcaoAuditoria.CRIACAO, registro.getAcao());
    }
}
