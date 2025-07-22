package com.avanade.decolatech.viajava.domain.repository;

import com.avanade.decolatech.viajava.domain.model.Documento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DocumentoRepository extends JpaRepository<Documento, String> {
    Optional<Documento> findByNumeroDocumento(String numeroDocumento);
}
