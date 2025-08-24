package com.pica.srsms.Entity;


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

    @Lob
    @Basic(fetch = FetchType.EAGER) // Force eager loading
    @Column(length = 10485760) // 10MB
    private byte[] classDiagramImage;

    @Lob
    @Basic(fetch = FetchType.EAGER) // Force eager loading
    @Column(length = 10485760) // 10MB
    private byte[] stateDiagramImage;

    @Lob
    @Basic(fetch = FetchType.EAGER) // Force eager loading
    @Column(length = 10485760) // 10MB
    private byte[] useCaseDiagramImage;

    @Lob
    @Basic(fetch = FetchType.EAGER) // Force eager loading
    @Column(length = 10485760) // 10MB
    private byte[] sequenceDiagramImage;

    @OneToOne
    @JoinColumn(name="survey_id", nullable=false)
    private Survey survey;


}
