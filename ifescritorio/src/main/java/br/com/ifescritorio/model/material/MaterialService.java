package br.com.ifescritorio.model.material;

import jakarta.transaction.Transactional;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MaterialService {

   @Autowired
   private MaterialRepository repository;

   @Transactional
   public Material save(Material material) {

       material.setHabilitado(Boolean.TRUE);
       return repository.save(material);
   }

   public List<Material> listarTodos() {
	    return repository.findAll();
	}

	public Material obterPorID(Long id) {
	    return repository.findById(id).get();
	}
   
   public void delete(Long id) {
	    repository.deleteById(id);
	}
}