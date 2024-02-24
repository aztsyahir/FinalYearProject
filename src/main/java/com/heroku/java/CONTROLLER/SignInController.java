package com.heroku.java.CONTROLLER;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.heroku.java.MODEL.Admin;
import com.heroku.java.MODEL.Player;
import com.heroku.java.DAO.AdminDAO;
import com.heroku.java.DAO.PlayerDAO;

import jakarta.servlet.http.HttpSession;
import java.sql.SQLException;

@SpringBootApplication
@Controller

public class SignInController {
    private final AdminDAO adminDAO;
    private final PlayerDAO playerDAO;

    @Autowired
    public SignInController(AdminDAO adminDAO, PlayerDAO playerDAO) {
        this.adminDAO = adminDAO;
        this.playerDAO = playerDAO;
    }

    @GetMapping("/SignIn")
    public String SignIn() {
        return "SignIn";
    }

    @PostMapping("/SignIn")
    public String SignInPage(@RequestParam(name = "success", required = false) Boolean success, HttpSession session,
            String email, String password, Model model) {

        try {
            Admin admin = adminDAO.findAdminByEmailAndPassword(email, password);
            if (admin != null) {
                session.setAttribute("adminname", admin.getAdminname());
                session.setAttribute("adminid", admin.getAdminid());
                return "redirect:/AdminEvent?success=true";
            }

            Player player = playerDAO.findPlayerByEmailAndPassword(email, password);
            if (player != null) {
                session.setAttribute("playerid", player.getPlayerid());
                session.setAttribute("playername", player.getPlayername());
                return "redirect:/PlayerEvent?success=true";
            }

            return "redirect:/SignIn?success=false";
        } catch (SQLException sqe) {
            sqe.printStackTrace();
            return "redirect:/SignIn?error";
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/SignIn?error";
        }
    }

    @GetMapping("/SignOut")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}
