package com.pica.test_planms.Entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Srs {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 10000)
    private String GeneratedFunctional;
    @Column(length = 10000)
    private String GeneratedNonFunctional;
    @Column(length = 10000)
    private String GeneratedUserstory;
    @Column(length = 10000)
    private String overview;
    @Column(length = 10000)
    private String codeClassDiagram;
    @Column(length = 10000)
    private String codeStateDiagram;
    @Column(length = 10000)
    private String codeUseCaseDiagram;
    @Column(length = 10000)
    private String codeSequenceDiagram;



    @OneToOne
    @JoinColumn(name="survey_id", nullable=false)
    private Survey survey;


}
