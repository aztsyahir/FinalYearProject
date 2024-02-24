package com.heroku.java.DAO;

import com.heroku.java.MODEL.Player;

import javax.sql.DataSource;

import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class PlayerDAO {
    private final DataSource dataSource;

    public PlayerDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Player findPlayerByEmailAndPassword(String playeremail, String playerpassword) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            String sql = "SELECT playerid, playername FROM player WHERE playeremail = ? AND playerpassword = ?";

            final var statement = connection.prepareStatement(sql);
            statement.setString(1, playeremail);
            statement.setString(2, playerpassword);

            final var resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int playerid = resultSet.getInt("playerid");
                String playername = resultSet.getString("playername");
                return new Player(playerid, playername, playeremail, playerpassword);
            }

        }
        return null;
    }

    public Player SignUp(@ModelAttribute("PlayerSignUp") Player player) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            String sql = "INSERT INTO player (playername, playeremail, playergender, playerage, playerpassword) VALUES (?,?,?,?,?)";
            final var statement = connection.prepareStatement(sql);

            String playername = player.getPlayername();
            String playeremail = player.getPlayeremail();
            String playergender = player.getPlayergender();
            int playerage = player.getPlayerage();
            String playerpassword = player.getPlayerpassword();

            statement.setString(1, playername);
            statement.setString(2, playeremail);
            statement.setString(3, playergender);
            statement.setInt(4, playerage);
            statement.setString(5, playerpassword);
            statement.executeUpdate();
        }
        return player;
}
}