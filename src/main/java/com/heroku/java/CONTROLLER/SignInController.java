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
import com.heroku.java.DAO.Player.PlayerSignInDAO;
import com.heroku.java.DAO.Admin.AdminSignInDAO;

import jakarta.servlet.http.HttpSession;
import java.sql.SQLException;

@SpringBootApplication
@Controller

public class SignInController {
    private final AdminSignInDAO adminSignInDAO;
    private final PlayerSignInDAO playerSignInDAO;

    @Autowired
    public SignInController(AdminSignInDAO adminSignInDAO, PlayerSignInDAO playerSignInDAO) {
        this.adminSignInDAO = adminSignInDAO;
        this.playerSignInDAO = playerSignInDAO;
    }

    @GetMapping("/SignIn")
    public String SignIn() {
        return "SignIn";
    }

    @PostMapping("/SignIn")
    public String SignInPage(@RequestParam(name = "success", required = false) Boolean success, HttpSession session,
            String email, String password, Model model) {

        try {
            Admin admin = adminSignInDAO.AdminSignIn(email, password);
            if (admin != null) {
                session.setAttribute("adminname", admin.getAdminname());
                session.setAttribute("adminid", admin.getAdminid());

                System.out.println("Admin SignIn Name : " + admin.getAdminname());
                System.out.println("admin SignIn ID : " + admin.getAdminid());

                return "redirect:/AdminEvent?success=true";
            }

            Player player = playerSignInDAO.PlayerSignIn(email, password);
            if (player != null) {
                session.setAttribute("playerid", player.getPlayerid());
                session.setAttribute("playername", player.getPlayername());

                System.out.println("Player SignIn Name : " + player.getPlayername());
                System.out.println("Player SignIn ID: " + player.getPlayerid());

                return "redirect:/PlayerEvent?success=true";
            }

            return "redirect:/SignIn?success=false";
        } catch (SQLException sqe) {
            System.out.println("Error Code = " + sqe.getErrorCode());
            System.out.println("SQL state = " + sqe.getSQLState());
            System.out.println("Message = " + sqe.getMessage());
            System.out.println("printTrace /n");
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
