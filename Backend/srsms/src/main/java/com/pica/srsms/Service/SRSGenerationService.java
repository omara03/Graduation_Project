package com.pica.srsms.Service;

import com.pica.srsms.Entity.DocumentSection;
import com.pica.srsms.Entity.SRSDocument;
import com.pica.srsms.Entity.Srs;
import com.pica.srsms.Entity.Survey;
import com.pica.srsms.Repository.DocumentSectionRepository;
import com.pica.srsms.Repository.SRSDocumentRepository;
import com.pica.srsms.Repository.SrsRepository;
import com.pica.srsms.Repository.SurveyRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SRSGenerationService {
    private final SRSTemplateService templateService;
    private final SRSDocumentRepository documentRepository;
    private final AIClientService aiClient;
    private final SurveyRepository surveyRepository;
    private final SrsRepository srsRepository;
    private final DocumentSectionRepository documentSectionRepository;

    // initialize a document based on a survey
    @Transactional
    public SRSDocument initializeDocument(Long surveyId) {
        Survey survey = surveyRepository.findById(surveyId)
                .orElseThrow(() -> new RuntimeException("Survey not found"));

        SRSDocument document = templateService.parseTemplate(survey);
        return documentRepository.save(document);
    }

    @Transactional
    public DocumentSection generateSection(Long documentId, String sectionNumber) {
        SRSDocument document = documentRepository.findById(documentId)
                .orElseThrow(() -> new RuntimeException("Document not found"));

        DocumentSection section = document.getSections().stream()
                .filter(s -> s.getSectionNumber().equals(sectionNumber))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Section not found"));

        String prompt = buildPrompt(section, document);
        String generatedContent = aiClient.generateContent(prompt);

        section.setGeneratedContent(generatedContent);
        section.setGenerated(true);
        document.setUpdatedAt(LocalDateTime.now());

        return section;
    }

    private String buildPrompt(DocumentSection section, SRSDocument document) {
        Survey survey = document.getSurvey();
        switch (section.getSectionNumber()) {


            case "1.1":
                return String.format("""
You are generating a Software Requirements Specification (SRS) document.

Project Title: %s
Project Overview: %s

Write the [bold]1.1 Purpose[/bold] section of the SRS document.
Structure:
- Begin with a brief introduction summarizing the importance of the purpose.
- Present the body using bullet points (•) where applicable.
- End with a brief conclusion reaffirming the section's value.

Formatting Guidelines:
- Use [bold] for emphasis.
- Only use dot bullets (•), no numbered lists.
- Use concise professional language suitable for Microsoft Word formatting.
- Maintain clarity and formal tone.
- [bold]Word Limit:[/bold] 120–180 words.

Additional Instructions: %s
""", survey.getProjectName(), survey.getOverview(), section.getPlaceholder());

            case "1.2":
                return """
You are writing the [bold]1.2 Document Conventions[/bold] section of an SRS.

Structure:
- Start with a short introduction explaining why conventions matter.
- Describe formatting standards, terminology, naming conventions, and requirement tagging using bullet points (•).
- End with a brief note on how these conventions ensure consistency.

Formatting Guidelines:
- Use [bold] and [underline] tags for emphasis.
- Only use dot bullets (•).
- Use concise and formal language.
- [bold]Word Limit:[/bold] 150–200 words.

Additional Instructions: """ + section.getPlaceholder();

            case "1.3":
                return String.format("""
Project Name: %s
Project Overview: %s

Write the [bold]1.3 Intended Audience[/bold] section for the SRS.
Structure:
- Begin with a short introduction on identifying the document’s readers.
- List target audiences and how each will use the document using dot bullet points (•).
- Conclude with reading recommendations per audience group.

Formatting Guidelines:
- Use formal technical writing.
- Use [bold] for roles or headings.
- Only use dot bullet points (•).
- [bold]Word Limit:[/bold] 120–180 words.

%s
""", survey.getProjectName(), survey.getOverview(), section.getPlaceholder());

            case "1.4":
                return String.format("""
You are writing section [bold]1.4 Product Scope[/bold] of an SRS document.

Project Title: %s
Project Overview: %s

Structure:
- Start with a short overview of what scope means.
- Detail the scope using dot bullet points (•), including benefits and business alignment.
- End with a brief statement summarizing product impact.

Formatting Guidelines:
- Use [bold] for key terms.
- Only use dot bullet points (•).
- Avoid technical or vague content.
- [bold]Word Limit:[/bold] 150–200 words.

Additional Instructions: %s
""", survey.getProjectName(), survey.getOverview(), section.getPlaceholder());

            case "1.5":
                return """
You are generating the [bold]1.5 References[/bold] section of an SRS document.

Structure:
- Start with a brief introduction describing the value of listed references.
- List each reference using bullet points (•) in the format:
  [Title], Author, Version, Date, Location/Link
- If no references exist, state: "No external references were used."
- Conclude by mentioning how references support document credibility.

Formatting Guidelines:
- Use dot bullet points (•) only.
- Maintain consistent structure across all items.
- [bold]Word Limit:[/bold] 80–120 words.

Additional Instructions: """ + section.getPlaceholder();

            case "2.1":
                return """
                        You are writing section [bold]2.1 Product Perspective[/bold] of the SRS.
                        
                        Instructions:
                        - Start with a brief introductory sentence explaining the purpose of this section.
                        - In the body, describe the product's context in relation to:
                          • Existing systems
                          • System dependencies or integrations
                          • Whether it is a new product, a replacement, or an upgrade
                        - End with a brief conclusion summarizing the significance of understanding the product’s perspective.
                        - Use [bold]...[/bold] for emphasis and only dot bullets (•).
                        - [bold]Word Limit:[/bold] 150–200 words
                        
                        Additional Instructions: """ + section.getPlaceholder();

            case "2.2":
                return String.format("""
                        You are creating section [bold]2.2 Product Functions[/bold] for the SRS.
                        
                        Project Functionalities:
                        %s
                        
                        Instructions:
                        - Start with a short introduction to explain this section outlines the system’s primary functions.
                        - Then list each major system function and its purpose using:
                          • Dot bullet points (no numbering)
                          • [bold] for key function names
                        - Conclude by summarizing how these functions support the overall goals.
                        - Do not include UI or implementation details.
                        - [bold]Word Limit:[/bold] 150–250 words
                        
                        Additional Instructions: %s
                        """, survey.getCoreFunctionalities(), section.getPlaceholder());

            case "2.3":
                return """
                        You are writing section [bold]2.3 User Classes and Characteristics[/bold] of an SRS.
                        
                        Instructions:
                        - Begin with a brief overview of why understanding user classes is essential.
                        - In the body, identify and describe each user type:
                          • Their role
                          • Access level
                          • Technical skill level
                          • Any special needs
                        - Finish with a concluding sentence explaining how these distinctions impact system design.
                        - Use [bold] and dot bullets only.
                        - [bold]Word Limit:[/bold] 150–200 words
                        
                        Additional Instructions: """ + section.getPlaceholder();

            case "2.4":
                return """
                        You are writing [bold]2.4 Operating Environment[/bold] for the SRS.
                        
                        Instructions:
                        - Begin with a short introduction stating the relevance of the operating environment.
                        - Describe the technical environment including:
                          • Hardware platforms
                          • Operating systems
                          • Required software or middleware
                          • Network configurations (if relevant)
                        - Close with a sentence indicating how this environment supports or constrains the software.
                        - Use only dot bullets.
                        - [bold]Word Limit:[/bold] 100–150 words
                        
                        Additional Instructions: """ + section.getPlaceholder();

            case "2.5":
                return """
                        You are writing section [bold]2.5 Design and Implementation Constraints[/bold].
                        
                        Instructions:
                        - Start with a short introduction about the importance of understanding constraints.
                        - In the body, list constraints such as:
                          • Regulatory or compliance constraints
                          • Specific programming languages, tools, or platforms
                          • Hardware or performance limitations
                        - Conclude with how these constraints influence the software design or delivery.
                        - Use only dot bullet points and [bold] tags.
                        - [bold]Word Limit:[/bold] 150–200 words
                        
                        Additional Instructions: """ + section.getPlaceholder();

            case "2.6":
                return """
                        Write section [bold]2.6 User Documentation[/bold] of the SRS.
                        
                        Instructions:
                        - Open with a short statement about the role of user documentation.
                        - Include expected deliverables:
                          • Manuals
                          • Help files
                          • Tutorials
                        - Mention formats (PDF, online help, printed) and user class availability.
                        - Finish with a summary sentence on how documentation aids usability.
                        - Use only dot bullet points.
                        - [bold]Word Limit:[/bold] 100–150 words
                        
                        Additional Instructions: """ + section.getPlaceholder();

            case "2.7":
                return """
                        Write section [bold]2.7 Assumptions and Dependencies[/bold].
                        
                        Instructions:
                        - Start with a brief explanation of the section’s purpose.
                        - List:
                          • Assumptions (e.g., third-party systems availability)
                          • Dependencies (on APIs, components, services)
                        - Conclude by indicating how these items influence the software’s scope.
                        - Only use dot bullets for clarity.
                        - [bold]Word Limit:[/bold] 100–150 words
                        
                        Additional Instructions: """ + section.getPlaceholder();

            case "3.1":
                return """
                        You are writing the [bold]3.1 User Interfaces[/bold] section of an SRS.
                        
                        Instructions:
                        - Introduce the types of interfaces users will interact with.
                        - Describe interface types and their design expectations:
                          • Web, mobile, desktop UI
                          • GUI standards
                          • Navigation flow
                          • Error-handling behavior
                        - End with a sentence on how UI consistency ensures usability.
                        - Use only dot bullets.
                        - [bold]Word Limit:[/bold] 200–250 words
                        
                        Additional Instructions: """ + section.getPlaceholder();

            case "3.2":
                return """
                        You are writing the [bold]3.2 Hardware Interfaces[/bold] section of an SRS.
                        
                        Instructions:
                        - Start with a short introduction about hardware interactions.
                        - Specify hardware components such as:
                          • Sensors
                          • Printers
                          • Barcode scanners
                        - Mention data formats and communication protocols.
                        - Conclude with how these interfaces affect compatibility and integration.
                        - Dot bullets only.
                        - [bold]Word Limit:[/bold] 150–200 words
                        
                        Additional Instructions: """ + section.getPlaceholder();

            case "3.3":
                return """
                        You are writing the [bold]3.3 Software Interfaces[/bold] section of an SRS.
                        
                        Instructions:
                        - Begin by introducing why software interfaces are necessary.
                        - Identify external systems such as:
                          • Databases
                          • APIs
                          • Authentication services
                        - Describe protocols and data formats.
                        - Wrap up by explaining how software interfaces affect data flow.
                        - Use only dot bullets.
                        - [bold]Word Limit:[/bold] 150–200 words
                        
                        Additional Instructions: """ + section.getPlaceholder();

            case "3.4":
                return """
                        You are writing the [bold]3.4 Communications Interfaces[/bold] section of an SRS.
                        
                        Instructions:
                        - Introduce this section’s purpose.
                        - Explain communication technologies:
                          • Protocols (HTTP, TCP/IP)
                          • Expected bandwidth/data rates
                          • Security and error handling
                        - Conclude with how communication protocols affect reliability.
                        - Use dot bullets.
                        - [bold]Word Limit:[/bold] 150–200 words
                        
                        Additional Instructions: """ + section.getPlaceholder();

//            case "4.1":
//                return """
//                        You are writing the [bold]4.1 System Feature[/bold] section of an SRS.
//
//                        Instructions:
//                        - Start with a sentence introducing the feature.
//                        - Detail:
//                          • [bold]Description[/bold]
//                          • [bold]Priority[/bold] (High, Medium, Low)
//                          • [bold]User Interaction[/bold]
//                          • [bold]Functional Requirements[/bold]
//                          • [bold]Error Handling[/bold]
//                        - Conclude with how this feature supports overall functionality.
//                        - Use dot bullets.
//                        - [bold]Word Limit:[/bold] 200–300 words
//
//                        Additional Instructions: """ + section.getPlaceholder();

//            case "4":
//                return String.format("""
//You are writing the [bold]4. Functional Requirements[/bold] section of a Software Requirements Specification (SRS) document.
//
//Project Title: %s
//Project Overview: %s
//
//Instructions:
//- Begin with a brief introductory paragraph explaining the purpose of this section: to outline the core system features and the functional requirements associated with each.
//- Then generate multiple features, starting from [bold]4.1[/bold], [bold]4.2[/bold], and so on, as needed based on the project.
//- For each feature:
//  • Include a [bold]Feature Number and Title[/bold] (e.g., 4.1 User Registration)
//  • Provide a concise [bold]Feature Description[/bold] explaining the capability or service provided by the system.
//  • List all [bold]Functional Requirements[/bold] related to that feature using only dot-based bullet points (•).
//  • Each requirement must include:
//    - A unique [bold]Requirement ID[/bold] (e.g., FR-1, FR-2)
//    - [bold]Description[/bold] of what the system must do
//    - [bold]User Interaction[/bold] if the user initiates or responds to the behavior
//    - [bold]Inputs[/bold] and [bold]Outputs[/bold] if applicable
//- Do not use numbered bullet points — use dot (•) bullets only.
//- Apply [bold] and [underline] formatting for important keywords, headings, and fields.
//- End with a brief concluding paragraph summarizing the significance of the defined features and requirements in achieving the system’s goals.
//- Maintain professional and structured technical writing.
//- [bold]Word Limit:[/bold] 200–300 words.
//
//Additional Instructions: %s
//""", survey.getProjectName(), survey.getOverview(), section.getPlaceholder());


//            case "4":
//                return String.format("""
//You are writing the [bold]4. System Features[/bold] section of a Software Requirements Specification (SRS) document.
//
//Project Title: %s
//Project Overview: %s
//
//Instructions:
//- Begin the section with a brief introduction explaining the purpose of system features in an SRS document — to describe key capabilities the system must offer.
//- Then generate multiple system features, beginning from [bold]4.1[/bold], [bold]4.2[/bold], and so on.
//- For each system feature:
//  • Start with a [bold]Feature Number and Title[/bold] (e.g., 4.1 User Registration)
//  • Write a concise [bold]Feature Description[/bold] explaining what this feature enables or provides.
//  • Then list the [bold]Functional Requirements[/bold] related to this feature using dot-based bullet points (•) only.
//  • Each functional requirement should include:
//    - A unique [bold]Requirement ID[/bold] (e.g., FR-1, FR-2, etc.)
//    - [bold]Description[/bold] of what the system must do
//    - [bold]User Interaction[/bold] if applicable
//    - [bold]Inputs[/bold] and [bold]Outputs[/bold] if relevant
//- Repeat this format for 2–3 key system features, incrementing the section numbers accordingly (4.1, 4.2, 4.3, etc.).
//- Do not use numbered bullet points — use dot bullets (•) only.
//- Use [bold] and [underline] formatting consistently for headings and keywords.
//- Ensure each system feature block (title, description, and related requirements) is approximately 100–150 words.
//- End the section with a short concluding paragraph summarizing the overall purpose of the described system features.
//
//Additional Instructions: %s
//""", survey.getProjectName(), survey.getOverview(), section.getPlaceholder());



            case "4":
                return String.format("""
You are writing the [bold]4. System Features[/bold] section of an SRS.

Project Title: %s
Project Overview: %s

Instructions:
- Begin with a brief intro about the role of system features in defining software capabilities.
- Generate multiple features starting from [bold]4.1[/bold], [bold]4.2[/bold], etc.
- For each:
  • Include a [bold]Feature Number and Title[/bold] (e.g., 4.1 User Login)
  • Add a short [bold]Feature Description[/bold]
  • List [bold]Functional Requirements[/bold] as dot bullets (•), each with:
    - [bold]Requirement ID[/bold] (e.g., FR-1)
    - [bold]Description[/bold]
    - [bold]User Interaction[/bold] if applicable
    - [bold]Inputs[/bold] and [bold]Outputs[/bold] if relevant
- Use only • bullets (no numbers).
- Apply [bold] and [underline] formatting for keywords.
- Each feature block: 100–150 words.
- Conclude with a short summary of how these features support the system.

Additional Instructions: %s
""", survey.getProjectName(), survey.getOverview(), section.getPlaceholder());




            case "5.1":
                return """
                        You are writing the [bold]5.1 Performance Requirements[/bold] section of an SRS.
                        
                        Instructions:
                        - Begin with a sentence about performance importance.
                        - Specify:
                          • [bold]Response Time[/bold]
                          • [bold]Throughput[/bold]
                          • [bold]Concurrency Support[/bold]
                          • [bold]Resource Utilization[/bold]
                        - Wrap up with how meeting these ensures reliability.
                        - Use dot bullets only.
                        - [bold]Word Limit:[/bold] 200–250 words
                        
                        Additional Instructions: """ + section.getPlaceholder();

            case "5.2":
                return """
                        You are writing the [bold]5.2 Safety Requirements[/bold] section of an SRS.
                        
                        Instructions:
                        - Introduce potential system hazards.
                        - List:
                          • Potential risks
                          • Mitigation strategies
                          • Compliance with safety standards
                        - Finish by noting the importance of safety planning.
                        - Dot bullets only.
                        - [bold]Word Limit:[/bold] 150–200 words
                        
                        Additional Instructions: """ + section.getPlaceholder();

            case "5.3":
                return """
                        You are writing the [bold]5.3 Security Requirements[/bold] section of an SRS.
                        
                        Instructions:
                        - Start by explaining the role of security.
                        - Address:
                          • Authentication and authorization
                          • Data encryption
                          • GDPR or other compliance
                        - Conclude with a note on maintaining user trust.
                        - Only use dot bullets.
                        - [bold]Word Limit:[/bold] 200–250 words
                        
                        Additional Instructions: """ + section.getPlaceholder();

            case "5.4":
                return """
                        You are writing the [bold]5.4 Software Quality Attributes[/bold] section.
                        
                        Instructions:
                        - Open with an explanation of software quality metrics.
                        - Discuss:
                          • Reliability
                          • Maintainability
                          • Usability
                          • Portability
                        - End with a sentence on how these impact product success.
                        - Use dot bullets.
                        - [bold]Word Limit:[/bold] 200–250 words
                        
                        Additional Instructions: """ + section.getPlaceholder();

            case "5.5":
                return String.format("""
                        You are writing the [bold]5.5 Business Rules[/bold] section of an SRS.
                        
                        Project: %s
                        Business Requirements: %s
                        
                        Instructions:
                        - Begin with a statement introducing the role of business rules.
                        - Include:
                          • Role-based permissions
                          • Operational constraints
                          • Key logic rules
                          • Compliance mandates
                        - End by explaining how business rules enforce organizational policy.
                        - Dot bullets only.
                        - [bold]Word Limit:[/bold] 200–300 words
                        
                        Additional Instructions: %s
                        """, survey.getProjectName(), survey.getBusinessRequirements() != null ? survey.getBusinessRequirements() : "Not Specified", section.getPlaceholder());

            case "6":
                return """
                        You are writing the [bold]6. Other Requirements[/bold] section of an SRS.
                        
                        Instructions:
                        - Start with a short paragraph introducing this section.
                        - Describe:
                          • Database needs
                          • Internationalization
                          • Legal issues
                          • Reusability
                        - Wrap up with how these improve system robustness.
                        - Dot bullets only.
                        - [bold]Word Limit:[/bold] 150–200 words
                        
                        Additional Instructions: """ + section.getPlaceholder();

            case "A":
                String allContent = document.getAllContent();
                return """
                        You are creating [bold]Appendix A: Glossary[/bold] for an SRS.
                        
                        Instructions:
                        - Begin with a sentence explaining the glossary's purpose.
                        - Extract:
                          • Technical terms
                          • Acronyms
                          • Project/domain-specific terms
                        - For each entry:
                          [Term]: Definition (mention source if possible)
                        - Alphabetize entries.
                        - Include only relevant terms.
                        - End with a sentence about glossary usefulness.
                        - [bold]Word Limit:[/bold] 250–300 words
                        
                        """ + allContent + "\n" + section.getPlaceholder();

            case "B":
                return """
                        You are writing [bold]Appendix B: Analysis Models[/bold] of an SRS.
                        
                        Instructions:
                        - Begin with an introductory sentence on system models.
                        - Include:
                          • Data flow diagrams
                          • Class diagrams
                          • State diagrams
                        - Briefly explain each model's relevance.
                        - End by stating how models help developers understand architecture.
                        - Use dot bullets.
                        - [bold]Word Limit:[/bold] 150–200 words
                        
                        Additional Instructions: """ + section.getPlaceholder();

            case "C":
                return """
                        You are generating [bold]Appendix C: To Be Determined Items[/bold].
                        
                        Instructions:
                        - Introduce why TBDs are tracked.
                        - Identify open issues using:
                          [TBD-001] Description (Section X.Y)
                          Owner:
                          Target Date:
                        - List TBDs by appearance.
                        - Conclude with why addressing TBDs early is crucial.
                        - [bold]Word Limit:[/bold] 150–250 words
                        
                        """ + document.getAllContent() + "\n" + section.getPlaceholder();

            default:
                return "Write comprehensive content for section " +
                        section.getSectionNumber() + " " + section.getTitle() +
                        " based on this project information:\n" +
                        "Project Name: " + survey.getProjectName() + "\n" +
                        "Overview: " + survey.getOverview() + "\n" +
                        "[bold]Word Limit:[/bold] 150-200 words.\n" +
                        section.getPlaceholder();
        }
    }




    @Transactional
    public SRSDocument generateFullDocument(Long documentId) {
        SRSDocument document = documentRepository.findById(documentId)
                .orElseThrow(() -> new RuntimeException("Document not found"));


        // Generate all sections sequentially

        Srs latestSrs = srsRepository.findTopByOrderByIdDesc();
        String features = "";
        String nonFunction = "";

        String codeClassDiagram = "";
        String codeStateDiagram = "";
        String codeUseCaseDiagram = "";
        String codeSequenceDiagram = "";

        for (DocumentSection section : document.getSections()) {
            if (!section.isGenerated()) {
                if(section.getSectionNumber().equals("B")) {  // if the section is Appendix B
                    // Special handling for Appendix B - Class Diagram
                    try {
                        // the function saves the code in the agile srs not in the traditional.
                        codeClassDiagram = aiClient.generateCodeClassDiagram();
                        codeStateDiagram = aiClient.generateCodeStateDiagram();
                        codeUseCaseDiagram = aiClient.generateCodeUseCaseDiagram();
                        codeSequenceDiagram = aiClient.generateCodeSequenceDiagram();
//                        System.out.print("Generated code class diagram: " + codeClassDiagram);
                 //       System.out.print("Generated code state diagram: " + codeStateDiagram);
//                        System.out.print("Generated code sequence diagram: " + codeSequenceDiagram);
//                        System.out.print("Generated code usecase diagram: " + codeUseCaseDiagram);

                        // send the code to kroki and get the png diagram and save it as byte[] in a new variable called classDiagram
                        byte[] krokiClassDiagram = aiClient.generateTraditionalDiagram(codeClassDiagram);
                        byte[] krokiStateDiagram = aiClient.generateTraditionalDiagram(codeStateDiagram);
                        byte[] krokiUseCaseDiagram = aiClient.generateTraditionalDiagram(codeUseCaseDiagram);
                        byte[] krokiSequenceDiagram = aiClient.generateTraditionalDiagram(codeSequenceDiagram);

                        section.setClassDiagramImage(krokiClassDiagram);
                        section.setGeneratedContent(codeClassDiagram);
                        section.setGenerated(true);

                        section.setStateDiagramImage(krokiStateDiagram);
                        section.setGeneratedContent(codeStateDiagram);
                        section.setGenerated(true);

                        section.setUseCaseDiagramImage(krokiUseCaseDiagram);
                        section.setGeneratedContent(codeUseCaseDiagram);
                        section.setGenerated(true);

                        section.setSequenceDiagramImage(krokiSequenceDiagram);
                        section.setGeneratedContent(codeSequenceDiagram);
                        section.setGenerated(true);

                    } catch (Exception e) {
                        section.setGeneratedContent("Failed to generate a diagram: " + e.getMessage());
                        section.setGenerated(true);
                    }
                }
                else{ // normal section case
                    String prompt = buildPrompt(section, document);
                    String generatedContent = aiClient.generateContent(prompt);
                    section.setGeneratedContent(generatedContent);
                    section.setGenerated(true);

                    // Save Functional Requirements (Section 4)
                    if (section.getSectionNumber().equals("4")) {
                        features = generatedContent;
                        // latestSrs.setGeneratedFunctional(generatedContent);
                        //srsRepository.save(latestSrs);
                    }

                    // Collect Non-Functional Requirements (Sections 5.1 to 5.3)
                    if (section.getSectionNumber().equals("5.1") || section.getSectionNumber().equals("5.2")
                            || section.getSectionNumber().equals("5.3")) {
                        nonFunction += generatedContent;


                    }

                    // Append to the accumulated content
                    if (!section.getSectionNumber().equals("A") && !section.getSectionNumber().equals("B")
                            && !section.getSectionNumber().equals("C")) {
                        String sectionHeader = "=== " + section.getSectionNumber() + " " +
                                section.getTitle() + " ===\n";
                        document.appendGeneratedContent(sectionHeader + generatedContent);
                    }
                }
            }
        }

        latestSrs.setGeneratedFunctional(features);
        latestSrs.setGeneratedNonFunctional(nonFunction);
        latestSrs.setCodeClassDiagram(codeClassDiagram);
        latestSrs.setCodeStateDiagram(codeStateDiagram);
        latestSrs.setCodeUseCaseDiagram(codeUseCaseDiagram);
        latestSrs.setCodeSequenceDiagram(codeSequenceDiagram);
        srsRepository.save(latestSrs);
        document.setUpdatedAt(LocalDateTime.now());
        return documentRepository.save(document);
    }

    public SRSDocument getDocument(Long documentId) {
        return documentRepository.findById(documentId)
                .orElseThrow(() -> new RuntimeException("Document not found"));
    }
}