package com.heroku.java.CONTROLLER;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

//import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.heroku.java.MODEL.Admin;
import com.heroku.java.MODEL.Player;
import com.heroku.java.DAO.AdminDAO;
import com.heroku.java.DAO.PlayerDAO;


import jakarta.servlet.http.HttpSession;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
// import java.util.ArrayList;
// import java.util.Map;
import java.sql.SQLException;

// import org.jscience.physics.amount.Amount;
// import org.jscience.physics.model.RelativisticModel;
// import javax.measure.unit.SI;
@SpringBootApplication
@Controller

public class SignInController {
    private final AdminDAO adminDAO;
    private final PlayerDAO playerDAO;

    @Autowired
    // public SignInController(DataSource dataSource) {
    //     this.dataSource = dataSource;
    // }
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

    // @PostMapping("/SignIn")
    // public String SignInPage(@RequestParam(name = "success", required = false) Boolean success, HttpSession session,
    //         String email, String password, User user, Model model) {

    //     try {

    //         Connection connection = dataSource.getConnection();

    //         String sql = "SELECT adminid,adminname,adminemail,adminpassword FROM admin WHERE adminemail = ?";
    //         final var statement = connection.prepareStatement(sql);
    //         statement.setString(1, email);

    //         final var resultSet = statement.executeQuery();

    //         System.out.println("admin's password : " + password);
    //         System.out.println("admin's email : " + email);
    //         if (resultSet.next()) {

    //             int userid = resultSet.getInt("adminid");
    //             String username = resultSet.getString("adminname");
    //             String adminemail = resultSet.getString("adminemail");
    //             String adminpassword = resultSet.getString("adminpassword");

    //             System.out.println(username);
    //             // if they're admin
    //             System.out.println("Email : " + adminemail.equals(email) + " | " + email);
    //             System.out.println("Password status : " + adminpassword.equals(password));

    //             if (adminemail.equals(email)
    //                     && adminpassword.equals(password)) {

    //                 session.setAttribute("adminname", username);
    //                 session.setAttribute("adminid", userid);
    //                 connection.close();
    //                 return "redirect:/AdminEvent?success=true";
    //             }
    //         }

    //         String sql2 = "SELECT playerid,playername,playeremail,playerpassword,playerage FROM player WHERE playeremail=? ";
    //         final var statement2 = connection.prepareStatement(sql2);
    //         statement2.setString(1, email);

    //         final var resultSet2 = statement2.executeQuery();
    //         while (resultSet2.next()) {
    //             int userid = resultSet2.getInt("playerid");
    //             String username = resultSet2.getString("playername");
    //             String playeremail = resultSet2.getString("playeremail");
    //             String playerpassword = resultSet2.getString("playerpassword");

    //             System.out.println("player's name : " + username);
    //             System.out.println("player's email : " + email);
    //             if (playeremail.equals(email)
    //                     && playerpassword.equals(password)) {

    //                 session.setAttribute("playerid", userid);
    //                 session.setAttribute("playername", username);

    //                 connection.close();
    //                 return "redirect:/PlayerEvent?success=true";
    //             }
    //         }

    //         connection.close();
    //         return "redirect:/SignIn?success=false";

    //     } catch (SQLException sqe) {
    //         System.out.println("Error Code = " + sqe.getErrorCode());
    //         System.out.println("SQL state = " + sqe.getSQLState());
    //         System.out.println("Message = " + sqe.getMessage());
    //         System.out.println("printTrace /n");
    //         sqe.printStackTrace();

    //         return "redirect:/SignIn?error";
    //     } catch (Exception e) {
    //         System.out.println("E message : " + e.getMessage());
    //         return "redirect:/SignIn?error";
    //     }

    // }

    @GetMapping("/SignOut")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}
