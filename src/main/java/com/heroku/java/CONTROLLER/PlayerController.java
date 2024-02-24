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
    public String PlayerEvent() {
        return "Player/PlayerEvent";
    }

}
