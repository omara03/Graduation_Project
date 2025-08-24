package com.pica.srsms.Service;

import com.pica.srsms.Entity.Srs;
import com.pica.srsms.Repository.SrsRepository;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Service
public class ExcellService {
    private SrsRepository srsRepository;
    Workbook wb = new HSSFWorkbook();

    @Autowired
    public ExcellService(SrsRepository srsRepository) {
        this.srsRepository = srsRepository;
        this.wb = new HSSFWorkbook();
    }


    public String exportuserstory() {
        List<Srs> srsList = srsRepository.findAll();
        if (!srsList.isEmpty()) {
            Srs srs = srsList.get(3);
            String[] userStories = srs.getGeneratedUserstory().split("\\n");
            Sheet sheet = wb.createSheet("User Story");

            Row headerRow = sheet.createRow(0);
            Cell headerCell1 = headerRow.createCell(0);
            headerCell1.setCellValue("Description");
            Cell headerCell2 = headerRow.createCell(1);
            headerCell2.setCellValue("Acceptance Criteria");

            int rowNum = 1; // Start from the second row after headers

            for (String userStory : userStories) {
                // Split each user story into description and acceptance criteria
                String[] parts = userStory.split("\\|"); // Split by '|'
                String description = parts.length > 0 ? parts[0].trim() : "";
                String acceptanceCriteria = parts.length > 1 ? parts[1].trim() : "";

                Row dataRow = sheet.createRow(rowNum++);
                Cell cell1 = dataRow.createCell(0);
                cell1.setCellValue(description);
                Cell cell2 = dataRow.createCell(1);
                cell2.setCellValue(acceptanceCriteria);
            }

            return("done");

        } else {

            return("SRS not found!");
        }
    }

    public byte[] getExcelFile() throws IOException {
        exportuserstory();

        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            wb.write(out);
            return out.toByteArray();
        }
    }
}
