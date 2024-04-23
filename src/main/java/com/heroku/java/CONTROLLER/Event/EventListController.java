package com.heroku.java.CONTROLLER.Event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

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
    public String AdminEvent(HttpSession session,Model model,Admin admin) {
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

}
