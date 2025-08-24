package com.pica.srsms.Service;

import com.pica.srsms.Entity.Srs;
import com.pica.srsms.Repository.SrsRepository;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.Document;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

@Service
public class SrsService {
    @Autowired
    private SrsRepository srsRepository;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private AIClientService aiClient;


    private XWPFDocument document = new XWPFDocument();

    public String editOnFunctional(String newContent) {
        Srs srs = srsRepository.findAll().getLast();
        if (srs != null) {
            srs.setGeneratedFunctional(newContent);
            // Save the updated record back to the database
            srs = srsRepository.save(srs);
            return srs.getGeneratedFunctional(); // Return the updated content
        } else {
            return "SRS not found!";
        }
    }

    public String editOnnonFunctional(String newContent) {
        Srs srs = srsRepository.findAll().getLast();
        if (srs != null) {
            srs.setGeneratedNonFunctional(newContent);
            // Save the updated record back to the database
            srs = srsRepository.save(srs);
            return srs.getGeneratedNonFunctional(); // Return the updated content
        } else {
            return "SRS not found!";
        }
    }

    public String editOnoverview(String newContent) {
        Srs srs = srsRepository.findAll().getLast();
        if (srs != null) {
            srs.setOverview(newContent);
            // Save the updated record back to the database
            srs = srsRepository.save(srs);
            return srs.getOverview(); // Return the updated content
        } else {
            return "SRS not found!";
        }
    }

    public String editOnuserstory(String newContent) {
        Srs srs = srsRepository.findAll().getLast();
        if (srs != null) {
            srs.setGeneratedUserstory(newContent);
            // Save the updated record back to the database
            srs = srsRepository.save(srs);
            return srs.getGeneratedUserstory(); // Return the updated content
        } else {
            return "SRS not found!";
        }
    }

    public void writetoword(String content, String sectionTitle) {
        Srs srs = srsRepository.findAll().getLast();

        // Check if sectionTitle equals "Functional Requirements"
        if ("Overview".equals(sectionTitle)) {
            // Clear the current document by creating a new instance
            document = new XWPFDocument();
            System.out.println("Document cleared for new content.");
        }

        // Check if content equals "Diagram"
        if (Objects.equals(content, "Diagrams")) {
            System.out.println("Adding diagrams to the document1...");
            addDiagramToDocument("Class Diagram:", srs.getClassDiagramImage(), "class_diagram.png");
            System.out.println("Adding diagrams to the document2...");
            addDiagramToDocument("State Diagram:", srs.getStateDiagramImage(), "state_diagram.png");
            addDiagramToDocument("Sequence Diagram:", srs.getSequenceDiagramImage(), "sequence_diagram.png");
            addDiagramToDocument("Use Case Diagram:", srs.getUseCaseDiagramImage(), "use_case_diagram.png");
        } else {
            // Add section title
            XWPFParagraph titleParagraph = document.createParagraph();
            String[] titleParts = sectionTitle.split("\\*\\*");
            boolean bold = false;

            for (String part : titleParts) {
                XWPFRun titleRun = titleParagraph.createRun();
                titleRun.setBold(bold);
                titleRun.setFontSize(20); // Optional: Set font size for the title
                titleRun.setText(part.trim());
                bold = !bold; // Toggle bold state
            }
            titleParagraph.createRun().addBreak();

            // Process content line by line
            String[] lines = content.split("\n");
            for (String line : lines) {
                if (line.trim().isEmpty()) {
                    // Add a blank paragraph for free lines
                    document.createParagraph();
                    continue;
                }

                XWPFParagraph paragraph = document.createParagraph();

                // Handle bullet points based on markers
                String bulletPrefix = "";
                if (line.startsWith("!")) {
                    bulletPrefix = "• ";
                    line = line.substring(1).trim();
                } else if (line.startsWith("@")) {
                    bulletPrefix = "        ◦ ";
                    line = line.substring(1).trim();
                } else if (line.startsWith("#")) {
                    bulletPrefix = "            ▪ ";
                    line = line.substring(1).trim();
                } else if (line.startsWith("$")) {
                    bulletPrefix = "                - ";
                    line = line.substring(1).trim();
                }

                // Handle bold markers
                String[] parts = line.split("\\*\\*");
                boolean boldContent = false;
                boolean firstRun = true; // Track if it's the first run for bullet prefix
                for (String part : parts) {
                    XWPFRun run = paragraph.createRun();
                    run.setFontSize(16);
                    run.setBold(boldContent);
                    run.setText((firstRun ? bulletPrefix : "") + (boldContent ? " " : "") + part.trim() + (boldContent ? " " : ""));
                    boldContent = !boldContent; // Toggle bold state
                    firstRun = false; // Ensure bullet prefix is applied only once
                }
            }
        }

        // Add two breaks at the end
        XWPFParagraph endParagraph = document.createParagraph();
        endParagraph.createRun().addBreak();
        endParagraph.createRun().addBreak();
    }

