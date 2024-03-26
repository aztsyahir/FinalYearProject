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

    public Player PlayerProfile(int playerid) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            String sql = "SELECT * FROM player WHERE playerid = ?";
            final var statement = connection.prepareStatement(sql);
            statement.setInt(1, playerid);
            final var resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String playername = resultSet.getString("playername");
                String playeremail = resultSet.getString("playeremail");
                String playerpassword = resultSet.getString("playerpassword");
                String playergender = resultSet.getString("playergender");
                int playerage = resultSet.getInt("playerage");
                int playerstats = resultSet.getInt("playerstats");

                // debug
                System.out.println("Player name from db = " + playername);

                return new Player(playerid, playername, playeremail, playerpassword, playergender, playerage,
                        playerstats);
            }
        } catch (SQLException sqe) {
            System.out.println("Error Code = " + sqe.getErrorCode());
            System.out.println("SQL state = " + sqe.getSQLState());
            System.out.println("Message = " + sqe.getMessage());
            System.out.println("printTrace /n");
            sqe.printStackTrace();
        }
        return null;
    }

    public void updatePlayerStats(int playerid, int totalPercentage) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            String sql = "UPDATE player SET playerstats = ? WHERE playerid = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, totalPercentage);
            statement.setInt(2, playerid);
            statement.executeUpdate();
        }
    }

    public Player UpdatePlayer(@ModelAttribute("PlayerProfile") Player player) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            String sql = "UPDATE player SET playername=?, playeremail=?, playerpassword=?, playergender=?,playerage=?, playerstats=? WHERE playerid=?";
            final var statement = connection.prepareStatement(sql);
            String playerpassword = player.getPlayerpassword();
            System.out.println("password: " + playerpassword);

            statement.setString(1, player.getPlayername());
            statement.setString(2, player.getPlayeremail());
            statement.setString(3, playerpassword);
            statement.setString(4, player.getPlayergender());
            statement.setInt(5, player.getPlayerage());
            statement.setInt(6, player.getPlayerstats());
            statement.setInt(7, player.getPlayerid());

            statement.executeUpdate();
        }
        return player;
    }

    public String DeletePlayer(Player player) throws SQLException {

        try (Connection connection = dataSource.getConnection()) {
            String sql = "DELETE FROM player WHERE playerid =?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, player.getPlayerid());
            statement.executeUpdate();
        }
        return null;
    }

}