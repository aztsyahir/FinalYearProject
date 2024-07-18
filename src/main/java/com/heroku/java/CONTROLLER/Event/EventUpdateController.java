package com.heroku.java.CONTROLLER.Event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpSession;

import com.heroku.java.DAO.Admin.ValidateDAO;
import com.heroku.java.DAO.Event.EventUpdateDAO;
import com.heroku.java.DAO.Event.EventWithdrawDAO;
import com.heroku.java.DAO.Player.PlayerEmailDAO;
import com.heroku.java.SERVICES.EmailService;

import com.heroku.java.MODEL.Event;
import com.heroku.java.MODEL.EventDetail;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@Controller
public class EventUpdateController {
    private final EventUpdateDAO eventUpdateDAO;
    private final EventWithdrawDAO eventWithdrawDAO;
    private final PlayerEmailDAO playerEmailDAO;
    private final EmailService emailService;
    private final ValidateDAO validateDAO;

    @Autowired
    public EventUpdateController(EventUpdateDAO eventUpdateDAO, EventWithdrawDAO eventWithdrawDAO, PlayerEmailDAO playerEmailDAO, EmailService emailService, ValidateDAO validateDAO) {
        this.eventUpdateDAO = eventUpdateDAO;
        this.eventWithdrawDAO = eventWithdrawDAO;
        this.playerEmailDAO = playerEmailDAO;
        this.emailService = emailService;
        this.validateDAO = validateDAO;
    }

    @GetMapping("/EventUpdate")
    public String EventUpdate(@RequestParam("eventid") int eventid, @RequestParam("edid") int edid,
            @RequestParam(name = "success", required = false) Boolean success, HttpSession session, Model model) {
        int Adminid = (int) session.getAttribute("adminid");
        String Adminname = (String) session.getAttribute("adminname");

        System.out.println("Admin id in session (Event Update): " + Adminid);
        System.out.println("Admin name in session (Event Update): " + Adminname);

        try {
            Event event = eventUpdateDAO.DisplayEvent(edid, eventid);
            model.addAttribute("EventUpdate", event);
            return "Event/EventUpdate";
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            // TODO: handle exception
            return "Signin";
        }
    }

    @PostMapping("/EventUpdate")
    public String EventUpdate(@ModelAttribute("EventUpdate") Event event, @RequestParam("edimgs") MultipartFile edimgs,
            @RequestParam(name = "success", required = false) Boolean success,@RequestParam("eventid") int eventid, @RequestParam("edid") int eventdetailid, HttpSession session, EventDetail ed,
            Model model) throws IOException {
        int Adminid = (int) session.getAttribute("adminid");
        String Adminname = (String) session.getAttribute("adminname");

        System.out.println("Admin id in session (Event Update): " + Adminid);
        System.out.println("Admin name in session (Event Update): " + Adminname);

        System.out.println("Event Detail ID to be updated: " + eventdetailid);
        try {


            if (!edimgs.isEmpty()) {
                // Save the uploaded image to the database
                ed.setEdimgbyte(edimgs.getBytes());
            } else {
                // No new image uploaded, use the existing image data from the database
                EventDetail existingDetail = eventUpdateDAO.EventUpdateimg(eventdetailid);
                if (existingDetail != null) {
                    ed.setEdimgbyte(existingDetail.getEdimgbyte());
                }
            }

            if (event.getEventDetail() == null) {
                event.setEventDetail(new EventDetail());
            }

            eventUpdateDAO.EventUpdate(event, ed,eventdetailid);
            return "redirect:/AEventDetail?eventid=" + eventid + "&UpdateEventSuccess=true";

        } catch (SQLException e) {
            e.printStackTrace();
            // TODO: handle exception
            return "redirect:/error";
        }
    }

    @GetMapping("/CancelEvent")
    public String cancelEvent(@RequestParam("edid") int edid, @RequestParam("eventid") int eventid,
            HttpSession session, Model model, Event event, EventDetail ed) {
        int Adminid = (int) session.getAttribute("adminid");
        String Adminname = (String) session.getAttribute("adminname");

        System.out.println("Admin id in session (Event Cancelation): " + Adminid);
        System.out.println("Admin name in session (Event Cancelation): " + Adminname);

        try {
            eventUpdateDAO.CancelEvent(edid);

            List<String> playerEmails = playerEmailDAO.getPlayerEmail();
            ed = validateDAO.getEventDetail(edid);

            for (String playerEmail : playerEmails) {
                String subject = "Event Has Been Cancelled: " + ed.getEventname();
                System.out.println("Event name: " + ed.getEventname());
                // String htmlContent = buildHtmlContent(event, ed);
                emailService.sendHtmlEmailWithContentBuild(playerEmail, subject, ed);
            }

            return "redirect:/AEventDetail?eventid=" + eventid + "&CancelEventSuccess=true";
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "redirect:/AEventDetail?eventid=" + eventid + "&CancelEventfalse=true";
        }

    }

    @PostMapping("/WithdrawEvent")
    public String withdrawEvent(@RequestParam("edid") int edid, HttpSession session, Model model) {
        int playerid = (int) session.getAttribute("playerid");
        String playername = (String) session.getAttribute("playername");

        System.out.println("Player id in session (Withdraw Event): " + playerid);
        System.out.println("Player name in session (Withdraw Event): " + playername);

        try {
            eventWithdrawDAO.WithdrawEvent(edid);
            return "redirect:/EventHistory?eventid=" + edid + "&WithdrawEventSuccess=true";
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "redirect:/Signin";
        }
    }
}