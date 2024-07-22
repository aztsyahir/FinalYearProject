package com.heroku.java.DAO.Admin;

import com.heroku.java.MODEL.Admin;
import javax.sql.DataSource;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.ModelAttribute;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Repository
public class AdminProfileDAO {
    private final DataSource dataSource;

    public AdminProfileDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Admin AdminProfile(int adminid) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            String sql = "SELECT * FROM admin WHERE adminid = ?";
            final var statement = connection.prepareStatement(sql);
            statement.setInt(1, adminid);
            final var resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String adminname = resultSet.getString("adminname");
                String adminemail = resultSet.getString("adminemail");
                String adminpassword = resultSet.getString("adminpassword");

                // debug
                System.out.println("Admin name from db = " + adminname);

                return new Admin(adminid, adminname, adminemail, adminpassword);
            }
            connection.close();
        } catch (SQLException sqe) {
            System.out.println("Error Code = " + sqe.getErrorCode());
            System.out.println("SQL state = " + sqe.getSQLState());
            System.out.println("Message = " + sqe.getMessage());
            System.out.println("printTrace /n");
            sqe.printStackTrace();
        }
        return null;
    }

    public Admin UpdateAdmin(@ModelAttribute("adminProfile") Admin admin) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            String sql = "UPDATE admin SET adminname=?, adminemail=?, adminpassword=? WHERE adminid=?";
            final var statement = connection.prepareStatement(sql);

            String adminpassword = admin.getAdminpassword();
            System.out.println("password: " + adminpassword);

            statement.setString(1, admin.getAdminname());
            statement.setString(2, admin.getAdminemail());
            statement.setString(3, adminpassword);
            statement.setInt(4, admin.getAdminid());
            statement.executeUpdate();
            connection.close();
        }
        return admin;
    }

    public String DeleteAdmin(Admin admin) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            String sql = "DELETE FROM admin WHERE adminid =?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, admin.getAdminid());
            statement.executeUpdate();
            connection.close();
        }
        return null;
    }

}
