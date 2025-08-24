package com.pica.test_planms.Service;

import com.pica.test_planms.Entity.Srs;
import com.pica.test_planms.Entity.Survey;
import com.pica.test_planms.Entity.TestPlanDocument;
import com.pica.test_planms.Entity.TestPlanDocumentSection;
import com.pica.test_planms.Repository.TestPlanDocumentSectionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TestPlanTemplateService {

    @Autowired
    private final TestPlanDocumentSectionRepository sectionRepository;

    public TestPlanDocument parseTemplate(Survey survey, Srs srs) {
        TestPlanDocument document = new TestPlanDocument();
        document.setSurvey(survey);
        document.setSrs(srs);
        document.setVersion("1.0");
        document.setCreatedAt(LocalDateTime.now());

        // 1. Introduction
        document.getSections().add(createSection("2.1", "Objectives",
                "Outline the main goals of the test plan, such as preparation, responsibilities, and required tools.", document));
        document.getSections().add(createSection("2.2", "Background ",
                "Provide a brief history of the project and the context leading to the development of the system.", document));
        document.getSections().add(createSection("2.3", "Scope",
                "Define what the test plan will cover, including features, interfaces, and system aspects to be tested.", document));
        document.getSections().add(createSection("3.1", "Program modules:",
                "Lists the software components and code modules that will be tested.", document));
        document.getSections().add(createSection("3.2", "Job Control Procedures",
                "Identify the scripts and control procedures used to run application and utility programs.", document));

        // 2. Overall Description
        document.getSections().add(createSection("3.3", "User Procedures",
                "Describe the user-facing processes and interactions that will be tested.", document));
        document.getSections().add(createSection("3.4", "Operator Procedures",
                "Cover the backend or administrative procedures that operators will perform during system operation.", document));
        document.getSections().add(createSection("4", "Features to be Tested",
                "List the key system functionalities that will be tested to ensure they work as intended", document));
        document.getSections().add(createSection("5", "Features Not to Be Tested",
                "List features excluded from testing, typically due to deferred development or limited current usage.", document));
        document.getSections().add(createSection("6.1", "Conversion Testing",
                "Describe the approach for verifying the accuracy of data migration using automated audits and random record comparisons.", document));
        document.getSections().add(createSection("6.2", "Job Stream Testing",
                "Ensure end-to-end payroll processing is validated using test data and standard job execution flows.", document));
        document.getSections().add(createSection("6.3", "Interface Testing",
                "Verify that data exchanged between integrated systems is correctly generated, transferred, and processed.", document));

        // 3. External Interface Requirements
        document.getSections().add(createSection("6.4", "Security Testing",
                "Ensures that unauthorized access to system functions is properly prevented and controlled.", document));
        document.getSections().add(createSection("6.5", "Recovery Testing",
                "Validate the system’s ability to restore operations correctly after unexpected failures or interruptions.", document));
        document.getSections().add(createSection("6.6", "Performance Testing",
                "Measure system response times and efficiency under expected production workloads.", document));
        document.getSections().add(createSection("6.7", "Regression Testing",
                "Check that existing features still work after changes or updates to the system.", document));

        // 4. System Features
        document.getSections().add(createSection("6.8", "Comprehensiveness",
                "Ensure that all system features, user procedures, operator procedures, and job controls are covered by at least one test case.", document));
        // Add more features as needed

        // 5. Other Nonfunctional Requirements
        document.getSections().add(createSection("6.9", "Constraints",
                "States any deadlines or limitations that may affect the testing process, such as time, resources, or critical go-live dates.", document));
        document.getSections().add(createSection("7", "Item Pass/Fail Criteria:",
                "Define the conditions under which a test item is considered to have passed or failed based on system standards and specific requirements.", document));
        document.getSections().add(createSection("8.1", "Suspension Criteria:",
                "Specify the conditions under which testing must be paused.", document));
        document.getSections().add(createSection("8.2", "Resumption Requirements",
                "Define what must happen before testing can continue after a suspension.", document));
        document.getSections().add(createSection("9", "Test Deliverables",
                "List all documents and test data to be produced and submitted after testing is completed.", document));
        // Appendices
        document.getSections().add(createSection("10", "Task List",
                "Outlines the sequence of testing-related activities, their dependencies, required skills, responsible roles, estimated effort, and target completion dates.", document));

        document.getSections().add(createSection("11.1", "Hardware",
                "Specify the computing equipment needed for testing, including terminals and environment setup.", document));
        document.getSections().add(createSection("11.2.1", "Operating System",
                "State that the production OS will be used during testing.", document));
        document.getSections().add(createSection("11.2.2", "Communications Software",
                "Indicate that online program testing will be conducted using test communication software.", document));
        document.getSections().add(createSection("11.3", "Security",
                "Mention that existing security controls will be applied during testing.", document));
        document.getSections().add(createSection("11.4", "Tools",
                "List software tools required for test data generation, result comparison, and database auditing.", document));
        document.getSections().add(createSection("11.5", "Publications",
                "Identify reference documents needed to support the test process.", document));

        document.getSections().add(createSection("12", "Risks and Contingencies",
                "Identify potential issues that could impact testing and outlines backup plans to address them.", document));
        return document;
    }

    private TestPlanDocumentSection createSection(String number, String title,
                                                  String placeholder, TestPlanDocument document) {
        TestPlanDocumentSection section = new TestPlanDocumentSection();
        section.setSectionNumber(number);
        section.setTitle(title);
        section.setPlaceholder(placeholder);
        section.setDocument(document);
        section.setGenerated(false);
        return section;
    }


}
