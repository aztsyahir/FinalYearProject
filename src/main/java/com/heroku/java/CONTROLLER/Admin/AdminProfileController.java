package com.heroku.java.CONTROLLER.Admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import jakarta.servlet.http.HttpSession;
import com.heroku.java.DAO.Admin.AdminProfileDAO;
import com.heroku.java.MODEL.Admin;
import java.sql.SQLException;

@Controller
public class AdminProfileController {
    private final AdminProfileDAO AdminProfileDAO;

    @Autowired
    public AdminProfileController(AdminProfileDAO AdminProfileDAO) {
        this.AdminProfileDAO = AdminProfileDAO;
    }

    @GetMapping("/AdminProfile")
    public String AdminProfile(@RequestParam(name = "success", required = false) Boolean success, HttpSession session,
            Model model, Admin Admin) {

        Integer adminid = (Integer) session.getAttribute("adminid");
        String adminname = (String) session.getAttribute("adminname");

        System.out.println("Admin id in session (Admin Profile): " + adminid);
        System.out.println("Admin name in session (Admin Profile): " + adminname);
        if (adminid == null || adminname == null) {
            return "Home";
        }

        if (adminid != null) {
            try {
                Admin = AdminProfileDAO.AdminProfile(adminid);
                model.addAttribute("AdminProfile", Admin);
                return "Admin/AdminProfile";
            } catch (SQLException sqe) {
                System.out.println("Error Code = " + sqe.getErrorCode());
                System.out.println("SQL state = " + sqe.getSQLState());
                System.out.println("Message = " + sqe.getMessage());
                System.out.println("printTrace /n");
                sqe.printStackTrace();
            }
        }

        return "Admin/AdminEvent";
    }

    @PostMapping("UpdateAdmin")
    public String updateAdmin(HttpSession session, @ModelAttribute("AdminProfile") Admin Admin, Model model) {
        Integer adminid = (Integer) session.getAttribute("adminid");
        String adminname = (String) session.getAttribute("adminname");

        System.out.println("Admin id in session (Admin event): " + adminid);
        System.out.println("Admin name in session (Admin event): " + adminname);
        if (adminid == null || adminname == null) {
            return "Home";
        }
        try {
            Admin.setAdminid(adminid);
            Admin = AdminProfileDAO.UpdateAdmin(Admin);
            return "redirect:/AdminProfile?profileSuccess=true";
        } catch (SQLException sqe) {
            System.out.println("Error Code = " + sqe.getErrorCode());
            System.out.println("SQL state = " + sqe.getSQLState());
            System.out.println("Message = " + sqe.getMessage());
            System.out.println("printTrace /n");
            sqe.printStackTrace();
            return "redirect:/AdminProfile";
        }
    }
}
