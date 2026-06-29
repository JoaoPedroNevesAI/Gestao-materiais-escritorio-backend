package br.com.ifescritorio.api.material;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import br.com.ifescritorio.model.material.Material;
import br.com.ifescritorio.model.material.MaterialService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/material")
@CrossOrigin
@Tag(
    name = "API Material",
    description = "API responsável pelo gerenciamento de materiais do sistema"
)
public class MaterialController {

    @Autowired
    private MaterialService materialService;

    @Operation(
        summary = "Cadastrar material",
        description = "Realiza o cadastro de um novo material e gera automaticamente os patrimônios correspondentes à quantidade informada."
    )
    @PostMapping
    public ResponseEntity<Material> save(
            @RequestBody @Valid MaterialRequest request) {

        Authentication auth =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        System.out.println("================================");
        System.out.println("AUTH = " + auth);
        System.out.println("USER = " + auth.getName());
        System.out.println("ROLES = " + auth.getAuthorities());
        System.out.println("================================");

        Material material =
                materialService.save(
                        request.build());

        return new ResponseEntity<>(
                material,
                HttpStatus.CREATED);
    }

    @Operation(
        summary = "Listar materiais",
        description = "Retorna todos os materiais cadastrados no sistema."
    )
    @GetMapping
    public List<Material> listarTodos() {

        return materialService.listarTodos();
    }

    @Operation(
        summary = "Buscar material por ID",
        description = "Retorna um material específico a partir do seu identificador."
    )
    @GetMapping("/{id}")
    public Material obterPorID(
            @PathVariable Long id) {

        return materialService.obterPorID(id);
    }

    @Operation(
        summary = "Atualizar material",
        description = "Atualiza os dados de um material existente."
    )
    @PutMapping("/{id}")
    public ResponseEntity<Void> update(
            @PathVariable Long id,
            @RequestBody @Valid MaterialRequest request) {

        materialService.update(
                id,
                request.build());

        return ResponseEntity.ok().build();
    }

    @Operation(
        summary = "Excluir material",
        description = "Realiza a exclusão lógica de um material."
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id) {

        materialService.delete(id);

        return ResponseEntity.noContent().build();
    }

    @Operation(
        summary = "Filtrar materiais",
        description = "Permite filtrar materiais por nome, categoria e local."
    )
    @PostMapping("/filtrar")
    public List<Material> filtrar(

            @RequestParam(
                    value = "nome",
                    required = false)
            String nome,

            @RequestParam(
                    value = "idCategoria",
                    required = false)
            Long idCategoria,

            @RequestParam(
                    value = "idLocal",
                    required = false)
            Long idLocal) {

        return materialService.filtrar(
                nome,
                idCategoria,
                idLocal);
    }

    @Operation(
        summary = "Enviar imagem do material",
        description = "Realiza o upload da imagem associada a um material."
    )
    @PostMapping("/{id}/imagem")
    public ResponseEntity<Material> uploadImagem(
            @PathVariable Long id,
            @RequestParam("imagem")
            MultipartFile imagem) {

        Material material =
                materialService.salvarImagem(
                        id,
                        imagem);

        return ResponseEntity.ok(material);
    }
}