package br.com.ifescritorio.api.material;

import java.util.List;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.ifescritorio.model.material.Material;
import br.com.ifescritorio.model.material.MaterialService;
import jakarta.validation.Valid;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestParam;
@RestController
@RequestMapping("/api/material")
@CrossOrigin
public class MaterialController {

   @Autowired
   private MaterialService materialService;

   @PostMapping
   public ResponseEntity<Material> save(
       @RequestBody @Valid MaterialRequest request
   ) {

       Authentication auth =
           SecurityContextHolder.getContext().getAuthentication();

       System.out.println("================================");
       System.out.println("AUTH = " + auth);
       System.out.println("USER = " + auth.getName());
       System.out.println("ROLES = " + auth.getAuthorities());
       System.out.println("================================");

       Material material =
           materialService.save(request.build());

       return new ResponseEntity<>(
           material,
           HttpStatus.CREATED
       );
   }

   @GetMapping
   public List<Material> listarTodos() {
       return materialService.listarTodos();
   }

   @GetMapping("/{id}")
   public Material obterPorID(@PathVariable Long id) {
       return materialService.obterPorID(id);
   }
   
   @PutMapping("/{id}")
   public ResponseEntity<Material> update(@PathVariable Long id, @RequestBody @Valid MaterialRequest request) {

       materialService.update(id, request.build());

       return ResponseEntity.ok().build();
   }
   
   @DeleteMapping("/{id}")
   public ResponseEntity<Void> delete(@PathVariable Long id) {
       materialService.delete(id);
       return ResponseEntity.noContent().build();
   }
   
   @PostMapping("/filtrar")
   public List<Material> filtrar(

       @RequestParam(
           value = "nome",
           required = false
       )
       String nome,

       @RequestParam(
           value = "idCategoria",
           required = false
       )
       Long idCategoria,

       @RequestParam(
           value = "idLocal",
           required = false
       )
       Long idLocal
   ) {

       return materialService.filtrar(
           nome,
           idCategoria,
           idLocal
       );
   }
}
