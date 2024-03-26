package com.heroku.java.CONTROLLER;

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
import com.heroku.java.DAO.PlayerDAO;
import com.heroku.java.MODEL.Player;

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

@SpringBootApplication
@Controller

public class PlayerController {
    private final PlayerDAO playerDAO;

    @Autowired
    public PlayerController(PlayerDAO playerDAO) {
        this.playerDAO = playerDAO;
    }

    @GetMapping("/PlayerSignUp")
    public String PlayerSignUp() {
        return "Player/PlayerSignUp";
    }

    @PostMapping("/PlayerSignUp")
    public String playerSignUp(@ModelAttribute Player player) {
        try {
            playerDAO.SignUp(player);
            // Redirect to a success page or another appropriate page
            return "redirect:/SignIn?success=true";
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle database exception
            return "redirect:/PlayerSignUp?error=true";
        }
    }

    @GetMapping("/PlayerEvent")
    public String PlayerEvent(HttpSession session, Model model, Player player) {
        int playerid = (int) session.getAttribute("playerid");
        String playername = (String) session.getAttribute("playername");

        System.out.println("Player id in session (player event): " + playerid);
        System.out.println("Player name in session (player profile): " + playername);

        return "Player/PlayerEvent";
    }

    @GetMapping("/PlayerProfile")
    public String PlayerProfile(@RequestParam(name = "success", required = false) Boolean success, HttpSession session,
            Model model, Player player) {

        int playerid = (int) session.getAttribute("playerid");
        String playername = (String) session.getAttribute("playername");

        System.out.println("Player id in session (player profile): " + playerid);
        System.out.println("Player name in session (player profile): " + playername);

        if (playerid != 0) {
            try {
                player = playerDAO.PlayerProfile(playerid);
                model.addAttribute("PlayerProfile", player);
                return "Player/PlayerProfile";
            } catch (SQLException sqe) {
                System.out.println("Error Code = " + sqe.getErrorCode());
                System.out.println("SQL state = " + sqe.getSQLState());
                System.out.println("Message = " + sqe.getMessage());
                System.out.println("printTrace /n");
                sqe.printStackTrace();
            }
        }

        return "Player/PlayerEvent";
    }

    @GetMapping("/PlayerStats")
    public String PlayerStats(HttpSession session, Model model) {
        int playerid = (int) session.getAttribute("playerid");
        String playername = (String) session.getAttribute("playername");
        System.out.println("Player id in session (player stats): " + playerid);
        System.out.println("Player name in session (player stats): " + playername);
        return "Player/PlayerStats";
    }

    @PostMapping("PlayerStats")
    public String PlayerStats(HttpSession session, @RequestParam("experience") int experience,
            @RequestParam("tournaments") int tournaments, @RequestParam("tournamentwon") int tournamentwon,
            @RequestParam("mvps") int mvps, Model model) {

        int playerid = (int) session.getAttribute("playerid");
        String playername = (String) session.getAttribute("playername");
        System.out.println("Player id in session (player stats2): " + playerid);
        System.out.println("Player name in session (player stats2): " + playername);

        int total = experience + tournaments + tournamentwon + mvps;
        String Stringtotal = String.valueOf(total);
        double doubletotal = Double.parseDouble(Stringtotal);
        double Percentage = (doubletotal) / 300;
        double doubletotalPercentage = (Percentage * 100);
        int totalPercentage = (int) doubletotalPercentage;

        System.out.println("Total percentage: " + totalPercentage);
        try {
            // Update player stats in the database
            playerDAO.updatePlayerStats(playerid, totalPercentage);
            return "redirect:/PlayerProfile?statsSuccess=true";
        } catch (SQLException e) {
            e.printStackTrace();
            return "redirect:/PlayerStats";
        }
    }

    @PostMapping("UpdatePlayer")
    public String updatePlayer(HttpSession session, @ModelAttribute("PlayerProfile") Player player, Model model) {
        int playerid = (int) session.getAttribute("playerid");
        String playername = (String) session.getAttribute("playername");
        System.out.println("Player id in session (player update): " + playerid);
        System.out.println("Player name in session (player update): " + playername);

        if (playerid != 0) {
            try {
                player.setPlayerid(playerid);
                player = playerDAO.UpdatePlayer(player);
                return "redirect:/PlayerProfile?profileSuccess=true";
            } catch (SQLException sqe) {
                System.out.println("Error Code = " + sqe.getErrorCode());
                System.out.println("SQL state = " + sqe.getSQLState());
                System.out.println("Message = " + sqe.getMessage());
                System.out.println("printTrace /n");
                sqe.printStackTrace();
                return "redirect:/PlayerProfile";
            }
        }
        return "Player/PlayerProfile?profileSuccess=true";
    }

    @GetMapping("/DeletePlayer")
    public String deletePlayer(HttpSession session, Player player, Model model) {
        int playerid = (int) session.getAttribute("playerid");
        String playername = (String) session.getAttribute("playername");
        if (playerid != 0) {
            try {
                player.setPlayerid(playerid);
                playerDAO.DeletePlayer(player);
                System.out.println("player deleted: " + playername);
                return "redirect:/";
            } catch (SQLException sqe) {
                System.out.println("Error Code = " + sqe.getErrorCode());
                System.out.println("SQL state = " + sqe.getSQLState());
                System.out.println("Message = " + sqe.getMessage());
                System.out.println("printTrace /n");
                sqe.printStackTrace();
                return "redirect:/PlayerProfile";
            }
        }
        return "redirect:/PlayerProfile";
    }
}
