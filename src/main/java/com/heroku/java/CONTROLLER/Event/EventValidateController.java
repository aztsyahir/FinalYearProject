package com.heroku.java.CONTROLLER.Event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import jakarta.servlet.http.HttpSession;
import com.heroku.java.DAO.Admin.ValidateDAO;
import com.heroku.java.MODEL.EventDetail;
import com.heroku.java.MODEL.Player;
import com.heroku.java.MODEL.Team;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class EventValidateController {
    private final ValidateDAO validateDAO;

    @Autowired
    public EventValidateController(ValidateDAO validateDAO) {
        this.validateDAO = validateDAO;
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
    public String approveRegistration(@RequestParam("playerid") List<Integer> playerids, @RequestParam("edid") int edid,
            HttpSession session, Model model) {

        int adminid = (int) session.getAttribute("adminid");
        String Adminname = (String) session.getAttribute("adminname");

        System.out.println("Admin id in session (Event Validate Individual): " + adminid);
        System.out.println("Admin name in session (Event Validate Individual): " + Adminname);

        try {
            for (int playerid : playerids) {
                validateDAO.approveITopRank(playerid, edid, adminid);
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
    public String approveTeamRegistration(@RequestParam("teamid") List<Integer> teamids, @RequestParam("edid") int edid,
            HttpSession session, Model model) {

        int adminid = (int) session.getAttribute("adminid");
        String Adminname = (String) session.getAttribute("adminname");

        System.out.println("Admin id in session (Event Validate Team): " + adminid);
        System.out.println("Admin name in session (Event Validate Team): " + Adminname);

        try {
            for (int teamid : teamids) {
                validateDAO.approveTTopRank(teamid, edid, adminid);
            }

            return "redirect:/AdminEvent?ValidateSuccess=true";
        } catch (SQLException e) {
            e.printStackTrace();
            return "Signin";
        }
    }
}
