package br.com.ifescritorio.api.patrimonio;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.com.ifescritorio.model.patrimonio.Patrimonio;
import br.com.ifescritorio.model.patrimonio.PatrimonioService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/patrimonio")
@CrossOrigin
@Tag(
    name = "API Patrimônio",
    description = "API responsável pelo gerenciamento dos patrimônios do sistema"
)
public class PatrimonioController {

    @Autowired
    private PatrimonioService service;

    @Operation(
        summary = "Listar patrimônios",
        description = "Retorna todos os patrimônios cadastrados no sistema."
    )
    @GetMapping
    public List<Patrimonio> listarTodos() {

        return service.listarTodos();
    }

    @Operation(
        summary = "Buscar patrimônio por ID",
        description = "Retorna um patrimônio específico através do seu identificador."
    )
    @GetMapping("/{id}")
    public Patrimonio obterPorId(
            @PathVariable Long id) {

        return service.obterPorId(id);
    }

    @Operation(
        summary = "Excluir patrimônio",
        description = "Realiza a exclusão lógica de um patrimônio."
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id) {

        service.deletar(id);

        return ResponseEntity.noContent().build();
    }
    
    @Operation(
    	    summary = "Buscar patrimônio por código",
    	    description = "Retorna um patrimônio através do código patrimonial."
    	)
    	@GetMapping("/codigo/{codigo}")
    	public Patrimonio obterPorCodigo(
    	        @PathVariable String codigo) {

    	    return service.obterPorCodigo(codigo);
    	}

    	@Operation(
    	    summary = "Buscar patrimônio por QR Code",
    	    description = "Retorna um patrimônio através do valor armazenado no QR Code."
    	)
    	@GetMapping("/qrcode/{qrcode}")
    	public Patrimonio obterPorQrCode(
    	        @PathVariable String qrcode) {

    	    return service.obterPorQrCode(qrcode);
    	}
}