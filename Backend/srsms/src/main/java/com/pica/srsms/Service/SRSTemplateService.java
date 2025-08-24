package com.pica.srsms.Service;

import com.pica.srsms.Entity.DocumentSection;
import com.pica.srsms.Entity.SRSDocument;
import com.pica.srsms.Entity.Survey;
import com.pica.srsms.Repository.DocumentSectionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class SRSTemplateService {
    private final DocumentSectionRepository sectionRepository;

    public SRSDocument parseTemplate(Survey survey) {
        SRSDocument document = new SRSDocument();
        document.setSurvey(survey);
        document.setVersion("1.0");
        document.setCreatedAt(LocalDateTime.now());

        // 1. Introduction
        document.getSections().add(createSection("1.1", "Purpose",
                "Identify the product and describe the scope of this SRS", document));
        document.getSections().add(createSection("1.2", "Document Conventions",
                "Describe any standards or typographical conventions", document));
        document.getSections().add(createSection("1.3", "Intended Audience",
                "Describe the different types of readers", document));
        document.getSections().add(createSection("1.4", "Product Scope",
                "Provide a short description of the software being specified", document));
        document.getSections().add(createSection("1.5", "References",
                "List any other documents or web addresses referred to", document));

        // 2. Overall Description
        document.getSections().add(createSection("2.1", "Product Perspective",
                "Describe the context and origin of the product", document));
        document.getSections().add(createSection("2.2", "Product Functions",
                "Summarize the major functions the product must perform", document));
        document.getSections().add(createSection("2.3", "User Classes",
                "Identify the various user classes anticipated", document));
        document.getSections().add(createSection("2.4", "Operating Environment",
                "Describe the environment in which the software will operate", document));
        document.getSections().add(createSection("2.5", "Design Constraints",
                "Describe items that will limit the developers' options", document));
        document.getSections().add(createSection("2.6", "User Documentation",
                "List the user documentation components", document));
        document.getSections().add(createSection("2.7", "Assumptions",
                "List any assumed factors that could affect requirements", document));

        // 3. External Interface Requirements
        document.getSections().add(createSection("3.1", "User Interfaces",
                "Describe the logical characteristics of each user interface", document));
        document.getSections().add(createSection("3.2", "Hardware Interfaces",
                "Describe the hardware interfaces", document));
        document.getSections().add(createSection("3.3", "Software Interfaces",
                "Describe connections to other software components", document));
        document.getSections().add(createSection("3.4", "Communications Interfaces",
                "Describe any communications functions required", document));

        // 4. System Features
        document.getSections().add(createSection("4", "Features",
                "Provide each major feature with it's description and functional requirement. starting from 4.1", document));
        // Add more features as needed

        // 5. Other Nonfunctional Requirements
        document.getSections().add(createSection("5.1", "Performance Requirements",
                "State performance requirements under various circumstances", document));
        document.getSections().add(createSection("5.2", "Safety Requirements",
                "Specify requirements concerned with possible loss or damage", document));
        document.getSections().add(createSection("5.3", "Security Requirements",
                "Specify requirements regarding security or privacy issues", document));
        document.getSections().add(createSection("5.4", "Software Quality Attributes",
                "Specify additional quality characteristics", document));
        document.getSections().add(createSection("5.5", "Business Rules",
                "List operating principles about the product including roles and permissions", document));
        // Appendices
        document.getSections().add(createSection("A", "Glossary",
                "Define all terms, acronyms, and abbreviations used in this document", document));

        document.getSections().add(createSection("B", "Analysis Models",
                "Include relevant diagrams and models (class diagrams)", document));

        document.getSections().add(createSection("C", "To Be Determined List",
                "Track all outstanding items requiring future clarification", document));
        return document;
    }

    private DocumentSection createSection(String number, String title,
                                          String placeholder, SRSDocument document) {
        DocumentSection section = new DocumentSection();
        section.setSectionNumber(number);
        section.setTitle(title);
        section.setPlaceholder(placeholder);
        section.setDocument(document);
        section.setGenerated(false);
        return section;
    }
}