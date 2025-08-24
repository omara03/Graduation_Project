package com.pica.srsms.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StakeholderDTO {
    private Long id;
    private String name;
    private String description;
    private Long surveyId;

}



//"Data Transfer Object" (DTO)
//Entity is class mapped to table.
// Dto is class mapped to "view" layer mostly
// . What needed to store is entity &
// which needed to 'show' on web page is DTO
//DTOs are used only to transfer data from one process or context to another