    private void addDiagramToDocument(String title, byte[] diagramImage, String fileName) {
        if (diagramImage != null) {
            try (InputStream is = new ByteArrayInputStream(diagramImage)) {
                // Create a paragraph for the diagram title
                XWPFParagraph contentParagraph = document.createParagraph();
                XWPFRun contentRun = contentParagraph.createRun();
                contentRun.setText(title);
                contentRun.addBreak();

                // Add the image to the document
                contentRun.addPicture(is,
                        Document.PICTURE_TYPE_PNG, // Ensure this matches the actual image format
                        fileName,
                        Units.toEMU(400),  // width (400 pixels)
                        Units.toEMU(300)); // height (300 pixels)
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InvalidFormatException e) {
                throw new RuntimeException(e);
            }
        }
        else {
            System.out.println("Diagram image is null, skipping addition to document.");
        }
    }

    public byte[] getWordFile() throws IOException {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            document.write(out);
            return out.toByteArray();
        }
    }


    public String generateAgileDiagrams() throws IOException {
        Srs srs = srsRepository.findAll().getLast();




        // the function saves the code in the agile srs not in the traditional.
        String codeClassDiagram = aiClient.generateCodeClassDiagram();
        String codeStateDiagram = aiClient.generateCodeStateDiagram();
        String codeUseCaseDiagram = aiClient.generateCodeUseCaseDiagram();
        String codeSequenceDiagram = aiClient.generateCodeSequenceDiagram();
//                        System.out.print("Generated code class diagram: " + codeClassDiagram);
        System.out.print("Generated code state diagram: " + codeStateDiagram);
//                        System.out.print("Generated code sequence diagram: " + codeSequenceDiagram);
//                        System.out.print("Generated code usecase diagram: " + codeUseCaseDiagram);

        // send the code to kroki and get the png diagram and save it as byte[] in a new variable called classDiagram
        byte[] krokiClassDiagram = aiClient.generateTraditionalDiagram(codeClassDiagram);
        byte[] krokiStateDiagram = aiClient.generateTraditionalDiagram(codeStateDiagram);
        byte[] krokiUseCaseDiagram = aiClient.generateTraditionalDiagram(codeUseCaseDiagram);
        byte[] krokiSequenceDiagram = aiClient.generateTraditionalDiagram(codeSequenceDiagram);

        srs.setClassDiagramImage(krokiClassDiagram);
        srs.setCodeClassDiagram(codeClassDiagram);

        srs.setStateDiagramImage(krokiStateDiagram);
        srs.setCodeStateDiagram(codeStateDiagram);

        srs.setUseCaseDiagramImage(krokiUseCaseDiagram);
        srs.setCodeUseCaseDiagram(codeUseCaseDiagram);

        srs.setSequenceDiagramImage(krokiSequenceDiagram);
        srs.setCodeSequenceDiagram(codeSequenceDiagram);

        srsRepository.save(srs);

        return "Diagrams generated and saved successfully!";
    }
}
