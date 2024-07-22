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
import com.heroku.java.MODEL.Event;
import com.heroku.java.MODEL.EventDetail;
import com.heroku.java.MODEL.Player;
import com.heroku.java.MODEL.Team;
import com.heroku.java.SERVICES.EmailService;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
public class EventValidateController {
    private final ValidateDAO validateDAO;
    private final EmailService emailService;

    @Autowired
    public EventValidateController(ValidateDAO validateDAO, EmailService emailService) {
        this.validateDAO = validateDAO;
        this.emailService = emailService;
    }

    @GetMapping("/EventValidateI")
    public String EventValidate(@RequestParam(name = "success", required = false) Boolean success,
            @RequestParam("edid") int edid, HttpSession session,
            Model model) {
        Integer adminid = (Integer) session.getAttribute("adminid");
        String adminname = (String) session.getAttribute("adminname");

        System.out.println("Admin id in session (Admin event): " + adminid);
        System.out.println("Admin name in session (Admin event): " + adminname);
        if (adminid == null || adminname == null) {
            return "Home";
        }

        try {
            ArrayList<Player> players = validateDAO.getIndividual(edid);
            model.addAttribute("players", players);

            EventDetail eventDetail = validateDAO.getEventDetail(edid);
            model.addAttribute("event", eventDetail);

            int totalPlayersRegistered = validateDAO.getTotalPlayersRegistered(edid);
            model.addAttribute("totalPlayers", totalPlayersRegistered);

            return "Event/EventValidateI";
        } catch (SQLException e) {
            e.printStackTrace();
            return "Signin";
        }
    }

    @PostMapping("/EventValidateI")
    public String approveRegistration(@ModelAttribute EventDetail ed, @ModelAttribute Event event,
            @RequestParam("playerid") List<Integer> playerids, @RequestParam("edid") int edid,
            HttpSession session, Model model) {

        Integer adminid = (Integer) session.getAttribute("adminid");
        String adminname = (String) session.getAttribute("adminname");

        System.out.println("Admin id in session (Individual Event Validation): " + adminid);
        System.out.println("Admin name in session (Individual Event Validation): " + adminname);
        if (adminid == null || adminname == null) {
            return "Home";
        }

        try {
            // ArrayList<Player> allPlayers = validateDAO.getIndividual(edid);
            ed = validateDAO.getEventDetail(edid);

            Set<String> approvedEmails = new HashSet<>();
            Set<String> rejectedEmails = new HashSet<>();

            boolean approveStatus = false;
            for (int playerid : playerids) {
                System.out.println("playerid : " + playerid);
                approveStatus = validateDAO.approveITopRank(playerid, edid, adminid);
                System.out.println("playerid : " + playerid + " | approved status : " + approveStatus);
                if (approveStatus) {
                    System.out.println("Player id : " + playerid + " status true");
                    approvedEmails.addAll(validateDAO.getIndividualEmail(playerid));
                }
            }

            System.out.println("approvedEmails : " + approvedEmails);

            // Send approval emails
            for (String approvedEmail : approvedEmails) {
                System.out.println("email :: " + approvedEmail);
                System.out.println("event :" + ed.getEventname());
                System.out.println("ed type :" + ed.getEdtype());
                System.out.println("ed date :" + ed.getEddate());
                String subject = "Your Registration approved!";
                emailService.sendHtmlEmailWithContentBuild(approvedEmail, subject, ed);
            }

            // Reject teams that are not listed in teamids
            List<Integer> rejectedPlayerIds = validateDAO.rejectPlayer(playerids, edid, adminid);
            for (int rejectedPlayerId : rejectedPlayerIds) {
                rejectedEmails.addAll(validateDAO.getIndividualEmail(rejectedPlayerId));
            }

            // Send rejection emails to those who are not approved
            for (String rejectedEmail : rejectedEmails) {
                if (!approvedEmails.contains(rejectedEmail)) {
                    String subject = "Your Registration Rejected!";
                    emailService.sendHtmlEmailWithContentBuild(rejectedEmail, subject, ed);
                }
            }

            return "redirect:/AdminEvent?ValidateSuccess=true";
        } catch (SQLException e) {
            e.printStackTrace();
            return "Signin";
        }
    }

    @GetMapping("/EventValidateT")
    public String EventValidateT(@RequestParam(name = "success", required = false) Boolean success,
            @RequestParam("edid") int edid, HttpSession session,
            Model model) {
        Integer adminid = (Integer) session.getAttribute("adminid");
        String adminname = (String) session.getAttribute("adminname");

        System.out.println("Admin id in session (Team Event Validation): " + adminid);
        System.out.println("Admin name in session (Team Event Validation): " + adminname);
        if (adminid == null || adminname == null) {
            return "Home";
        }
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
    public String approveTeamRegistration(
            @ModelAttribute EventDetail ed,
            @ModelAttribute Event event,
            @RequestParam("teamid") List<Integer> teamids,
            @RequestParam("edid") int edid,
            @RequestParam("eventid") int eventid,
            HttpSession session,
            Model model) {

        Integer adminid = (Integer) session.getAttribute("adminid");
        String adminname = (String) session.getAttribute("adminname");

        System.out.println("Admin id in session (Admin event): " + adminid);
        System.out.println("Admin name in session (Admin event): " + adminname);
        if (adminid == null || adminname == null) {
            return "Home";
        }

        try {
            // ArrayList<Team> allTeams = validateDAO.getTeam(edid);
            ed = validateDAO.getEventDetail(edid);

            Set<String> approvedEmails = new HashSet<>();
            Set<String> rejectedEmails = new HashSet<>();

            // Approve the teams listed in teamids
            boolean approveStatus = false;
            for (int teamid : teamids) {
                System.out.println("teamid : " + teamid);
                approveStatus = validateDAO.approveTTopRank(teamid, edid, adminid);
                if (approveStatus) {
                    approvedEmails.addAll(validateDAO.getMemberEmailByTeamId(teamid));
                }
            }

            // Send approval emails
            for (String approvedEmail : approvedEmails) {
                System.out.println("email :: " + approvedEmail);
                System.out.println("event :" + ed.getEventname());
                System.out.println("ed type :" + ed.getEdtype());
                System.out.println("ed date :" + ed.getEddate());
                String subject = "Your Registration is Approved!";
                emailService.sendHtmlEmailWithContentBuild(approvedEmail, subject, ed);
            }

            // Reject teams that are not listed in teamids
            List<Integer> rejectedTeamIds = validateDAO.rejectTeam(teamids, edid, adminid);
            for (int rejectedTeamId : rejectedTeamIds) {
                rejectedEmails.addAll(validateDAO.getMemberEmailByTeamId(rejectedTeamId));
            }

            // Send rejection emails to those who are not approved
            for (String rejectedEmail : rejectedEmails) {
                if (!approvedEmails.contains(rejectedEmail)) {
                    String subject = "Your Registration is Rejected!";
                    emailService.sendHtmlEmailWithContentBuild(rejectedEmail, subject, ed);
                }
            }

            return "redirect:/AdminEvent?ValidateSuccess=true";
        } catch (SQLException e) {
            e.printStackTrace();
            return "Signin";
        }
    }
}
