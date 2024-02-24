package com.heroku.java.DAO;

import com.heroku.java.MODEL.Admin;

import javax.sql.DataSource;

import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class AdminDAO {
    private final DataSource dataSource;

    public AdminDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Admin findAdminByEmailAndPassword(String adminemail, String adminpassword) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            String sql = "SELECT adminid, adminname FROM admin WHERE adminemail = ? AND adminpassword = ?";
           
            final var statement = connection.prepareStatement(sql);
            statement.setString(1, adminemail);
            statement.setString(2, adminpassword);

            final var resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int adminid = resultSet.getInt("adminid");
                String adminname = resultSet.getString("adminname");
                return new Admin(adminid, adminname, adminemail, adminpassword);
            }

        }
        return null;
    }
}
