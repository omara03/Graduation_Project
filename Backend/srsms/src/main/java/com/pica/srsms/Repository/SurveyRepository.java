package com.pica.srsms.Repository;

import com.pica.srsms.Entity.Survey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface SurveyRepository extends JpaRepository<Survey, Long> {
    Survey findTopByOrderByIdDesc();


}
