package com.pica.srsms.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SurveyDTO {


    private Long id;
    private String model;
    private String ProjectName;
    private String InterviewNotes;
    private String methodology; // "Agile" or "Traditional"
    private String overview;
    private String coreFunctionalities;
    private String nonFunctionalRequirements;
    private String userStory; // Only for Agile
    private String userStories; // Detailed user stories for Agile
    private String projectOverview; // Project Overview for Traditional
    private String businessRequirements; // Business Requirements for Traditional
    private List<StakeholderDTO> stakeholders;



}
