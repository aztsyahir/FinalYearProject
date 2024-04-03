package com.heroku.java.CONTROLLER.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import java.util.Base64;

import jakarta.servlet.http.HttpSession;
import com.heroku.java.DAO.Event.EventCreateDAO;
import com.heroku.java.MODEL.Event;

import java.sql.SQLException;

@Controller
public class EventCreateController {
    private final EventCreateDAO eventCreateDAO;

     @Autowired
    public EventCreateController(EventCreateDAO eventCreateDAO) {
        this.eventCreateDAO = eventCreateDAO;
    }
    @GetMapping("/EventCreate")
    public String AdminEvent() {
        return "Event/EventCreate";
    }

    @PostMapping("/EventCreate")
    public String EventCreate(Event event, Model model) {
        try {
            MultipartFile eventimgs = event.getEventimgs();
            event.setEventimgbytes(eventimgs.getBytes());
            eventCreateDAO.EventCreate(event);

            return "redirect:/EventCreate?CreateEventSuccess=true";
        } catch (Exception e) {
            // TODO: handle exception
            return "redirect:/EventCreate?CreateEventError=true";
        }
    }

}
