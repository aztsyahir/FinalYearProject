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
import com.heroku.java.DAO.Event.EventUpdateDAO;
import com.heroku.java.DAO.Player.PlayerEmailDAO;
import com.heroku.java.SERVICES.EmailService;

import jakarta.servlet.http.HttpSession;

import com.heroku.java.MODEL.Event;
import com.heroku.java.MODEL.EventDetail;

import java.io.IOException;
import java.sql.SQLException;

@Controller
public class EventCreateController {
    private final EventCreateDAO eventCreateDAO;
    private final EventUpdateDAO eventUpdateDAO;
    private final PlayerEmailDAO playerEmailDAO;
    private final EmailService emailService;

    @Autowired
    public EventCreateController(EventCreateDAO eventCreateDAO, EventUpdateDAO eventUpdateDAO,
            PlayerEmailDAO playerEmailDAO, EmailService emailService) {
        this.eventCreateDAO = eventCreateDAO;
        this.eventUpdateDAO = eventUpdateDAO;
        this.playerEmailDAO = playerEmailDAO;
        this.emailService = emailService;

    }

    @GetMapping("/EventCreate")
    public String AdminEvent(@RequestParam(name = "success", required = false) Boolean success, HttpSession session) {
        Integer adminid = (Integer) session.getAttribute("adminid");
        String adminname = (String) session.getAttribute("adminname");

        System.out.println("Admin id in session (Admin event create): " + adminid);
        System.out.println("Admin name in session (Admin event create): " + adminname);
        if (adminid == null || adminname == null) {
            return "Home";
        }
        return "Event/EventCreate";
    }

    @PostMapping("/EventCreate")
    public String EventCreate(@RequestParam(name = "success", required = false) Boolean success, HttpSession session,
            Event event, EventDetail ed, @RequestParam("eventimgs") MultipartFile edimgs, Model model)
            throws IOException {
        try {
            ed.setEdimgbyte(edimgs.getBytes());
            eventCreateDAO.EventCreate(event, ed);

            List<String> playerEmails = playerEmailDAO.getPlayerEmail();

            for (String playerEmail : playerEmails) {
                String subject = "New Event Announcement: " + event.getEventname();
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
            return "redirect:/Signin";
        }
    }

    @PostMapping("NewEventSet")
    public String NewEventSet(@RequestParam(name = "success", required = false) Boolean success,
            @RequestParam("eventid") int eventid, @RequestParam("eventname") String eventname, HttpSession session,
            Event event, EventDetail ed, Model model)
            throws IOException {
        try {
            EventDetail existingDetail = eventUpdateDAO.EventUpdateimg(eventid);
            if (existingDetail != null) {
                ed.setEdimgbyte(existingDetail.getEdimgbyte());
            }
            event.setEventname(eventname);
            System.out.println("New Event ID set: " + event.getEventid());
            System.out.println("Event Name New: " + event.getEventname());
            eventCreateDAO.NewEventSet(event, ed);
            model.addAttribute("event", event);

            List<String> playerEmails = playerEmailDAO.getPlayerEmail();

            for (String playerEmail : playerEmails) {
                String subject = "New Event Announcement: " + event.getEventname();
                String htmlContent = buildHtmlContent(event, ed);
                emailService.sendHtmlEmail(playerEmail, subject, htmlContent);
            }
            return "redirect:/AEventDetail?eventid=" + eventid + "&NewEventSuccess=true";
        } catch (SQLException sqe) {
            System.out.println("Error Code = " + sqe.getErrorCode());
            System.out.println("SQL state = " + sqe.getSQLState());
            System.out.println("Message = " + sqe.getMessage());
            System.out.println("printTrace /n");
            sqe.printStackTrace();
            return "redirect:/AEventDetail?eventid=" + eventid + "&NewEventfalse=true";
        }
    }

    private String buildHtmlContent(Event event, EventDetail ed) {
        StringBuilder message = new StringBuilder();
        message.append("<p>Dear Flatballer,</p>");
        message.append("<p>We are happy to announce you that new ultimate frisbee event is in our website!</p>");
        message.append("<h2>Event Details</h2>");
        message.append("<p><strong>Event Name:</strong> ").append(event.getEventname()).append("</p>");
        message.append("<p><strong>Event Type:</strong> ").append(ed.getEdtype()).append("</p>");
        message.append("<p><strong>Event Date:</strong> ").append(ed.getEddate()).append("</p>");
        message.append("<p><strong>Last Registration Date:</strong> ").append(ed.getEdlastdate()).append("</p>");
        message.append("<p><strong>Minimum Stats Required(%):</strong> ").append(ed.getEdstats()).append("</p>");
        message.append("<p>For more information of other event, chock out our ULTIMATENAV website!</p>");
        return message.toString();
    }
}
