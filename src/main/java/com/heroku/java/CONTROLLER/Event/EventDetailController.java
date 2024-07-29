package com.heroku.java.CONTROLLER.Event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.sql.SQLException;
import java.util.ArrayList;
import com.heroku.java.DAO.Event.EventDetailDAO;
import com.heroku.java.DAO.Event.EventRegisterDAO;
import com.heroku.java.DAO.Player.PlayerProfileDAO;
import com.heroku.java.DAO.Event.EventListDAO;

import com.heroku.java.MODEL.Event;
import com.heroku.java.MODEL.Player;
import com.heroku.java.MODEL.Team;

import jakarta.servlet.http.HttpSession;

@Controller
public class EventDetailController {
    private final EventDetailDAO EventDetailDAO;
    private final EventRegisterDAO EventRegisterDAO;
    private final PlayerProfileDAO PlayerProfileDAO;
    private final EventListDAO EventListDAO;

    @Autowired
    public EventDetailController(EventDetailDAO eventDetailDAO, EventRegisterDAO eventRegisterDAO,
            PlayerProfileDAO playerProfileDAO, EventListDAO eventListDAO) {
        this.EventDetailDAO = eventDetailDAO;
        this.EventRegisterDAO = eventRegisterDAO;
        this.PlayerProfileDAO = playerProfileDAO;
        this.EventListDAO = eventListDAO;
    }

    @GetMapping("/AEventDetail")
    public String EventDetail(@RequestParam(name = "success", required = false) Boolean success, HttpSession session,
            Model model, @RequestParam("eventid") int eventid) {
        Integer adminid = (Integer) session.getAttribute("adminid");
        String adminname = (String) session.getAttribute("adminname");

        System.out.println("Admin id in session (Admin event Detail): " + adminid);
        System.out.println("Admin name in session (Admin event Detail): " + adminname);
        if (adminid == null || adminname == null) {
            return "Home";
        }
        System.out.println("eventid AEventDetail: " + eventid);
        try {
            ArrayList<Event> eventDetail = EventDetailDAO.getEventDetail(eventid);
            model.addAttribute("event", eventDetail);

            return "Event/AEventDetail";
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "Event/AEventDetail";
        }
    }

    @GetMapping("/participants")
    @ResponseBody
    public ArrayList<Player> getParticipants(@RequestParam("edid") int edid) throws SQLException {
        System.out.println("edid for participants is " + edid);
        return EventListDAO.getParticipant(edid);
    }

    @GetMapping("/TeamList")
    @ResponseBody
    public ArrayList<Team> getTeam(@RequestParam("edid") int edid) throws SQLException {
        System.out.println("edid for teamList is " + edid);
        return EventListDAO.getTeam(edid);
    }

    public String teamList(@RequestParam("edid") int edid, Model model) {
        try {
            ArrayList<Team> teams = EventListDAO.getTeam(edid); // Assuming you have a TeamDAO with the getTeam method
            model.addAttribute("teams", teams);
            return "Event/TeamList";
        } catch (SQLException e) {
            e.printStackTrace();
            return "error"; // Return an error page or handle the error appropriately
        }
    }

    @GetMapping("/PEventDetail")
    public String PEventDetail(@RequestParam(name = "success", required = false) Boolean success, HttpSession session,
            Model model, @RequestParam("eventid") int eventid, @RequestParam("edid") int edid) {
        Integer playerid = (Integer) session.getAttribute("playerid");
        String playername = (String) session.getAttribute("playername");

        System.out.println("Player id in session (Player event Detail): " + playerid);
        System.out.println("Player name in session (Player event Detail): " + playername);

        if (playerid == null || playername == null) {
            return "Home";
        }

        try {
            ArrayList<Event> eventDetail = EventDetailDAO.getEventDetail(eventid);
            Event ieventRegister = EventRegisterDAO.RegisterEventView(edid, playerid);
            Player player = PlayerProfileDAO.PlayerProfile(playerid);
            model.addAttribute("event", eventDetail);
            model.addAttribute("events", ieventRegister);
            model.addAttribute("player", player);
            return "Event/PEventDetail";
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "Event/PEventDetail";
        }
    }
}
