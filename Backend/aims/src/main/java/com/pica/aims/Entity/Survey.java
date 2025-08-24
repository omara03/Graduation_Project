package com.pica.aims.Entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


@Getter
@Setter

public class Survey {

    private Long id;
    private String ProjectName;
    private String model;
    private String methodology; // "Agile" or "Traditional"
    private String InterviewNotes;
    private String overview;
    private String coreFunctionalities;
    private String nonFunctionalRequirements;
    private String userStory; // Only for Agile
    private String businessRequirements;
    private List<Stakeholder> stakeholders = new ArrayList<>();
}
