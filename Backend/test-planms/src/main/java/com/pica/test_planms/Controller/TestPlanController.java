package com.pica.test_planms.Controller;

import com.pica.test_planms.Entity.TestPlanDocument;
import com.pica.test_planms.Service.TestPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

//@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/testplan")
public class TestPlanController {

    @Autowired
    TestPlanService testPlanService;
    @PostMapping("/initialize")
    public ResponseEntity<TestPlanDocument> initializeDocument() {
        return ResponseEntity.ok(testPlanService.initializeDocument());
    }
}
