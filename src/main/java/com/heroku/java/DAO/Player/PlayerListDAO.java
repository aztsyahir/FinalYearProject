package com.heroku.java.DAO.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.springframework.stereotype.Repository;

import com.heroku.java.MODEL.Player;

import java.util.ArrayList;

@Repository
public class PlayerListDAO {
    private final DataSource dataSource;

    public PlayerListDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public ArrayList<Player> getPlayerList(String name) throws SQLException {
        ArrayList<Player> player = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()) {
            String sql = "SELECT playerid, playername, playerstats FROM player WHERE playername LIKE ? ORDER BY playername DESC";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, "%" + name + "%");
                try (var resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        int playerid = resultSet.getInt("playerid");
                        String playername = resultSet.getString("playername");
                        int playerstats = resultSet.getInt("playerstats");
                        player.add(new Player(playerid, playername, playerstats));
                    }
                }
            }
            return player;
        }
    }

    public void AddMember(int playerid, int teamid) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            String sql = "INSERT INTO member (playerid, teamid) VALUES (?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, playerid);
                statement.setInt(2, teamid);
                statement.executeUpdate();
            }
        }
    }
}