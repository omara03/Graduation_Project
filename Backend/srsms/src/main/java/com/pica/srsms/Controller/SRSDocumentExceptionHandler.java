package com.pica.srsms.Controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.IOException;

@ControllerAdvice
public class SRSDocumentExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleDocumentNotFound(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ex.getMessage());
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<String> handleDocumentGeneration(IOException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Failed to generate document: " + ex.getMessage());
    }
}