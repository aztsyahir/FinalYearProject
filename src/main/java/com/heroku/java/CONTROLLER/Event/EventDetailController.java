package com.heroku.java.CONTROLLER.Event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

//import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpSession;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Map;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Base64;
import com.heroku.java.DAO.Event.EventDetailDAO;
import com.heroku.java.MODEL.Event;

@Controller
public class EventDetailController {
     private final EventDetailDAO EventDetailDAO;
     @Autowired
    public EventDetailController(EventDetailDAO eventDetailDAO) {
        this.EventDetailDAO = eventDetailDAO;
    }
    @GetMapping("/AEventDetail")
    public String EventDetail(Model model, @RequestParam("eventid") int eventid) {
        
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
