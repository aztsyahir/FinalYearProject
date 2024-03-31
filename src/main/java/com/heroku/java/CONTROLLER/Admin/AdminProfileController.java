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

        int Adminid = (int) session.getAttribute("adminid");
        String Adminname = (String) session.getAttribute("adminname");

        System.out.println("Admin id in session (Admin profile): " + Adminid);
        System.out.println("Admin name in session (Admin profile): " + Adminname);

        if (Adminid != 0) {
            try {
                Admin = AdminProfileDAO.AdminProfile(Adminid);
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
        int Adminid = (int) session.getAttribute("adminid");
        String Adminname = (String) session.getAttribute("adminname");
        System.out.println("Admin id in session (Admin update): " + Adminid);
        System.out.println("Admin name in session (Admin update): " + Adminname);

        if (Adminid != 0) {
            try {
                Admin.setAdminid(Adminid);
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
        return "Admin/AdminProfile?profileSuccess=true";
    }

    @GetMapping("/DeleteAdmin")
    public String deleteAdmin(HttpSession session, Admin Admin, Model model) {
        int Adminid = (int) session.getAttribute("adminid");
        String Adminname = (String) session.getAttribute("adminname");
        if (Adminid != 0) {
            try {
                Admin.setAdminid(Adminid);
                AdminProfileDAO.DeleteAdmin(Admin);
                System.out.println("Admin deleted: " + Adminname);
                return "redirect:/";
            } catch (SQLException sqe) {
                System.out.println("Error Code = " + sqe.getErrorCode());
                System.out.println("SQL state = " + sqe.getSQLState());
                System.out.println("Message = " + sqe.getMessage());
                System.out.println("printTrace /n");
                sqe.printStackTrace();
                return "redirect:/AdminProfile";
            }
        }
        return "redirect:/AdminProfile";
    }
}
