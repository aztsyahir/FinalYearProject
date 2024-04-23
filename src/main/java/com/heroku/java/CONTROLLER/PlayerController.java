package com.heroku.java.CONTROLLER;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import jakarta.servlet.http.HttpSession;

import com.heroku.java.MODEL.Player;

// @SpringBootApplication
@Controller

public class PlayerController {

    @GetMapping("/PlayerEvent")
    public String PlayerEvent(HttpSession session, Model model, Player player) {
        int playerid = (int) session.getAttribute("playerid");
        String playername = (String) session.getAttribute("playername");

        System.out.println("Player id in session (player event): " + playerid);
        System.out.println("Player name in session (player event): " + playername);

        return "Player/PlayerEvent";
    }
}
