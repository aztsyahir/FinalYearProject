package com.heroku.java.CONTROLLER.Event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;
import java.sql.SQLException;
import java.util.ArrayList;
import com.heroku.java.DAO.Event.EventListDAO;
import com.heroku.java.MODEL.Event;
import com.heroku.java.MODEL.Admin;

@Controller
public class EventListController {
    private final EventListDAO eventListDAO;
    @Autowired
    public EventListController(EventListDAO eventListDAO) {
        this.eventListDAO = eventListDAO;
    }
    //admin event list
    @GetMapping("/AdminEvent")
    public String AdminEvent(@RequestParam(name = "success", required = false) Boolean success, HttpSession session,Model model,Admin admin) {
        int adminid = (int) session.getAttribute("adminid");
        String adminname = (String) session.getAttribute("adminname");
        
        System.out.println("Admin id in session (Admin event): " + adminid);
        System.out.println("Admin name in session (Admin event): " + adminname);

        ArrayList<Event> eventList;
        try {
            eventList = eventListDAO.getEvents();
            model.addAttribute("event", eventList);
            return "Admin/AdminEvent";
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "Signin";
        }
    }


    //admin event list
    @GetMapping("/PlayerEvent")
    public String PlayerEvent(@RequestParam(name = "success", required = false) Boolean success, HttpSession session,Model model,Admin admin) {
        int playerid = (int) session.getAttribute("playerid");
        String playername = (String) session.getAttribute("playername");
        
        System.out.println("Player id in session (Player event): " + playerid);
        System.out.println("Player name in session (Player event): " + playername);

        ArrayList<Event> eventList;
        try {
            eventList = eventListDAO.getEvents();
            model.addAttribute("event", eventList);
            return "Player/PlayerEvent";
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "Signin";
        }
    }
    

}
