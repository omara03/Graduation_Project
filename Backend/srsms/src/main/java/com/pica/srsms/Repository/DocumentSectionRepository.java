package com.pica.srsms.Repository;

import com.pica.srsms.Entity.DocumentSection;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DocumentSectionRepository extends JpaRepository<DocumentSection, Long> {
    List<DocumentSection> findByDocumentId(Long documentId);
}

