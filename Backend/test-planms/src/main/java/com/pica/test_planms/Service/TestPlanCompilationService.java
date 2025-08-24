package com.pica.test_planms.Service;

import com.pica.test_planms.Entity.TestPlanDocument;
import com.pica.test_planms.Entity.TestPlanDocumentSection;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.*;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTAbstractNum;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLvl;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STJc;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STNumberFormat;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class TestPlanCompilationService {
    public byte[] generateDocx(TestPlanDocument document) throws IOException {
        XWPFDocument docx = new XWPFDocument();

        // Add title page
        addTitlePage(docx, document);

        // Add table of contents
        addTableOfContents(docx, document);

        // Add all sections
        for (TestPlanDocumentSection section : document.getSections()) {
                WordFormatter.addFormattedParagraphs(docx, section.getGeneratedContent());
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        docx.write(out);
        docx.close();

        return out.toByteArray();
    }

    private void addTitlePage(XWPFDocument docx, TestPlanDocument document) {
        XWPFParagraph title = docx.createParagraph();
        title.setAlignment(ParagraphAlignment.CENTER);
        XWPFRun titleRun = title.createRun();
        titleRun.setText("SOFTWARE TEST PLAN");
        titleRun.setBold(true);
        titleRun.setFontSize(18);
        titleRun.addBreak();

        XWPFParagraph project = docx.createParagraph();
        project.setAlignment(ParagraphAlignment.CENTER);
        XWPFRun projectRun = project.createRun();
        projectRun.setText("For: " + document.getSurvey().getProjectName());
        projectRun.setFontSize(14);
        projectRun.addBreak();

        XWPFParagraph version = docx.createParagraph();
        version.setAlignment(ParagraphAlignment.CENTER);
        XWPFRun versionRun = version.createRun();
        versionRun.setText("Version: " + document.getVersion());
        versionRun.setFontSize(12);
        versionRun.addBreak();
    }

    private void addTableOfContents(XWPFDocument docx, TestPlanDocument document) {
        XWPFParagraph toc = docx.createParagraph();
        toc.setAlignment(ParagraphAlignment.LEFT);
        XWPFRun tocRun = toc.createRun();
        tocRun.setText("TABLE OF CONTENTS");
        tocRun.setBold(true);
        tocRun.setFontSize(14);
        tocRun.addBreak();

        for (TestPlanDocumentSection section : document.getSections()) {
            XWPFParagraph item = docx.createParagraph();
            XWPFRun itemRun = item.createRun();
            itemRun.setText(section.getSectionNumber() + " " + section.getTitle());
            itemRun.addTab();
            // Add page number reference would go here
            itemRun.addBreak();
        }
    }

    public class WordFormatter {

        public static void addFormattedParagraphs(XWPFDocument doc, String content) {
            String[] lines = content.split("\\r?\\n");

            XWPFNumbering numbering = doc.createNumbering();
            BigInteger bulletNumID = numbering.addNum(numbering.addAbstractNum(createBulletAbstractNum()));

            for (String line : lines) {
                line = line.trim();
                if (line.isEmpty()) {
                    doc.createParagraph(); // Blank line
                    continue;
                }

                XWPFParagraph para;

                // Section heading (e.g., "1.2 Document Conventions" or "Appendix A: Glossary")
                if (line.matches("^(\\d+(\\.\\d+)*|[A-Ca-c])\\s+.+") || line.startsWith("## ")) {
                    String text = line.replaceFirst("^##\\s*", "").trim();
                    para = doc.createParagraph();
                    para.setStyle("Heading2");
                    writeStyledText(para, text, true, 14);  // forceBold = true, fontSize = 14
                    continue;
                }

                // Bullet list (e.g., "* Item" or "• Item" or "- Item")
                if (line.matches("^[*•-]\\s+.+")) {
                    String text = line.replaceFirst("^[*•-]\\s+", "");
                    para = doc.createParagraph();
                    para.setNumID(bulletNumID);
                    writeStyledText(para, text, false, 11);
                    continue;
                }

                // Regular paragraph
                para = doc.createParagraph();
                writeStyledText(para, line, false, 11);
            }
        }

        // Write styled text using multiple supported markers
        private static void writeStyledText(XWPFParagraph para, String text, boolean forceBold, int fontSize) {
            Pattern pattern = Pattern.compile(
                    "\\[bold\\](.+?)\\[/bold\\]" +
                            "|\\[underline\\](.+?)\\[/underline\\]" +
                            "|<u>(.+?)</u>" +                           // HTML underline
                            "|\\[italic\\](.+?)\\[/italic\\]" +
                            "|\\*\\*(.+?)\\*\\*" +                      // Markdown bold
                            "|_([^_]+)_"                                // Markdown italic
            );

            Matcher matcher = pattern.matcher(text);
            int lastEnd = 0;

            while (matcher.find()) {
                // Text before match
                if (matcher.start() > lastEnd) {
                    XWPFRun plainRun = para.createRun();
                    plainRun.setText(text.substring(lastEnd, matcher.start()));
                    plainRun.setFontSize(fontSize);
                    if (forceBold) plainRun.setBold(true);
                }

                XWPFRun styledRun = para.createRun();
                styledRun.setFontSize(fontSize);

                if (matcher.group(1) != null || matcher.group(5) != null) { // [bold] or ** **
                    styledRun.setBold(true);
                    styledRun.setText(matcher.group(1) != null ? matcher.group(1) : matcher.group(5));
                } else if (matcher.group(2) != null || matcher.group(3) != null) { // [underline] or <u>
                    styledRun.setUnderline(UnderlinePatterns.SINGLE);
                    styledRun.setText(matcher.group(2) != null ? matcher.group(2) : matcher.group(3));
                } else if (matcher.group(4) != null || matcher.group(6) != null) { // [italic] or _ _
                    styledRun.setItalic(true);
                    styledRun.setText(matcher.group(4) != null ? matcher.group(4) : matcher.group(6));
                }

                lastEnd = matcher.end();
            }

            // Remaining text after last match
            if (lastEnd < text.length()) {
                XWPFRun finalRun = para.createRun();
                finalRun.setText(text.substring(lastEnd));
                finalRun.setFontSize(fontSize);
                if (forceBold) finalRun.setBold(true);
            }
        }

        private static XWPFAbstractNum createBulletAbstractNum() {
            CTAbstractNum abstractNum = CTAbstractNum.Factory.newInstance();
            abstractNum.setAbstractNumId(BigInteger.ZERO);

            CTLvl level = abstractNum.addNewLvl();
            level.setIlvl(BigInteger.ZERO);
            level.addNewNumFmt().setVal(STNumberFormat.BULLET);
            level.addNewLvlText().setVal("•");
            level.addNewLvlJc().setVal(STJc.LEFT);
            level.addNewPPr().addNewInd().setLeft(BigInteger.valueOf(720));

            return new XWPFAbstractNum(abstractNum);
        }
    }
}
