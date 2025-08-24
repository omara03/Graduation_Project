package com.pica.srsms.Repository;

import com.pica.srsms.Entity.Srs;
import com.pica.srsms.Entity.Survey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SrsRepository extends JpaRepository<Srs, Long> {
    Optional<Srs> findBySurveyId(Long surveyId);
    Srs findTopByOrderByIdDesc();
}
