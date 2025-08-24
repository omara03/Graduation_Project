package com.pica.srsms.Service;

import com.pica.srsms.DTO.StakeholderDTO;
import com.pica.srsms.DTO.SurveyDTO;
import com.pica.srsms.Entity.Survey;
import com.pica.srsms.Repository.SurveyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SurveyService {
    @Autowired
    private SurveyRepository surveyRepository;

    // Create a new survey
    public Survey createSurvey(Survey survey) {
        return surveyRepository.save(survey);
    }

    // Get a survey by ID
    public Survey getSurveyById(Long id) {
        return surveyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Survey not found"));
    }

    // Convert Survey to SurveyDTO
    public SurveyDTO convert(Survey survey) {
        SurveyDTO surveyDTO = new SurveyDTO();
        surveyDTO.setId(survey.getId());
        surveyDTO.setModel(survey.getModel());
        surveyDTO.setMethodology(survey.getMethodology());
        surveyDTO.setOverview(survey.getOverview());
        surveyDTO.setCoreFunctionalities(survey.getCoreFunctionalities());
        surveyDTO.setNonFunctionalRequirements(survey.getNonFunctionalRequirements());
        surveyDTO.setUserStory(survey.getUserStory());
        surveyDTO.setBusinessRequirements(survey.getBusinessRequirements());
        surveyDTO.setInterviewNotes(survey.getInterviewNotes());
        surveyDTO.setProjectName(survey.getProjectName());

        // Convert each Stakeholder to StakeholderDTO
        List<StakeholderDTO> stakeholderDTOs = survey.getStakeholders().stream()
                .map(stakeholder -> {
                    StakeholderDTO dto = new StakeholderDTO();
                    dto.setId(stakeholder.getId());
                    dto.setName(stakeholder.getName());
                    dto.setDescription(stakeholder.getDescription());
                    // Assuming 'survey' is just a reference to Survey ID or name in JSON
                    dto.setSurveyId(survey.getId()); // Set Survey ID as String
                    return dto;
                })
                .collect(Collectors.toList());

        surveyDTO.setStakeholders(stakeholderDTOs);
        return surveyDTO;
    }

    // Get the first survey's ID
    public long getFirstSurvey() {
        List<Survey> surveys = surveyRepository.findAll();
        if (surveys.isEmpty()) {
            throw new RuntimeException("No surveys found");
        }
        return surveys.get(0).getId();
    }
    public Survey getLastSurvey() {
        List<Survey> surveys = surveyRepository.findAll();
        if (surveys.isEmpty()) {
            throw new RuntimeException("No surveys found");
        }
        Survey lastSurvey = surveys.getLast();
        System.out.println("Last Survey core functionalities: " + lastSurvey.getCoreFunctionalities());
        return lastSurvey;
    }

    public List<Survey> getAll() {
        List<Survey> surveys = surveyRepository.findAll();
        // Load stakeholders for each survey to avoid lazy loading issues
        surveys.forEach(survey -> survey.getStakeholders().size());
        return surveys;
    }

    // Delete a survey by ID
    public void deleteSurveyById(Long id) {
        surveyRepository.deleteById(id);
    }

    // Delete all surveys
    public void deleteAllSurveys() {
        surveyRepository.deleteAll();
    }

}
