package com.pica.srsms.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class SrsDTO {
    private Long srsId;
    private String GeneratedFunctional;
    private String GeneratedNonFunctional;
    private String GeneratedUserstory;
    private String overview;
}
