package com.heroku.java.SERVICES;

import java.awt.Color;
import java.io.IOException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.heroku.java.DAO.Event.EventListDAO;
import com.heroku.java.MODEL.Player;
import com.heroku.java.MODEL.Team;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class PDFServices {

    private final EventListDAO eventListDAO;

    @Autowired
    public PDFServices(EventListDAO eventListDAO) {
        this.eventListDAO = eventListDAO;
    }

    public void generatePdfFileIndividual(HttpServletResponse response, int edid)
            throws DocumentException, IOException, SQLException {
        response.setContentType("application/pdf");
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd:HH:mm:ss");
        String currentDateTime = dateFormat.format(new Date());
        String headerkey = "Content-Disposition";
        String headervalue = "attachment; filename=UltimateNav_" + currentDateTime + ".pdf";
        response.setHeader(headerkey, headervalue);

        ArrayList<Player> listParticipant = eventListDAO.getParticipant(edid);

        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());
        document.open();

        // Title and Date
        Font fontTitle = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 24);
        Paragraph title = new Paragraph("UltimateNav: Frisbee Event Discovery", fontTitle);
        title.setAlignment(Paragraph.ALIGN_CENTER);
        document.add(title);

        Font fontDate = FontFactory.getFont(FontFactory.HELVETICA, 12);
        Paragraph date = new Paragraph("Date Created: " + currentDateTime, fontDate);
        date.setAlignment(Paragraph.ALIGN_CENTER);
        document.add(date);

        document.add(new Paragraph("\n")); // Add space between sections

        // Stakeholder Information
        Font fontStakeholder = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);
        Paragraph StakeholderInfoTitle = new Paragraph("Kelab Piring Terbang Cakrawala", fontStakeholder);
        document.add(StakeholderInfoTitle);

        Font fontStakeholderDetails = FontFactory.getFont(FontFactory.HELVETICA, 12);
        Paragraph StakeholderDetails = new Paragraph(

                "21-1-9, Solok Sungai Pinang 6,\n" +
                        "11600 Georgetown,\n" +
                        "Pulau Pinang",
                fontStakeholderDetails);
        document.add(StakeholderDetails);

        document.add(new Paragraph("\n"));

        // Participant Table
        Font fontParticipant = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);
        Paragraph ParticipantTitle = new Paragraph("Approved Participants List", fontParticipant);
        document.add(ParticipantTitle);

        PdfPTable table = new PdfPTable(3);
        table.setWidthPercentage(100);
        table.setSpacingBefore(5);

        // Table Header
        Font fontTableHeader = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
        addTableHeader(table, fontTableHeader);

        // Table Rows
        for (Player player : listParticipant) {
            table.addCell(String.valueOf(player.getPlayerid()));
            table.addCell(player.getPlayername());
            table.addCell(String.valueOf(player.getPlayerstats()));
        }

        document.add(table);
        document.close();

    }

    private void addTableHeader(PdfPTable table, Font font) {
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(new Color(3, 206, 252));
        cell.setPadding(5);
        cell.setPhrase(new Phrase("WHERE ID", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("PLAYER NAME", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("PLAYER STATISTIC", font));
        table.addCell(cell);
    }

    public void generatePdfFileTeam(HttpServletResponse response, int edid)
            throws DocumentException, IOException, SQLException {
        response.setContentType("application/pdf");
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd:HH:mm:ss");
        String currentDateTime = dateFormat.format(new Date());
        String headerkey = "Content-Disposition";
        String headervalue = "attachment; filename=UltimateNav_" + currentDateTime + ".pdf";
        response.setHeader(headerkey, headervalue);

        ArrayList<Team> listTeam = eventListDAO.getTeam(edid);

        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());
        document.open();

        // Title and Date
        Font fontTitle = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 24);
        Paragraph title = new Paragraph("UltimateNav: Frisbee Event Discovery", fontTitle);
        title.setAlignment(Paragraph.ALIGN_CENTER);
        document.add(title);

        Font fontDate = FontFactory.getFont(FontFactory.HELVETICA, 12);
        Paragraph date = new Paragraph("Date Created: " + currentDateTime, fontDate);
        date.setAlignment(Paragraph.ALIGN_CENTER);
        document.add(date);

        document.add(new Paragraph("\n")); // Add space between sections

        // Stakeholder Information
        Font fontStakeholder = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);
        Paragraph StakeholderInfoTitle = new Paragraph("Kelab Piring Terbang Cakrawala", fontStakeholder);
        document.add(StakeholderInfoTitle);

        Font fontStakeholderDetails = FontFactory.getFont(FontFactory.HELVETICA, 12);
        Paragraph StakeholderDetails = new Paragraph(

                "21-1-9, Solok Sungai Pinang 6,\n" +
                        "11600 Georgetown,\n" +
                        "Pulau Pinang",
                fontStakeholderDetails);
        document.add(StakeholderDetails);

        document.add(new Paragraph("\n")); // Add space between sections

        // Team Table
        Font fontTeam = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);
        Paragraph TeamTitle = new Paragraph("Approved Teams List", fontTeam);
        document.add(TeamTitle);

        PdfPTable table = new PdfPTable(3); // Adjust the number of columns as needed
        table.setWidthPercentage(100);
        table.setSpacingBefore(5);

        // Table Header
        Font fontTableHeader = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
        addTableHeaderTeam(table, fontTableHeader);

        // Table Rows
        for (Team team : listTeam) {
            table.addCell(String.valueOf(team.getTeamid()));
            table.addCell(team.getTeamname());
            table.addCell(String.valueOf(team.getTeamstats())); // Placeholder, replace with actual category if
                                                                // available
        }

        document.add(table);
        document.close();

    }

    private void addTableHeaderTeam(PdfPTable table, Font font) {
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(new Color(3, 206, 252));
        cell.setPadding(5);
        cell.setPhrase(new Phrase("TEAM ID", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("TEAM NAME", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("TEAM STATISTIC", font));
        table.addCell(cell);
    }
}
