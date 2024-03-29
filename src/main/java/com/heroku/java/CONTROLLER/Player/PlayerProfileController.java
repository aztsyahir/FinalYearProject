package com.heroku.java.CONTROLLER.Player;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import jakarta.servlet.http.HttpSession;
import com.heroku.java.DAO.Player.PlayerProfileDAO;
import com.heroku.java.MODEL.Player;

import java.sql.SQLException;


@Controller
public class PlayerProfileController {
    private final PlayerProfileDAO playerProfileDAO;

    @Autowired
    public PlayerProfileController(PlayerProfileDAO playerProfileDAO) {
        this.playerProfileDAO = playerProfileDAO;
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
                player = playerProfileDAO.PlayerProfile(playerid);
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

    @PostMapping("UpdatePlayer")
    public String updatePlayer(HttpSession session, @ModelAttribute("PlayerProfile") Player player, Model model) {
        int playerid = (int) session.getAttribute("playerid");
        String playername = (String) session.getAttribute("playername");
        System.out.println("Player id in session (player update): " + playerid);
        System.out.println("Player name in session (player update): " + playername);

        if (playerid != 0) {
            try {
                player.setPlayerid(playerid);
                player = playerProfileDAO.UpdatePlayer(player);
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
                playerProfileDAO.DeletePlayer(player);
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
