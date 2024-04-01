package com.heroku.java.CONTROLLER.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import jakarta.servlet.http.HttpSession;
import com.heroku.java.DAO.Admin.AdminProfileDAO;
import com.heroku.java.MODEL.Admin;

import java.sql.SQLException;

@Controller
public class EventCreateController {
    @GetMapping("/EventCreate")
    public String AdminEvent() {
        return "Event/EventCreate";
    }

}
