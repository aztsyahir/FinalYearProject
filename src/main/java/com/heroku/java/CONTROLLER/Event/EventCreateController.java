package com.heroku.java.CONTROLLER.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

import com.heroku.java.DAO.Event.EventCreateDAO;
import com.heroku.java.DAO.Player.PlayerEmailDAO;
import com.heroku.java.SERVICES.EmailService;
import com.heroku.java.MODEL.Event;
import com.heroku.java.MODEL.EventDetail;

import java.io.IOException;
import java.sql.SQLException;

@Controller
public class EventCreateController {
    private final EventCreateDAO eventCreateDAO;
    private final PlayerEmailDAO playerEmailDAO;
    private final EmailService emailService;

     @Autowired
    public EventCreateController(EventCreateDAO eventCreateDAO, PlayerEmailDAO playerEmailDAO, EmailService emailService) {
        this.eventCreateDAO = eventCreateDAO;
        this.playerEmailDAO = playerEmailDAO;
        this.emailService = emailService;
        
    }
    @GetMapping("/EventCreate")
    public String AdminEvent() {
        return "Event/EventCreate";
    }

    @PostMapping("/EventCreate")
    public String EventCreate(Event event, EventDetail ed, @RequestParam("eventimgs")MultipartFile edimgs, Model model) throws IOException { 
        try {
            ed.setEdimgbyte(edimgs.getBytes());
            eventCreateDAO.EventCreate(event,ed);

                   // Fetch all player emails from database (example)
            List<String> playerEmails = playerEmailDAO.getPlayerEmail();

           // Send individualized email to each player
           for (String playerEmail : playerEmails) {
            String subject = "New Event Created: " + event.getEventname();
            String htmlContent = buildHtmlContent(event, ed);
            emailService.sendHtmlEmail(playerEmail, subject, htmlContent);
        }
            return "redirect:/AdminEvent?eventcreateSuccess=true";
        } catch (SQLException sqe) {
            System.out.println("Error Code = " + sqe.getErrorCode());
            System.out.println("SQL state = " + sqe.getSQLState());
            System.out.println("Message = " + sqe.getMessage());
            System.out.println("printTrace /n");
            sqe.printStackTrace();
            return "redirect:/EventCreate?CreateEventError=true";
        }
    }
    private String buildHtmlContent(Event event, EventDetail ed) {
        StringBuilder message = new StringBuilder();
        message.append("<h2>Event Details</h2>");
        message.append("<p><strong>Event Name:</strong> ").append(event.getEventname()).append("</p>");
        message.append("<p><strong>Event Type:</strong> ").append(ed.getEdtype()).append("</p>");
        message.append("<p><strong>Event Date:</strong> ").append(ed.getEddate()).append("</p>");
        message.append("<p><strong>Last Registration Date:</strong> ").append(ed.getEdlastdate()).append("</p>");
        message.append("<p><strong>Minimum Stats Required:</strong> ").append(ed.getEdstats()).append("</p>");
        // message.append("<img src=\"cid:eventImage\">"); // Embed event image (image cid)

        return message.toString();
    }

}
