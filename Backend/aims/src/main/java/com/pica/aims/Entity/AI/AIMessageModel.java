package com.pica.aims.Entity.AI;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class AIMessageModel {
    private String role;
    private  String content;
}