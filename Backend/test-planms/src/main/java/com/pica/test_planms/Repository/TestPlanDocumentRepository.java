package com.pica.test_planms.Repository;

import com.pica.test_planms.Entity.TestPlanDocument;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestPlanDocumentRepository extends JpaRepository<TestPlanDocument, Long> {
}
