package com.pica.srsms.Repository;

import com.pica.srsms.Entity.SRSDocument;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SRSDocumentRepository extends JpaRepository<SRSDocument, Long> {
}
