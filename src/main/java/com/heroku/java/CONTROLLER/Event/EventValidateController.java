package com.heroku.java.CONTROLLER.Event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpSession;

import com.heroku.java.DAO.Admin.ValidateDAO;
import com.heroku.java.DAO.Event.EventDetailDAO;
import com.heroku.java.DAO.Event.EventUpdateDAO;
import com.heroku.java.DAO.Event.EventWithdrawDAO;
import com.heroku.java.MODEL.Event;
import com.heroku.java.MODEL.EventDetail;
import com.heroku.java.MODEL.Player;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class EventValidateController {
    private final ValidateDAO validateDAO;
    private final EventDetailDAO eventDetailDAO;

    @Autowired
    public EventValidateController(ValidateDAO validateDAO, EventDetailDAO eventDetailDAO) {
        this.validateDAO = validateDAO;
        this.eventDetailDAO = eventDetailDAO;
    }

    @GetMapping("/EventValidateI")
    public String EventValidate(@RequestParam(name = "success", required = false) Boolean success, @RequestParam("edid") int edid, HttpSession session,
    Model model) {
        int Adminid = (int) session.getAttribute("adminid");
        String Adminname = (String) session.getAttribute("adminname");

        System.out.println("Admin id in session (Event Validate Individual): " + Adminid);
        System.out.println("Admin name in session (Event Validate Individual): " + Adminname);
        

        try {
            ArrayList<Player> players = validateDAO.getIndividual(edid);
            model.addAttribute("players", players);

            EventDetail eventDetail = validateDAO.getEventDetail(edid); // Adjust this method if necessary
        model.addAttribute("event", eventDetail);

        int totalPlayersRegistered = validateDAO.getTotalPlayersRegistered(edid);
        model.addAttribute("totalPlayers", totalPlayersRegistered);

            return "Event/EventValidateI"; // Assuming this is your Thymeleaf template name
        } catch (SQLException e) {
            e.printStackTrace();
            return "Signin"; // Handle appropriately, redirect to login or error page
        }
}
}
