package com.pica.test_planms.Service;


import com.pica.test_planms.Entity.Srs;
import com.pica.test_planms.Entity.Survey;
import com.pica.test_planms.Entity.TestPlanDocument;
import com.pica.test_planms.Repository.TestPlanDocumentRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class TestPlanService {
    @Autowired
    TestPlanTemplateService testPlanTemplateService;
    @Autowired
    TestPlanDocumentRepository testPlanDocumentRepository;

    @Autowired
    RestTemplate restTemplate;
    @Transactional
    public TestPlanDocument initializeDocument() {
        Survey survey = restTemplate.getForObject("http://SRSMS/api/surveys/getlast", Survey.class);
        System.out.println("surveyId: " + survey.getId());
        Srs srs = restTemplate.getForObject("http://SRSMS/api/srs/getLatestSrs", Srs.class);
        System.out.println("surveyId: " + srs.getId());
        TestPlanDocument document = testPlanTemplateService.parseTemplate(survey, srs);
        return testPlanDocumentRepository.save(document);
    }

    public Long latestSurveyId(){
        Survey survey = restTemplate.getForObject("http://SRSMS/api/surveys/getlast", Survey.class);
        Long surveyId = survey.getId();
        return surveyId;
    }

}
