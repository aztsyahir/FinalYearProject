package com.heroku.java.CONTROLLER;

import java.io.IOException;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.heroku.java.SERVICES.PDFServices;
import com.lowagie.text.DocumentException;

import jakarta.servlet.http.HttpServletResponse;

@Controller
public class PDFController {

    private final PDFServices PDFServices;

    @Autowired
    public PDFController(PDFServices PDFServices) {
        this.PDFServices = PDFServices;
    }

    @PostMapping("/export-to-pdf-participant")
    public void generatePdfFile(HttpServletResponse response, @RequestParam("edid") int edid)
            throws DocumentException, IOException, SQLException {
        PDFServices.generatePdfFileIndividual(response, edid);
    }

    @PostMapping("/export-to-pdf-team")
    public void generatePdfFileTeam(HttpServletResponse response, @RequestParam("edid") int edid)
            throws DocumentException, IOException, SQLException {
        PDFServices.generatePdfFileTeam(response, edid);
    }
}
