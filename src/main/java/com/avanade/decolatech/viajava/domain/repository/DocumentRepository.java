package com.avanade.decolatech.viajava.domain.repository;

import com.avanade.decolatech.viajava.domain.model.Document;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentRepository extends JpaRepository<Document, String> {
}
