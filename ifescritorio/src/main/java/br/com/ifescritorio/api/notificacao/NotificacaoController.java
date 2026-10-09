package br.com.ifescritorio.api.notificacao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import br.com.ifescritorio.model.notificacao.Notificacao;
import br.com.ifescritorio.model.notificacao.NotificacaoService;
import br.com.ifescritorio.model.usuario.Usuario;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/notificacao")
@CrossOrigin
@Tag(
    name = "API Notificações",
    description = "API responsável pelo gerenciamento das notificações dos usuários"
)
public class NotificacaoController {

    @Autowired
    private NotificacaoService service;

    @Operation(
        summary = "Listar notificações",
        description = "Retorna todas as notificações do usuário autenticado."
    )
    @GetMapping
    public List<Notificacao> listar(
            @AuthenticationPrincipal Usuario usuario) {

        return service.listarPorUsuario(
                usuario.getId());
    }

    @Operation(
        summary = "Listar notificações não lidas",
        description = "Retorna somente as notificações ainda não lidas."
    )
    @GetMapping("/nao-lidas")
    public List<Notificacao> listarNaoLidas(
            @AuthenticationPrincipal Usuario usuario) {

        return service.listarNaoLidas(
                usuario.getId());
    }

    @Operation(
        summary = "Contar notificações não lidas",
        description = "Retorna a quantidade de notificações não lidas."
    )
    @GetMapping("/nao-lidas/count")
    public long contarNaoLidas(
            @AuthenticationPrincipal Usuario usuario) {

        return service.contarNaoLidas(
                usuario.getId());
    }

    @Operation(
        summary = "Marcar notificação como lida",
        description = "Marca uma notificação específica como lida."
    )
    @PutMapping("/{id}/ler")
    public void marcarComoLida(
            @PathVariable Long id,
            @AuthenticationPrincipal Usuario usuario) {

        service.marcarComoLida(
                id,
                usuario.getId());
    }

    @Operation(
        summary = "Marcar todas como lidas",
        description = "Marca todas as notificações do usuário como lidas."
    )
    @PutMapping("/ler-todas")
    public void marcarTodasComoLidas(
            @AuthenticationPrincipal Usuario usuario) {

        service.marcarTodasComoLidas(
                usuario.getId());
    }

    @Operation(
        summary = "Excluir notificação",
        description = "Exclui uma notificação do usuário autenticado."
    )
    @DeleteMapping("/{id}")
    public void excluir(
            @PathVariable Long id,
            @AuthenticationPrincipal Usuario usuario) {

        service.excluir(
                id,
                usuario.getId());
    }
}