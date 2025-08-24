package com.pica.srsms.Controller;

import com.pica.srsms.Service.ExcellService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

//@CrossOrigin(origins = "http://localhost:3000")

@RestController
@RequestMapping("Excell")
public class ExcellController {
    @Autowired
    private ExcellService excellService ;

@GetMapping("/getexcell")
    public ResponseEntity<byte[]> getExcellData() throws IOException {
        try {
            byte[] excellbyte = excellService.getExcelFile();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("", "AI_UserStory.xlsx");
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(excellbyte);
        } catch (IOException e) {
            return ResponseEntity.status(500).build();
        }
    }}
