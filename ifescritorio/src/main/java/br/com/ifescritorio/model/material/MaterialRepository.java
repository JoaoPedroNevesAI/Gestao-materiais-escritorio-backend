package br.com.ifescritorio.model.material;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MaterialRepository extends JpaRepository<Material, Long> {

    List<Material> findByNomeContainingIgnoreCaseOrderByNomeAsc(
        String nome
    );

    @Query("""
        SELECT m
        FROM Material m
        WHERE m.categoria.id = :idCategoria
    """)
    List<Material> consultarPorCategoria(
        Long idCategoria
    );

    @Query("""
        SELECT m
        FROM Material m
        WHERE m.local.id = :idLocal
    """)
    List<Material> consultarPorLocal(
        Long idLocal
    );

    @Query("""
        SELECT m
        FROM Material m
        WHERE LOWER(m.nome) LIKE LOWER(CONCAT('%', :nome, '%'))
        AND m.categoria.id = :idCategoria
    """)
    List<Material> consultarPorNomeECategoria(
        String nome,
        Long idCategoria
    );
}