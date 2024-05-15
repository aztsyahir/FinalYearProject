package com.heroku.java.CONTROLLER.Event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;
import java.sql.Date;
import java.util.Calendar;
import java.util.stream.Collectors;
import java.sql.SQLException;
import java.text.DateFormatSymbols;
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

    // admin event list
    @GetMapping("/AdminEvent")
    public String AdminEvent(@RequestParam(name = "success", required = false) Boolean success, HttpSession session,
            Model model, Admin admin) {
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

    // admin event list
    @GetMapping("/PlayerEvent")
    public String PlayerEvent(@RequestParam(name = "success", required = false) Boolean success, HttpSession session,
            Model model, Admin admin) {
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

    @GetMapping("/PlayerEventCalendar")
    public String PlayerEventCalendar(@RequestParam(name = "month", required = false) Integer month,
                                      @RequestParam(name = "year", required = false) Integer year,
                                      HttpSession session, Model model, Admin admin) {
        try {
            // Default to current month and year if not provided
            Calendar cal = Calendar.getInstance();
            if (month == null) {
                month = cal.get(Calendar.MONTH) + 1; // Calendar.MONTH is 0-based
            }
            if (year == null) {
                year = cal.get(Calendar.YEAR);
            }
    
            // Set calendar to the first day of the given month
            cal.set(year, month - 1, 1);
    
            int daysInMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
            int firstDayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
    
            ArrayList<ArrayList<String>> rows = new ArrayList<>();
            ArrayList<String> currentRow = new ArrayList<>();
    
            // Fill initial empty days of the first week
            for (int i = 1; i < firstDayOfWeek; i++) {
                currentRow.add("");
            }
    
            // Fill days of the month
            for (int day = 1; day <= daysInMonth; day++) {
                if (currentRow.size() == 7) {
                    rows.add(currentRow);
                    currentRow = new ArrayList<>();
                }
                currentRow.add(String.valueOf(day));
            }
    
            // Fill remaining days of the last week
            while (currentRow.size() < 7) {
                currentRow.add("");
            }
            rows.add(currentRow);
    
            // Fetch events for the specified month and year
            ArrayList<Event> eventList = eventListDAO.getEventsForMonth(month, year);
            for (Event event : eventList) {
                Date eventDate = event.getEventDetail().getEddate();
                cal.setTime(eventDate);
                int eventDay = cal.get(Calendar.DAY_OF_MONTH);
    
                int weekIndex = (eventDay + firstDayOfWeek - 2) / 7;
                int dayIndex = (eventDay + firstDayOfWeek - 2) % 7;
                rows.get(weekIndex).set(dayIndex, event.getEventname());
            }
    
            model.addAttribute("rows", rows);
            model.addAttribute("monthYear", String.format("%1$tB %1$tY", cal));
            return "Event/PlayerEventCalendar";
        } catch (SQLException e) {
            e.printStackTrace();
            return "Signin";
        }
    }
}
