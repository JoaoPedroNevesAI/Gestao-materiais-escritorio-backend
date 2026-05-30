package br.com.ifescritorio.api.local;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.com.ifescritorio.model.local.Local;
import br.com.ifescritorio.model.local.LocalService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/local")
@CrossOrigin
public class LocalController {

    @Autowired
    private LocalService localService;


    @PostMapping
    public ResponseEntity<Local> save(
        @RequestBody
        @Valid
        LocalRequest request
    ) {

        Local local =
            localService.save(
                request.build()
            );

        return new ResponseEntity<>(
            local,
            HttpStatus.CREATED
        );
    }


    @GetMapping
    public List<Local> listarTodos() {

        return localService.listarTodos();
    }


    @GetMapping("/{id}")
    public Local obterPorID(
        @PathVariable Long id
    ) {

        return localService.obterPorID(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Local> update(
        @PathVariable Long id,
        @RequestBody
        @Valid
        LocalRequest request
    ) {

        localService.update(
            id,
            request.build()
        );

        return ResponseEntity.ok().build();
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
        @PathVariable Long id
    ) {

        localService.delete(id);

        return ResponseEntity.noContent().build();
    }
}