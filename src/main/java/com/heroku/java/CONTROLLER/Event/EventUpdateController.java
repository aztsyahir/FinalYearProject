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

import com.heroku.java.DAO.Event.EventUpdateDAO;

import com.heroku.java.MODEL.Event;
import com.heroku.java.MODEL.EventDetail;

import java.io.IOException;
import java.sql.SQLException;

@Controller
public class EventUpdateController {
    private final EventUpdateDAO eventUpdateDAO;

    @Autowired
    public EventUpdateController(EventUpdateDAO eventUpdateDAO) {
        this.eventUpdateDAO = eventUpdateDAO;
    }

    @GetMapping("/EventUpdate")
    public String EventUpdate(@RequestParam("eventid") int eventid,
            @RequestParam(name = "success", required = false) Boolean success, HttpSession session, Model model) {
        int Adminid = (int) session.getAttribute("adminid");
        String Adminname = (String) session.getAttribute("adminname");

        System.out.println("Admin id in session (Event Update): " + Adminid);
        System.out.println("Admin name in session (Event Update): " + Adminname);

        try {
            Event event = eventUpdateDAO.DisplayEvent(eventid);
            model.addAttribute("EventUpdate", event);
            return "Event/EventUpdate";
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            // TODO: handle exception
            return "Signin";
        }
    }

    @PostMapping("/EventUpdate")
    public String EventUpdate(@ModelAttribute("EventUpdate") Event event, @RequestParam("edimgs") MultipartFile edimgs,
            @RequestParam(name = "success", required = false) Boolean success, HttpSession session, EventDetail ed,
            Model model) throws IOException {
                int Adminid = (int) session.getAttribute("adminid");
                String Adminname = (String) session.getAttribute("adminname");
        
                System.out.println("Admin id in session (Event Update): " + Adminid);
                System.out.println("Admin name in session (Event Update): " + Adminname);
        try {

            int eventid = event.getEventid();

            if (!edimgs.isEmpty()) {
                // Save the uploaded image to the database
                ed.setEdimgbyte(edimgs.getBytes());
            } else {
                // No new image uploaded, use the existing image data from the database
                EventDetail existingDetail = eventUpdateDAO.EventUpdateimg(eventid);
                if (existingDetail != null) {
                    ed.setEdimgbyte(existingDetail.getEdimgbyte());
                }
            }

            if (event.getEventDetail() == null) {
                event.setEventDetail(new EventDetail());
            }

            eventUpdateDAO.EventUpdate(event, ed);
            return "redirect:/AEventDetail?eventid=" + eventid + "&UpdateEventSuccess=true";

        } catch (SQLException e) {
            e.printStackTrace();
            // TODO: handle exception
            return "redirect:/error";
        }
    }

    @GetMapping("/CancelEvent")
    public String cancelEvent(@RequestParam("edid") int edid,@RequestParam("eventid") int eventid,
            HttpSession session, Model model) {
                int Adminid = (int) session.getAttribute("adminid");
                String Adminname = (String) session.getAttribute("adminname");
        
                System.out.println("Admin id in session (Event Cancelation): " + Adminid);
                System.out.println("Admin name in session (Event Cancelation): " + Adminname);

        try {
            eventUpdateDAO.CancelEvent(edid);
            return "redirect:/AEventDetail?eventid=" + eventid + "&CancelEventSuccess=true";
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "redirect:/AEventDetail?eventid=" + eventid + "&CancelEventfalse=true";
        }

}
}
