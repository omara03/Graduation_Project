package com.pica.aims.Entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter


public class Srs {
    private Long id;
    private String GeneratedFunctional;
    private String GeneratedNonFunctional;
    private String GeneratedUserstory;
    private String overview;
    private String codeClassDiagram;
    private String codeStateDiagram;
    private String codeUseCaseDiagram;
    private String codeSequenceDiagram;

}
