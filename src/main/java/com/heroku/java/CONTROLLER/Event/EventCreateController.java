package com.heroku.java.CONTROLLER.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import java.util.Base64;
import java.util.List;

import jakarta.servlet.http.HttpSession;
import com.heroku.java.DAO.Event.EventCreateDAO;
import com.heroku.java.DAO.Player.PlayerEmailDAO;
import com.heroku.java.SERVICES.EmailService;
import com.heroku.java.MODEL.Event;
import com.heroku.java.MODEL.EventDetail;
import com.heroku.java.MODEL.Player;

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

            // Send email notification to all players
            String subject = "New Event Created: " + event.getEventname();
            String message = "A new event has been created. Join now!";
            emailService.sendEmail(playerEmails.toArray(new String[0]), subject, message);

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

}
