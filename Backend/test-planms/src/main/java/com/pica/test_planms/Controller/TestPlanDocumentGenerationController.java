package com.pica.test_planms.Controller;

import com.pica.test_planms.Entity.TestPlanDocument;
import com.pica.test_planms.Service.TestPlanCompilationService;
import com.pica.test_planms.Service.TestPlanGenerationService;
import com.pica.test_planms.Service.TestPlanService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/testdocument")
public class TestPlanDocumentGenerationController {
    @Autowired
    TestPlanService testPlanService;
    @Autowired
    private TestPlanGenerationService testPlanGenerationService;
    @Autowired
    private TestPlanCompilationService testPlanCompilationService;

    @GetMapping("/generate-all")
    public ResponseEntity<TestPlanDocument> generatefulltestplan(){
        Long latestSurveyId = testPlanService.latestSurveyId();
        TestPlanDocument testPlanDocument = testPlanGenerationService.generatefulltestplandocument(latestSurveyId);
        return ResponseEntity.ok(testPlanDocument);
    }

    @Transactional // Keep transaction open for serialization
    @GetMapping("/download")
    public ResponseEntity<byte[]> downloadDocument() {
        try {
            Long documentId = testPlanService.latestSurveyId();  // it should be the last document's id but the last survey id is also the last document id.
            TestPlanDocument testPlanDocument = testPlanGenerationService.getDocument(documentId);
            byte[] docx = testPlanCompilationService.generateDocx(testPlanDocument);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION,
//                            "attachment; filename=\"SOFTWARE TEST PLAN_" + testPlanDocument.getSurvey().getProjectName() + ".docx\"")
                            "attachment; filename=\"SOFTWARE TEST PLAN.docx\"")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(docx);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
