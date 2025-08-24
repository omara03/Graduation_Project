package com.pica.srsms.Entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Survey {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 100)
    private String ProjectName;
    @Column(length = 100)
    private String model; // "openai"
    @Column(length = 100)
    private String methodology; // "Agile" or "Traditional"
    @Column(length = 10000)
    private String InterviewNotes;
    @Column(length = 10000)
    private String overview;
    @Column(length = 10000)
    private String coreFunctionalities;
    @Column(length = 10000)
    private String nonFunctionalRequirements;
    @Column(length = 10000)
    private String userStory; // Only for Agile
    @Column(length = 10000)
    private String businessRequirements;

    @OneToMany(mappedBy = "survey", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    @ToString.Exclude
    private List<Stakeholder> stakeholders = new ArrayList<>();
}
