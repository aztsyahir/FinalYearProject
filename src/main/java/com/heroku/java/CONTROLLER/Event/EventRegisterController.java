package com.heroku.java.CONTROLLER.Event;

import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.heroku.java.DAO.Event.EventRegisterDAO;
import com.heroku.java.MODEL.EventDetail;
import com.heroku.java.MODEL.Player;

@Controller
public class EventRegisterController {
    @Autowired
    private EventRegisterDAO eventRegisterDAO;
    
    private EventRegisterController (EventRegisterDAO eventRegisterDAO) {
        this.eventRegisterDAO = eventRegisterDAO;
    }
    
    // @GetMapping("/IEventRegister")
    //     public String IEventRegister(){
    //         return "Event/IEventRegister";
    // }

    @PostMapping("/IEventRegister")
    public String IEventRegister(@RequestParam("edid") int edid, 
    @RequestParam("playerid") int playerid) throws SQLException{

        try {
            eventRegisterDAO.IRegisterEvent(edid, playerid);
            return "redirect:/PlayerEventCalendar";
        } catch (Exception e) {
            e.printStackTrace();
            // TODO: handle exception
            return "redirect:/PlayerEventCalendar";
        }
    }
}
