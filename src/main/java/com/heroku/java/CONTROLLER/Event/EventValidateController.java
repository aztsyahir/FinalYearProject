package com.heroku.java.CONTROLLER.Event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import jakarta.servlet.http.HttpSession;
import com.heroku.java.DAO.Admin.ValidateDAO;
import com.heroku.java.DAO.Player.PlayerEmailDAO;
import com.heroku.java.MODEL.Event;
import com.heroku.java.MODEL.EventDetail;
import com.heroku.java.MODEL.Player;
import com.heroku.java.MODEL.Team;
import com.heroku.java.SERVICES.EmailService;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class EventValidateController {
    private final ValidateDAO validateDAO;
    private PlayerEmailDAO playerEmailDAO;
    private final EmailService emailService;

    @Autowired
    public EventValidateController(ValidateDAO validateDAO, PlayerEmailDAO playerEmailDAO, EmailService emailService) {
        this.validateDAO = validateDAO;
        this.playerEmailDAO = playerEmailDAO;
        this.emailService = emailService;
    }

    @GetMapping("/EventValidateI")
    public String EventValidate(@RequestParam(name = "success", required = false) Boolean success,
            @RequestParam("edid") int edid, HttpSession session,
            Model model) {
        int Adminid = (int) session.getAttribute("adminid");
        String Adminname = (String) session.getAttribute("adminname");

        System.out.println("Admin id in session (Event Validate Individual): " + Adminid);
        System.out.println("Admin name in session (Event Validate Individual): " + Adminname);

        try {
            ArrayList<Player> players = validateDAO.getIndividual(edid);
            model.addAttribute("players", players);

            EventDetail eventDetail = validateDAO.getEventDetail(edid); // Adjust this method if necessary
            model.addAttribute("event", eventDetail);

            int totalPlayersRegistered = validateDAO.getTotalPlayersRegistered(edid);
            model.addAttribute("totalPlayers", totalPlayersRegistered);

            return "Event/EventValidateI"; // Assuming this is your Thymeleaf template name
        } catch (SQLException e) {
            e.printStackTrace();
            return "Signin"; // Handle appropriately, redirect to login or error page
        }
    }

    @PostMapping("/EventValidateI")
    public String approveRegistration(@ModelAttribute EventDetail ed, @ModelAttribute Event event, @RequestParam("playerid") List<Integer> playerids, @RequestParam("edid") int edid,
            HttpSession session, Model model) {

        int adminid = (int) session.getAttribute("adminid");
        String Adminname = (String) session.getAttribute("adminname");

        System.out.println("Admin id in session (Event Validate Individual): " + adminid);
        System.out.println("Admin name in session (Event Validate Individual): " + Adminname);

        try {
              // Get all registered player ids for the event
        ArrayList<Player> allPlayers = validateDAO.getIndividual(edid);
        ArrayList<String> registerEmails = playerEmailDAO.getMemberEmail(ed.getEdid());
        
        // Approve the displayed players
        for (int playerid : playerids) {
            validateDAO.approveITopRank(playerid, edid, adminid);
            for (String registerEmail : registerEmails) {
                String subject = "Your Registration approved! ";
                String htmlContent = buildHtmlContent(event, ed);
                emailService.sendHtmlEmail(registerEmail, subject, htmlContent);
        }
    }
        // Reject the players not in the displayed list
        for (Player player : allPlayers) {
            if (!playerids.contains(player.getPlayerid())) {
                validateDAO.rejectPlayer(player.getPlayerid(), edid, adminid);
                for (String registerEmail : registerEmails) {
                    String subject = "Your Registration Rejected! ";
                    String htmlContent = buildHtmlContent(event, ed);
                    emailService.sendHtmlEmail(registerEmail, subject, htmlContent);
            }
            }
        }

            return "redirect:/AdminEvent?ValidateSuccess=true";
        } catch (SQLException e) {
            e.printStackTrace();
            return "Signin"; // Handle appropriately, redirect to error page
        }
}

    @GetMapping("/EventValidateT")
    public String EventValidateT(@RequestParam(name = "success", required = false) Boolean success,
            @RequestParam("edid") int edid, HttpSession session,
            Model model) {
        int Adminid = (int) session.getAttribute("adminid");
        String Adminname = (String) session.getAttribute("adminname");

        System.out.println("Admin id in session (Event Validate Team): " + Adminid);
        System.out.println("Admin name in session (Event Validate Team): " + Adminname);

        try {
            ArrayList<Team> teams = validateDAO.getTeam(edid);
            model.addAttribute("teams", teams);

            EventDetail eventDetail = validateDAO.getEventDetail(edid);
            model.addAttribute("event", eventDetail);

            int totalTeamsRegistered = validateDAO.getTotalTeamsRegistered(edid);
            model.addAttribute("totalTeams", totalTeamsRegistered);

            return "Event/EventValidateT";
        } catch (SQLException e) {
            e.printStackTrace();
            return "Signin";
        }
    }

    @PostMapping("/EventValidateT")
    public String approveTeamRegistration(@ModelAttribute EventDetail ed, @ModelAttribute Event event, @RequestParam("teamid") List<Integer> teamids, @RequestParam("edid") int edid,
            HttpSession session, Model model) {

        int adminid = (int) session.getAttribute("adminid");
        String Adminname = (String) session.getAttribute("adminname");

        System.out.println("Admin id in session (Event Validate Team): " + adminid);
        System.out.println("Admin name in session (Event Validate Team): " + Adminname);

        try {
            // Get all registered team ids for the event
        ArrayList<Team> allTeams = validateDAO.getTeam(edid);
        ArrayList<String> registerEmails = playerEmailDAO.getMemberEmail(edid);

        // Approve the displayed teams
        for (int teamid : teamids) {
            validateDAO.approveTTopRank(teamid, edid, adminid);
            for (String registerEmail : registerEmails) {
                String subject = "Your Registration approved! ";
                String htmlContent = buildHtmlContent(event, ed);
                emailService.sendHtmlEmail(registerEmail, subject, htmlContent);
        }
        }

        // Reject the teams not in the displayed list
        for (Team team : allTeams) {
            if (!teamids.contains(team.getTeamid())) {
                validateDAO.rejectTeam(team.getTeamid(), edid, adminid);
                for (String registerEmail : registerEmails) {
                    String subject = "Your Registration Rejected! ";
                    String htmlContent = buildHtmlContent(event, ed);
                    emailService.sendHtmlEmail(registerEmail, subject, htmlContent);
            }
            }
        }

            return "redirect:/AdminEvent?ValidateSuccess=true";
        } catch (SQLException e) {
            e.printStackTrace();
            return "Signin";
        }
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
