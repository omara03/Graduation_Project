package com.pica.aims.Service;


import com.pica.aims.Entity.Srs;
import com.pica.aims.Entity.Survey;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class AIService {

    @Autowired
    RestTemplate restTemplate;
    @Autowired
    QnAService qnAService;
    // @Qualifier("openAiChatClientBuilder")  // For OpenAI model
    private final ChatClient openAiChatClient;
    //    @Qualifier("ollamaChatClientBuilder")  // For Ollama model
    private final ChatClient ollamaChatClient;

    @Autowired
    public AIService(@Qualifier("openAiChatClientBuilder") ChatClient.Builder openAiChatClientBuilder,
                     @Qualifier("ollamaChatClientBuilder") ChatClient.Builder ollamaChatClientBuilder
    ) {
        this.openAiChatClient = openAiChatClientBuilder.build();
        this.ollamaChatClient = ollamaChatClientBuilder.build();
    }


    private Survey getLatestSurveyFromSrsms() {
        try {
            System.out.println("Attempting to fetch surveys from: " + "http://SRSMS/api/surveys/getlast");  // Debug log

//            ResponseEntity<List<Survey>> response = restTemplate.exchange(
//                    "http://SRSMS/api/surveys/getall",
//                    HttpMethod.GET,
//                    null,
//                    new ParameterizedTypeReference<List<Survey>>() {}
//            );
            Survey survey = restTemplate.getForObject("http://SRSMS/api/surveys/getlast", Survey.class);

            if (survey == null) {
                System.err.println("Received null survey");
                return null;
            }

            System.out.println("Successfully fetched survey ID from getLastestSurvey fucntion: " + survey.getId());
            return survey;

        } catch (RestClientException e) {
            System.err.println("Failed to fetch survey: " + e.getMessage());
            return null;
        }
    }


    public String generateFunctional() {

        Survey survey = getLatestSurveyFromSrsms();

        // Handle null survey
        if (survey == null) {
            return "Error: No survey data found. Please ensure surveys exist in srsms.";
        }

        // Ensure coreFunctionalities is not null
        String coreFuncs = survey.getCoreFunctionalities() != null
                ? survey.getCoreFunctionalities()
                : "No core functionalities defined";

        String prompt = "Generate the Functional Requirements section directly, without any introduction or title. " +
                "Do not include headings or phrases like 'Here are the...'. " +
                "Start immediately with the content. " +
                "Use the following bullet formatting style: use ! for top-level bullet points, @ for second-level, # for third-level, $ for fourth-level, and so on if needed (%, ^, *). " +
                "Use bold formatting where appropriate to highlight important terms or actions. " +
                "Include free lines between points or groups for better readability. " +
                "Use this user data as a reference (Optional to use): " + survey.getCoreFunctionalities();
        String aiGeneratedContent;
        if (survey.getModel().equalsIgnoreCase("openai")) {
            System.out.println("Using OpenAI model for generating functional requirements.");
            aiGeneratedContent = openAiChatClient.prompt().user(prompt).call().content();
        } else {
            System.out.println("Using ollama model for generating functional requirements.");
            aiGeneratedContent = ollamaChatClient.prompt().user(prompt).call().content();
        }

        Srs srs = new Srs();
        srs.setGeneratedFunctional(aiGeneratedContent);
        System.out.println(aiGeneratedContent);
        // Send the data to srsms to save it
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Srs> request = new HttpEntity<>(srs, headers);

            // returns to the srsms
            ResponseEntity<Srs> response = restTemplate.postForEntity("http://SRSMS/api/srs/saveGeneratedFunctional", request, Srs.class); // Assuming this endpoint in srsms

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                // returns to the aicontroller that the srsms saved the data and return to display the functional requirements to the  user
                System.out.println("In generateFunctional Ai Service method");
                System.out.println("Saved SRS ID: " + response.getBody().getId());

                return aiGeneratedContent;
            } else {
                return "Error: Failed to save SRS document.";
            }

        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    @CircuitBreaker(name = "aiBreaker", fallbackMethod = "aiBreakerFallback")
    @Retry(name = "aiBreaker", fallbackMethod = "aiBreakerFallback")
    @RateLimiter(name = "aiBreaker", fallbackMethod = "aiBreakerFallback")
    public String generateNonFunctional() {
        Survey survey = getLatestSurveyFromSrsms();
        String prompt = "Generate the Non-Functional Requirements section directly, without any introduction or title. " +
                "Do not include headings or phrases like 'Here are the...'. " +
                "Start immediately with the content. " +
                "Use the following bullet formatting style: use ! for top-level bullet points, @ for second-level, # for third-level, $ for fourth-level, and so on if needed (%, ^, *). " +
                "Use bold formatting where appropriate to highlight important terms or qualities. " +
                "Include free lines between points or groups for better readability. " +
                "Cover the following aspects in the output:\n" +
                "Performance:\n" +
                "Scalability:\n" +
                "Reliability:\n" +
                "Usability:\n" +
                "Security:\n" +
                "Availability:\n" +
                "Maintainability:\n" +
                "Interoperability:\n" +
                "Compatibility:\n" +
                "Data Handling:\n" +
                "Compliance:\n" +
                "Localization:\n" +
                "Use this user data as a reference (Optional to use): " + survey.getNonFunctionalRequirements();


        String aiGeneratedContent;
        if (survey.getModel().equalsIgnoreCase("openai")) {
            System.out.println("Using OpenAI model for generating functional requirements.");
            aiGeneratedContent = openAiChatClient.prompt().user(prompt).call().content();
        } else {
            System.out.println("Using ollama model for generating functional requirements.");
            aiGeneratedContent = ollamaChatClient.prompt().user(prompt).call().content();
        }

        Srs srs = new Srs();
        srs.setId(survey.getId());
        srs.setGeneratedNonFunctional(aiGeneratedContent);

        // Send the data to srsms to save it
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Srs> request = new HttpEntity<>(srs, headers);

            // returns to the srsms
            ResponseEntity<Srs> response = restTemplate.postForEntity("http://SRSMS/api/srs/saveGeneratedNonFunctional", request, Srs.class); // Assuming this endpoint in srsms

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                // returns to the aicontroller that the srsms saved the data and return to display the Nonfunctional requirements to the  user
                System.out.println("In generateNonFunctional Ai Service method");
                System.out.println("Saved SRS ID: " + response.getBody().getId());

                return aiGeneratedContent;
            } else {
                return "Error: Failed to save SRS document.";
            }

        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    public String aiBreakerFallback(Throwable throwable) {
        return "Fallback response: " + (throwable != null ? throwable.getMessage() : "Unknown error");
    }

    public String generateUserStory() {
        Survey survey = getLatestSurveyFromSrsms();

        String prompt = "Generate user stories directly without any introduction or title. " +
                "Do not include phrases like 'Here are the user stories'. " +
                "Each user story should follow this format exactly: As a ____, I want ___, so that ____. " +
                "After each story, include a section titled 'Acceptance Criteria' using the following bullet formatting style: " +
                "use ! for top-level bullet points, @ for second-level, # for third-level, $ for fourth-level, and so on (%, ^, *). " +
                "Use bold formatting to highlight important parts. " +
                "Include free lines between each user story for better readability. " +
                "Use this user data as a reference (Optional to use): " + survey.getUserStory();

        String aiGeneratedContent;
        if (survey.getModel().equalsIgnoreCase("openai")) {
            System.out.println("Using OpenAI model for generating functional requirements.");
            aiGeneratedContent = openAiChatClient.prompt().user(prompt).call().content();
        } else {
            System.out.println("Using ollama model for generating functional requirements.");
            aiGeneratedContent = ollamaChatClient.prompt().user(prompt).call().content();
        }

        Srs srs = new Srs();
        srs.setGeneratedUserstory(aiGeneratedContent);

        // Send the data to srsms to save it
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Srs> request = new HttpEntity<>(srs, headers);

            // returns to the srsms
            ResponseEntity<Srs> response = restTemplate.postForEntity("http://SRSMS/api/srs/saveGeneratedUserStory", request, Srs.class); // Assuming this endpoint in srsms

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                // returns to the aicontroller that the srsms saved the data and return to display the UserStory to the  user
                System.out.println("In generateUserStory Ai Service method");
                System.out.println("Saved SRS ID: " + response.getBody().getId());

                return aiGeneratedContent;
            } else {
                return "Error: Failed to save SRS document.";
            }

        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    public String generateOverview(){
        Survey survey = getLatestSurveyFromSrsms();
        String prompt = "Generate the Overview and Key Features section directly, without any introduction or title. " +
                "Do not include phrases like 'Here is the overview' or 'The following are the key features'. " +
                "Use bold formatting to highlight important elements, terms, or features. " +
                "If listing key features, use the following bullet formatting style: use ! for top-level bullet points, @ for second-level, # for third-level, and continue deeper if needed ($, %, ^, *). " +
                "Separate sections and groups of ideas with free lines for better readability. " +
                "Dont add bullets like • " +
                "Use this user data as a reference (Optional to use): " + survey.getOverview();
        String aiGeneratedContent;
        if (survey.getModel().equalsIgnoreCase("openai")) {
            System.out.println("Using OpenAI model for generating functional requirements.");
            aiGeneratedContent = openAiChatClient.prompt().user(prompt).call().content();
        } else {
            System.out.println("Using ollama model for generating functional requirements.");
            aiGeneratedContent = ollamaChatClient.prompt().user(prompt).call().content();
        }

        Srs srs = new Srs();

        srs.setId(survey.getId());
        srs.setOverview(aiGeneratedContent);

        // Send the data to srsms to save it
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Srs> request = new HttpEntity<>(srs, headers);

            // returns to the srsms
            ResponseEntity<Srs> response = restTemplate.postForEntity("http://SRSMS/api/srs/saveGeneratedOverview", request, Srs.class); // Assuming this endpoint in srsms

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                // returns to the aicontroller that the srsms saved the data and return to display the Overview to the  user
                System.out.println("In generateOverview  Ai Service method");
                System.out.println("Saved SRS ID: " + response.getBody().getId());

                return aiGeneratedContent;
            } else {
                return "Error: Failed to save SRS document.";
            }

        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }

    }

    public String generateContent(String prompt) {
        Survey survey = getLatestSurveyFromSrsms();
        System.out.println("Now in generateContent method of AIService \n Prompt: " + prompt);
        String aiGeneratedContent;

        // replace the true with survey.getModel().equalsIgnoreCase("gemini") to use the Gemini model
        if(true){
            aiGeneratedContent =  qnAService.getAnswer(prompt); // Gemini returns only the code text
        } else if(survey.getModel().equalsIgnoreCase("openai")) {
            aiGeneratedContent = openAiChatClient.prompt().user(prompt).call().content();
        } else {
            aiGeneratedContent = ollamaChatClient.prompt().user(prompt).call().content();
        }
        System.out.println("Now in generateContent method of AIService \n Generated Content: " + aiGeneratedContent);
        return aiGeneratedContent;
    }

    public String generateCodeClassDiagram() {
        Survey survey = getLatestSurveyFromSrsms();
        System.out.println("Now in generateCodeClassDiagram method of AIService \n Survey ID: " + survey.getId());
        String prompt = "Based on the following project description, generate ONLY the PlantUML code for a class diagram.\n" +
                "- Do NOT include any explanations, markdown formatting, triple backticks, or language tags.\n" +
                "- Output must contain only raw PlantUML syntax, starting with @startuml and ending with @enduml.\n\n" +
                "Project Name: " + survey.getProjectName() + "\n" +
                "Core Functionalities: " + survey.getCoreFunctionalities() + "\n" +
                "User Story: " + survey.getUserStory() + "\n" +
                "Overview: " + survey.getOverview() + "\n\n" +
                "Return only the PlantUML code — no formatting, no text, no backticks.";

        String aiGeneratedContent;
        aiGeneratedContent =  qnAService.getAnswer(prompt); // Gemini returns only the code text
//        System.out.println(aiGeneratedContent);
//        if (false) {
//            aiGeneratedContent = openAiChatClient.prompt().user(prompt).call().content();
//        } else {
//            aiGeneratedContent = ollamaChatClient.prompt().user(prompt).call().content();
//        }

        Srs srs = new Srs();

        srs.setId(survey.getId());
        srs.setCodeClassDiagram(aiGeneratedContent);

        // Send the data to srsms to save it
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Srs> request = new HttpEntity<>(srs, headers);

            // returns to the srsms
            ResponseEntity<Srs> response = restTemplate.postForEntity("http://SRSMS/api/srs/saveGeneratedCodeClassDiagram", request, Srs.class); // Assuming this endpoint in srsms

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                // returns to the aicontroller that the srsms saved the data and return to display the CodeClassDiagram to the  user
                System.out.println("In generateCodeClassDiagram Ai Service method");
                System.out.println("Saved SRS ID: " + response.getBody().getId());

                return aiGeneratedContent;
            } else {
                return "Error: Failed to save SRS document.";
            }

        } catch (Exception e) {
            return ("Error: " + e.getMessage());
        }
    }

    public String generateCodeStateDiagram() {
        Survey survey = getLatestSurveyFromSrsms();
        System.out.println("Now in generateCodeStateDiagram method of AIService \n Survey ID: " + survey.getId());
        String prompt = "Based on the following project description, generate ONLY the PlantUML code for a State machine diagram.\n" +
                "- Do NOT include any explanations, markdown formatting, triple backticks, or language tags.\n" +
                "- Output must contain only raw PlantUML syntax, starting with @startuml and ending with @enduml.\n\n" +
                "Project Name: " + survey.getProjectName() + "\n" +
                "Core Functionalities: " + survey.getCoreFunctionalities() + "\n" +
                "User Story: " + survey.getUserStory() + "\n" +
                "Overview: " + survey.getOverview() + "\n\n" +
                "Return only the PlantUML code — no formatting, no text, no backticks.";

        String aiGeneratedContent;
        aiGeneratedContent =  qnAService.getAnswer(prompt); // Gemini returns only the code text

        System.out.println("Generated code state diagram output" + aiGeneratedContent);
//        if (false) {
//            aiGeneratedContent = openAiChatClient.prompt().user(prompt).call().content();
//        } else {
//            aiGeneratedContent = ollamaChatClient.prompt().user(prompt).call().content();
//        }

        Srs srs = new Srs();

        srs.setId(survey.getId());
        srs.setCodeStateDiagram(aiGeneratedContent);

        // Send the data to srsms to save it
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Srs> request = new HttpEntity<>(srs, headers);

            // returns to the srsms
            ResponseEntity<Srs> response = restTemplate.postForEntity("http://SRSMS/api/srs/saveGeneratedCodeStateDiagram", request, Srs.class); // Assuming this endpoint in srsms

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                // returns to the aicontroller that the srsms saved the data and return to display the CodeClassDiagram to the  user
                System.out.println("In generateCodeStateDiagram Ai Service method");
                System.out.println("Saved SRS ID: " + response.getBody().getId());

                return aiGeneratedContent;
            } else {
                return "Error: Failed to save SRS document.";
            }

        } catch (Exception e) {
            return ("Error: " + e.getMessage());
        }
    }

    public String generateCodeUseCaseDiagram() {
        Survey survey = getLatestSurveyFromSrsms();
        System.out.println("Now in generateCodeUseCaseDiagram method of AIService \n Survey ID: " + survey.getId());
        String prompt = "Based on the following project description, generate ONLY the PlantUML code for a UseCase diagram.\n" +
                "- Do NOT include any explanations, markdown formatting, triple backticks, or language tags.\n" +
                "- Output must contain only raw PlantUML syntax, starting with @startuml and ending with @enduml.\n\n" +
                "Project Name: " + survey.getProjectName() + "\n" +
                "Core Functionalities: " + survey.getCoreFunctionalities() + "\n" +
                "User Story: " + survey.getUserStory() + "\n" +
                "Overview: " + survey.getOverview() + "\n\n" +
                "Return only the PlantUML code — no formatting, no text, no backticks.";

        String aiGeneratedContent;
        aiGeneratedContent =  qnAService.getAnswer(prompt); // Gemini returns only the code text
//        System.out.println(aiGeneratedContent);
//        if (false) {
//            aiGeneratedContent = openAiChatClient.prompt().user(prompt).call().content();
//        } else {
//            aiGeneratedContent = ollamaChatClient.prompt().user(prompt).call().content();
//        }

        Srs srs = new Srs();

        srs.setId(survey.getId());
        srs.setCodeUseCaseDiagram(aiGeneratedContent);

        // Send the data to srsms to save it
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Srs> request = new HttpEntity<>(srs, headers);

            // returns to the srsms
            ResponseEntity<Srs> response = restTemplate.postForEntity("http://SRSMS/api/srs/saveGeneratedCodeUseCaseDiagram", request, Srs.class); // Assuming this endpoint in srsms

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                // returns to the aicontroller that the srsms saved the data and return to display the CodeClassDiagram to the  user
                System.out.println("In generateCodeUseCaseDiagram Ai Service method");
                System.out.println("Saved SRS ID: " + response.getBody().getId());

                return aiGeneratedContent;
            } else {
                return "Error: Failed to save SRS document.";
            }

        } catch (Exception e) {
            return ("Error: " + e.getMessage());
        }
    }

    public String generateCodeSequenceDiagram() {
        Survey survey = getLatestSurveyFromSrsms();
        System.out.println("Now in generateCodeSequenceDiagram method of AIService \n Survey ID: " + survey.getId());
        String prompt = "Based on the following project description, generate ONLY the PlantUML code for a Sequence diagram.\n" +
                "- Do NOT include any explanations, markdown formatting, triple backticks, or language tags.\n" +
                "- Output must contain only raw PlantUML syntax, starting with @startuml and ending with @enduml.\n\n" +
                "Project Name: " + survey.getProjectName() + "\n" +
                "Core Functionalities: " + survey.getCoreFunctionalities() + "\n" +
                "User Story: " + survey.getUserStory() + "\n" +
                "Overview: " + survey.getOverview() + "\n\n" +
                "Return only the PlantUML code — no formatting, no text, no backticks.";

        String aiGeneratedContent;
        aiGeneratedContent =  qnAService.getAnswer(prompt); // Gemini returns only the code text
//        System.out.println(aiGeneratedContent);
//        if (false) {
//            aiGeneratedContent = openAiChatClient.prompt().user(prompt).call().content();
//        } else {
//            aiGeneratedContent = ollamaChatClient.prompt().user(prompt).call().content();
//        }

        Srs srs = new Srs();

        srs.setId(survey.getId());
        srs.setCodeSequenceDiagram(aiGeneratedContent);

        // Send the data to srsms to save it
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Srs> request = new HttpEntity<>(srs, headers);

            // returns to the srsms
            ResponseEntity<Srs> response = restTemplate.postForEntity("http://SRSMS/api/srs/saveGeneratedCodeSequenceDiagram", request, Srs.class); // Assuming this endpoint in srsms

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                // returns to the aicontroller that the srsms saved the data and return to display the CodeClassDiagram to the  user
                System.out.println("In generateCodeSequenceDiagram Ai Service method");
                System.out.println("Saved SRS ID: " + response.getBody().getId());

                return aiGeneratedContent;
            } else {
                return "Error: Failed to save SRS document.";
            }

        } catch (Exception e) {
            return ("Error: " + e.getMessage());
        }
    }
}
