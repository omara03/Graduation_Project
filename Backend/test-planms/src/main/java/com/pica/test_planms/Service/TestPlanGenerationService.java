package com.pica.test_planms.Service;

import com.netflix.discovery.converters.Auto;
import com.pica.test_planms.Entity.Srs;
import com.pica.test_planms.Entity.Survey;
import com.pica.test_planms.Entity.TestPlanDocument;
import com.pica.test_planms.Entity.TestPlanDocumentSection;
import com.pica.test_planms.Repository.TestPlanDocumentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.text.Document;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TestPlanGenerationService {
    private final AIClientService aiClientService;
    @Autowired
    TestPlanDocumentRepository testPlanDocumentRepository;
    private String buildPrompt(TestPlanDocument testPlanDocument, TestPlanDocumentSection testPlanDocumentSection) {
        Survey survey = testPlanDocument.getSurvey();
        Srs srs = testPlanDocument.getSrs();

        switch (testPlanDocumentSection.getSectionNumber()){
            case "2.1":
                return String.format("""
You are writing the [bold]2.1 Objectives[/bold] section of a Software Test Plan document.

Project Name: %s
Project Overview: %s
Test Scope: %s
Tools & Technologies to be used: %s

Instructions:
- Start with a brief introduction to the purpose of the test plan.
- Clearly outline the primary objectives, including:
  • What the testing aims to achieve
  • Who is responsible for planning, execution, and validation
  • What tools or technologies will be utilized
  • Any overarching principles guiding the testing process
- Use dot-based bullet points (•) to list objectives clearly.
- Use [bold] and [underline] formatting to highlight important keywords.
- End with a brief summary connecting the objectives to successful project delivery.
- Maintain a professional and concise tone.
- [bold]Word Limit:[/bold] 100–150 words.

Additional Instructions: %s
""", survey.getProjectName(), survey.getOverview(), srs.getGeneratedFunctional(), srs.getGeneratedNonFunctional(), testPlanDocumentSection.getPlaceholder()); //tools and tech


            case "2.2":
                return String.format("""
You are writing the [bold]2.2 Background[/bold] section of a Software Test Plan.

Project Name: %s  
Project Overview: %s

Instructions:
- Write a brief history of the project and the context that led to the development of the system.
- Mention any significant milestones or business needs that triggered the project.
- Avoid excessive detail or repetition.
- Write clearly and formally in 70–100 words.
- Use [bold] formatting where necessary for key terms.

Additional Notes: %s
""", survey.getProjectName(), survey.getOverview(), testPlanDocumentSection.getPlaceholder());

            case "2.3":
                return String.format("""
You are writing the [bold]2.3 Scope[/bold] section of a Software Test Plan.

Project Title: %s  
Project Overview: %s  
Functional Requirements: %s  
Non-Functional Requirements: %s  

Instructions:
- Briefly define the boundaries of the testing effort.
- Mention features, interfaces, and system aspects to be tested.
- Focus on what is included in scope, not what is excluded.
- Use dot bullet points (•) where appropriate.
- Write concisely using professional language.

[bold]Word Limit:[/bold] 80–100 words.

Additional Instructions: %s
""",
                        survey.getProjectName(),
                        survey.getOverview(),
                        srs.getGeneratedFunctional(),
                        srs.getGeneratedNonFunctional(),
                        testPlanDocumentSection.getPlaceholder());





            case "3.1":
                return String.format("""
You are writing the [bold]3.1 Program Modules[/bold] section of a Software Test Plan.

Project Title: %s
Project Overview: %s
Functional Requirements: %s

Instructions:
- Start with a brief sentence on the importance of identifying testable components.
- List all major software components or code modules that will undergo testing.
- Mention modules by name and relate them briefly to their corresponding functionality.
- Use bullet points (•) only — no numbering.
- Be clear and concise.

Additional Instructions: %s
""",
                        survey.getProjectName(),
                        survey.getOverview(),
                        srs.getGeneratedFunctional(),
                        testPlanDocumentSection.getPlaceholder()
                );


            case "3.2":
                return String.format("""
You are writing the [bold]3.2 Job Control Procedures[/bold] section of a Software Test Plan.

Project Title: %s
Project Overview: %s
Functional Requirements: %s

Instructions:
- Begin with a short sentence explaining the purpose of job control procedures in software testing.
- Identify the scripts, batch files, or automation tools used to initiate, monitor, or terminate application and utility programs during testing.
- Use bullet points (•) only.
- Keep the language brief, technical, and structured.

Additional Instructions: %s
""",
                        survey.getProjectName(),
                        survey.getOverview(),
                        srs.getGeneratedFunctional(),
                        testPlanDocumentSection.getPlaceholder()
                );


            case "3.3":
                return String.format("""
You are writing the [bold]3.3 User Procedures[/bold] section of a Software Test Plan.

Project Title: %s
Project Overview: %s
Functional Requirements: %s

Instructions:
- Briefly explain the goal of testing user-facing procedures.
- List major user interactions or workflows that must be verified (e.g., login, checkout, profile updates).
- Focus on usability, correctness, and expected outcomes.
- Use dot-based bullet points (•) only.
- Keep the language clear and minimal.

Additional Instructions: %s
""",
                        survey.getProjectName(),
                        survey.getOverview(),
                        srs.getGeneratedFunctional(),
                        testPlanDocumentSection.getPlaceholder()
                );

            case "3.4":
                return String.format("""
You are writing the [bold]3.4 Operator Procedures[/bold] section of a Software Test Plan.

Project Title: %s
Project Overview: %s
Functional Requirements: %s

Instructions:
- Briefly introduce the purpose of operator testing procedures.
- List backend or administrative tasks that system operators must perform (e.g., log monitoring, system resets, configuration updates).
- Focus on reliability and correct system operation under operator control.
- Use only dot-based bullet points (•).
- Use concise and technical language.

Additional Instructions: %s
""",
                        survey.getProjectName(),
                        survey.getOverview(),
                        srs.getGeneratedFunctional(),
                        testPlanDocumentSection.getPlaceholder()
                );


            case "4":
                return String.format("""
You are writing the [bold]4 Features to be Tested[/bold] section of a Software Test Plan.

Project Title: %s
Project Overview: %s
Functional Requirements: %s

Instructions:
- Begin with a short statement describing the intent of this section: to identify system functionalities that will be validated during testing.
- List the key features derived from the functional requirements that must be tested.
- Focus on core functionalities critical to system behavior.
- Use dot-based bullet points (•) only.
- Keep wording concise and specific.
- [bold]Word Limit:[/bold] 80–120 words.

Additional Instructions: %s
""",
                        survey.getProjectName(),
                        survey.getOverview(),
                        srs.getGeneratedFunctional(),
                        testPlanDocumentSection.getPlaceholder()
                );


            case "5":
                return String.format("""
You are writing the [bold]5 Features Not to Be Tested[/bold] section of a Software Test Plan.

Project Title: %s
Project Overview: %s
Functional Requirements: %s

Instructions:
- Start with a short introduction.
- List features or modules excluded from testing at this stage.
- For each, mention the reason (e.g., under development, low priority, or minimal usage).
- Use dot-based bullets (•) only.
- Keep the explanation brief, objective, and clear.
- End with a brief summary statement about the exclusion.

[bold]Word Limit:[/bold] 100–150 words.

Additional Instructions: %s
""",
                        survey.getProjectName(),
                        survey.getOverview(),
                        srs.getGeneratedFunctional(),
                        testPlanDocumentSection.getPlaceholder());


            case "6.1":
                return String.format("""
You are writing the [bold]6.1 Conversion Testing[/bold] section of a Software Test Plan.

Project Title: %s
Project Overview: %s
Functional Requirements: %s

Instructions:
- Start with a short introduction explaining what conversion testing is.
- Outline how data migration will be verified for accuracy and completeness.
- Mention use of automated audits, sampling, or random record checks.
- Include validation steps for source and destination data.
- Use dot-based bullets (•) where helpful.
- Keep the explanation precise and minimal.

[bold]Word Limit:[/bold] 100–150 words.

Additional Instructions: %s
""",
                        survey.getProjectName(),
                        survey.getOverview(),
                        srs.getGeneratedFunctional(),
                        testPlanDocumentSection.getPlaceholder());


            case "6.2":
                return String.format("""
You are writing the [bold]6.2 Job Stream Testing[/bold] section of a Software Test Plan.

Project Title: %s  
Project Overview: %s  
Functional Requirements: %s  

Instructions:
- Begin with a brief explanation of job stream testing and its purpose.
- Describe how the end-to-end payroll or batch job execution will be tested.
- Specify use of test data and standard processing flows.
- Mention validation of job dependencies, success criteria, and output verification.
- Use dot-based bullets (•) where suitable.
- Keep the content concise and technical.

[bold]Word Limit:[/bold] 100–150 words.

Additional Instructions: %s
""",
                        survey.getProjectName(),
                        survey.getOverview(),
                        srs.getGeneratedFunctional(),
                        testPlanDocumentSection.getPlaceholder());


            case "6.3":
                return String.format("""
You are writing the [bold]6.3 Interface Testing[/bold] section of a Software Test Plan.

Project Title: %s  
Project Overview: %s  
Functional Requirements: %s  

Instructions:
- Begin with a short description of interface testing and its importance in integrated systems.
- Explain how data exchanges between external/internal systems will be tested.
- Mention validation of data generation, transmission, reception, and processing accuracy.
- Highlight use of mock systems, stubs, or real-time data flows if applicable.
- Use dot-based bullets (•) when listing strategies or tools.
- Keep the explanation focused and minimal.

[bold]Word Limit:[/bold] 100–150 words.

Additional Instructions: %s
""",
                        survey.getProjectName(),
                        survey.getOverview(),
                        srs.getGeneratedFunctional(),
                        testPlanDocumentSection.getPlaceholder());


            case "6.4":
                return String.format("""
You are writing the [bold]6.4 Security Testing[/bold] section of a Software Test Plan.

Project Title: %s  
Project Overview: %s  
Functional Requirements: %s  
Non-Functional Requirements: %s  

Instructions:
- Begin with a short explanation of the goal of security testing.
- Describe how the system will be tested for unauthorized access prevention and control.
- Mention common methods like role-based access tests, penetration testing, and input validation.
- Ensure testing includes authentication, authorization, session management, and data protection.
- Use dot-based bullets (•) where helpful.
- Keep it concise and focused on key strategies.

[bold]Word Limit:[/bold] 100–150 words.

Additional Instructions: %s
""",
                        survey.getProjectName(),
                        survey.getOverview(),
                        srs.getGeneratedFunctional(),
                        srs.getGeneratedNonFunctional(),
                        testPlanDocumentSection.getPlaceholder());



            case "6.5":
                return String.format("""
You are writing the [bold]6.5 Recovery Testing[/bold] section of a Software Test Plan.

Project Title: %s  
Project Overview: %s  
Functional Requirements: %s  
Non-Functional Requirements: %s  

Instructions:
- Start with a brief definition of recovery testing and its role in assessing fault tolerance.
- Explain how system recovery from crashes, power failures, or unexpected shutdowns will be validated.
- Describe the expected restoration of data, state, or functionality.
- Mention any automation tools or procedures used for simulating failures.
- Use dot-based bullets (•) when appropriate.
- Keep the explanation clear and minimal.

[bold]Word Limit:[/bold] 100–150 words.

Additional Instructions: %s
""",
                        survey.getProjectName(),
                        survey.getOverview(),
                        srs.getGeneratedFunctional(),
                        srs.getGeneratedNonFunctional(),
                        testPlanDocumentSection.getPlaceholder());


            case "6.6":
                return String.format("""
You are writing the [bold]6.6 Performance Testing[/bold] section of a Software Test Plan.

Project Title: %s  
Project Overview: %s  
Functional Requirements: %s  
Non-Functional Requirements: %s  

Instructions:
- Begin with a short statement about performance testing and its purpose.
- Describe how the system’s speed, responsiveness, and resource usage will be tested.
- Include strategies for measuring response time, throughput, and system stability under load.
- Mention tools or automation techniques used.
- Use dot-based bullet points (•) if needed.
- Keep content concise and targeted.

[bold]Word Limit:[/bold] 100–150 words.

Additional Instructions: %s
""",
                        survey.getProjectName(),
                        survey.getOverview(),
                        srs.getGeneratedFunctional(),
                        srs.getGeneratedNonFunctional(),
                        testPlanDocumentSection.getPlaceholder());


            case "6.7":
                return String.format("""
You are writing the [bold]6.7 Regression Testing[/bold] section of a Software Test Plan.

Project Title: %s  
Project Overview: %s  
Functional Requirements: %s  
Non-Functional Requirements: %s  

Instructions:
- Start with a short statement defining regression testing and its importance.
- Describe the process of verifying that existing functionality is unaffected by code changes or updates.
- Mention how often regression tests will be executed and what tools/frameworks will be used.
- Use dot-based bullet points (•) for steps or practices if needed.
- Keep the explanation brief, clear, and structured.

[bold]Word Limit:[/bold] 100–150 words.

Additional Instructions: %s
""",
                        survey.getProjectName(),
                        survey.getOverview(),
                        srs.getGeneratedFunctional(),
                        srs.getGeneratedNonFunctional(),
                        testPlanDocumentSection.getPlaceholder());


            case "6.8":
                return String.format("""
You are writing the [bold]6.8 Comprehensiveness[/bold] section of a Software Test Plan.

Project Title: %s  
Project Overview: %s  
Functional Requirements: %s  

Instructions:
- Begin with a brief statement about the importance of full test coverage.
- Explain how the test plan ensures that every system feature, user operation, and control process is validated.
- Describe how traceability to requirements is maintained.
- Use dot-based bullet points (•) if helpful for listing coverage strategies.
- Keep it concise, formal, and clearly structured.

[bold]Word Limit:[/bold] 100–150 words.

Additional Instructions: %s
""",
                        survey.getProjectName(),
                        survey.getOverview(),
                        srs.getGeneratedFunctional(),
                        testPlanDocumentSection.getPlaceholder());


            case "6.9":
                return String.format("""
You are writing the [bold]6.9 Constraints[/bold] section of a Software Test Plan.

Project Title: %s  
Project Overview: %s  

Instructions:
- Start with a short introduction on why it's important to identify testing constraints.
- Clearly outline any limitations that may affect testing, such as:
  • Time restrictions (e.g., testing deadlines or project timelines)
  • Resource constraints (e.g., team availability, tools, environments)
  • Business-critical dates (e.g., go-live or compliance deadlines)
- Use dot bullets (•) only.
- Keep the tone clear, formal, and concise.

[bold]Word Limit:[/bold] 80–120 words.

Additional Instructions: %s
""",
                        survey.getProjectName(),
                        survey.getOverview(),
                        testPlanDocumentSection.getPlaceholder());


            case "7":
                return String.format("""
You are writing the [bold]7 Item Pass/Fail Criteria[/bold] section of a Software Test Plan.

Project Title: %s  
Functional Requirements: %s  
Non-Functional Requirements: %s  

Instructions:
- Begin with a brief explanation of why defining pass/fail criteria is essential in testing.
- Describe the general conditions that determine if a test item passes or fails, based on:
  • Functional correctness  
  • Performance benchmarks  
  • Usability or compliance standards
- Use dot bullets (•) to list criteria types or examples.
- Keep it formal and concise.

[bold]Word Limit:[/bold] 80–120 words.

Additional Instructions: %s
""",
                        survey.getProjectName(),
                        srs.getGeneratedFunctional(),
                        srs.getGeneratedNonFunctional(),
                        testPlanDocumentSection.getPlaceholder());


            case "8.1":
                return String.format("""
You are writing the [bold]8.1 Suspension Criteria[/bold] section of a Software Test Plan.

Project Title: %s  
Functional Requirements: %s  
Non-Functional Requirements: %s  

Instructions:
- Begin with a short note on the importance of defining when testing should be paused.
- List specific conditions that justify suspension of testing, such as:
  • Critical defects that block further testing  
  • Environmental or system failures  
  • Unavailable components or test data
- Use dot bullets (•) only.
- Keep it concise and professional.

[bold]Word Limit:[/bold] 80–120 words.

Additional Instructions: %s
""",
                        survey.getProjectName(),
                        srs.getGeneratedFunctional(),
                        srs.getGeneratedNonFunctional(),
                        testPlanDocumentSection.getPlaceholder());



            case "8.2":
                return String.format("""
You are writing the [bold]8.2 Resumption Criteria[/bold] section of a Software Test Plan.

Project Title: %s  
Functional Requirements: %s  
Non-Functional Requirements: %s  

Instructions:
- Begin with a brief explanation of when and why testing should resume after a suspension.
- Clearly define what conditions must be met to continue testing, such as:
  • Resolution of critical defects  
  • Restoration of test environment  
  • Availability of required components or data
- Use dot bullets (•) only.
- Keep it short, formal, and focused.

[bold]Word Limit:[/bold] 80–120 words.

Additional Instructions: %s
""",
                        survey.getProjectName(),
                        srs.getGeneratedFunctional(),
                        srs.getGeneratedNonFunctional(),
                        testPlanDocumentSection.getPlaceholder());


            case "9":
                return String.format("""
You are writing the [bold]9 Test Deliverables[/bold] section of a Software Test Plan.

Project Title: %s  
Functional Requirements: %s  
Non-Functional Requirements: %s  

Instructions:
- Begin with a short statement about the purpose of deliverables in testing.
- Then, list key deliverables expected after testing is completed, such as:
  • Test Plan Document  
  • Test Cases  
  • Test Data  
  • Test Summary Report  
  • Bug Reports or Logs  
  • Test Environment Configuration 
- Regarding the test data bullet point, mention the test data that is set to be used at the end of all bullet points 
- Use dot bullets (•) only.
- Keep it clear, concise, and professional.

Additional Instructions: %s
""",
                        survey.getProjectName(),
                        srs.getGeneratedFunctional(),
                        srs.getGeneratedNonFunctional(),
                        testPlanDocumentSection.getPlaceholder());


            case "10":
                return String.format("""
You are writing the [bold]10 Task List[/bold] section of a Software Test Plan.

Project Title: %s  
Functional Requirements: %s  
Non-Functional Requirements: %s  

Instructions:
- Start with a brief line explaining the purpose of a task list in the test plan.
- Then outline testing-related activities with details like:
  • Task description  
  • Sequence and dependencies  
  • Required skills or tools  
  • Responsible role or team  
  • Estimated effort or time  
  • Target completion date  
- Use dot bullets (•) only for listing tasks and avoid numbering.
- Keep the format professional and concise.
- Make sure that the title (10 Task List) is only as a header not a bolded word inside an introductory paragraph of that section
- Between each task list and the other, separate them with a line, to indicate the end of a one and start of the other, and also to be visually appealing

Additional Instructions: %s
""",
                        survey.getProjectName(),
                        srs.getGeneratedFunctional(),
                        srs.getGeneratedNonFunctional(),
                        testPlanDocumentSection.getPlaceholder());



            case "11.1":
                return String.format("""
You are writing the [bold]11.1 Hardware[/bold] section of a Software Test Plan.

Project Title: %s  
Functional Requirements: %s  
Non-Functional Requirements: %s  

Instructions:
- Start with a short line on the role of hardware in supporting testing.
- Then list the computing equipment required for the testing process, including:
  • Client and server machines  
  • Workstations, laptops, or terminals  
  • Hardware configurations and specifications  
  • Specialized devices (if applicable)  
  • Environmental setup needs (e.g., cooling, power, space)  
- Use dot bullets (•) only.
- Keep it technical and concise.

[bold]Word Limit:[/bold] 80–120 words.

Additional Instructions: %s
""",
                        survey.getProjectName(),
                        srs.getGeneratedFunctional(),
                        srs.getGeneratedNonFunctional(),
                        testPlanDocumentSection.getPlaceholder());


            case "11.2.1":
                return String.format("""
You are writing the [bold]11.2.1 Operating System[/bold] section of a Software Test Plan.

Project Title: %s  
Functional Requirements: %s  
Non-Functional Requirements: %s  

Instructions:
- Begin with a one-line statement confirming that the production OS will be used for testing.
- List the specific operating systems and versions that will be used (e.g., Windows 11, Ubuntu 22.04).
- Mention compatibility considerations if any (e.g., OS-specific behavior).
- Use clear, direct language and dot bullets (•) if listing multiple systems.

[bold]Word Limit:[/bold] 60–100 words.

Additional Instructions: %s
""",
                        survey.getProjectName(),
                        srs.getGeneratedFunctional(),
                        srs.getGeneratedNonFunctional(),
                        testPlanDocumentSection.getPlaceholder());



            case "11.2.2":
                return String.format("""
You are writing the [bold]11.2.2 Communications Software[/bold] section of a Software Test Plan.

Project Title: %s  
Functional Requirements: %s  
Non-Functional Requirements: %s  

Instructions:
- Start with a short statement confirming that online program testing will utilize test communication software.
- Mention the communication tools or protocols involved (e.g., sockets, HTTP, messaging queues).
- Specify any test-specific configurations, simulators, or tools used.
- Use concise wording and dot bullets (•) if listing multiple tools or protocols.

[bold]Word Limit:[/bold] 60–100 words.

Additional Instructions: %s
""",
                        survey.getProjectName(),
                        srs.getGeneratedFunctional(),
                        srs.getGeneratedNonFunctional(),
                        testPlanDocumentSection.getPlaceholder());


            case "11.3":
                return String.format("""
You are writing the [bold]11.3 Security[/bold] section of a Software Test Plan.

Project Title: %s  
Functional Requirements: %s  
Non-Functional Requirements: %s  

Instructions:
- Begin with a sentence confirming that existing system security controls will remain active during testing.
- Briefly list the types of controls enforced (e.g., access restrictions, encryption, authentication).
- Highlight any test-specific considerations (e.g., test accounts with limited privileges).
- Use clear technical language and dot bullets (•) if listing multiple controls.

[bold]Word Limit:[/bold] 60–90 words.

Additional Instructions: %s
""",
                        survey.getProjectName(),
                        srs.getGeneratedFunctional(),
                        srs.getGeneratedNonFunctional(),
                        testPlanDocumentSection.getPlaceholder());


            case "11.4":
                return String.format("""
You are writing the [bold]11.4 Tools[/bold] section of a Software Test Plan.

Project Title: %s  
Functional Requirements: %s  
Non-Functional Requirements: %s  

Instructions:
- List essential tools used in testing such as:
  • Test data generators  
  • Result comparison utilities  
  • Database audit tools
- Briefly explain the purpose of each tool.
- Use dot-based bullet points (•) only.
- Keep it concise and relevant to the system under test.

[bold]Word Limit:[/bold] 60–90 words.

Additional Instructions: %s
""",
                        survey.getProjectName(),
                        srs.getGeneratedFunctional(),
                        srs.getGeneratedNonFunctional(),
                        testPlanDocumentSection.getPlaceholder());


            case "11.5":
                return String.format("""
You are writing the [bold]11.5 Publications[/bold] section of a Software Test Plan.

Project Title: %s  
Functional Requirements: %s  
Non-Functional Requirements: %s  

Instructions:
- Identify reference documents that support the testing process, such as:
  • Software Requirements Specification (SRS)  
  • User manuals  
  • Design documents  
  • Technical standards
- Use dot bullets (•) only.
- Keep each entry brief but clear.

[bold]Word Limit:[/bold] 60–90 words.

Additional Instructions: %s
""",
                        survey.getProjectName(),
                        srs.getGeneratedFunctional(),
                        srs.getGeneratedNonFunctional(),
                        testPlanDocumentSection.getPlaceholder());


            case "12":
                return String.format("""
You are writing the [bold]12 Risks and Contingencies[/bold] section of a Software Test Plan.

Project Title: %s  
Project Overview: %s  
Functional Requirements: %s  
Non-Functional Requirements: %s  

Instructions:
- Identify possible risks that could impact testing such as:
  • Delays in development  
  • Resource limitations  
  • Environment instability
- For each risk, provide a short backup or mitigation plan.
- Use bullet points (•) only.
- Write clearly and concisely using structured formatting.

[bold]Word Limit:[/bold] 70–100 words.

Additional Instructions: %s
""",
                        survey.getProjectName(),
                        survey.getOverview(),
                        srs.getGeneratedFunctional(),
                        srs.getGeneratedNonFunctional(),
                        testPlanDocumentSection.getPlaceholder());



            default:
                return "Write comprehensive content for section " +
                        testPlanDocumentSection.getSectionNumber() + " " + testPlanDocumentSection.getTitle() +
                        " based on this project information:\n" +
                        "Project Name: " + survey.getProjectName() + "\n" +
                        "Overview: " + survey.getOverview() + "\n" +
                        "[bold]Word Limit:[/bold] 150-200 words.\n" +
                        testPlanDocumentSection.getPlaceholder();
        }
    }
    @Transactional
    public TestPlanDocument generatefulltestplandocument(Long documentID) {
    TestPlanDocument testPlanDocument = testPlanDocumentRepository.findById(documentID).orElseThrow(()->new RuntimeException("Document not found"));

    for(TestPlanDocumentSection section : testPlanDocument.getSections()) {
        if(!section.isGenerated()){
            String prompt = buildPrompt(testPlanDocument, section);
            String generatedContent = aiClientService.generateContent(prompt);
            section.setGeneratedContent(generatedContent);
            section.setGenerated(true);
        }
    }
    testPlanDocument.setUpdatedAt(LocalDateTime.now());
    return testPlanDocumentRepository.save(testPlanDocument);
    }

    public TestPlanDocument getDocument(Long documentId) {
        return testPlanDocumentRepository.findById(documentId)
                .orElseThrow(() -> new RuntimeException("Document not found"));
    }
}
