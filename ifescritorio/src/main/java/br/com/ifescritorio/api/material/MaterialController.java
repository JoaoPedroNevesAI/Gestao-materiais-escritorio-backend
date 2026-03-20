package br.com.ifescritorio.api.material;

import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.com.ifescritorio.model.material.Material;
import br.com.ifescritorio.model.material.MaterialService;

@RestController
@RequestMapping("/api/material")
@CrossOrigin
public class MaterialController {

   @Autowired
   private MaterialService materialService;

   @PostMapping
   public ResponseEntity<Material> save(@RequestBody MaterialRequest request) {

       Material material = materialService.save(request.build());
       return new ResponseEntity<>(material, HttpStatus.CREATED);
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
   public ResponseEntity<Material> update(@PathVariable Long id, @RequestBody MaterialRequest request) {

       Material material = request.build();
       material.setId(id);

       return ResponseEntity.ok(materialService.save(material));
   }
   
   @DeleteMapping("/{id}")
   public ResponseEntity<Void> delete(@PathVariable Long id) {
       materialService.delete(id);
       return ResponseEntity.noContent().build();
   }
}
