package com.pica.srsms.Entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SRSDocument {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Survey survey;

    private String version;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "document", cascade = CascadeType.ALL)
    @JsonManagedReference // Add this annotation
    private List<DocumentSection> sections = new ArrayList<>();

    private StringBuilder allContent = new StringBuilder();

    public void appendGeneratedContent(String content) {
        if (content != null && !content.trim().isEmpty()) {
            allContent.append(content).append("\n\n");
        }
    }
    public String getAllContent() {
        return allContent.toString();
    }
}
