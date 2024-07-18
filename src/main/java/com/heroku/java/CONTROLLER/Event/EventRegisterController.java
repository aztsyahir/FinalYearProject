package com.heroku.java.CONTROLLER.Event;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.sql.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.io.IOException;


import com.heroku.java.DAO.Event.EventRegisterDAO;
import com.heroku.java.DAO.Player.PlayerEmailDAO;
import com.heroku.java.DAO.Player.PlayerListDAO;
import com.heroku.java.MODEL.Event;
import com.heroku.java.MODEL.EventDetail;
import com.heroku.java.MODEL.Player;
import com.heroku.java.MODEL.Team;
import com.heroku.java.SERVICES.EmailService;

import jakarta.servlet.http.HttpSession;

@Controller
public class EventRegisterController {
    @Autowired
    private EventRegisterDAO eventRegisterDAO;
    private PlayerListDAO playerListDAO;
    private PlayerEmailDAO playerEmailDAO;
    private final EmailService emailService;

    private EventRegisterController(EventRegisterDAO eventRegisterDAO, PlayerListDAO playerListDAO, PlayerEmailDAO playerEmailDAO, EmailService emailService) {
        this.eventRegisterDAO = eventRegisterDAO;
        this.playerListDAO = playerListDAO;
        this.playerEmailDAO = playerEmailDAO;
        this.emailService = emailService;
    }

    @PostMapping("/IEventRegister")
    public String IEventRegister(@RequestParam("edid") int edid,
            @RequestParam("playerid") int playerid, Model model) throws SQLException {
                System.out.println("edid registereed: " + edid);

        try {
            if (eventRegisterDAO.isPlayerRegistered(edid, playerid)) {
                model.addAttribute("alreadyRegistered", true);
                return "redirect:/PlayerEventCalendar?alreadyRegistered=true";
            }
            int playerStats = eventRegisterDAO.getPlayerStats(playerid); 
            int minStats = eventRegisterDAO.getEventMinStats(edid);
    
            if (playerStats < minStats) {
                model.addAttribute("statsTooLow", true);
                return "redirect:/PlayerEventCalendar?statsTooLow=true";
            }

            eventRegisterDAO.IRegisterEvent(edid, playerid);
            return "redirect:/PlayerEventCalendar?RegisterSuccess=true";
        } catch (Exception e) {
            e.printStackTrace();
            return "Signin";
        }
    }

    @GetMapping("/TEventRegister")
    public String TEventRegister(@RequestParam("edid") int edid, HttpSession session,
            Model model) {
        try {
            int playerid = (int) session.getAttribute("playerid");
            String playername = (String) session.getAttribute("playername");

            System.out.println("Player ID: " + playerid);
            System.out.println("Player Name: " + playername);
            System.out.println("Event ID: " + edid);

            Team team = eventRegisterDAO.TRegisterEvent(edid, playerid);
            model.addAttribute("teamid", team.getTeamid());

            System.out.println("Team ID: " + team.getTeamid());

            Event eventRegisterView = eventRegisterDAO.RegisterEventView(edid, playerid);
            model.addAttribute("event", eventRegisterView);
            model.addAttribute("edid", edid);
            model.addAttribute("edstats", eventRegisterView.getEventDetail().getEdstats());
            model.addAttribute("eventDetail", eventRegisterView.getEventDetail());
            return "Event/TEventRegister";
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            return "Signin";
        }
    }

    @GetMapping("/CancelTRegistration")
    public String CancelTRegistration(@RequestParam("teamid") int teamid, HttpSession session,
            Model model) {
        try {
            int playerid = (int) session.getAttribute("playerid");
            String playername = (String) session.getAttribute("playername");

            eventRegisterDAO.CancelTRegistration(teamid);
            return "redirect:/PlayerEventCalendar";
        } catch (Exception e) {
            e.printStackTrace();
            return "Signin";
        }
    }

    @PostMapping("/RegisterTeamMember")
    public String registerTeam(@ModelAttribute("team") Team team, @ModelAttribute("eventDetail") EventDetail ed, @ModelAttribute("event") Event event, @RequestParam("playerIds") List<Integer> playerIds, @RequestParam("edid") int edid,
            Model model) {
        try {
            // Save the team details
            eventRegisterDAO.updateTeamRegister(team);

            // Add each player to the team
            for (Integer playerId : playerIds) {
                eventRegisterDAO.addmember(playerId, team.getTeamid(), team);
            }

            ArrayList<String> memberEmails = playerEmailDAO.getMemberEmail(edid);
            // Send individualized email to each player
            for (String memberEmail : memberEmails) {
                String subject = "You are on the Team : " + team.getTeamname();
                String htmlContent = buildHtmlContent(event, ed);
                emailService.sendHtmlEmail(memberEmail, subject, htmlContent);
            }

            return "redirect:/PlayerEventCalendar?RegisterSuccess=true";
        } catch (SQLException e) {
            e.printStackTrace();
            return "redirect:/Signin";
        }
    }

    @GetMapping("/SearchMember")
    @ResponseBody
    public List<Player> searchPlayers(@RequestParam("query") String query, @RequestParam("edid") int edid) {
        try {
            return playerListDAO.getPlayerList(query, edid);
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>(); // Return an empty list or handle the error as needed
        }
    }

    @GetMapping("/isPlayerRegistered")
    @ResponseBody
    public Map<String, Boolean> isPlayerRegistered(@RequestParam("edid") int edid,
            @RequestParam("playerid") int playerid) {
        Map<String, Boolean> response = new HashMap<>();
        try {
            boolean isRegistered = eventRegisterDAO.isMemberRegistered(edid, playerid);
            response.put("isRegistered", isRegistered);
        } catch (SQLException e) {
            e.printStackTrace();
            response.put("isRegistered", false);
        }
        return response;
    }

    private String buildHtmlContent(Event event, EventDetail ed) {
        StringBuilder message = new StringBuilder();
        message.append("<h2>Event Details</h2>");
        message.append("<p><strong>Event Name:</strong> ").append(event.getEventname()).append("</p>");
        message.append("<p><strong>Event Type:</strong> ").append(ed.getEdtype()).append("</p>");
        message.append("<p><strong>Event Date:</strong> ").append(ed.getEddate()).append("</p>");

        return message.toString();
    }
}
