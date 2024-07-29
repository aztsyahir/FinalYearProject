package com.heroku.java.CONTROLLER.Event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import com.heroku.java.DAO.Event.EventFilterDAO;
import com.heroku.java.MODEL.Event;
import jakarta.servlet.http.HttpSession;

@Controller
public class EventFilterController {
    @Autowired
    private EventFilterDAO eventFilterDAO;

    private EventFilterController(EventFilterDAO eventFilterDAO) {
        this.eventFilterDAO = eventFilterDAO;
    }

    @GetMapping("/FilterEvent")
    public String FilterEvent(@RequestParam(name = "edtype", required = false) String EventType,
            @RequestParam(name = "startdate", required = false) String startDateString,
            @RequestParam(name = "enddate", required = false) String endDateString,
            @RequestParam(name = "edstate", required = false) String EventStates,
            @RequestParam(name = "edstats", required = false, defaultValue = "0") String EventStatsString,
            @RequestParam(name = "searchValue", required = false) String searchValue,
            @RequestParam(name = "success", required = false) Boolean success, HttpSession session,
            Model model) {

        Integer playerid = (Integer) session.getAttribute("playerid");
        String playername = (String) session.getAttribute("playername");

        System.out.println("Player id in session (Player Filter Event): " + playerid);
        System.out.println("Player name in session (Player Filter Event): " + playername);

        if (playerid == null || playername == null) {
            return "Home";
        }
        try {
            // Convert String dates to Date objects
            Date startDate = null;
            Date endDate = null;
            if (startDateString != null && !startDateString.isEmpty()) {
                startDate = Date.valueOf(startDateString);
            }
            if (endDateString != null && !endDateString.isEmpty()) {
                endDate = Date.valueOf(endDateString);
            }

            int EventStats = 0; // Default value
            if (EventStatsString != null && !EventStatsString.isEmpty()) {
                EventStats = Integer.parseInt(EventStatsString);
            }

            ArrayList<Event> FilterResults = eventFilterDAO.FilterEvent(EventType, startDate, endDate, EventStates,
                    EventStats, searchValue);

            model.addAttribute("event", FilterResults);
            model.addAttribute("eventFilterSuccess", true);
            return "Event/PlayerEventFiltered";
        } catch (SQLException e) {
            // Handle any SQL exceptions
            e.printStackTrace(); // Log the exception or handle it appropriately
            model.addAttribute("error", "An error occurred while filtering events: " + e.getMessage());
            return "Signin";
        }
    }

    @GetMapping("/AFilterEvent")
    public String AFilterEvent(@RequestParam(name = "searchValue", required = false) String searchValue,
            @RequestParam(name = "success", required = false) Boolean success, HttpSession session,
            Model model) {

        Integer adminid = (Integer) session.getAttribute("adminid");
        String adminname = (String) session.getAttribute("adminname");

        System.out.println("admin id in session (admin Filter Event): " + adminid);
        System.out.println("admin name in session (admin Filter Event): " + adminname);

        if (adminid == null || adminname == null) {
            return "Home";
        }
        try {

            ArrayList<Event> FilterResults = eventFilterDAO.AFilterEvent(searchValue);

            model.addAttribute("events", FilterResults);
            model.addAttribute("eventFilterSuccess", true);
            return "Event/AEventHistory";
        } catch (SQLException e) {
            // Handle any SQL exceptions
            e.printStackTrace(); // Log the exception or handle it appropriately
            model.addAttribute("error", "An error occurred while filtering events: " + e.getMessage());
            return "Signin";
        }
    }
}
