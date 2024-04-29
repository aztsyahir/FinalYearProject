package com.heroku.java.CONTROLLER.Event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.sql.SQLException;
import java.util.ArrayList;
import com.heroku.java.DAO.Event.EventDetailDAO;
import com.heroku.java.MODEL.Event;

import jakarta.servlet.http.HttpSession;

@Controller
public class EventDetailController {
     private final EventDetailDAO EventDetailDAO;
     @Autowired
    public EventDetailController(EventDetailDAO eventDetailDAO) {
        this.EventDetailDAO = eventDetailDAO;
    }
    @GetMapping("/AEventDetail")
    public String EventDetail(@RequestParam(name = "success", required = false) Boolean success, HttpSession session,Model model, @RequestParam("eventid") int eventid) {
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
}
