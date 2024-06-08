package com.heroku.java.CONTROLLER.Event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.sql.SQLException;
import java.util.ArrayList;
import com.heroku.java.DAO.Event.EventDetailDAO;
import com.heroku.java.DAO.Event.EventRegisterDAO;
import com.heroku.java.DAO.Player.PlayerProfileDAO;

import com.heroku.java.MODEL.Event;
import com.heroku.java.MODEL.Player;

import jakarta.servlet.http.HttpSession;

@Controller
public class EventDetailController {
    private final EventDetailDAO EventDetailDAO;
    private final EventRegisterDAO EventRegisterDAO;
    private final PlayerProfileDAO PlayerProfileDAO;

    @Autowired
    public EventDetailController(EventDetailDAO eventDetailDAO, EventRegisterDAO eventRegisterDAO,
            PlayerProfileDAO playerProfileDAO) {
        this.EventDetailDAO = eventDetailDAO;
        this.EventRegisterDAO = eventRegisterDAO;
        this.PlayerProfileDAO = playerProfileDAO;
    }

    @GetMapping("/AEventDetail")
    public String EventDetail(@RequestParam(name = "success", required = false) Boolean success, HttpSession session,
            Model model, @RequestParam("eventid") int eventid) {
        int Adminid = (int) session.getAttribute("adminid");
        String Adminname = (String) session.getAttribute("adminname");

        System.out.println("Admin id in session (AEvent Detail): " + Adminid);
        System.out.println("Admin name in session (AEvent Detail): " + Adminname);

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

    @GetMapping("/PEventDetail")
    public String PEventDetail(@RequestParam(name = "success", required = false) Boolean success, HttpSession session,
            Model model, @RequestParam("eventid") int eventid, @RequestParam("edid") int edid) {
        int playerid = (int) session.getAttribute("playerid");
        String playername = (String) session.getAttribute("playername");

        System.out.println("Player id in session (AEvent Detail): " + playerid);
        System.out.println("Player name in session (AEvent Detail): " + playername);

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
