package com.pica.srsms.Controller;
import brave.Span;
import brave.Tracer;
import com.pica.srsms.Entity.Srs;
import com.pica.srsms.Entity.Survey;
import com.pica.srsms.Repository.SrsRepository;
import com.pica.srsms.Repository.SurveyRepository;
import com.pica.srsms.Service.SrsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;

import static org.hibernate.query.sqm.tree.SqmNode.log;

//@CrossOrigin(origins = "http://localhost:3000")

@RestController
@RequestMapping("/api/srs")
public class SrsController {

    @Autowired
    private SrsService srsService;
    @Autowired
    SrsRepository srsRepository;
    @Autowired
    private SurveyRepository surveyRepository;



    @GetMapping(value = "/generatesrs")
    public ResponseEntity<byte[]> getAIResponse() {

        try {
            byte[] wordBytes = srsService.getWordFile();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", "Agile_SRS.docx");
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(wordBytes);
        } catch (IOException e) {
            return ResponseEntity.status(500).build();
        }
    }


    @PostMapping(value = "/editfunctional")
    public ResponseEntity<String>editonfunctional(@RequestBody String newcontent){
        srsService.editOnFunctional(newcontent);
        srsService.writetoword(newcontent,"Functional Requirements");
        return ResponseEntity.ok(newcontent);
    }


    @PostMapping(value = "/editnonfunctional")
    public ResponseEntity<String>editonnonfunctional(@RequestBody String newcontent){
        srsService.editOnnonFunctional(newcontent);
        srsService.writetoword(newcontent,"Non Functional Requirements");
        return ResponseEntity.ok(newcontent);
    }


    @PostMapping(value = "/edituserstory")
    public ResponseEntity<String>editonuserstory(@RequestBody String newcontent){
        srsService.editOnuserstory(newcontent);
        srsService.writetoword(newcontent,"User Stories");
        return ResponseEntity.ok(newcontent);
    }


