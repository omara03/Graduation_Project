package com.pica.srsms.Repository;

import com.pica.srsms.Entity.Stakeholder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StakeholderRepository extends JpaRepository<Stakeholder, Long> {
    @Query("SELECT s FROM Stakeholder s WHERE s.survey.id = :surveyId")
    List<Stakeholder> findBySurveyId(@Param("surveyId") Long surveyId);

}
