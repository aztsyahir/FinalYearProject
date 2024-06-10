package com.heroku.java.CONTROLLER.Event;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.heroku.java.DAO.Event.EventRegisterDAO;
import com.heroku.java.DAO.Player.PlayerListDAO;
import com.heroku.java.MODEL.Event;
import com.heroku.java.MODEL.EventDetail;
import com.heroku.java.MODEL.Player;
import com.heroku.java.MODEL.Team;

import jakarta.servlet.http.HttpSession;

@Controller
public class EventRegisterController {
    @Autowired
    private EventRegisterDAO eventRegisterDAO;
    private PlayerListDAO playerListDAO;

    private EventRegisterController(EventRegisterDAO eventRegisterDAO, PlayerListDAO playerListDAO) {
        this.eventRegisterDAO = eventRegisterDAO;
        this.playerListDAO = playerListDAO;
    }

    @PostMapping("/IEventRegister")
    public String IEventRegister(@RequestParam("edid") int edid,
            @RequestParam("playerid") int playerid, Model model) throws SQLException {

        try {
            if (eventRegisterDAO.isPlayerRegistered(edid, playerid)) {
                model.addAttribute("alreadyRegistered", true);
                return "redirect:/PlayerEventCalendar?alreadyRegistered=true";
            }
            eventRegisterDAO.IRegisterEvent(edid, playerid);
            return "redirect:/PlayerEventCalendar?RegisterSuccess=true";
        } catch (Exception e) {
            e.printStackTrace();
            // TODO: handle exception
            return "redirect:/PlayerEventCalendar";
        }
    }

    @PostMapping("/TEventRegister")
    public String TeamEventRegister(@RequestParam("edid") int edid, HttpSession session,
            Model model) {
        try {
            int playerid = (int) session.getAttribute("playerid");
            String playername = (String) session.getAttribute("playername");

            System.out.println("Player ID: " + playerid);
            System.out.println("Player Name: " + playername);
            System.out.println("Event ID: " + edid);

            eventRegisterDAO.TRegisterEvent(edid, playerid);
            Event eventRegisterView = eventRegisterDAO.RegisterEventView(edid, playerid);
            model.addAttribute("event", eventRegisterView);
            return "redirect:/TEventRegister?edid=" + edid;
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            return "SignIn";
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
            return "Event/TEventRegister";
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            return "SignIn";
        }
    }

    @GetMapping("/SearchMember")
    @ResponseBody
    public List<Player> searchPlayers(@RequestParam("query") String query) {
        try {
            return playerListDAO.getPlayerList(query);
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>(); // Return an empty list or handle the error as needed
        }
    }

    @PostMapping("/AddMember")
    public String AddMember(@RequestParam("teamid") int teamid, @RequestParam("playerid") int playerid,
            @RequestParam("edid") int edid) throws SQLException {
        try {
            playerListDAO.AddMember(playerid, teamid);
            return "redirect:/TEventRegister?edid=" + edid;
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            return "redirect:/TEventRegister?edid=" + edid;
        }

    }
}