    @PostMapping(value = "/editoverview")
    public ResponseEntity<String>editonoverview(@RequestBody String newcontent){
        srsService.editOnoverview(newcontent);
        srsService.writetoword(newcontent,"Overview");
        return ResponseEntity.ok(newcontent);
    }

//    @PostMapping("/saveGeneratedFunctional")
//    public ResponseEntity<Srs> saveGeneratedFunctional(@RequestBody Srs srs) {
//        try {
//            System.out.println("Saving generated functional content: " + srs.getGeneratedFunctional());
//            Srs savedSrs = srsRepository.save(srs);
//            return ResponseEntity.ok(savedSrs);
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null); // Or return an error message in the body
//        }
//    }

    @PostMapping("/saveGeneratedFunctional")
    public ResponseEntity<Srs> saveGeneratedFunctional(@RequestBody Srs srs) {
        Srs latestSrs;
        try {
            System.out.println("In saveGeneratedFunctional controller method");

            Survey latestSurvey = surveyRepository.findTopByOrderByIdDesc();

            latestSrs = new Srs();
            latestSrs.setSurvey(latestSurvey);
//            System.out.println("Latest SRS found: " + latestSrs);
//            System.out.println(srs.getGeneratedFunctional());
            String test = srs.getGeneratedFunctional();
            latestSrs.setGeneratedFunctional(test);
//            System.out.println("Latest SRS found 2: " + latestSrs.getGeneratedFunctional());
            srsRepository.save(latestSrs);
        } catch (Exception e) {
            System.err.println("Failed to save SRS: " + e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
        return ResponseEntity.ok(latestSrs);
    }

    @PostMapping("/saveGeneratedNonFunctional")
    public ResponseEntity<Srs> saveGeneratedNonFunctional(@RequestBody Srs incoming) {
        // 1) pull out the Survey ID
//        Survey latestSurvey = surveyRepository.findTopByOrderByIdDesc();
//        incoming.setSurvey(latestSurvey);
        ////////////////////
        Srs latestSrs = srsRepository.findTopByOrderByIdDesc();
        String nonFunctional = incoming.getGeneratedNonFunctional();
        latestSrs.setGeneratedNonFunctional(nonFunctional);
        srsRepository.save(latestSrs);
        System.out.println("Latest SRS found 3: " + latestSrs);
        return ResponseEntity.ok(latestSrs);
        ////////////////////
//        if (incoming.getSurvey() == null || incoming.getSurvey().getId() == null) {
//            return ResponseEntity.badRequest().build();
//        }
//        Long surveyId = incoming.getSurvey().getId();

        // 2) find-or-create the SRS record for that survey
//        Srs srs = srsRepository.findBySurveyId(surveyId)
//                .orElseGet(() -> {
//                    // ensure Survey exists
//                    Survey sv = surveyRepository.findById(surveyId)
//                            .orElseThrow(() -> new ResponseStatusException(
//                                    HttpStatus.NOT_FOUND,
//                                    "Survey not found: " + surveyId));
//                    // create new SRS linked to that survey
//                    Srs fresh = new Srs();
//                    fresh.setSurvey(sv);
//                    return fresh;
//                });
//
//        // 3) update the AI-generated field
//        srs.setGeneratedNonFunctional(incoming.getGeneratedNonFunctional());
//
//        // 4) save & return
//        Srs saved = srsRepository.save(srs);
//  return ResponseEntity.ok(saved);
    }

    @PostMapping("/saveGeneratedUserStory")
    public ResponseEntity<Srs> saveGeneratedUserStory(@RequestBody Srs incoming) {
        try {
            System.out.println("In saveGeneratedUserStory controller method");
            //Srs existingSrs = srsRepository.findAll().getLast();
            Srs latestSrs = srsRepository.findTopByOrderByIdDesc();
            String userStory = incoming.getGeneratedUserstory();
            latestSrs.setGeneratedUserstory(userStory);
            srsRepository.save(latestSrs);
            return ResponseEntity.ok(latestSrs);
//            if (existingSrs != null) {
//                System.out.println("Existing SRS found: " + existingSrs);
//                existingSrs.setGeneratedUserstory(srs.getGeneratedUserstory());
//                existingSrs = srsRepository.save(existingSrs);
////                srs = existingSrs;
//                return ResponseEntity.ok(existingSrs);
//            }
//            else{
//                System.out.println("No existing SRS found, creating a new one.");
//                Srs savedSrs = srsRepository.save(srs);
//                return ResponseEntity.ok(savedSrs);
//            }

        } catch (Exception e) {
            System.err.println("Failed to save SRS: " + e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/saveGeneratedOverview")
    public ResponseEntity<Srs> saveGeneratedOverview(@RequestBody Srs incoming) {
        try {
            Srs latestSrs = srsRepository.findTopByOrderByIdDesc();
            String overview = incoming.getOverview();
            latestSrs.setOverview(overview);
            srsRepository.save(latestSrs);
            return ResponseEntity.ok(latestSrs);


//            System.out.println("In saveGeneratedOverview controller method");
//            Srs existingSrs = srsRepository.findAll().getLast();
//            if (existingSrs != null) {
//                System.out.println("Existing SRS found: " + existingSrs);
//                existingSrs.setOverview(srs.getOverview());
//                existingSrs = srsRepository.save(existingSrs);
////                srs = existingSrs;
//                return ResponseEntity.ok(existingSrs);
//            }
//            else{
//                System.out.println("No existing SRS found, creating a new one.");
//                Srs savedSrs = srsRepository.save(srs);
//                return ResponseEntity.ok(savedSrs);
//            }

        } catch (Exception e) {
            System.err.println("Failed to save SRS: " + e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }
    @PostMapping("/saveGeneratedCodeClassDiagram")
    public ResponseEntity<Srs> saveGeneratedCodeClassDiagram(@RequestBody Srs incoming) {
        try {
            Srs latestSrs = srsRepository.findTopByOrderByIdDesc();
            String codeClassDiagram = incoming.getCodeClassDiagram();
            latestSrs.setCodeClassDiagram(codeClassDiagram);
            srsRepository.save(latestSrs);
            return ResponseEntity.ok(latestSrs);
        } catch (Exception e) {
            System.err.println("Failed to save SRS: " + e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/saveGeneratedCodeStateDiagram")
    public ResponseEntity<Srs> saveGeneratedCodeStateDiagram(@RequestBody Srs incoming) {
        try {
            Srs latestSrs = srsRepository.findTopByOrderByIdDesc();
            String codeStateDiagram = incoming.getCodeStateDiagram();
            System.out.println("In save controller method" + codeStateDiagram);
            latestSrs.setCodeStateDiagram(codeStateDiagram);
            srsRepository.save(latestSrs);
            return ResponseEntity.ok(latestSrs);
        } catch (Exception e) {
            System.err.println("Failed to save SRS: " + e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/saveGeneratedCodeUseCaseDiagram")
    public ResponseEntity<Srs> saveGeneratedCodeUseCaseDiagram(@RequestBody Srs incoming) {
        try {
            Srs latestSrs = srsRepository.findTopByOrderByIdDesc();
            String codeUseCaseDiagram = incoming.getCodeUseCaseDiagram();
            latestSrs.setCodeUseCaseDiagram(codeUseCaseDiagram);
            srsRepository.save(latestSrs);
            return ResponseEntity.ok(latestSrs);
        } catch (Exception e) {
            System.err.println("Failed to save SRS: " + e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/saveGeneratedCodeSequenceDiagram")
    public ResponseEntity<Srs> saveGeneratedCodeSequenceDiagram(@RequestBody Srs incoming) {
        try {
            Srs latestSrs = srsRepository.findTopByOrderByIdDesc();
            String codeSequenceDiagram = incoming.getCodeSequenceDiagram();
            latestSrs.setCodeSequenceDiagram(codeSequenceDiagram);
            srsRepository.save(latestSrs);
            return ResponseEntity.ok(latestSrs);
        } catch (Exception e) {
            System.err.println("Failed to save SRS: " + e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/getLatestSrs")
    public Srs getLatestSrs() {
        return srsRepository.findTopByOrderByIdDesc();
    }

    @GetMapping("/generateAgileDiagrams")
    public ResponseEntity<String> generateAgileDiagram() throws IOException {
        try {
            String generationContext = srsService.generateAgileDiagrams();
            srsService.writetoword("Diagrams","Diagrams");
            return ResponseEntity.ok(generationContext);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }


    @GetMapping("/downloadClassDiagram")
    public ResponseEntity<byte[]> downloadClassDiagram() {
        try {
            // Retrieve the latest SRS entity
//            Srs latestSrs = srsRepository.findTopByOrderByIdDesc();
            Srs latestsrs = srsRepository.findAll().getLast();
            // Check if the class diagram image exists
            if (latestsrs.getClassDiagramImage() == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(null); // Return 404 if no image is found
            }

            // Prepare response headers for file download
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_PNG); // Set MIME type for PNG
            headers.setContentDispositionFormData("attachment", "class_diagram.png"); // Force download with filename

            // Return the image bytes
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(latestsrs.getClassDiagramImage());
        } catch (Exception e) {
            // Handle errors
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }






}
