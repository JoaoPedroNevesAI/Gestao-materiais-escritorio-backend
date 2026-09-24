package br.com.ifescritorio.model.patrimonio;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PatrimonioRepository
        extends JpaRepository<Patrimonio, Long> {

    Optional<Patrimonio> findByCodigoPatrimonio(
            String codigoPatrimonio);

    Optional<Patrimonio> findByQrCode(
            String qrCode);
    
    @Query(value = "SELECT nextval('patrimonio_codigo_seq')", nativeQuery = true)
    Long proximoNumeroCodigo();
}