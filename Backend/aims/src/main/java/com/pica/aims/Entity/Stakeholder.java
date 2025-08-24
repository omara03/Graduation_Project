package com.pica.aims.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;


@Setter
@Getter

public class Stakeholder {

    private Long id;
    private String name;
    private String description;
//    private Long companyId;
    private Survey survey;

}
