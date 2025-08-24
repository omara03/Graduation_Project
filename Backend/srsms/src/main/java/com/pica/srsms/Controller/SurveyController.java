package com.pica.srsms.Controller;



import com.pica.srsms.DTO.SurveyDTO;
import com.pica.srsms.Entity.Survey;
import com.pica.srsms.Service.SurveyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.micrometer.tracing.Tracer;
import io.micrometer.tracing.Span;

import java.util.List;

import static org.hibernate.query.sqm.tree.SqmNode.log;

//@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/surveys")
public class SurveyController {

    @Autowired
    private SurveyService surveyService;
//    @Autowired
//    private Tracer tracer;
//    private static final Logger log = LoggerFactory.getLogger(SurveyController.class);
//    public SurveyController(Tracer tracer) {
//        this.tracer = tracer;
//    }


    @PostMapping("/create")
    public ResponseEntity<SurveyDTO> createSurvey(@RequestBody Survey survey)
    {
        Survey savedSurvey = surveyService.createSurvey(survey);
        SurveyDTO response = surveyService.convert(savedSurvey);
        return ResponseEntity.ok(response);
    }

    @GetMapping("delete")
    public ResponseEntity deleteSurvey(@RequestParam("id") Long id)
    {
        surveyService.deleteSurveyById(id);
        return ResponseEntity.ok().build();
    }


    @GetMapping("/{id}")
    public ResponseEntity<Survey> getSurveyById(@PathVariable Long id)
    {
        Survey survey = surveyService.getSurveyById(id);
        return new ResponseEntity<>(survey, HttpStatus.OK);
    }


    @GetMapping("/getall")
    public ResponseEntity<List<Survey>> getAllSurveys()
    {
        System.out.println("Inside getAllSurveys function");
        List<Survey> surveys = surveyService.getAll();
        // 333333333333333333333333333333333333333333333333333333333333333333333333
        System.out.println("Surveys fetched from getAllSurveys function: " + surveys);
        return new ResponseEntity<>(surveys, HttpStatus.OK);
    }

    @GetMapping("/deleteall")
    public ResponseEntity deleteallsurveys()
    {
        surveyService.deleteAllSurveys();
        return ResponseEntity.ok().build();

    }

    @GetMapping("/getlast")
    public ResponseEntity<Survey> getLastSurvey()
    {
        System.out.println("Inside getLastSurvey function");
        Survey lastSurvey = surveyService.getLastSurvey();
        return new ResponseEntity<>(lastSurvey, HttpStatus.OK);
    }

//    @PostMapping("/create")
//    public ResponseEntity<?> createSurvey(@RequestBody Survey survey) {
//        // Create a new span
//        Span span = tracer.nextSpan().name("survey-creation").start();
//
//        try {
//            // Put the span in scope so it's available for tracing
//            try (Tracer.SpanInScope ws = tracer.withSpan(span)) {
//                log.info("Creating survey - TraceId: {}", span.context().traceId());
//                // Your business logic here
//                return ResponseEntity.ok().build();
//            }
//        } finally {
//            // Always end the span
//            span.end();
//        }
//    }
}