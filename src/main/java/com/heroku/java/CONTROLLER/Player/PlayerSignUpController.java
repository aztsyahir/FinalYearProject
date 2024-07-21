package com.heroku.java.CONTROLLER.Player;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import com.heroku.java.DAO.Player.PlayerSignUpDAO;
import com.heroku.java.MODEL.Player;


import java.sql.SQLException;


@Controller
public class PlayerSignUpController {
    private final PlayerSignUpDAO playerSignUpDAO;

    @Autowired
    public PlayerSignUpController(PlayerSignUpDAO playerSignUpDAO) {
        this.playerSignUpDAO = playerSignUpDAO;
    }

     @GetMapping("/PlayerSignUp")
    public String PlayerSignUp() {
        return "Player/PlayerSignUp";
    }

    @PostMapping("/PlayerSignUp")
    public String playerSignUp(@ModelAttribute Player player, Model model) {
        try {
            if (playerSignUpDAO.emailExists(player.getPlayeremail())) {
                return "redirect:/PlayerSignUp?EmailExists=true";
            }
            playerSignUpDAO.PlayerSignUp(player);
            // Redirect to a success page or another appropriate page
            return "redirect:/SignIn?SignUpSuccess=true";
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle database exception
            return "redirect:/PlayerSignUp?SignUpError=true";
        }
    }
}
