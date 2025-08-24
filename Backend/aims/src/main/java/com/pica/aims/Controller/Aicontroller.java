package com.pica.aims.Controller;


import com.pica.aims.Service.AIService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:3000")

@RestController
@RequestMapping("/api/Ai/generate")
public class Aicontroller {

    @Autowired
    private AIService aiService;


    @GetMapping(value = "/Functional")
    public ResponseEntity<String> generateFunctional()
    {
        try {
            String content=aiService.generateFunctional();
            return ResponseEntity.ok(content);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @GetMapping(value = "/NonFunctional")
    public ResponseEntity<String> generateNonFunctional()
    {
        String content=aiService.generateNonFunctional();
        return ResponseEntity.ok(content);
    }

    @GetMapping(value = "/userstories")
    public ResponseEntity<String> generateUserStory()
    {
        String content=aiService.generateUserStory();
        return ResponseEntity.ok(content);
    }

    @GetMapping(value = "/overview")
    public ResponseEntity<String> generateOverview()
    {
        String content=aiService.generateOverview();
        return ResponseEntity.ok(content);
    }

    @PostMapping
    public ResponseEntity<Map<String,String>> generateContent(@RequestBody String prompt) {
        try {
            String generatedContent = aiService.generateContent(prompt);
            return ResponseEntity.ok(Map.of("content", generatedContent));
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "error", e.getMessage()
                    ));
        }
    }

    @GetMapping(value = "/generateCodeClassDiagram")
    public ResponseEntity<String> generateCodeClassDiagram() {
        String content = aiService.generateCodeClassDiagram();
        return ResponseEntity.ok(content);
    }

    @GetMapping(value = "/generateCodeStateDiagram")
    public ResponseEntity<String> generateCodeStateDiagram() {
        String content = aiService.generateCodeStateDiagram();
        return ResponseEntity.ok(content);
    }

    @GetMapping(value = "/generateCodeUseCaseDiagram")
    public ResponseEntity<String> generateCodeUseCaseDiagram() {
        String content = aiService.generateCodeUseCaseDiagram();
        return ResponseEntity.ok(content);
    }

    @GetMapping(value = "/generateCodeSequenceDiagram")
    public ResponseEntity<String> generateCodeSequenceDiagram() {
        String content = aiService.generateCodeSequenceDiagram();
        return ResponseEntity.ok(content);
    }

}

