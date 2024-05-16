package com.heroku.java;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Map;

//package com.heroku.java.CONTROLLER;

//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;

//import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.web.bind.annotation.ModelAttribute;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.multipart.MultipartFile;

//import jakarta.servlet.http.HttpSession;
//import com.heroku.java.DAO.PlayerDAO;
//import com.heroku.java.DAO.Player.PlayerStatsDAO;
//import com.heroku.java.MODEL.Player;

//import javax.sql.DataSource;
//import java.sql.Connection;
//import java.sql.Date;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.util.Map;
//import java.sql.SQLException;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Base64;

@SpringBootApplication
@Controller
public class GettingStartedApplication {

    @GetMapping("/")
    public String index() {
        return "Home";
    }

    public static void main(String[] args) {
        SpringApplication.run(GettingStartedApplication.class, args);
    }
}
