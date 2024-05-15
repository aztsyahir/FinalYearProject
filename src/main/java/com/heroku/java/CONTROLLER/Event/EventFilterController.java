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

@Controller
public class EventFilterController {
    @Autowired
    private EventFilterDAO eventFilterDAO;

    private EventFilterController(EventFilterDAO eventFilterDAO) {
        this.eventFilterDAO = eventFilterDAO;
    }

    @GetMapping("/SearchEvent")
    public String searchEvent(@RequestParam(name = "searchValue", required = false) String searchValue, Model model) {
        try {
            // Perform the search based on the searchValue
            ArrayList<Event> searchResults = eventFilterDAO.searchEventsByName(searchValue);

            // Add the search results and the searchValue to the model
            model.addAttribute("event", searchResults);
            model.addAttribute("searchValue", searchValue);
            // Return the view name to display the search results
        } catch (SQLException e) {
            e.printStackTrace();
            model.addAttribute("error", "An error occurred during the search: " + e.getMessage());
        }
        return "Player/PlayerEvent?eventfilterSuccess=true";
    }

    @GetMapping("/FilterEvent")
    public String FilterEvent(@RequestParam(name = "edtype", required = false) String EventType,
            @RequestParam(name = "startdate", required = false) String startDateString,
            @RequestParam(name = "enddate", required = false) String endDateString,
            @RequestParam(name = "edstate", required = false) String EventStates,
            @RequestParam(name = "edstats", required = false, defaultValue = "0") String EventStatsString,
            Model model) {
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

            // Perform the search based on the searchValue
            ArrayList<Event> FilterResults = eventFilterDAO.FilterEvent(EventType, startDate, endDate, EventStates,
                    EventStats);
            model.addAttribute("event", FilterResults);
            return "Player/PlayerEvent";
        } catch (SQLException e) {
            // Handle any SQL exceptions
            e.printStackTrace(); // Log the exception or handle it appropriately
            model.addAttribute("error", "An error occurred while filtering events: " + e.getMessage());
            return "Player/PlayerEvent?eventfilterSuccess=true";
        }
    }
}
