package com.pica.test_planms.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TestPlanDocumentSection {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference // Add this annotation
    private TestPlanDocument document;

    private String sectionNumber; // e.g., "1.1"
    private String title;        // e.g., "Purpose"
    private String placeholder;  // Template prompt text

    //    @Column(length = 10000) // or use @Lob for very large content
    @Lob
    private String generatedContent;
    private boolean isGenerated;

}
