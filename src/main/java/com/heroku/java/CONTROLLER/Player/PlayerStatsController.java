package com.heroku.java.CONTROLLER.Player;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import jakarta.servlet.http.HttpSession;
import com.heroku.java.DAO.Player.PlayerStatsDAO;

import java.sql.SQLException;

@SpringBootApplication
@Controller

public class PlayerStatsController {
    private final PlayerStatsDAO playerStatsDAO;

    @Autowired
    public PlayerStatsController(PlayerStatsDAO playerStatsDAO) {
        this.playerStatsDAO = playerStatsDAO;
    }

    @GetMapping("/PlayerStats")
    public String PlayerStats(HttpSession session, Model model) {
        Integer playerid = (Integer) session.getAttribute("playerid");
        String playername = (String) session.getAttribute("playername");

        System.out.println("Player id in session (Player event calendar): " + playerid);
        System.out.println("Player name in session (Player event calendar): " + playername);

        if (playerid == null || playername == null) {
            return "Home";
        }
        return "Player/PlayerStats";
    }

    @PostMapping("PlayerStats")
    public String PlayerStats(HttpSession session, @RequestParam("experience") int experience,
            @RequestParam("tournaments") int tournaments, @RequestParam("tournamentwon") int tournamentwon,
            @RequestParam("mvps") int mvps, Model model) {

        Integer playerid = (Integer) session.getAttribute("playerid");
        String playername = (String) session.getAttribute("playername");

        System.out.println("Player id in session (Player event calendar): " + playerid);
        System.out.println("Player name in session (Player event calendar): " + playername);

        if (playerid == null || playername == null) {
            return "Home";
        }

        int total = experience + tournaments + tournamentwon + mvps;
        String Stringtotal = String.valueOf(total);
        double doubletotal = Double.parseDouble(Stringtotal);
        double Percentage = (doubletotal) / 400;
        double doubletotalPercentage = (Percentage * 100);
        int totalPercentage = (int) doubletotalPercentage;

        System.out.println("Total percentage: " + totalPercentage);
        try {
            // Update player stats in the database
            playerStatsDAO.updatePlayerStats(playerid, totalPercentage);
            return "redirect:/PlayerProfile?statsSuccess=true";
        } catch (SQLException e) {
            e.printStackTrace();
            return "redirect:/PlayerStats";
        }
    }
}
