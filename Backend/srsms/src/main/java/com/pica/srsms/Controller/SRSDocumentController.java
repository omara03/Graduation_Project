package com.pica.srsms.Controller;

import com.pica.srsms.Entity.DocumentSection;
import com.pica.srsms.Entity.SRSDocument;
import com.pica.srsms.Entity.Survey;
import com.pica.srsms.Service.DocumentCompilationService;
import com.pica.srsms.Service.SRSGenerationService;
import com.pica.srsms.Service.SurveyService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/srs-documents")
@RequiredArgsConstructor
public class SRSDocumentController {
    @Autowired
    private final SRSGenerationService generationService;
    @Autowired
    private final DocumentCompilationService compilationService;
    @Autowired
    private SurveyService surveyService;

    //POST /api/srs-documents/initialize?surveyId=5 → Uses survey 5
    //POST /api/srs-documents/initialize → Uses the last survey

    @PostMapping("/initialize")
    public ResponseEntity<SRSDocument> initializeDocument(@RequestParam(required = false) Long surveyId) {
        if (surveyId == null) {
            surveyId = surveyService.getLastSurvey().getId();
        }
        return ResponseEntity.ok(generationService.initializeDocument(surveyId));
    }

    @PostMapping("/{documentId}/generate-section")
    public ResponseEntity<DocumentSection> generateSection(
            @PathVariable Long documentId,
            @RequestParam String sectionNumber) throws InterruptedException {
        Thread.sleep(8000);
        return ResponseEntity.ok(
                generationService.generateSection(documentId, sectionNumber));

    }

    @GetMapping("/generate-all")
    public ResponseEntity<SRSDocument> generateFullDocument() {
        Long documentId = surveyService.getLastSurvey().getId();  // it should be the last document's id but the last survey id is also the last document id.
        SRSDocument document = generationService.generateFullDocument(documentId);
        return ResponseEntity.ok(document);
    }

    @Transactional // Keep transaction open for serialization
    @GetMapping("/download")
    public ResponseEntity<byte[]> downloadDocument() {
        try {
            Long documentId = surveyService.getLastSurvey().getId();  // it should be the last document's id but the last survey id is also the last document id.
            SRSDocument document = generationService.getDocument(documentId);
            byte[] docx = compilationService.generateDocx(document);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\"SRS_" + document.getSurvey().getProjectName() + ".docx\"")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(docx);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/{documentId}")
    public ResponseEntity<SRSDocument> getDocument(@PathVariable Long documentId) {
        SRSDocument document = generationService.getDocument(documentId);
        return ResponseEntity.ok(document);
    }
}