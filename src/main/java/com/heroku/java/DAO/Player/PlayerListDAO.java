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

    public ArrayList<Player> getPlayerList(String name, int edid) throws SQLException {
        ArrayList<Player> player = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()) {
            String sql = "SELECT p.playerid, p.playername, p.playerstats " +
                         "FROM player p " +
                         "WHERE p.playername LIKE ? " +
                         "AND NOT EXISTS ( " +
                         "    SELECT 1 FROM member m " +
                         "    JOIN team t ON m.teamid = t.teamid " +
                         "    WHERE m.playerid = p.playerid AND t.eventdetailid = ? " +
                         ") " +
                         "ORDER BY p.playername DESC";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                
                statement.setString(1, "%" + name + "%");
                statement.setInt(2, edid);
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

}