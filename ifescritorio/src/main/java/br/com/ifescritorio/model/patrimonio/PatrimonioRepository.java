package br.com.ifescritorio.model.patrimonio;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PatrimonioRepository
        extends JpaRepository<Patrimonio, Long> {

    Optional<Patrimonio> findByCodigoPatrimonio(
            String codigoPatrimonio);
}